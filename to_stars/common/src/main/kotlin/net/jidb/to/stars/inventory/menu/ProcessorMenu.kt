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

abstract class ProcessorMenu(type: MenuType<*>, id: Int, protected val playerInventory: Inventory, internal val container: Container, internal val data: ContainerData) : RecipeBookMenu(type, id) {

    abstract val inputSlots: IntArray

    abstract val outputSlots: IntArray

    abstract val batterySlots: IntArray

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

        val dataSchema = ContainerDataSchema<ProcessorDataKey>()
            .defineInclude(ToEnergyBlockEntityHandler.dataSchema) { ProcessorDataKey.entries[it.ordinal] }
            .defineShort(ProcessorDataKey.PROGRESS)
            .defineShort(ProcessorDataKey.MAX_PROGRESS)
            .defineShort(ProcessorDataKey.COMPLETIONS)
            .defineLong(ProcessorDataKey.RECIPE_ENERGY)
            .defineInt(ProcessorDataKey.RECIPE_TIME)
            .defineBool(ProcessorDataKey.RECIPE_SAME)

    }

    enum class ProcessorDataKey : StringRepresentable {

        ENERGY_TOTAL,

        ENERGY_CAPACITY,

        ENERGY_MAX_INPUT,

        ENERGY_MAX_OUTPUT,

        ENERGY_INSERT_CHANGE,

        ENERGY_INSERT_AVERAGE,

        ENERGY_EXTRACT_CHANGE,

        ENERGY_EXTRACT_AVERAGE,

        PROGRESS,

        MAX_PROGRESS,

        RECIPE_ENERGY,

        RECIPE_TIME,

        RECIPE_SAME,

        COMPLETIONS;

        override fun getSerializedName() = name.lowercase().replace("energy_", "")

    }

    open class ProcessorMenuData(var pos: BlockPos, var machineSpeed: Float, var machineUsage: Float, var machineBonus: Float, var machineBonusMax: Int, var lastRecipe: Optional<ResourceKey<Recipe<*>>>, var lastRecipeIcon: Optional<ResourceKey<Item>>) {

        constructor(entity: ProcessorBlockEntity) : this(entity.blockPos, entity.machineSpeed, entity.machineUsage, entity.machineBonus, entity.machineBonusMax, Optional.ofNullable(entity.lastRecipe), Optional.ofNullable(entity.lastRecipeIcon))

        companion object {

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
