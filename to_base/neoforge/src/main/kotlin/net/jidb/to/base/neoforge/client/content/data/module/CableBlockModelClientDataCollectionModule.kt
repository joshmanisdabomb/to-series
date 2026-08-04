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

/**
 * A [ClientDataCollectionModule] generating the models of a cable, which is drawn as one piece per direction rather than as a single model, since which sides it reaches out to depends on what it is next to.
 *
 * Each of the six directions gets a connection piece where the cable joins something and a plain end piece where it does not.
 * A rim can be drawn on top where the cable meets a machine rather than another cable, which is what makes the join look finished.
 *
 * @property center The model drawn where the cable reaches out in no direction.
 * @property connection The model drawn where it reaches out.
 * @property item The model the cable's own item is drawn from, or `null` to leave the item to another module. Defaults to `null`.
 * @property textures The textures filling that item model, given the name of the block's texture. Defaults to the ordinary cable mapping.
 * @property rim The model drawn over a join to a machine, or `null` where nothing extra is drawn. Defaults to `null`.
 * @since 0.6.0
 */
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
