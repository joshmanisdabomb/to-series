package net.jidb.to.base.client.pub.library

import com.mojang.serialization.MapCodec
import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.client.service.ClientServices
import net.minecraft.client.renderer.special.SpecialModelRenderer
import net.minecraft.resources.Identifier

/**
 * [SimpleLibrary] implementation that registers a type of [SpecialModelRenderer], and provides access to those types in one place.
 * A special model is drawn by code rather than baked from JSON, as vanilla does for chests and shields, so what a library entry holds is the codec that reads the unbaked renderer from an item model.
 *
 * @param modid The mod ID associated with the library.
 * @since 0.6.0
 */
open class SpecialModelLibrary(modid: String) : SimpleLibrary<MapCodec<out SpecialModelRenderer.Unbaked<*>>>(modid) {

    /**
     * Declares a type of [SpecialModelRenderer], registering it with the client platform when the library is built.
     *
     * @param R The unbaked type of the special model renderer.
     * @param model A [MapCodec] that reads the unbaked renderer from its JSON definition.
     * @param id The identifier to register the renderer under, or `null` to use the identifier of the entry. Defaults to `null`.
     * @return The [Library.LibraryEntry] for the declared property.
     * @since 0.6.0
     */
    operator fun <R : SpecialModelRenderer.Unbaked<*>> invoke(model: MapCodec<R>, id: Identifier? = null) = invoke({
        val model = it()
        ClientServices.platform.models.registerSpecialModel(id ?: this.id, model);
        { model }
    }, { model })

}
