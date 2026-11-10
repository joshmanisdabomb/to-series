package net.jidb.to.stars.inventory.menu

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.inventory.data.ContainerDataSchema
import net.jidb.to.base.pub.block.entity.ToEnergyBlockEntityHandler
import net.jidb.to.base.pub.inventory.ToBaseQuickStackHelper
import net.jidb.to.base.service.Services
import net.jidb.to.stars.block.entity.ProcessorBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.player.StackedItemContents
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.RecipeBookMenu
import net.minecraft.world.inventory.RecipeBookType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.inventory.StackedContentsCompatible
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.Level
import java.util.Optional

/**
 * The interface a processor machine that runs any recipe is opened into.
 *
 * @param type The menu type being built.
 * @param id The id of the menu.
 * @property playerInventory The inventory of the player who opened it.
 * @property container The machine the menu is opened on.
 * @property data The machine's figures, as the interface reads them.
 */
abstract class AbstractProcessorMenu(type: MenuType<*>, id: Int, protected val playerInventory: Inventory, internal val container: Container, internal val data: ContainerData) : RecipeBookMenu(type, id) {

    /**
     * Which of the slots a recipe's ingredients are taken from.
     */
    abstract val inputSlots: IntArray

    /**
     * Which of the slots a recipe's results are put into.
     */
    abstract val outputSlots: IntArray

    /**
     * Which of the slots the machine draws energy out of a battery in.
     */
    abstract val batterySlots: IntArray

    override fun stillValid(player: Player) = container.stillValid(player)

    override fun quickMoveStack(player: Player, index: Int) = ToBaseQuickStackHelper.start(this, player, index) { stack, index, slot, player ->
        val from = ToBaseQuickStackHelper.fromContainer(::moveItemStackTo, stack, index, slots.size)
        if (from != null) return@start from

        val itemTransfer = Services.platform.transfer.itemProvider.fromContainer(slot.container, null)
        val energy = if (itemTransfer != null) ToBaseMod.transferProviders.to_energy.fromItemStack(stack, itemTransfer, index) else null
        if (energy != null) {
            if (ToBaseQuickStackHelper.toContainer(::moveItemStackTo, stack, index, slots.size, batterySlots.toList().toIntArray()) == true) {
                return@start true
            }
        } else if (ToBaseQuickStackHelper.toContainer(::moveItemStackTo, stack, index, slots.size, inputSlots.toList().toIntArray()) == true) {
            return@start true
        }
        ToBaseQuickStackHelper.betweenInventoryHotbar(::moveItemStackTo, stack, index, slots.size) == true
    }

    override fun getRecipeBookType() = RecipeBookType.FURNACE

    override fun fillCraftSlotsStackedContents(stackedContents: StackedItemContents) {
        (container as? StackedContentsCompatible)?.fillStackedContents(stackedContents)
    }

    /**
     * Whether an item can go in this input slot.
     *
     * @param stack The item being put in.
     * @param index The index of the input slot.
     * @param level The level the generator is in.
     * @return Whether this item can go in the slot.
     */
    open fun canPlaceInput(stack: ItemStack, index: Int, level: Level): Boolean = true

    /**
     * What the client was told about the machine as the menu opened, or `null` on the server, where the machine itself is to hand.
     */
    abstract fun getClientData(): AbstractProcessorMenuData?

    /**
     * A slot for the processor's input, which runs a callback to the parent's [AbstractProcessorMenu.canPlaceInput] to decide what can go here.
     *
     * @param container The generator the slot belongs to.
     * @param index Which slot this is.
     * @param x The x position of the slot.
     * @param y The y position of the slot.
     */
    inner class AbstractProcessorInputSlot(container: Container, index: Int, x: Int, y: Int) : Slot(container, index, x, y) {

        override fun mayPlace(stack: ItemStack) = canPlaceInput(stack, index, playerInventory.player.level())

        override fun setChanged() {
            super.setChanged()
            this@AbstractProcessorMenu.slotsChanged(container)
        }

    }

