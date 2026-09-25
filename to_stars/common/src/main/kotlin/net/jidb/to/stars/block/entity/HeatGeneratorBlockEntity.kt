package net.jidb.to.stars.block.entity

import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.HeatGeneratorBlock
import net.jidb.to.stars.inventory.menu.HeatGeneratorMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.NonNullList
import net.minecraft.server.level.ServerLevel
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

/**
 * The block entity of a machine that makes heat, which holds how hot it currently is and what it is burning to stay that way.
 *
 * Heat is driven towards a target rather than set outright: while there is fuel it climbs by whatever that fuel is worth, and once the fuel runs out it falls back towards where the generator settles.
 *
 * @param type The block entity type being built.
 * @param pos The position of the block.
 * @param state The state of the block.
 */
abstract class HeatGeneratorBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : BaseContainerBlockEntity(type, pos, state), WorldlyContainer {

    /**
     * The items in the generator, i.e. the fuel it is burning.
     */
    protected var inventory = NonNullList.withSize(HeatGeneratorMenu.allSlots.size, ItemStack.EMPTY)

    /**
     * How hot the generator currently is.
     */
    var heat = 0f

    /**
     * How much its heat changed on the last tick, which is what a tooltip reads to say whether it is climbing or falling.
     */
    var heatChange = 0f

    /**
     * How much heat the fuel currently burning adds per tick.
     */
    var heatAdd = 0f

    /**
     * How long the generator takes over its fuel, as a multiple of what the fuel itself lasts.
     */
    var heatSpeed = (state.block as HeatGeneratorBlock).machine.machineSpeed

    /**
     * How much heat the generator makes per tick from a fuel of ordinary value.
     */
    var heatValue = (state.block as HeatGeneratorBlock).machine.generatorHeat

    /**
     * The heat the generator settles at while it is doing nothing.
     */
    var heatInitial = (state.block as HeatGeneratorBlock).machine.generatorInitial

    /**
     * How far above [heatInitial] the generator can be driven before it is running too hot.
     */
    var heatRange = (state.block as HeatGeneratorBlock).machine.generatorRange

    /**
     * The extra heat the generator is given for running at its limit.
     */
    var heatBonus = (state.block as HeatGeneratorBlock).machine.generatorBonus

    /**
     * What is left of the generator's heat each tick once it stops being fed, i.e. how slowly it cools.
     */
    var heatCooling = (state.block as HeatGeneratorBlock).machine.generatorCooling

    /**
     * How long the fuel currently burning has left, in ticks.
     */
    var heatDuration: Short = 0

    /**
     * How long the fuel currently burning lasted to begin with, which is what the flame in the interface is drawn against.
     */
    var heatMaxDuration: Short = 0

    /**
     * How the generator's figures are read by and written from the interface it is opened into.
     */
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

    /**
     * How much heat an item is worth to this generator, against a fuel of ordinary value.
     *
     * @param stack The item being burned.
     * @return How much it is worth, or `0` where it will not burn at all.
     */
    abstract fun getFuelValue(stack: ItemStack): Float

    /**
     * How long an item burns for in this generator, before its own speed applies.
     *
     * @param stack The item being burned.
     * @return How long it burns for, in ticks, or `0` where it will not burn at all.
     */
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
        heat = input.getFloatOr("heat", 0f)
        heatChange = input.getFloatOr("heatChange", heatChange)
        heatAdd = input.getFloatOr("heatAdd", heatAdd)
        heatSpeed = input.getFloatOr("heatSpeed", heatSpeed)
        heatValue = input.getFloatOr("heatValue", heatValue)
        heatInitial = input.getFloatOr("heatInitial", heatInitial)
        heatRange = input.getFloatOr("heatRange", heatRange)
        heatBonus = input.getFloatOr("heatBonus", heatBonus)
        heatCooling = input.getFloatOr("heatCooling", heatCooling)
        heatDuration = input.getShortOr("heatDuration", heatDuration).toShort()
        heatMaxDuration = input.getShortOr("heatMaxDuration", heatMaxDuration).toShort()
        items.clear()
        ContainerHelper.loadAllItems(input, items)
    }

    override fun saveAdditional(output: ValueOutput) {
        output.putFloat("heat", heat)
        output.putFloat("heatChange", heatChange)
        output.putFloat("heatAdd", heatAdd)
        output.putFloat("heatSpeed", heatSpeed)
        output.putFloat("heatValue", heatValue)
        output.putFloat("heatInitial", heatInitial)
        output.putFloat("heatRange", heatRange)
        output.putFloat("heatBonus", heatBonus)
        output.putFloat("heatCooling", heatCooling)
        output.putShort("heatDuration", heatDuration)
        output.putShort("heatMaxDuration", heatMaxDuration)
        ContainerHelper.saveAllItems(output, items)
        super.saveAdditional(output)
    }

    companion object {

        /**
         * Ticks the block entity.
         *
         * @param level The level it is in.
         * @param pos Its position.
         * @param state Its state.
         * @param entity The block entity being ticked.
         */
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

            if (level is ServerLevel) {
                val above = pos.above()
                ToStarsMod.blocks.boiler.setHeat(level, level.getBlockState(above), above, entity.heat, Direction.DOWN)
            }
        }

    }

}
