package net.jidb.to.stars.neoforge.client.data.content

import net.jidb.to.base.client.data.collection.module.lang.StorageIdentifierLanguageClientDataCollectionModule
import net.jidb.to.base.client.data.collection.module.model.block.FireBlockModelClientDataCollectionModule
import net.jidb.to.base.client.data.collection.module.model.block.FullRotatingBlockModelClientDataCollectionModule
import net.jidb.to.base.data.collection.module.loot.NoopBlockLootDataCollectionModule
import net.jidb.to.base.data.collection.module.loot.OreBlockLootDataCollectionModule
import net.jidb.to.base.data.collection.module.loot.SilkBlockLootDataCollectionModule
import net.jidb.to.base.data.collection.module.recipe.CompactRecipeDataCollectionModule
import net.jidb.to.base.data.collection.module.recipe.OreRecipeDataCollectionModule
import net.jidb.to.base.data.collection.module.tag.DictTagDataCollectionModule
import net.jidb.to.base.data.collection.module.tag.MiningBlockTagDataCollectionModule
import net.jidb.to.base.data.collection.module.tag.MiningBlockTagDataCollectionModule.ToolType
import net.jidb.to.base.data.collection.module.tag.SimpleBlockTagDataCollectionModule
import net.jidb.to.base.data.library.DataCollectionLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.tags.BlockTags
import net.neoforged.neoforge.common.Tags

object ToStarsDataLibrary : DataCollectionLibrary(ToStarsMod.modid) {

    val nuclear_waste by this {
        addModule(::FullRotatingBlockModelClientDataCollectionModule)
        addModule { SimpleBlockTagDataCollectionModule(ToStarsMod.blockTags.nuke_passthrough) }
        addModule { MiningBlockTagDataCollectionModule(ToolType.SHOVEL, 3) }
        addModule(::SilkBlockLootDataCollectionModule)
    }
    val nuclear_fire by this {
        addModule(::FireBlockModelClientDataCollectionModule)
        addModule { SimpleBlockTagDataCollectionModule(BlockTags.FIRE, BlockTags.REPLACEABLE) }
        addModule(::NoopBlockLootDataCollectionModule)
    }
    val heavy_uranium_shielding by this {
        addModule { SimpleBlockTagDataCollectionModule(ToStarsMod.blockTags.nuke_shielding) }
    }

    val uranium by this {
        addAffects(clear = true) { it.identifier().path.contains("uranium") }
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 3) }
        addModule { DictTagDataCollectionModule {
            if (it.contains("enriched_uranium")) it.replace("enriched_uranium", "uranium")
            else if (it.contains("heavy_uranium")) it
            else if (!it.contains("ore")) it.replace("uranium", "raw_uranium")
            else it
        } }
        addModule(::CompactRecipeDataCollectionModule)
    }
    val uranium_ores by this {
        addAffects(clear = true) { it.identifier().path.contains("uranium_ore") }
        addModule { SimpleBlockTagDataCollectionModule(Tags.Blocks.ORE_RATES_SINGULAR) }
        addModule { OreBlockLootDataCollectionModule(ToStarsMod.items.uranium) }
        addModule(::OreRecipeDataCollectionModule)
    }
    val storage_blocks by this {
        addAffects(clear = true) { it.identifier().path.endsWith("_block") }
        addModule(::StorageIdentifierLanguageClientDataCollectionModule)
    }

}
