package net.jidb.to.base.client.pub.library

import net.jidb.to.base.api.library.AdvancedLibrary
import net.jidb.to.base.api.library.Library
import net.jidb.to.base.client.service.ClientServices
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.resources.Identifier

/**
 * [AdvancedLibrary] implementation that creates a [ModelLayerLocation] for each model layer the mod draws with, and provides access to those locations in one place.
 * The [LayerDefinition] describing the geometry is registered along with the location, so declaring an entry here is all that a renderer needs to be able to bake the layer.
 *
 * @param modid The mod ID associated with the library.
 * @since 0.6.0
 */
open class ModelLayerLibrary(modid: String) : AdvancedLibrary<Identifier, ModelLayerLocation>(modid) {

    /**
     * Declares a model layer, creating it through the client platform when the library is built.
     *
     * @param layer The name of the layer, or `null` to use the default layer name. Defaults to `null`.
     * @param model The identifier of the model the layer belongs to, or `null` to use the identifier of the entry. Defaults to `null`.
     * @param provider A function that builds the [LayerDefinition] for the layer.
     * @return The [Library.LibraryEntry] for the declared property.
     * @since 0.6.0
     */
    operator fun invoke(layer: String? = null, model: Identifier? = null, provider: () -> LayerDefinition): Library<Identifier, ModelLayerLocation>.LibraryEntry<Identifier, ModelLayerLocation> {
        val library = this as Library<Identifier, ModelLayerLocation>
        return library.LibraryEntry({
            val location = ClientServices.platform.models.createLayer(it(), layer, provider);
            { location }
        }, { model ?: it.id })
    }

}
