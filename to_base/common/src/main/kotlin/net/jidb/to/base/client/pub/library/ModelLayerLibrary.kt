package net.jidb.to.base.client.pub.library

import net.jidb.to.base.api.library.AdvancedLibrary
import net.jidb.to.base.api.library.Library
import net.jidb.to.base.client.service.ClientServices
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.resources.Identifier

open class ModelLayerLibrary(modid: String) : AdvancedLibrary<Identifier, ModelLayerLocation>(modid) {

    operator fun invoke(layer: String? = null, model: Identifier? = null, provider: () -> LayerDefinition): Library<Identifier, ModelLayerLocation>.LibraryEntry<Identifier, ModelLayerLocation> {
        val library = this as Library<Identifier, ModelLayerLocation>
        return library.LibraryEntry({
            val location = ClientServices.platform.models.createLayer(it(), layer, provider);
            { location }
        }, { model ?: it.id })
    }

}
