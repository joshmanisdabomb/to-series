package net.jidb.to.base.fabric.client.platform

import com.mojang.serialization.MapCodec
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry
import net.jidb.to.base.client.api.platform.ModelsClientPlatformModule
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.renderer.special.SpecialModelRenderer
import net.minecraft.client.renderer.special.SpecialModelRenderers
import net.minecraft.resources.Identifier

object ModelsFabricClientPlatformModule : ModelsClientPlatformModule() {

    override fun createLayer(model: Identifier, layer: String?, provider: () -> LayerDefinition): ModelLayerLocation {
        val location = ModelLayerLocation(model, layer ?: "main")
        ModelLayerRegistry.registerModelLayer(location, provider)
        return location
    }

    override fun <R : SpecialModelRenderer.Unbaked<*>> registerSpecialModel(model: Identifier, renderer: MapCodec<R>) {
        SpecialModelRenderers.ID_MAPPER.put(model, renderer)
    }

}
