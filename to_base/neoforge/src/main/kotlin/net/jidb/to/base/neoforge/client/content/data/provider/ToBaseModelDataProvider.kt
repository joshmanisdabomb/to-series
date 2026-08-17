package net.jidb.to.base.neoforge.client.content.data.provider

import net.jidb.to.base.ToBaseMod
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.client.data.models.model.ItemModelUtils
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.renderer.item.ClientItem
import net.minecraft.client.renderer.item.ClientItem.Properties
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.core.Holder
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.util.stream.Stream

class ToBaseModelDataProvider(output: PackOutput) : ModelProvider(output, ToBaseMod.modid) {

    override fun getKnownBlocks() = Stream.empty<Holder<Block>>()
    override fun getKnownItems() = Stream.empty<Holder<Item>>()

    override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators) {
        val icon = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "icon")
        val iconModel = ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(icon, TextureMapping.layer0(Material(icon.withPrefix("item/"))), itemModels.modelOutput))
        val iconItem = ClientItem(iconModel, Properties.DEFAULT)
        itemModels.itemModelOutput.register(icon, iconItem)
    }

}
