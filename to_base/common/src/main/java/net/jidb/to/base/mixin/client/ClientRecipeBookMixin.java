package net.jidb.to.base.mixin.client;

import net.jidb.to.base.hooks.recipe.category.CustomRecipeBookCategoryRegistry;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Map;

/**
 * A mixin that modifies the behavior of the {@link net.minecraft.client.ClientRecipeBook} to allow the injection of {@link net.jidb.to.base.api.recipe.CustomRecipeBookCategory} via {@link net.jidb.to.base.hooks.recipe.category.CustomRecipeBookCategoryRegistry}.
 *
 * This mixin intercepts the {@link net.minecraft.client.ClientRecipeBook#rebuildCollections} method in the {@link net.minecraft.client.ClientRecipeBook}.
 *
 * @see net.minecraft.client.ClientRecipeBook
 * @see net.jidb.to.base.hooks.recipe.category.CustomRecipeBookCategoryRegistry
 * @since 1.1.0
 */
@Mixin(ClientRecipeBook.class)
public abstract class ClientRecipeBookMixin {

    /**
     * Modifies the behavior of the {@link net.minecraft.client.ClientRecipeBook#rebuildCollections} method to inject {@link net.jidb.to.base.api.recipe.CustomRecipeBookCategory} objects and their associated recipes into the vanilla client recipe book map.
     *
     * @param info A {@link org.spongepowered.asm.mixin.injection.callback.CallbackInfo} object required by mixin to manage callback behavior within the injected method.
     * @param recipeListsByCategory A map containing all the recipe display entries for the current level, grouped by their default {@link net.minecraft.world.item.crafting.RecipeBookCategory}.
     * @param byCategory A map where extended custom recipe categories and their associated recipe collections are stored. This map is modified to include the recipes from custom recipe categories.
     * @since 1.1.0
     */
    @Inject(method = "rebuildCollections", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    public void recipeCategoryHook(CallbackInfo info, Map<RecipeBookCategory, List<List<RecipeDisplayEntry>>> recipeListsByCategory, Map<ExtendedRecipeBookCategory, List<RecipeCollection>> byCategory) {
        CustomRecipeBookCategoryRegistry.INSTANCE.getAll().forEach(category -> byCategory.put(category, category.getRecipes(recipeListsByCategory)));
    }

}
