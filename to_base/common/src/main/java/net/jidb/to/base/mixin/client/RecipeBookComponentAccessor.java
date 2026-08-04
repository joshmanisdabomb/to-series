package net.jidb.to.base.mixin.client;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Exposes private fields in the recipe book component.
 * Allows custom screens to supply their own book instance.
 *
 * @see net.minecraft.client.gui.screens.recipebook.RecipeBookComponent
 */
@Mixin(RecipeBookComponent.class)
public interface RecipeBookComponentAccessor {

    /**
     * Replaces the recipe book that the component reads its collections from.
     *
     * @param book the recipe book to display.
     * @see net.minecraft.client.gui.screens.recipebook.RecipeBookComponent#book
     */
    @Accessor("book")
    void to_base$setBook(ClientRecipeBook book);

}
