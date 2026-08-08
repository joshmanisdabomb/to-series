package net.jidb.to.stars.inventory.menu

import net.jidb.to.base.api.inventory.data.ContainerDataSchema
import net.jidb.to.base.pub.block.entity.ToEnergyBlockEntityHandler
import net.jidb.to.stars.block.entity.ProcessorBlockEntity
import net.jidb.to.stars.recipe.processor.ProcessorRecipe
import net.jidb.to.stars.recipe.processor.ProcessorRecipeInput
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.recipebook.ServerPlaceRecipe
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.StringRepresentable
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.StackedItemContents
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.RecipeBookMenu
import net.minecraft.world.inventory.RecipeBookType
import net.minecraft.world.inventory.StackedContentsCompatible
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import java.util.Optional

/**
 * The interface a machine that runs processor recipes is opened into, which is backed by the recipe book so that a recipe can be filled in from it.
 *
 * @param type The menu type being built.
 * @param id The id of the menu.
 * @property playerInventory The inventory of the player who opened it.
 * @property container The machine the menu is opened on.
 * @property data The machine's figures, as the interface reads them.
 */
abstract class ProcessorMenu(type: MenuType<*>, id: Int, protected val playerInventory: Inventory, internal val container: Container, internal val data: ContainerData) : RecipeBookMenu(type, id) {

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

    /**
     * What the client was told about the machine as the menu opened, or `null` on the server, where the machine itself is to hand.
     */
    abstract var clientData: ProcessorMenuData?

    override fun getRecipeBookType() = RecipeBookType.FURNACE

    override fun fillCraftSlotsStackedContents(stackedContents: StackedItemContents) {
        (container as? StackedContentsCompatible)?.fillStackedContents(stackedContents)
    }

    override fun handlePlacement(useMaxItems: Boolean, allowDroppingItemsToClear: Boolean, recipe: RecipeHolder<*>, level: ServerLevel, inventory: Inventory): PostPlaceAction = ServerPlaceRecipe.placeRecipe(object : ServerPlaceRecipe.CraftingMenuAccess<ProcessorRecipe> {

        override fun fillCraftSlotsStackedContents(stackedContents: StackedItemContents) = this@ProcessorMenu.fillCraftSlotsStackedContents(stackedContents)

        override fun clearCraftingContent() {
            inputSlots.forEach { this@ProcessorMenu.getSlot(it).set(ItemStack.EMPTY) }
        }

        override fun recipeMatches(recipe: RecipeHolder<ProcessorRecipe>) = recipe.value().matches(ProcessorRecipeInput((container as ProcessorBlockEntity).blockState, inputSlots.map(container::getItem)), level)

    }, inputSlots.size, 1, inputSlots.map(::getSlot), inputSlots.map(::getSlot), inventory, recipe as RecipeHolder<ProcessorRecipe>, useMaxItems, allowDroppingItemsToClear)

    companion object {

        /**
         * How the machine's figures are laid out across the shorts a menu can send, which begins with the energy figures every machine holding energy shares.
         */
        val dataSchema = ContainerDataSchema<ProcessorDataKey>()
            .defineInclude(ToEnergyBlockEntityHandler.dataSchema) { ProcessorDataKey.entries[it.ordinal] }
            .defineShort(ProcessorDataKey.PROGRESS)
            .defineShort(ProcessorDataKey.MAX_PROGRESS)
            .defineShort(ProcessorDataKey.COMPLETIONS)
            .defineLong(ProcessorDataKey.RECIPE_ENERGY)
            .defineInt(ProcessorDataKey.RECIPE_TIME)
            .defineBool(ProcessorDataKey.RECIPE_SAME)

    }

    /**
     * Enum that defines each of the figures a processor's interface reads from it.
     */
    enum class ProcessorDataKey : StringRepresentable {

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
    open class ProcessorMenuData(var pos: BlockPos, var machineSpeed: Float, var machineUsage: Float, var machineBonus: Float, var machineBonusMax: Int, var lastRecipe: Optional<ResourceKey<Recipe<*>>>, var lastRecipeIcon: Optional<ResourceKey<Item>>) {

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
                ProcessorMenuData::pos,
                ByteBufCodecs.FLOAT,
                ProcessorMenuData::machineSpeed,
                ByteBufCodecs.FLOAT,
                ProcessorMenuData::machineUsage,
                ByteBufCodecs.FLOAT,
                ProcessorMenuData::machineBonus,
                ByteBufCodecs.INT,
                ProcessorMenuData::machineBonusMax,
                ResourceKey.streamCodec(Registries.RECIPE).apply(ByteBufCodecs::optional),
                ProcessorMenuData::lastRecipe,
                ResourceKey.streamCodec(Registries.ITEM).apply(ByteBufCodecs::optional),
                ProcessorMenuData::lastRecipeIcon,
                ::ProcessorMenuData
            )

        }

    }

}
