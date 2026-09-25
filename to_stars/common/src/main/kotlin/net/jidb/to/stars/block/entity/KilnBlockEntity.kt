package net.jidb.to.stars.block.entity

import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.inventory.menu.KilnMenu
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
import net.minecraft.world.level.block.entity.FuelValues
import net.minecraft.world.level.block.state.BlockState
import java.util.Optional

/**
 * Represents the [net.minecraft.world.level.block.entity.BlockEntity] for the Kiln, a specialized type of furnace used for smelting unsmokables and unblastables in-game.
 * Contains code to halve the burn duration of fuel and a custom [net.minecraft.world.item.crafting.RecipeCache] [quickCheck] to only map recipes that are mapped in the furnace, but not mapped in the smoker or blast furnace.
 * Extends vanilla [AbstractFurnaceBlockEntity].
 *
 * @constructor Creates a [KilnBlockEntity] instance.
 * @param pos The [BlockPos] of the [net.minecraft.world.level.block.entity.BlockEntity] in the world.
 * @param state The [BlockState] associated with the [net.minecraft.world.level.block.entity.BlockEntity].
 *
 * @see net.jidb.to.stars.block.KilnBlock
 * @see KilnMenu
 * @since 0.2.0
 */
class KilnBlockEntity(pos: BlockPos, state: BlockState) : AbstractFurnaceBlockEntity(ToStarsMod.blockEntities.kiln, pos, state, RecipeType.SMELTING) {

    override fun getDefaultName() = blockState.block.name

    override fun createMenu(id: Int, inventory: Inventory) = KilnMenu(id, inventory, this, dataAccess)

    override fun getBurnDuration(fuel: FuelValues, stack: ItemStack) = super.getBurnDuration(fuel, stack).div(2)

    companion object {

        /**
         * Creates a custom cached check object for retrieving kilnable recipes.
         * This check verifies that a smelting recipe exists while ensuring that corresponding recipes from other recipe types (e.g., smoking, campfire cooking, blasting) do not exist.
         *
         * @return A cached check function capable of querying kilnable recipes.
         */
        fun createCustomCheck(): RecipeManager.CachedCheck<SingleRecipeInput, SmeltingRecipe> {
            val smelting = RecipeManager.createCheck(RecipeType.SMELTING)
            val others = listOf(
                RecipeManager.createCheck(RecipeType.SMOKING),
                RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING),
                RecipeManager.createCheck(RecipeType.BLASTING)
            )

            return { input, level -> getKilnRecipe(input, level, smelting, others) }
        }

        /**
         * Retrieves a kiln recipe based on the provided input, using the given cached recipe checkers.
         *
         * @param input The input ingredient for the recipe.
         * @param level The server level where the recipe is being queried.
         * @param smelting The cached check for smelting recipes to include recipes with.
         * @param others A list of cached checks for other abstract cooking recipes to exclude recipes with.
         * @return An optional containing a modified kiln smelting recipe if one is found and valid.
         */
        fun getKilnRecipe(input: SingleRecipeInput, level: ServerLevel, smelting: RecipeManager.CachedCheck<SingleRecipeInput, SmeltingRecipe>, others: List<RecipeManager.CachedCheck<SingleRecipeInput, out AbstractCookingRecipe>>): Optional<RecipeHolder<SmeltingRecipe>> {
            val recipe = smelting.getRecipeFor(input, level)
            if (recipe.isPresent) {
                if (others.stream().anyMatch { check -> check.getRecipeFor(input, level).isPresent }) {
                    return Optional.empty()
                }
                val holder = recipe.get()

                val original = holder.value()
                val modified = SmeltingRecipe(
                    Recipe.CommonInfo(original.showNotification()),
                    AbstractCookingRecipe.CookingBookInfo(original.category(), original.group()),
                    original.input(),
                    ItemStackTemplate.fromStack(original.assemble(input)),
                    original.experience(),
                    original.cookingTime() / 2
                )

                return Optional.of(RecipeHolder(holder.id(), modified))
            }
            return Optional.empty()
        }

    }

}
