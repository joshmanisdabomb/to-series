package net.jidb.to.base.neoforge.client.platform

import com.mojang.serialization.MapCodec
import net.jidb.to.base.client.api.platform.ModelsClientPlatformModule
import net.jidb.to.base.neoforge.platform.DeferredForgeEventRegistry
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.renderer.special.SpecialModelRenderer
import net.minecraft.resources.Identifier
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent

object ModelsForgeClientPlatformModule : ModelsClientPlatformModule() {

    val layer_registry = DeferredForgeEventRegistry(EntityRenderersEvent.RegisterLayerDefinitions::class.java)
    val special_registry = DeferredForgeEventRegistry(RegisterSpecialModelRendererEvent::class.java)

    override fun createLayer(model: Identifier, layer: String?, provider: () -> LayerDefinition): ModelLayerLocation {
        val location = ModelLayerLocation(model, layer ?: "main")
        layer_registry.register(model.namespace) { event ->
            event.registerLayerDefinition(location, provider)
        }
        return location
    }

    override fun <R : SpecialModelRenderer.Unbaked<*>> registerSpecialModel(model: Identifier, renderer: MapCodec<R>) {
        special_registry.register(model.namespace) { event ->
            event.register(model, renderer)
        }
    }

}
