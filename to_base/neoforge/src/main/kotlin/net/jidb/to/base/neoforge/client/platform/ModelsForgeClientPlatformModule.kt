package net.jidb.to.base.neoforge.client.platform

import com.mojang.serialization.MapCodec
import net.jidb.to.base.client.api.platform.ModelsClientPlatformModule
import net.jidb.to.base.neoforge.platform.DeferredForgeEventRegistry
import net.minecraft.client.color.block.BlockTintSource
import net.minecraft.client.color.item.ItemTintSource
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.renderer.special.SpecialModelRenderer
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent

object ModelsForgeClientPlatformModule : ModelsClientPlatformModule() {

    val layerRegistry = DeferredForgeEventRegistry(EntityRenderersEvent.RegisterLayerDefinitions::class.java)

    val specialRegistry = DeferredForgeEventRegistry(RegisterSpecialModelRendererEvent::class.java)

    val itemTintRegistry = DeferredForgeEventRegistry(RegisterColorHandlersEvent.ItemTintSources::class.java)

    val blockTintRegistry = DeferredForgeEventRegistry(RegisterColorHandlersEvent.BlockTintSources::class.java)

    override fun createLayer(model: Identifier, layer: String?, provider: () -> LayerDefinition): ModelLayerLocation {
        val location = ModelLayerLocation(model, layer ?: "main")
        layerRegistry.register(model.namespace) { event ->
            event.registerLayerDefinition(location, provider)
        }
        return location
    }

    override fun <R : SpecialModelRenderer.Unbaked<*>> registerSpecialModel(model: Identifier, renderer: MapCodec<R>) {
        specialRegistry.register(model.namespace) { event ->
            event.register(model, renderer)
        }
    }

    override fun registerBlockTint(modid: String, tint: BlockTintSource, vararg blocks: Block) {
        blockTintRegistry.register(modid) { event ->
            event.register(listOf(tint), *blocks)
        }
    }

    override fun <S : ItemTintSource> registerItemTint(id: Identifier, source: MapCodec<S>) {
        itemTintRegistry.register(id.namespace) { event ->
            event.register(id, source)
        }
    }

}
