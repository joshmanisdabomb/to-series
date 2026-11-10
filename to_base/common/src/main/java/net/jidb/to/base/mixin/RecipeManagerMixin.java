package net.jidb.to.base.mixin;

import net.jidb.to.base.hooks.recipe.RecipePropertySetRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipePropertySet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * A mixin that modifies the behavior of the {@link net.minecraft.world.item.crafting.RecipeManager} to allow the injection of {@link net.minecraft.world.item.crafting.RecipeManager.IngredientExtractor} via {@link net.jidb.to.base.hooks.recipe.RecipePropertySetRegistry}.
 *
 * This mixin intercepts the {@link net.minecraft.world.item.crafting.RecipeManager#finalizeRecipeLoading} method.
 *
 * @see net.minecraft.world.item.crafting.RecipeManager
 * @see net.jidb.to.base.hooks.recipe.RecipePropertySetRegistry
 * @since 1.1.0
 */
@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {

    @Shadow
    private Map<ResourceKey<RecipePropertySet>, RecipePropertySet> propertySets;

    @Shadow
    private RecipeMap recipes;

    @Inject(method = "finalizeRecipeLoading", at = @At(value = "TAIL"))
    public void recipeCategoryHook(FeatureFlagSet enabledFlags, CallbackInfo info) {
        Map<ResourceKey<RecipePropertySet>, RecipePropertySet> properties = new HashMap<>();
        properties.putAll(propertySets);
        properties.putAll(RecipePropertySetRegistry.INSTANCE.buildPropertySets(recipes, enabledFlags));
        propertySets = properties;
    }

}
