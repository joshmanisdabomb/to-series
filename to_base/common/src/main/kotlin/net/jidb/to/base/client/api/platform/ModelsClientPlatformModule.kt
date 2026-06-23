package net.jidb.to.base.client.api.platform

import com.mojang.serialization.MapCodec
import net.minecraft.client.color.block.BlockTintSource
import net.minecraft.client.color.item.ItemTintSource
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.renderer.special.SpecialModelRenderer
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Block

abstract class ModelsClientPlatformModule {

    abstract fun createLayer(model: Identifier, layer: String? = null, provider: () -> LayerDefinition): ModelLayerLocation

    abstract fun <R : SpecialModelRenderer.Unbaked<*>> registerSpecialModel(model: Identifier, renderer: MapCodec<R>)

    abstract fun registerBlockTint(modid: String, tint: BlockTintSource, vararg blocks: Block)
    abstract fun <S : ItemTintSource> registerItemTint(id: Identifier, source: MapCodec<S>)
}