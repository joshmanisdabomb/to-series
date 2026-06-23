package net.jidb.to.stars.neoforge.client.data.module

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.item.render.EnergyStorageSpecialRenderer
import net.jidb.to.stars.neoforge.client.data.ToStarsModels
import net.minecraft.client.data.models.model.ItemModelUtils
import net.minecraft.client.data.models.model.ModelLocationUtils
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.resources.Identifier
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

class PowerBankModelClientDataCollectionModule(val tier: Int) : ClientDataCollectionModule() {

    override fun generateBlockModels(collection: DataCollection<Block>, event: ModelClientDataCollectionEvent): Boolean {
        val top = ToStarsModels.POWER_BANK.updateTemplate { it.extend().suffix("_top").build() }.updateTexture {
            it.put(TextureSlot.BOTTOM, Material(Identifier.fromNamespaceAndPath(ToStarsMod.modid, "block/machine_enclosure_${tier}_bottom")))
            it.copySlot(TextureSlot.SIDE, TextureSlot.FRONT)
            it.copySlot(TextureSlot.BOTTOM, TextureSlot.END)
        }
        val side = ToStarsModels.POWER_BANK.updateTemplate { it.extend().suffix("_side").build() }.updateTexture {
            it.put(TextureSlot.BOTTOM, Material(Identifier.fromNamespaceAndPath(ToStarsMod.modid, "block/machine_enclosure_${tier}_bottom")))
            it.copySlot(TextureSlot.INNER_TOP, TextureSlot.TOP)
            it.copySlot(TextureSlot.BOTTOM, TextureSlot.END)
        }
        val bottom = ToStarsModels.POWER_BANK.updateTemplate { it.extend().suffix("_bottom").build() }.updateTexture {
            it.put(TextureSlot.BOTTOM, Material(Identifier.fromNamespaceAndPath(ToStarsMod.modid, "block/machine_enclosure_${tier}_bottom")))
            it.put(TextureSlot.END, Material(Identifier.fromNamespaceAndPath(ToStarsMod.modid, "block/power_bank_${tier}_bottom")))
            it.copySlot(TextureSlot.INNER_TOP, TextureSlot.TOP)
            it.copySlot(TextureSlot.SIDE, TextureSlot.FRONT)
        }

        event.blockEx.createUprightDirectionalBlock(collection.`object`, top, side, bottom)
        return true
    }

    override fun generateItemModels(collection: DataCollection<Item>, event: ModelClientDataCollectionEvent): Boolean {
        val block = (collection.`object` as BlockItem).block
        event.item.itemModelOutput.accept(collection.`object`, ItemModelUtils.specialModel(
            ModelLocationUtils.getModelLocation(block, "_top"),
            EnergyStorageSpecialRenderer.Unbaked(block)
        ))
        return true
    }

}
