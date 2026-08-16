package net.jidb.to.base.fabric.client.platform

import com.mojang.serialization.MapCodec
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry
import net.jidb.to.base.client.api.platform.ModelsClientPlatformModule
import net.minecraft.client.color.block.BlockTintSource
import net.minecraft.client.color.item.ItemTintSource
import net.minecraft.client.color.item.ItemTintSources
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.renderer.special.SpecialModelRenderer
import net.minecraft.client.renderer.special.SpecialModelRenderers
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Block

object ModelsFabricClientPlatformModule : ModelsClientPlatformModule() {

    override fun createLayer(model: Identifier, layer: String?, provider: () -> LayerDefinition): ModelLayerLocation {
        val location = ModelLayerLocation(model, layer ?: "main")
        ModelLayerRegistry.registerModelLayer(location, provider)
        return location
    }

    override fun <R : SpecialModelRenderer.Unbaked<*>> registerSpecialModel(model: Identifier, renderer: MapCodec<R>) {
        SpecialModelRenderers.ID_MAPPER.put(model, renderer)
    }

    override fun registerBlockTint(modid: String, tint: BlockTintSource, vararg blocks: Block) {
        BlockColorRegistry.register(listOf(tint), *blocks)
    }

    override fun <S : ItemTintSource> registerItemTint(id: Identifier, source: MapCodec<S>) {
        ItemTintSources.ID_MAPPER.put(id, source)
    }

}
