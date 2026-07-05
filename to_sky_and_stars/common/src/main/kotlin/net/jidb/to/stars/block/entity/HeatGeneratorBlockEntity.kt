package net.jidb.to.stars.block.entity

import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.HeatGeneratorBlock
import net.jidb.to.stars.inventory.menu.HeatGeneratorMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.NonNullList
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.world.ContainerHelper
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

abstract class HeatGeneratorBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : BaseContainerBlockEntity(type, pos, state), WorldlyContainer {

    protected var inventory = NonNullList.withSize(HeatGeneratorMenu.allSlots.size, ItemStack.EMPTY)

    var heat = 0f
    var heatChange = 0f
    var heatAdd = 0f

    var heatSpeed = (state.block as HeatGeneratorBlock).machine.machineSpeed
    var heatValue = (state.block as HeatGeneratorBlock).machine.generatorHeat
    var heatInitial = (state.block as HeatGeneratorBlock).machine.generatorInitial
    var heatRange = (state.block as HeatGeneratorBlock).machine.generatorRange
    var heatBonus = (state.block as HeatGeneratorBlock).machine.generatorBonus
    var heatCooling = (state.block as HeatGeneratorBlock).machine.generatorCooling

    var heatDuration: Short = 0
    var heatMaxDuration: Short = 0

    val dataAccess = object : ContainerData {

        override fun get(key: Int) = HeatGeneratorMenu.dataSchema.getShort(key) { when (it) {
            HeatGeneratorMenu.HeatGeneratorDataKey.HEAT -> heat
            HeatGeneratorMenu.HeatGeneratorDataKey.CHANGE -> heatChange
            HeatGeneratorMenu.HeatGeneratorDataKey.ADD -> heatAdd
            HeatGeneratorMenu.HeatGeneratorDataKey.DURATION -> heatDuration
            HeatGeneratorMenu.HeatGeneratorDataKey.MAX_DURATION -> heatMaxDuration
            HeatGeneratorMenu.HeatGeneratorDataKey.SPEED -> heatSpeed
            HeatGeneratorMenu.HeatGeneratorDataKey.VALUE -> heatValue
            HeatGeneratorMenu.HeatGeneratorDataKey.INITIAL -> heatInitial
            HeatGeneratorMenu.HeatGeneratorDataKey.RANGE -> heatRange
            HeatGeneratorMenu.HeatGeneratorDataKey.BONUS -> heatBonus
            HeatGeneratorMenu.HeatGeneratorDataKey.COOLING -> heatCooling
        } } ?: 0

        override fun set(key: Int, value: Int) {
            HeatGeneratorMenu.dataSchema.set(key, value.toShort()) { index, fn -> when (index) {
                HeatGeneratorMenu.HeatGeneratorDataKey.HEAT -> heat = fn(heat)
                HeatGeneratorMenu.HeatGeneratorDataKey.CHANGE -> heatChange = fn(heatChange)
                HeatGeneratorMenu.HeatGeneratorDataKey.ADD -> heatAdd = fn(heatAdd)
                HeatGeneratorMenu.HeatGeneratorDataKey.DURATION -> heatDuration = fn(heatDuration)
                HeatGeneratorMenu.HeatGeneratorDataKey.MAX_DURATION -> heatMaxDuration = fn(heatMaxDuration)
                HeatGeneratorMenu.HeatGeneratorDataKey.SPEED -> heatSpeed = fn(heatSpeed)
                HeatGeneratorMenu.HeatGeneratorDataKey.VALUE -> heatValue = fn(heatValue)
                HeatGeneratorMenu.HeatGeneratorDataKey.INITIAL -> heatInitial = fn(heatInitial)
                HeatGeneratorMenu.HeatGeneratorDataKey.RANGE -> heatRange = fn(heatRange)
                HeatGeneratorMenu.HeatGeneratorDataKey.BONUS -> heatBonus = fn(heatBonus)
                HeatGeneratorMenu.HeatGeneratorDataKey.COOLING -> heatCooling = fn(heatCooling)
            } }
        }

        override fun getCount() = HeatGeneratorMenu.dataSchema.getDataSize()

    }

    abstract fun getFuelValue(stack: ItemStack): Float
    abstract fun getFuelDuration(stack: ItemStack): Short

    override fun getDefaultName() = blockState.block.name

    override fun getItems() = inventory

    public override fun setItems(items: NonNullList<ItemStack>) {
        inventory = items
    }

    override fun getContainerSize() = inventory.size

    override fun getSlotsForFace(side: Direction) = HeatGeneratorMenu.allSlots

    override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
        val current = inventory[slot]
        if (current.count > getMaxStackSize(stack)) {
            return false
        }

        return getFuelDuration(stack) > 0
    }

    override fun canPlaceItemThroughFace(slot: Int, stack: ItemStack, side: Direction?) = canPlaceItem(slot, stack)

    override fun canTakeItemThroughFace(slot: Int, stack: ItemStack, side: Direction) = false

    override fun loadAdditional(input: ValueInput) {
        super.loadAdditional(input)
        items.clear()
        ContainerHelper.loadAllItems(input, items)
    }

    override fun saveAdditional(output: ValueOutput) {
        ContainerHelper.saveAllItems(output, items)
        super.saveAdditional(output)
    }

    companion object {
        fun tick(level: Level, pos: BlockPos, state: BlockState, entity: HeatGeneratorBlockEntity) {
            if (level.isClientSide) return

            if (entity.heatDuration > 0) {
                val new = entity.heat.coerceAtLeast(entity.heatInitial) + entity.heatAdd
                entity.heatChange = new - entity.heat
                entity.heat = new
                entity.heatDuration--
                if (entity.heatDuration <= 0 && HeatGeneratorMenu.fuelSlots.all { entity.items[it].isEmpty }) {
                    level.playSound(null, pos, ToStarsMod.sounds.generator_empty, SoundSource.BLOCKS)
                }
            } else {
                for (slot in HeatGeneratorMenu.fuelSlots) {
                    val stack = entity.items[slot]
                    if (stack.isEmpty) continue
                    val duration = entity.getFuelDuration(stack)
                    if (duration > 0) {
                        entity.heatAdd = entity.getFuelValue(stack) * entity.heatValue
                        entity.heatDuration = Mth.ceil(duration * entity.heatSpeed).toShort()
                        entity.heatMaxDuration = entity.heatDuration
                        entity.items[slot].shrink(1)
                        if (entity.items[slot].isEmpty) {
                            entity.items[slot] = ItemStack.EMPTY
                        }
                    }
                    break
                }
                if (entity.heatDuration <= 0) {
                    entity.heatAdd = 0f
                    val new = entity.heat.coerceAtMost(entity.heatInitial + entity.heatRange) * entity.heatCooling
                    entity.heatChange = new - entity.heat
                    entity.heat = new
                    if (entity.heat < entity.heatInitial) {
                        entity.heat = 0f
                    }
                }
            }
            if (entity.heat >= entity.heatInitial + entity.heatRange) {
                entity.heat = entity.heatInitial + entity.heatRange + entity.heatBonus
            }

            val lit = state.getValue(BlockStateProperties.LIT)
            if (lit && entity.heat <= 0f) {
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, false), 3)
                setChanged(level, pos, state)
            } else if (!lit && entity.heat > 0f) {
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), 3)
                setChanged(level, pos, state)
            }
        }
    }

}