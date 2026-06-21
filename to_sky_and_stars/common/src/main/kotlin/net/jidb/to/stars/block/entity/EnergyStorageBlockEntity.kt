package net.jidb.to.stars.block.entity

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.transfer.TransferContext
import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.api.transfer.TransferTransactionJournal
import net.jidb.to.base.pub.item.component.ToEnergyItemData
import net.jidb.to.base.pub.transfer.ToEnergyTransferContext
import net.jidb.to.base.pub.transfer.energy.InputToEnergyTransferContext
import net.jidb.to.base.pub.transfer.energy.OutputToEnergyTransferContext
import net.jidb.to.base.pub.transfer.energy.ToEnergyWorldlyProvider
import net.jidb.to.base.service.Services
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.EnergyStorageBlock
import net.jidb.to.stars.inventory.menu.EnergyStorageMenu
import net.jidb.to.stars.network.EnergyStorageSyncPayload
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentMap
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.ContainerHelper
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import kotlin.math.max
import kotlin.math.min

class EnergyStorageBlockEntity(pos: BlockPos, state: BlockState) : BaseContainerBlockEntity(ToStarsMod.blockEntities.power_bank, pos, state), WorldlyContainer, ToEnergyWorldlyProvider {

    protected var inventory = NonNullList.withSize(EnergyStorageMenu.allSlots.size, ItemStack.EMPTY)

    var energy = 0L

    val historyInsert = LongArray(20) { 0 }
    val historyExtract = LongArray(20) { 0 }
    var historyInsertAvg = 0L
    var historyExtractAvg = 0L

    var capacity = (state.block as? EnergyStorageBlock)?.machine?.bankStorage ?: 0L
    var maxInput = (state.block as? EnergyStorageBlock)?.machine?.maxInput ?: 0L
    var maxOutput = (state.block as? EnergyStorageBlock)?.machine?.maxOutput ?: 0L

    var nextEnergySync = 0

    val transfer = object : ToEnergyTransferContext {

        val journal = object : TransferTransactionJournal<LongArray>() {
            override fun create() = longArrayOf(energy, historyInsert[0], historyExtract[0])
            override fun rewind(snapshot: LongArray) {
                energy = snapshot[0]
                historyInsert[0] = snapshot[1]
                historyExtract[0] = snapshot[2]
            }
        }

        override fun getSlotCount() = 1

        override fun getAmountAt(resource: Unit, index: Int) = energy

        override fun getCapacityAt(resource: Unit, index: Int) = capacity

        override fun insert(resource: Unit, amount: Long, transaction: TransferTransaction): Long {
            val remaining = max(0L, maxInput - historyInsert[0])
            val inserted = min(capacity - energy, min(amount, remaining))
            if (inserted > 0) {
                journal.store(transaction)
                energy += inserted
                historyInsert[0] += inserted
                return inserted
            } else {
                return 0
            }
        }

        override fun extract(resource: Unit, amount: Long, transaction: TransferTransaction): Long {
            val remaining = max(0L, maxOutput - historyExtract[0])
            val extracted = min(energy, min(amount, remaining))
            if (extracted > 0) {
                journal.store(transaction)
                energy -= extracted
                historyExtract[0] += extracted
                return extracted
            } else {
                return 0
            }
        }

    }
    val transferInput = InputToEnergyTransferContext(transfer)
    val transferOutput = OutputToEnergyTransferContext(transfer)

    val dataAccess = object : ContainerData {

        override fun get(key: Int) = EnergyStorageMenu.dataSchema.get(key) { index -> when (index) {
            EnergyStorageMenu.EnergyStorageDataKey.TOTAL -> energy
            EnergyStorageMenu.EnergyStorageDataKey.CAPACITY -> capacity
            EnergyStorageMenu.EnergyStorageDataKey.MAX_INPUT -> maxInput
            EnergyStorageMenu.EnergyStorageDataKey.MAX_OUTPUT -> maxOutput
            EnergyStorageMenu.EnergyStorageDataKey.INSERT_CHANGE -> historyInsert[1]
            EnergyStorageMenu.EnergyStorageDataKey.INSERT_AVERAGE -> historyInsertAvg
            EnergyStorageMenu.EnergyStorageDataKey.EXTRACT_CHANGE -> historyExtract[1]
            EnergyStorageMenu.EnergyStorageDataKey.EXTRACT_AVERAGE -> historyExtractAvg
        } }

        override fun set(key: Int, value: Int) = EnergyStorageMenu.dataSchema.set(key, value.toShort()) { index, apply -> when (index) {
            EnergyStorageMenu.EnergyStorageDataKey.TOTAL -> energy = apply(energy)
            EnergyStorageMenu.EnergyStorageDataKey.CAPACITY -> capacity = apply(capacity)
            EnergyStorageMenu.EnergyStorageDataKey.MAX_INPUT -> maxInput = apply(maxInput)
            EnergyStorageMenu.EnergyStorageDataKey.MAX_OUTPUT -> maxOutput = apply(maxOutput)
            EnergyStorageMenu.EnergyStorageDataKey.INSERT_CHANGE -> historyInsert[1] = apply(historyInsert[1])
            EnergyStorageMenu.EnergyStorageDataKey.INSERT_AVERAGE -> historyInsertAvg = apply(historyInsertAvg)
            EnergyStorageMenu.EnergyStorageDataKey.EXTRACT_CHANGE -> historyExtract[1] = apply(historyExtract[1])
            EnergyStorageMenu.EnergyStorageDataKey.EXTRACT_AVERAGE -> historyExtractAvg = apply(historyExtractAvg)
        } }

        override fun getCount() = EnergyStorageMenu.dataSchema.getDataSize()

    }

