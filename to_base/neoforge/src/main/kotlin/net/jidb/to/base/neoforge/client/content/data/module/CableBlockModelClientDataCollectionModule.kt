package net.jidb.to.base.neoforge.client.content.data.module

import net.jidb.to.base.api.helper.IdentifierHelper.identifier
import net.jidb.to.base.client.data.api.ToDataClientHelper
import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.neoforge.client.content.data.ToBaseModels
import net.jidb.to.base.pub.block.CableBlock
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.blockstates.MultiPartGenerator
import net.minecraft.client.data.models.model.ItemModelUtils
import net.minecraft.client.data.models.model.ModelTemplate
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.client.renderer.block.dispatch.Variant
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

open class CableBlockModelClientDataCollectionModule(val center: TexturedModel.Provider, val connection: TexturedModel.Provider, val item: ModelTemplate? = null, val textures: (Identifier) -> TextureMapping = ToBaseModels.cableTexture, val rim: TexturedModel.Provider? = null) : ClientDataCollectionModule() {

    override fun generateBlockModels(collection: DataCollection<Block>, event: ModelClientDataCollectionEvent): Boolean {
        val center = Variant(center.create(collection.`object`, event.block.modelOutput))
        val connection = Variant(connection.create(collection.`object`, event.block.modelOutput))
        val rim = rim?.let { Variant(it.create(collection.`object`, event.block.modelOutput)) }
        event.block.blockStateOutput.accept(
            MultiPartGenerator.multiPart(collection.`object`).apply {
                CableBlock.attachments.forEach { (dir, prop) ->
                    with(BlockModelGenerators.condition().negatedTerm(prop, CableBlock.CableAttachType.NONE), BlockModelGenerators.variants(connection).with(BlockModelGenerators.UV_LOCK).with(ToDataClientHelper.directionalRotation(dir)))
                    with(BlockModelGenerators.condition().term(prop, CableBlock.CableAttachType.NONE), BlockModelGenerators.variants(center).with(BlockModelGenerators.UV_LOCK).with(ToDataClientHelper.directionalRotation(dir)))
                    if (rim != null) {
                        with(BlockModelGenerators.condition().term(prop, CableBlock.CableAttachType.NODE), BlockModelGenerators.variants(rim).with(BlockModelGenerators.UV_LOCK).with(ToDataClientHelper.directionalRotation(dir)))
                    }
                }
            }
        )
        return true
    }

    override fun generateItemModels(collection: DataCollection<Item>, event: ModelClientDataCollectionEvent): Boolean {
        if (item == null) return false
        val cableItem = item.create(
            collection.`object`,
            textures(collection.`object`.identifier.withPrefix("block/")),
            event.item.modelOutput
        )
        event.item.itemModelOutput.accept(collection.`object`, ItemModelUtils.plainModel(cableItem))
        return true
    }

}