    companion object {

        /**
         * How the machine's figures are laid out across the shorts a menu can send, which begins with the energy figures every machine holding energy shares.
         */
        val dataSchema = ContainerDataSchema<AbstractProcessorDataKey>()
            .defineInclude(ToEnergyBlockEntityHandler.dataSchema) { AbstractProcessorDataKey.entries[it.ordinal] }
            .defineShort(AbstractProcessorDataKey.PROGRESS)
            .defineShort(AbstractProcessorDataKey.MAX_PROGRESS)
            .defineShort(AbstractProcessorDataKey.COMPLETIONS)
            .defineLong(AbstractProcessorDataKey.RECIPE_ENERGY)
            .defineInt(AbstractProcessorDataKey.RECIPE_TIME)
            .defineBool(AbstractProcessorDataKey.RECIPE_SAME)

    }

    /**
     * Enum that defines each of the figures a processor's interface reads from it.
     */
    enum class AbstractProcessorDataKey : StringRepresentable {

        /**
         * How much energy the machine is holding.
         */
        ENERGY_TOTAL,

        /**
         * How much energy it can hold.
         */
        ENERGY_CAPACITY,

        /**
         * How much energy can be put into it per tick.
         */
        ENERGY_MAX_INPUT,

        /**
         * How much energy can be taken out of it per tick.
         */
        ENERGY_MAX_OUTPUT,

        /**
         * How much energy was put in on the last tick.
         */
        ENERGY_INSERT_CHANGE,

        /**
         * How much energy has been put in per tick lately.
         */
        ENERGY_INSERT_AVERAGE,

        /**
         * How much energy was taken out on the last tick.
         */
        ENERGY_EXTRACT_CHANGE,

        /**
         * How much energy has been taken out per tick lately.
         */
        ENERGY_EXTRACT_AVERAGE,

        /**
         * How far through the current recipe the machine is, in ticks.
         */
        PROGRESS,

        /**
         * How long that recipe takes it, in ticks.
         */
        MAX_PROGRESS,

        /**
         * How much energy the recipe asks for.
         */
        RECIPE_ENERGY,

        /**
         * How long the recipe asks for, in ticks.
         */
        RECIPE_TIME,

        /**
         * Whether what is in the machine still matches the recipe it built its efficiency up on.
         */
        RECIPE_SAME,

        /**
         * How many times the current recipe has been run in a row.
         */
        COMPLETIONS;

        override fun getSerializedName() = name.lowercase().replace("energy_", "")

    }

    /**
     * What the client is told about a processor as its menu opens, i.e. the figures it needs to draw the interface but cannot read off the machine itself.
     *
     * @property pos The position of the machine.
     * @property machineSpeed How long it takes over a recipe, as a multiple.
     * @property machineUsage How much energy it spends on a recipe, as a multiple.
     * @property machineBonus How much more efficient it becomes with each run of the same recipe.
     * @property machineBonusMax How many runs it keeps gaining over.
     * @property lastRecipe The recipe it last ran, or empty where it has run none.
     * @property lastRecipeIcon The item shown for that recipe.
     */
    open class AbstractProcessorMenuData(var pos: BlockPos, var machineSpeed: Float, var machineUsage: Float, var machineBonus: Float, var machineBonusMax: Int, var lastRecipe: Optional<ResourceKey<Recipe<*>>>, var lastRecipeIcon: Optional<ResourceKey<Item>>) {

        /**
         * Reads what the client needs to be told straight off a machine.
         *
         * @param entity The machine the menu is being opened on.
         */
        constructor(entity: ProcessorBlockEntity) : this(entity.blockPos, entity.machineSpeed, entity.machineUsage, entity.machineBonus, entity.machineBonusMax, Optional.ofNullable(entity.lastRecipe), Optional.ofNullable(entity.lastRecipeIcon))

        companion object {

            /**
             * The codec this is sent to the client through as the menu opens.
             */
            val codec = StreamCodec.composite(
                BlockPos.STREAM_CODEC,
                AbstractProcessorMenuData::pos,
                ByteBufCodecs.FLOAT,
                AbstractProcessorMenuData::machineSpeed,
                ByteBufCodecs.FLOAT,
                AbstractProcessorMenuData::machineUsage,
                ByteBufCodecs.FLOAT,
                AbstractProcessorMenuData::machineBonus,
                ByteBufCodecs.INT,
                AbstractProcessorMenuData::machineBonusMax,
                ResourceKey.streamCodec(Registries.RECIPE).apply(ByteBufCodecs::optional),
                AbstractProcessorMenuData::lastRecipe,
                ResourceKey.streamCodec(Registries.ITEM).apply(ByteBufCodecs::optional),
                AbstractProcessorMenuData::lastRecipeIcon,
                ::AbstractProcessorMenuData
            )

        }

    }

}
