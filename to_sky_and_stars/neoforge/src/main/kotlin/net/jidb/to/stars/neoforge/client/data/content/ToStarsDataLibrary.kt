package net.jidb.to.stars.neoforge.client.data.content

import net.jidb.to.base.api.helper.IdentifierHelper.identifier
import net.jidb.to.base.client.data.pub.collection.module.lang.IdentifierLanguageClientDataCollectionModule
import net.jidb.to.base.client.data.pub.collection.module.lang.StorageIdentifierLanguageClientDataCollectionModule
import net.jidb.to.base.client.data.pub.collection.module.model.block.FireBlockModelClientDataCollectionModule
import net.jidb.to.base.client.data.pub.collection.module.model.block.FullRotatingBlockModelClientDataCollectionModule
import net.jidb.to.base.client.data.pub.collection.module.model.block.SimpleBlockModelClientDataCollectionModule
import net.jidb.to.base.client.data.pub.collection.module.model.item.TintedItemModelClientDataCollectionModule
import net.jidb.to.base.data.api.library.DataCollectionLibrary
import net.jidb.to.base.data.pub.collection.module.loot.*
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
import net.jidb.to.stars.client.item.tint.BatteryItemTint
import net.jidb.to.stars.neoforge.client.data.module.*
import net.jidb.to.stars.neoforge.data.module.EnergySilkBlockLootDataCollectionModule
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
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
            define('b', TagKey.create(Registries.ITEM, Identifier.withDefaultNamespace("buttons")))
            define('d', Blocks.DISPENSER)
            event.helper.createHas(this, ToStarsMod.itemTags.enriched_uranium)
            this
        } }
    }

    val machine_enclosure_tier_1 by this {
        addAffects(clear = true) { (it.identifier().path.startsWith("copper_") || it.identifier().path.startsWith("gold_")) && it.identifier().path.endsWith("_machine_enclosure") }
        addModule { SimpleBlockModelClientDataCollectionModule(TexturedModel.CUBE_TOP_BOTTOM.updateTexture {
            val material = it.get(TextureSlot.BOTTOM)
            it.put(TextureSlot.BOTTOM, Material(Identifier.fromNamespaceAndPath(modid, "block/machine_enclosure_1_bottom"), material.forceTranslucent))
        }) }
    }
    val copper_machine_enclosure by this {
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            pattern("ccc")
            pattern("pCp")
            pattern("iii")
            define('p', ToStarsMod.blocks.power_cable)
            define('c', Items.COPPER_INGOT)
            define('C', Blocks.COPPER_BLOCK.weathering.unaffected)
            define('i', Items.IRON_INGOT)
            event.helper.createHas(this, Items.COPPER_INGOT)
            this
        } }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.root_advancement_unlock) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.copper_power_bank_unlock) }
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 1) }
    }
    val gold_machine_enclosure by this {
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            pattern("ggg")
            pattern("rMr")
            pattern("iGi")
            define('r', Items.REDSTONE)
            define('M', ToStarsMod.blocks.copper_machine_enclosure)
            define('G', Blocks.GOLD_BLOCK)
            define('g', Items.GOLD_INGOT)
            define('i', Items.IRON_INGOT)
            event.helper.createHas(this, ToStarsMod.blocks.copper_machine_enclosure)
            this
        } }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.gold_battery_unlock) }
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 2) }
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

    val power_banks by this {
        addAffects(clear = true) { it.identifier().path.endsWith("_power_bank") }
        addModule(::EnergySilkBlockLootDataCollectionModule)
    }
    val power_bank_tier_1 by this {
        addAffects(clear = true) { (it.identifier().path.startsWith("copper_") || it.identifier().path.startsWith("gold_")) && it.identifier().path.endsWith("_power_bank") }
        addModule { PowerBankModelClientDataCollectionModule(1) }
    }
    val copper_power_bank by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 1) }
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            pattern("bpb")
            pattern("bpb")
            pattern("RMR")
            define('b', ToStarsMod.itemTags.batteries)
            define('R', Blocks.REDSTONE_BLOCK)
            define('M', ToStarsMod.blocks.copper_machine_enclosure)
            define('p', ToStarsMod.blocks.power_cable)
            event.helper.createHas(this, ToStarsMod.itemTags.copper_power_bank_unlock)
            this
        } }
    }
    val gold_power_bank by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 2) }
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            pattern("bpb")
            pattern("bpb")
            pattern("RMR")
            define('b', ToStarsMod.itemTags.batteries)
            define('R', Blocks.REDSTONE_BLOCK)
            define('M', ToStarsMod.blocks.gold_machine_enclosure)
            define('p', ToStarsMod.blocks.power_cable)
            event.helper.createHas(this, ToStarsMod.blocks.gold_machine_enclosure)
            this
        } }
        addModule { ShapedRecipeDataCollectionModule(id = it.entry.identifier().path + "_from_upgrade") { collection, event ->
            pattern("ggg")
            pattern("rMr")
            pattern("iGi")
            define('r', Items.REDSTONE)
            define('M', ToStarsMod.blocks.copper_power_bank)
            define('G', Blocks.GOLD_BLOCK)
            define('g', Items.GOLD_INGOT)
            define('i', Items.IRON_INGOT)
            event.helper.createHas(this, ToStarsMod.blocks.copper_power_bank)
            this
        } }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.gold_battery_unlock) }
    }

    val batteries by this {
        addAffects(clear = true) { it.identifier().path.endsWith("_battery") }
        addModule { TintedItemModelClientDataCollectionModule(BatteryItemTint()) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.batteries) }
    }
    val copper_battery by this {
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            pattern(" n ")
            pattern("crc")
            pattern("crc")
            define('n', Items.IRON_NUGGET)
            define('c', Items.COPPER_INGOT)
            define('r', Items.REDSTONE)
            event.helper.createHas(this, ToStarsMod.blocks.copper_machine_enclosure)
            this
        } }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.copper_power_bank_unlock) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.gold_battery_unlock) }
    }
    val gold_battery by this {
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            pattern(" i ")
            pattern("gbg")
            pattern("grg")
            define('b', ToStarsMod.items.copper_battery)
            define('i', Items.IRON_INGOT)
            define('g', Items.GOLD_INGOT)
            define('r', Blocks.REDSTONE_BLOCK)
            event.helper.createHas(this, ToStarsMod.itemTags.gold_battery_unlock)
            this
        } }
    }

    val boiler by this {
        addModule(::BoilingCauldronBlockModelClientDataCollectionModule)
        addModule { SimpleBlockTagDataCollectionModule(BlockTags.CAULDRONS) }
        addModule { SimpleBlockLootDataCollectionModule(Items.CAULDRON) }
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE) }
    }

    val generators by this {
        addAffects(clear = true) { it.identifier().path.endsWith("_generator") }
        addModule { IdentifierLanguageClientDataCollectionModule { it.replace("solid", "solid-fired").replace("fluid", "liquid-fired") } }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.heat_generators) }
    }
    val generator_tier_1 by this {
        addAffects(clear = true) { (it.identifier().path.startsWith("copper_") || it.identifier().path.startsWith("gold_")) && it.identifier().path.endsWith("_generator") }
        addModule {
            val unlit = TexturedModel.ORIENTABLE.updateTexture {
                val bottom = it.get(TextureSlot.BOTTOM)
                it.put(TextureSlot.BOTTOM, Material(Identifier.fromNamespaceAndPath(modid, "block/machine_enclosure_1_bottom"), bottom.forceTranslucent))
                val side = it.get(TextureSlot.SIDE)
                it.put(TextureSlot.SIDE, Material(side.sprite.withPath { it.replace("_solid_generator", "_machine_enclosure") }, side.forceTranslucent))
                val top = it.get(TextureSlot.TOP)
                it.put(TextureSlot.TOP, Material(top.sprite.withPath { it.replace("solid_", "").replace("fluid_", "").replace("_top", "") }, top.forceTranslucent))
                val front = it.get(TextureSlot.FRONT)
                it.put(TextureSlot.FRONT, Material(front.sprite.withPath { it.replace("_front", "") }, front.forceTranslucent))
            }
            LitMachineBlockModelClientDataCollectionModule(unlit, unlit.updateTexture {
                val top = it.get(TextureSlot.TOP)
                it.put(TextureSlot.TOP, Material(top.sprite.withSuffix("_lit"), top.forceTranslucent))
                val front = it.get(TextureSlot.FRONT)
                it.put(TextureSlot.FRONT, Material(front.sprite.withSuffix("_lit"), front.forceTranslucent))
            })
        }
    }
    val copper_solid_generator by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 1) }
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            pattern("e")
            pattern("m")
            pattern("f")
            define('e', Blocks.IRON_BARS)
            define('m', ToStarsMod.blocks.copper_machine_enclosure)
            define('f', Blocks.FURNACE)
            event.helper.createHas(this, ToStarsMod.blocks.copper_machine_enclosure)
            this
        } }
    }
    val gold_solid_generator by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 2) }
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            pattern("e")
            pattern("m")
            pattern("f")
            define('e', Blocks.IRON_BARS)
            define('m', ToStarsMod.blocks.gold_machine_enclosure)
            define('f', Blocks.FURNACE)
            event.helper.createHas(this, ToStarsMod.blocks.gold_machine_enclosure)
            this
        } }
        addModule { ShapedRecipeDataCollectionModule(id = it.entry.identifier().path + "_from_upgrade") { collection, event ->
            pattern("ggg")
            pattern("rMr")
            pattern("iGi")
            define('r', Items.REDSTONE)
            define('M', ToStarsMod.blocks.copper_solid_generator)
            define('G', Blocks.GOLD_BLOCK)
            define('g', Items.GOLD_INGOT)
            define('i', Items.IRON_INGOT)
            event.helper.createHas(this, ToStarsMod.blocks.copper_solid_generator)
            this
        } }
    }

    val rotor_blades by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 1) }
        addModule(::RotorModelClientDataCollectionModule)
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            pattern("b b")
            pattern(" c ")
            pattern("b b")
            define('b', Items.IRON_INGOT)
            define('c', Blocks.IRON_BLOCK)
            event.helper.createHas(this, ToStarsMod.itemTags.heat_generators)
            this
        } }
    }

    val turbines by this {
        addAffects(clear = true) { it.identifier().path.endsWith("_turbine") }
    }
    val turbine_tier_1 by this {
        addAffects(clear = true) { (it.identifier().path.startsWith("copper_") || it.identifier().path.startsWith("gold_")) && it.identifier().path.endsWith("_turbine") }
        addModule {
            val unlit = TexturedModel.createDefault({
                val texture = it.identifier.withPrefix("block/")
                TextureMapping()
                    .put(TextureSlot.DOWN, Material(Identifier.fromNamespaceAndPath(modid, "block/machine_enclosure_1_bottom")))
                    .put(TextureSlot.EAST, Material(texture.withSuffix("_alt")))
                    .put(TextureSlot.WEST, Material(texture.withSuffix("_side")))
                    .put(TextureSlot.NORTH, Material(texture.withSuffix("_front")))
                    .put(TextureSlot.SOUTH, Material(texture.withPath { it.replace("_turbine", "_power_bank_front") }))
                    .put(TextureSlot.UP, Material(texture.withPath { it.replace("_turbine", "_machine_enclosure_top") }))
                    .put(TextureSlot.PARTICLE, Material(texture.withPath { it.replace("_turbine", "_machine_enclosure_side") }))
            }, ModelTemplates.CUBE)
            LitMachineBlockModelClientDataCollectionModule(unlit, unlit.updateTexture {
                val east = it.get(TextureSlot.EAST)
                it.put(TextureSlot.EAST, Material(east.sprite.withSuffix("_lit"), east.forceTranslucent))
                val west = it.get(TextureSlot.WEST)
                it.put(TextureSlot.WEST, Material(west.sprite.withSuffix("_lit"), west.forceTranslucent))
            })
        }
    }
    val copper_turbine by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 1) }
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            pattern("ccc")
            pattern("sss")
            pattern("rmr")
            define('c', ToStarsMod.blocks.power_cable)
            define('r', Items.REDSTONE)
            define('s', ToStarsMod.items.magnetic_iron)
            define('m', ToStarsMod.blocks.copper_machine_enclosure)
            event.helper.createHas(this, ToStarsMod.blocks.copper_machine_enclosure)
            this
        } }
    }
    val gold_turbine by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 2) }
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            pattern("ccc")
            pattern("sss")
            pattern("rmr")
            define('c', ToStarsMod.blocks.power_cable)
            define('r', Items.REDSTONE)
            define('s', ToStarsMod.items.magnetic_iron)
            define('m', ToStarsMod.blocks.gold_machine_enclosure)
            event.helper.createHas(this, ToStarsMod.blocks.gold_machine_enclosure)
            this
        } }
        addModule { ShapedRecipeDataCollectionModule(id = it.entry.identifier().path + "_from_upgrade") { collection, event ->
            pattern("ggg")
            pattern("rMr")
            pattern("iGi")
            define('r', Items.REDSTONE)
            define('M', ToStarsMod.blocks.copper_turbine)
            define('G', Blocks.GOLD_BLOCK)
            define('g', Items.GOLD_INGOT)
            define('i', Items.IRON_INGOT)
            event.helper.createHas(this, ToStarsMod.blocks.copper_turbine)
            this
        } }
    }

}
