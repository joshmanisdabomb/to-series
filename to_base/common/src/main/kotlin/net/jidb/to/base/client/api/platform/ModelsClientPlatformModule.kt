package net.jidb.to.base.client.api.platform

import com.mojang.serialization.MapCodec
import net.minecraft.client.color.block.BlockTintSource
import net.minecraft.client.color.item.ItemTintSource
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.renderer.special.SpecialModelRenderer
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Block

/**
 * A [ClientPlatform]-specific module that defines a cross-platform contract for handling models.
 * This module has code for creating a [ModelLayerLocation], and for registering a [SpecialModelRenderer], a [BlockTintSource] and an [ItemTintSource].
 *
 * @since 0.6.0
 */
abstract class ModelsClientPlatformModule {

    /**
     * Creates and registers a [ModelLayerLocation] for the given model, along with the [LayerDefinition] describing its geometry.
     *
     * @param model The [Identifier] of the model the layer belongs to.
     * @param layer The name of the layer, or `null` to use the default layer name. Defaults to `null`.
     * @param provider A function that builds the [LayerDefinition] for the layer.
     * @return The [ModelLayerLocation] the layer was registered under.
     * @since 0.6.0
     */
    abstract fun createLayer(model: Identifier, layer: String? = null, provider: () -> LayerDefinition): ModelLayerLocation

    /**
     * Registers a [SpecialModelRenderer], which draws an item with code rather than from a baked model, as vanilla does for chests and shields.
     *
     * @param R The unbaked type of the special model renderer.
     * @param model The [Identifier] to register the renderer under, as it is named in the item model JSON.
     * @param renderer A [MapCodec] that reads the unbaked renderer from its JSON definition.
     * @since 0.6.0
     */
    abstract fun <R : SpecialModelRenderer.Unbaked<*>> registerSpecialModel(model: Identifier, renderer: MapCodec<R>)

    /**
     * Registers a [BlockTintSource] against one or more blocks, which colours their model at render time.
     *
     * @param modid The unique namespace of the mod registering the tint.
     * @param tint The tint source that produces the colour.
     * @param blocks The blocks the tint source applies to.
     * @since 0.7.0
     */
    abstract fun registerBlockTint(modid: String, tint: BlockTintSource, vararg blocks: Block)

    /**
     * Registers a type of [ItemTintSource], which item model JSON can then refer to by identifier to colour a layer.
     *
     * @param S The type of the item tint source.
     * @param id The [Identifier] to register the tint source type under.
     * @param source A [MapCodec] that reads the tint source from its JSON definition.
     * @since 0.7.0
     */
    abstract fun <S : ItemTintSource> registerItemTint(id: Identifier, source: MapCodec<S>)

}
