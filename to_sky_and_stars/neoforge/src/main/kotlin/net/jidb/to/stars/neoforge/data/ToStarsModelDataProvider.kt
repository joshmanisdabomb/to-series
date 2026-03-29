package net.jidb.to.stars.neoforge.data

import net.jidb.to.base.neoforge.data.model.ExtendedBlockModelGenerators
import net.jidb.to.stars.ToStarsMod
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.data.PackOutput

class ToStarsModelDataProvider(output: PackOutput) : ModelProvider(output, ToStarsMod.MOD_ID) {
    override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators) {
        val extendedModels = ExtendedBlockModelGenerators(blockModels)

        extendedModels.createFullRotatedVariantBlock(ToStarsMod.blocks.nuclear_waste)
        extendedModels.createFire(ToStarsMod.blocks.nuclear_fire)

        itemModels.generateFlatItem(ToStarsMod.items.test_item, ModelTemplates.FLAT_ITEM)
    }
}