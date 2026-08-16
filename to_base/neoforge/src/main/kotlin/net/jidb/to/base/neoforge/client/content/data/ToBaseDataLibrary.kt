package net.jidb.to.base.neoforge.client.content.data

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.data.pub.collection.module.lang.SimpleLanguageClientDataCollectionModule
import net.jidb.to.base.client.data.pub.collection.module.model.block.HorizontalBlockModelClientDataCollectionModule
import net.jidb.to.base.content.ToBaseItemTagLibrary
import net.jidb.to.base.content.block.ResearchDeskBlock
import net.jidb.to.base.data.api.library.DataCollectionLibrary
import net.jidb.to.base.data.pub.collection.module.loot.CustomBlockLootDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.loot.SilkBlockLootDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.recipe.ShapedRecipeDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.tag.MiningBlockTagDataCollectionModule
import net.jidb.to.base.neoforge.client.content.data.module.ResearchDeskBlockModelClientDataCollectionModule
import net.jidb.to.base.service.Services
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items

object ToBaseDataLibrary : DataCollectionLibrary(ToBaseMod.modid) {

    val test_block by this {
        addModule { SilkBlockLootDataCollectionModule(ToBaseMod.content.items.test_item) }
    }

    val test_block_2 by this {
        addModule { HorizontalBlockModelClientDataCollectionModule(TexturedModel.ORIENTABLE.updateTexture {
            val material = it.get(TextureSlot.BOTTOM)
            it.put(TextureSlot.BOTTOM, Material(material.sprite().withPath { it.replace("_bottom", "_top") }, material.forceTranslucent))
        }) }
    }

    val research_desk by this {
        addModule { SimpleLanguageClientDataCollectionModule("Researcher's Desk") }
        addModule { CustomBlockLootDataCollectionModule { collection, event -> event.helper.propertyBlockLoot(
            ToBaseMod.content.blocks.research_desk,
            ResearchDeskBlock.segment,
            ResearchDeskBlock.ResearchDeskSegment.LEFT)
        } }
        addModule { MiningBlockTagDataCollectionModule(MiningBlockTagDataCollectionModule.ToolType.AXE) }
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            pattern("bpi")
            pattern("SSS")
            pattern("W B")
            define('W', ItemTags.PLANKS)
            define('S', ItemTags.WOODEN_SLABS)
            define('B', Services.platform.tags.getCommonItem("bookshelves")!!)
            define('b', Items.BOOK)
            define('p', Items.PAPER)
            define('i', Items.INK_SAC)
            event.helper.createHas(this, ToBaseItemTagLibrary.research_desk_unlock)
            this
        } }
        addModule(::ResearchDeskBlockModelClientDataCollectionModule)
    }

}