    override fun getDefaultName() = blockState.block.name

    override fun getItems() = inventory

    public override fun setItems(items: NonNullList<ItemStack>) {
        inventory = items
    }

    override fun createMenu(id: Int, player: Inventory) = EnergyStorageMenu(id, player, this, dataAccess)

    override fun getContainerSize() = inventory.size

    override fun getMaxStackSize() = 1

    override fun getMaxStackSize(stack: ItemStack) = 1

    override fun getSlotsForFace(side: Direction) = when (side) {
        Direction.UP -> EnergyStorageMenu.transferFromSlots
        Direction.DOWN -> EnergyStorageMenu.allSlots
        else -> EnergyStorageMenu.transferToSlots
    }

    override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
        val current = inventory[slot]
        if (current.count >= getMaxStackSize()) {
            return false
        }

        return stack.has(ToBaseMod.itemComponents.energy_data)
    }

    override fun canPlaceItemThroughFace(slot: Int, stack: ItemStack, side: Direction?) = canPlaceItem(slot, stack)

    override fun canTakeItemThroughFace(slot: Int, stack: ItemStack, side: Direction): Boolean {
        val energy = stack.get(ToBaseMod.itemComponents.energy_data) ?: return true
        if (slot in EnergyStorageMenu.transferFromSlots) {
            return energy.energy == 0L
        } else if (slot in EnergyStorageMenu.transferToSlots) {
            return energy.energy == energy.max
        }
        return true
    }

    override fun loadAdditional(input: ValueInput) {
        super.loadAdditional(input)
        energy = input.getLongOr("energy", 0L)
        capacity = input.getLongOr("capacity", capacity)
        maxInput = input.getLongOr("maxInput", maxInput)
        maxOutput = input.getLongOr("maxOutput", maxOutput)
        items.clear()
        ContainerHelper.loadAllItems(input, items)
    }

    override fun saveAdditional(output: ValueOutput) {
        output.putLong("energy", energy)
        output.putLong("capacity", capacity)
        output.putLong("maxInput", maxInput)
        output.putLong("maxOutput", maxOutput)
        ContainerHelper.saveAllItems(output, items)
        super.saveAdditional(output)
    }

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag {
        val tag = CompoundTag()
        tag.putLong("energy", energy)
        tag.putLong("capacity", capacity)
        return tag
    }

    override fun getTransferContext(level: LevelReader, pos: BlockPos, side: Direction?): ToEnergyTransferContext {
        if (side == null) return transfer
        val output = blockState.getValue(DirectionalBlock.FACING)
        if (side == output) return transferOutput
        return transferInput
    }

    override fun applyImplicitComponents(components: DataComponentGetter) {
        super.applyImplicitComponents(components)
        val energy = components.get(ToBaseMod.itemComponents.energy_data)
        if (energy != null) {
            this.energy = energy.energy
            capacity = energy.max
            maxInput = energy.maxInput
            maxOutput = energy.maxOutput
        }
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        components.set(ToBaseMod.itemComponents.energy_data, ToEnergyItemData(energy, capacity, maxInput, maxOutput))
    }

    companion object {
        fun tick(level: Level, pos: BlockPos, state: BlockState, entity: EnergyStorageBlockEntity) {
            if (level.isClientSide) return

            entity.historyInsertAvg = entity.historyInsert.average().toLong()
            entity.historyExtractAvg = entity.historyExtract.average().toLong()
            for (i in entity.historyInsert.size - 1 downTo 1) entity.historyInsert[i] = entity.historyInsert[i - 1]
            entity.historyInsert[0] = 0L
            for (i in entity.historyExtract.size - 1 downTo 1) entity.historyExtract[i] = entity.historyExtract[i - 1]
            entity.historyExtract[0] = 0L

            val itemTransfer = Services.platform.transfer.itemProvider.fromContainer(entity, null)
            if (itemTransfer != null) {
                for (i in EnergyStorageMenu.transferFromSlots) {
                    val item = ToBaseMod.transferProviders.to_energy.fromItemStack(entity.inventory[i], itemTransfer, i)
                    if (item != null) {
                        TransferContext.moveAny(Unit, entity.maxInput, item, entity.transfer)
                    }
                }
                for (i in EnergyStorageMenu.transferToSlots) {
                    val item = ToBaseMod.transferProviders.to_energy.fromItemStack(entity.inventory[i], itemTransfer, i)
                    if (item != null) {
                        TransferContext.moveAny(Unit, entity.maxOutput, entity.transfer, item)
                    }
                }
            }

            val facing = state.getValue(DirectionalBlock.FACING)
            if (level.isLoaded(pos.relative(facing))) {
                val other = ToBaseMod.transferProviders.to_energy.fromBlock(level, pos.relative(facing), facing.opposite)
                if (other != null) {
                    val context = entity.getTransferContext(level, pos, facing)
                    TransferContext.moveAny(Unit, entity.maxOutput, context, other)
                }
            }

            if (entity.nextEnergySync <= 0) {
                if (entity.historyInsert.take(7).sum() > 0 || entity.historyExtract.take(7).sum() > 0) {
                    Services.platform.networking.sendToPlayersTrackingChunk(level as ServerLevel, ChunkPos.containing(pos), EnergyStorageSyncPayload(pos, entity.energy))
                    entity.nextEnergySync = 7
                }
            } else {
                entity.nextEnergySync--
            }
        }
    }

}
