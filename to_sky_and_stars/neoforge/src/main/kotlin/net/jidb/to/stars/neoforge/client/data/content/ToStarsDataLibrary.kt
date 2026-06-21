package net.jidb.to.stars.neoforge.client.data.content

import net.jidb.to.base.api.helper.IdentifierHelper.identifier
import net.jidb.to.base.client.data.pub.collection.module.lang.StorageIdentifierLanguageClientDataCollectionModule
import net.jidb.to.base.client.data.pub.collection.module.model.block.FireBlockModelClientDataCollectionModule
import net.jidb.to.base.client.data.pub.collection.module.model.block.FullRotatingBlockModelClientDataCollectionModule
import net.jidb.to.base.client.data.pub.collection.module.model.block.SimpleBlockModelClientDataCollectionModule
import net.jidb.to.base.data.api.library.DataCollectionLibrary
import net.jidb.to.base.data.pub.collection.module.loot.CustomBlockLootDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.loot.NoopBlockLootDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.loot.OreBlockLootDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.loot.SilkBlockLootDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.recipe.CompactRecipeDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.recipe.OreRecipeDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.recipe.ShapedRecipeDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.tag.DictTagDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.tag.MiningBlockTagDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.tag.MiningBlockTagDataCollectionModule.ToolType
import net.jidb.to.base.data.pub.collection.module.tag.SimpleBlockTagDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.tag.SimpleItemTagDataCollectionModule
import net.jidb.to.base.neoforge.client.content.data.module.Cable4BlockModelClientDataCollectionModule
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.AtomicBombBlock
import net.jidb.to.stars.neoforge.client.data.module.AtomicBombBlockModelClientDataCollectionModule
import net.jidb.to.stars.neoforge.client.data.module.HeatCableBlockModelClientDataCollectionModule
import net.jidb.to.stars.neoforge.client.data.module.PowerBankModelClientDataCollectionModule
import net.jidb.to.stars.neoforge.data.module.EnergySilkBlockLootDataCollectionModule
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.resources.Identifier
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
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
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.root_advancement_unlock) }
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

    val machine_enclosure_tier_1 by this {
        addAffects(clear = true) { listOf(ToStarsMod.blocks.copper_machine_enclosure, ToStarsMod.blocks.iron_machine_enclosure).any { block -> block.identifier.path == it.identifier().path } }
        addModule { SimpleBlockModelClientDataCollectionModule(TexturedModel.CUBE_TOP_BOTTOM.updateTexture {
            val material = it.get(TextureSlot.BOTTOM)
            it.put(TextureSlot.BOTTOM, Material(Identifier.fromNamespaceAndPath(modid, "block/machine_enclosure_1_bottom"), material.forceTranslucent))
        }) }
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 1) }
    }
    val copper_machine_enclosure by this {
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            pattern("ccc")
            pattern("pCp")
            pattern("iii")
            define('p', ToStarsMod.blocks.power_cable)
            define('c', Items.COPPER_INGOT)
            define('C', Blocks.COPPER_BLOCK)
            define('i', Items.IRON_INGOT)
            event.helper.createHas(this, Items.COPPER_INGOT)
            this
        } }
    }
    val iron_machine_enclosure by this {
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            pattern("iii")
            pattern("rMr")
            pattern("rIr")
            define('r', Items.REDSTONE)
            define('M', ToStarsMod.blocks.copper_machine_enclosure)
            define('I', Blocks.IRON_BLOCK)
            define('i', Items.IRON_INGOT)
            event.helper.createHas(this, ToStarsMod.blocks.copper_machine_enclosure)
            this
        } }
    }

    val power_cable by this {
        addModule { Cable4BlockModelClientDataCollectionModule(true) }
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 1) }
        addModule { ShapedRecipeDataCollectionModule(count = 3) { collection, event ->
            pattern("ccc")
            define('c', Items.COPPER_INGOT)
            event.helper.createHas(this, Items.COPPER_INGOT)
            this
        } }
    }
    val heat_pipe by this {
        addModule(::HeatCableBlockModelClientDataCollectionModule)
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 1) }
        addModule { ShapedRecipeDataCollectionModule(count = 8) { collection, event ->
            pattern("iii")
            pattern("w w")
            pattern("iii")
            define('i', Items.IRON_INGOT)
            define('w', Items.WATER_BUCKET)
            event.helper.createHas(this, Items.IRON_INGOT)
            this
        } }
    }
    val creative_power_source by this {
        addModule(::NoopBlockLootDataCollectionModule)
    }

    val power_bank_tier_1 by this {
        addAffects(clear = true) { listOf(ToStarsMod.blocks.copper_power_bank, ToStarsMod.blocks.iron_power_bank).any { block -> block.identifier.path == it.identifier().path } }
        addModule { PowerBankModelClientDataCollectionModule(1) }
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 1) }
        addModule(::EnergySilkBlockLootDataCollectionModule)
    }
    val copper_power_bank by this {
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            pattern("bpb")
            pattern("bpb")
            pattern("RMR")
            define('b', Items.REDSTONE) //TODO battery
            define('R', Blocks.REDSTONE_BLOCK)
            define('M', ToStarsMod.blocks.copper_machine_enclosure)
            define('p', ToStarsMod.blocks.power_cable)
            event.helper.createHas(this, Items.REDSTONE /*TODO battery*/, ToStarsMod.blocks.copper_machine_enclosure)
            this
        } }
    }
    val iron_power_bank by this {
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            pattern("bpb")
            pattern("bpb")
            pattern("RMR")
            define('b', Items.REDSTONE) //TODO battery
            define('R', Blocks.REDSTONE_BLOCK)
            define('M', ToStarsMod.blocks.iron_machine_enclosure)
            define('p', ToStarsMod.blocks.power_cable)
            event.helper.createHas(this, ToStarsMod.blocks.iron_machine_enclosure)
            this
        } }
        addModule { ShapedRecipeDataCollectionModule(id = it.entry.identifier().path + "_from_upgrade") { collection, event ->
            pattern("iii")
            pattern("rMr")
            pattern("rIr")
            define('r', Items.REDSTONE)
            define('M', ToStarsMod.blocks.copper_power_bank)
            define('I', Blocks.IRON_BLOCK)
            define('i', Items.IRON_INGOT)
            event.helper.createHas(this, ToStarsMod.blocks.iron_machine_enclosure, ToStarsMod.blocks.copper_power_bank)
            this
        } }
    }

}
