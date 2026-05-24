package net.jidb.to.stars.neoforge.client.data.content

import net.jidb.to.base.client.data.collection.module.lang.StorageIdentifierLanguageClientDataCollectionModule
import net.jidb.to.base.client.data.collection.module.model.block.FireBlockModelClientDataCollectionModule
import net.jidb.to.base.client.data.collection.module.model.block.FullRotatingBlockModelClientDataCollectionModule
import net.jidb.to.base.data.collection.module.loot.CustomBlockLootDataCollectionModule
import net.jidb.to.base.data.collection.module.loot.NoopBlockLootDataCollectionModule
import net.jidb.to.base.data.collection.module.loot.OreBlockLootDataCollectionModule
import net.jidb.to.base.data.collection.module.loot.SilkBlockLootDataCollectionModule
import net.jidb.to.base.data.collection.module.recipe.CompactRecipeDataCollectionModule
import net.jidb.to.base.data.collection.module.recipe.OreRecipeDataCollectionModule
import net.jidb.to.base.data.collection.module.recipe.ShapedRecipeDataCollectionModule
import net.jidb.to.base.data.collection.module.tag.DictTagDataCollectionModule
import net.jidb.to.base.data.collection.module.tag.MiningBlockTagDataCollectionModule
import net.jidb.to.base.data.collection.module.tag.MiningBlockTagDataCollectionModule.ToolType
import net.jidb.to.base.data.collection.module.tag.SimpleBlockTagDataCollectionModule
import net.jidb.to.base.data.collection.module.tag.SimpleItemTagDataCollectionModule
import net.jidb.to.base.data.library.DataCollectionLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.AtomicBombBlock
import net.jidb.to.stars.neoforge.client.data.module.AtomicBombBlockModelClientDataCollectionModule
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.level.block.Blocks
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
        addModule { ShapedRecipeDataCollectionModule(count = 24) { collection, event ->
            pattern("ccc")
            pattern("uiu")
            pattern("ccc")
            define('c', Tags.Items.CONCRETE_POWDERS)
            define('i', Blocks.IRON_BLOCK)
            define('u', ToStarsMod.items.heavy_uranium)
            event.helper.createHas(this, ToStarsMod.items.heavy_uranium)
            this
        } }
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
    val enriched_uranium by this {
        addAffects(clear = true) { it.identifier().path.contains("enriched_uranium") }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.enriched_uranium) }
    }

    val atomic_bomb by this {
        addModule(::AtomicBombBlockModelClientDataCollectionModule)
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 1) }
        addModule { CustomBlockLootDataCollectionModule { collection, event -> event.helper.propertyBlockLoot(
            ToStarsMod.blocks.atomic_bomb,
            AtomicBombBlock.SEGMENT,
            AtomicBombBlock.AtomicBombSegment.MIDDLE)
        } }
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            pattern("ccc")
            pattern("bdc")
            pattern("ccc")
            define('c', Blocks.IRON_BLOCK)
            define('b', ItemTags.BUTTONS)
            define('d', Blocks.DISPENSER)
            event.helper.createHas(this, ToStarsMod.itemTags.enriched_uranium)
            this
        } }
    }

}
