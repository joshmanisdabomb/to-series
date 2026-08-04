package net.jidb.to.base.mixin.client;

import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.BiConsumer;

/**
 * Exposes the output consumer and private model helpers of Minecraft's item model generator.
 * This lets the To Lay the Foundations common source set use them.
 *
 * @see net.minecraft.client.data.models.ItemModelGenerators
 */
@Mixin(ItemModelGenerators.class)
public interface ItemModelGeneratorsAccessor {

    /**
     * Returns the sink that finished model instances are handed to.
     *
     * @return the model output consumer.
     * @see net.minecraft.client.data.models.ItemModelGenerators#modelOutput
     */
    @Accessor("modelOutput")
    BiConsumer<Identifier, ModelInstance> to_base$getModelOutput();

    /**
     * Returns the sink that maps items onto the model that renders them.
     *
     * @return the item model output.
     * @see net.minecraft.client.data.models.ItemModelGenerators#itemModelOutput
     */
    @Accessor("itemModelOutput")
    ItemModelOutput to_base$getItemModelOutput();

    /**
     * Generates a flat sprite model for an item from the given template.
     *
     * @param item the item to generate a model for.
     * @param modelTemplate the model template to build from.
     * @see net.minecraft.client.data.models.ItemModelGenerators#generateFlatItem
     */
    @Invoker("generateFlatItem")
    void to_base$generateFlatItem(Item item, ModelTemplate modelTemplate);

    /**
     * Generates a flat sprite model with a second, tinted overlay layer.
     *
     * @param item the item to generate a model for.
     * @param overlaySuffix appended to the item's default texture path.
     * @param overlayTint supplies the tinter the overlay layer will use.
     * @see net.minecraft.client.data.models.ItemModelGenerators#generateItemWithTintedOverlay
     */
    @Invoker("generateItemWithTintedOverlay")
    void to_base$generateItemWithTintedOverlay(Item item, String overlaySuffix, ItemTintSource overlayTint);

}
