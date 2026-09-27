package net.jidb.to.stars.neoforge.client.data.content

import net.jidb.to.base.api.helper.IdentifierHelper.identifier
import net.jidb.to.base.client.data.pub.collection.module.lang.IdentifierLanguageClientDataCollectionModule
import net.jidb.to.base.client.data.pub.collection.module.lang.SimpleLanguageClientDataCollectionModule
import net.jidb.to.base.client.data.pub.collection.module.lang.StorageIdentifierLanguageClientDataCollectionModule
import net.jidb.to.base.client.data.pub.collection.module.model.block.FireBlockModelClientDataCollectionModule
import net.jidb.to.base.client.data.pub.collection.module.model.block.FullRotatingBlockModelClientDataCollectionModule
import net.jidb.to.base.client.data.pub.collection.module.model.block.SimpleBlockModelClientDataCollectionModule
import net.jidb.to.base.client.data.pub.collection.module.model.item.TintedItemModelClientDataCollectionModule
import net.jidb.to.base.data.api.library.DataCollectionLibrary
import net.jidb.to.base.data.pub.collection.module.loot.CustomBlockLootDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.loot.GeneralLootDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.loot.NoopBlockLootDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.loot.OreBlockLootDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.loot.SilkBlockLootDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.loot.SimpleBlockLootDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.recipe.CompactRecipeDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.recipe.OreRecipeDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.recipe.ShapedRecipeDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.recipe.SmithingRecipeDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.tag.DictTagDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.tag.MiningBlockTagDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.tag.MiningBlockTagDataCollectionModule.ToolType
import net.jidb.to.base.data.pub.collection.module.tag.SimpleBlockTagDataCollectionModule
import net.jidb.to.base.data.pub.collection.module.tag.SimpleItemTagDataCollectionModule
import net.jidb.to.base.neoforge.client.content.data.module.Cable4BlockModelClientDataCollectionModule
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.AtomicBombBlock
import net.jidb.to.stars.client.item.tint.BatteryItemTint
import net.jidb.to.stars.info.ProcessorType
import net.jidb.to.stars.neoforge.client.data.ToStarsDataHelper
import net.jidb.to.stars.neoforge.client.data.ToStarsModels
import net.jidb.to.stars.neoforge.client.data.module.AtomicBombBlockModelClientDataCollectionModule
import net.jidb.to.stars.neoforge.client.data.module.BoilingCauldronBlockModelClientDataCollectionModule
import net.jidb.to.stars.neoforge.client.data.module.HeatCableBlockModelClientDataCollectionModule
import net.jidb.to.stars.neoforge.client.data.module.LitMachineBlockModelClientDataCollectionModule
import net.jidb.to.stars.neoforge.client.data.module.PowerBankModelClientDataCollectionModule
import net.jidb.to.stars.neoforge.client.data.module.RotorModelClientDataCollectionModule
import net.jidb.to.stars.neoforge.data.module.EnergySilkBlockLootDataCollectionModule
import net.jidb.to.stars.neoforge.data.module.ProcessorRecipeDataCollectionModule
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.neoforged.neoforge.common.Tags

/**
 * [DataCollectionLibrary] implementation describing how each piece of this mod's content is generated, i.e. its models, translations, tags, drops and recipes.
 *
 * A few entries are not content at all but a tag or a recipe shared by several things, declared here so that they are only written once.
 */
object ToStarsDataLibrary : DataCollectionLibrary(ToStarsMod.modid) {

    /**
     * The generation of the nuclear waste block.
     */
    val nuclear_waste by this {
        addModule(::FullRotatingBlockModelClientDataCollectionModule)
        addModule { SimpleBlockTagDataCollectionModule(ToStarsMod.blockTags.nuke_passthrough) }
        addModule { MiningBlockTagDataCollectionModule(ToolType.SHOVEL, 3) }
        addModule(::SilkBlockLootDataCollectionModule)
    }

    /**
     * The generation of the nuclear fire block.
     */
    val nuclear_fire by this {
        addModule(::FireBlockModelClientDataCollectionModule)
        addModule { SimpleBlockTagDataCollectionModule(BlockTags.FIRE, BlockTags.REPLACEABLE) }
        addModule(::NoopBlockLootDataCollectionModule)
    }

    /**
     * The generation of heavy uranium shielding.
     */
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

    /**
     * The generation of uranium, i.e. the ore, the ingot, the nugget and the block it compacts into.
     */
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

    /**
     * The tags and drops shared by both kinds of uranium ore.
     */
    val uranium_ores by this {
        addAffects(clear = true) { it.identifier().path.contains("uranium_ore") }
        addModule { SimpleBlockTagDataCollectionModule(Tags.Blocks.ORE_RATES_SINGULAR) }
        addModule { OreBlockLootDataCollectionModule(ToStarsMod.items.uranium) }
        addModule(::OreRecipeDataCollectionModule)
    }

    /**
     * The data description shared by every compacted resource storage block.
     * @since 0.1.0
     */
    val storage_blocks by this {
        addAffects(clear = true) { it.identifier().path.endsWith("_block") }
        addModule(::StorageIdentifierLanguageClientDataCollectionModule)
    }

    /**
     * The generation of enriched uranium.
     */
    val enriched_uranium by this {
        addAffects(clear = true) { it.identifier().path.contains("enriched_uranium") }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.enriched_uranium) }
    }

    /**
     * The generation of the enriched uranium nugget.
     */
    val enriched_uranium_nugget by this {
        addModule { ProcessorRecipeDataCollectionModule(id = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "enriched_uranium_nugget_from_centrifuge").toString()) { collection, event ->
            requires(ToStarsMod.items.uranium)
            requiresType(ProcessorType.CENTRIFUGE)
            outputChance(collection.`object`.asItem(), 2, 5, 0.5f, ToStarsMod.items.heavy_uranium_nugget, 2, 5)
            time(600)
            energy(12000000)
            event.helper.createHas(this, ToStarsMod.items.uranium)
            this
        } }
    }

    /**
     * The data description for all steel items and blocks, excluding machines.
     * @since 0.2.0
     */
    val steel_resources by this {
        addAffects(clear = true) { it.identifier() == ToStarsMod.items.steel_ingot.identifier || it.identifier() == ToStarsMod.items.steel_nugget.identifier || it.identifier() == ToStarsMod.blocks.steel_block.identifier }
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 2) }
        addModule(::DictTagDataCollectionModule)
        addModule(::CompactRecipeDataCollectionModule)
    }

    /**
     * The generation of the atomic bomb.
     */
    val atomic_bomb by this {
        addModule(::AtomicBombBlockModelClientDataCollectionModule)
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 1) }
        addModule { CustomBlockLootDataCollectionModule { collection, event -> event.helper.propertyBlockLoot(
            ToStarsMod.blocks.atomic_bomb,
            AtomicBombBlock.segment,
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

    /**
     * The data description shared by the tier one machine enclosures.
     * @since 0.1.0
     */
    val machine_enclosure_tier_1 by this {
        addAffects(clear = true) { ToStarsDataHelper.isMachine(it.identifier(), "machine_enclosure", 1) }
        addModule { SimpleBlockModelClientDataCollectionModule(TexturedModel.CUBE_TOP_BOTTOM.updateTexture {
            val material = it.get(TextureSlot.BOTTOM)
            it.put(TextureSlot.BOTTOM, Material(Identifier.fromNamespaceAndPath(modid, "block/machine_enclosure_1_bottom"), material.forceTranslucent))
        }) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.machine_enclosures) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.tier_1_machine_enclosures) }
    }

    /**
     * The data description of the copper machine enclosure.
     * @see [net.jidb.to.stars.content.ToStarsBlockLibrary.copper_machine_enclosure]
     * @since 0.1.0
     */
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

    /**
     * The data description of the gold machine enclosure.
     * @see [net.jidb.to.stars.content.ToStarsBlockLibrary.gold_machine_enclosure]
     * @since 0.1.0
     */
    val gold_machine_enclosure by this {
        addModule { ShapedRecipeDataCollectionModule(modify = ToStarsDataHelper.createGoldMachineRecipe(ToStarsMod.blocks.copper_machine_enclosure)) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.gold_battery_unlock) }
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 2) }
    }

    /**
     * The data description shared by the tier two machine enclosures.
     * @since 0.2.0
     */
    val machine_enclosure_tier_2 by this {
        addAffects(clear = true) { ToStarsDataHelper.isMachine(it.identifier(), "machine_enclosure", 2) }
        addModule { SimpleBlockModelClientDataCollectionModule(TexturedModel.CUBE_TOP_BOTTOM.updateTexture {
            val material = it.get(TextureSlot.BOTTOM)
            it.put(TextureSlot.BOTTOM, Material(Identifier.fromNamespaceAndPath(modid, "block/machine_enclosure_2_bottom"), material.forceTranslucent))
        }) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.machine_enclosures) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.tier_2_machine_enclosures) }
    }

    /**
     * The data description of the steel machine enclosure.
     * @see [net.jidb.to.stars.content.ToStarsBlockLibrary.steel_machine_enclosure]
     * @since 0.2.0
     */
    val steel_machine_enclosure by this {
        addModule { ShapedRecipeDataCollectionModule(modify = ToStarsDataHelper.createSteelMachineRecipe(ToStarsMod.itemTags.tier_1_machine_enclosures)) }
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 2) }
    }

    /**
     * The data description of the netherite machine enclosure.
     * @see [net.jidb.to.stars.content.ToStarsBlockLibrary.netherite_machine_enclosure]
     * @since 0.2.0
     */
    val netherite_machine_enclosure by this {
        addModule { SmithingRecipeDataCollectionModule(
            Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
            Ingredient.of(ToStarsMod.blocks.steel_machine_enclosure),
            Ingredient.of(Items.NETHERITE_INGOT),
            null,
        ) { collection, event ->
            event.helper.createHas(this, ToStarsMod.blocks.steel_machine_enclosure)
            this
        } }
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 3) }
    }

    /**
     * The generation of the power cable.
     */
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

    /**
     * The generation of the heat pipe.
     */
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

    /**
     * The generation of the creative power source.
     */
    val creative_power_source by this {
        addModule(::NoopBlockLootDataCollectionModule)
    }

    /**
     * The data description shared by all power banks.
     * @see net.jidb.to.stars.block.EnergyStorageBlock
     * @since 0.2.0
     */
    val power_banks by this {
        addAffects(clear = true) { ToStarsDataHelper.isMachine(it.identifier(), "power_bank") }
        addModule(::EnergySilkBlockLootDataCollectionModule)
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.power_banks) }
    }

    /**
     * The data description shared by tier one power banks.
     * @see net.jidb.to.stars.block.EnergyStorageBlock
     * @since 0.1.0
     */
    val power_bank_tier_1 by this {
        addAffects(clear = true) { ToStarsDataHelper.isMachine(it.identifier(), "power_bank", 1) }
        addModule { PowerBankModelClientDataCollectionModule(1) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.tier_1_power_banks) }
    }

    /**
     * The data description of the copper power bank.
     * @see net.jidb.to.stars.content.ToStarsBlockLibrary.copper_power_bank
     * @since 0.1.0
     */
    val copper_power_bank by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 1) }
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            ToStarsDataHelper.createPowerBankRecipe(ToStarsMod.blocks.copper_machine_enclosure).invoke(this, collection, event)
            event.helper.createHas(this, ToStarsMod.itemTags.copper_power_bank_unlock)
            this
        } }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.copper_machines) }
    }

    /**
     * The data description of the gold power bank.
     * @see net.jidb.to.stars.content.ToStarsBlockLibrary.gold_power_bank
     * @since 0.1.0
     */
    val gold_power_bank by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 2) }
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            ToStarsDataHelper.createPowerBankRecipe(ToStarsMod.blocks.gold_machine_enclosure).invoke(this, collection, event)
            event.helper.createHas(this, ToStarsMod.blocks.gold_machine_enclosure)
            this
        } }
        addModule { ShapedRecipeDataCollectionModule(
            id = it.entry.identifier().toString() + "_from_upgrade",
            modify = ToStarsDataHelper.createGoldMachineRecipe(ToStarsMod.blocks.copper_power_bank)
        ) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.gold_battery_unlock) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.gold_machines) }
    }

    /**
     * The data description shared by tier two power banks.
     * @see net.jidb.to.stars.block.EnergyStorageBlock
     * @since 0.2.0
     */
    val power_bank_tier_2 by this {
        addAffects(clear = true) { ToStarsDataHelper.isMachine(it.identifier(), "power_bank", 2) }
        addModule { PowerBankModelClientDataCollectionModule(2) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.tier_2_power_banks) }
    }

    /**
     * The data description of the steel power bank.
     * @see net.jidb.to.stars.content.ToStarsBlockLibrary.steel_power_bank
     * @since 0.2.0
     */
    val steel_power_bank by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 2) }
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            ToStarsDataHelper.createPowerBankRecipe(ToStarsMod.blocks.steel_machine_enclosure).invoke(this, collection, event)
            event.helper.createHas(this, ToStarsMod.blocks.steel_machine_enclosure)
            this
        } }
        addModule { ShapedRecipeDataCollectionModule(
            id = it.entry.identifier().toString() + "_from_upgrade",
            modify = ToStarsDataHelper.createSteelMachineRecipe(ToStarsMod.itemTags.tier_1_power_banks)
        ) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.steel_machines) }
    }

    /**
     * The data description of the netherite power bank.
     * @see net.jidb.to.stars.content.ToStarsBlockLibrary.netherite_power_bank
     * @since 0.2.0
     */
    val netherite_power_bank by this {
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            ToStarsDataHelper.createPowerBankRecipe(ToStarsMod.blocks.netherite_machine_enclosure).invoke(this, collection, event)
            event.helper.createHas(this, ToStarsMod.blocks.netherite_machine_enclosure)
            this
        } }
        addModule { SmithingRecipeDataCollectionModule(
            Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
            Ingredient.of(ToStarsMod.blocks.steel_power_bank),
            Ingredient.of(Items.NETHERITE_INGOT),
            null,
            id = it.entry.identifier().toString() + "_from_upgrade",
        ) { collection, event ->
            event.helper.createHas(this, ToStarsMod.blocks.steel_power_bank)
            this
        } }
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 3) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.netherite_machines) }
    }

    /**
     * The data description shared by all batteries.
     * @see net.jidb.to.stars.item.BatteryItem
     * @since 0.1.0
     */
    val batteries by this {
        addAffects(clear = true) { it.identifier().path.endsWith("_battery") }
        addModule { TintedItemModelClientDataCollectionModule(BatteryItemTint()) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.batteries) }
    }

    /**
     * The data description of the copper battery.
     * @see net.jidb.to.stars.content.ToStarsItemLibrary.copper_battery
     * @since 0.1.0
     */
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
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.tier_1_batteries) }
    }

    /**
     * The data description of the gold battery.
     * @see net.jidb.to.stars.content.ToStarsItemLibrary.gold_battery
     * @since 0.1.0
     */
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
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.tier_1_batteries) }
    }

    /**
     * The data description of the steel battery.
     * @see net.jidb.to.stars.content.ToStarsItemLibrary.steel_battery
     * @since 0.2.0
     */
    val steel_battery by this {
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            pattern("iri")
            pattern("sbs")
            pattern("sds")
            define('b', ToStarsMod.itemTags.tier_1_batteries)
            define('i', Items.IRON_INGOT)
            define('s', ToStarsMod.items.steel_ingot)
            define('d', Items.DIAMOND)
            define('r', Items.REDSTONE)
            event.helper.createHas(this, ToStarsMod.items.steel_ingot)
            this
        } }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.tier_2_batteries) }
    }

    /**
     * The data description of the netherite battery.
     * @see net.jidb.to.stars.content.ToStarsItemLibrary.netherite_battery
     * @since 0.2.0
     */
    val netherite_battery by this {
        addModule { SmithingRecipeDataCollectionModule(
            Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
            Ingredient.of(ToStarsMod.items.steel_battery),
            Ingredient.of(Items.NETHERITE_INGOT),
            null
        ) { collection, event ->
            event.helper.createHas(this, ToStarsMod.items.steel_battery)
            this
        } }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.tier_2_batteries) }
    }

    /**
     * The generation of the boiler.
     */
    val boiler by this {
        addModule(::BoilingCauldronBlockModelClientDataCollectionModule)
        addModule { SimpleBlockTagDataCollectionModule(BlockTags.CAULDRONS) }
        addModule { SimpleBlockLootDataCollectionModule(Items.CAULDRON) }
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE) }
    }

    /**
     * The data description shared by every heat generator, including solid and fluid.
     * @see net.jidb.to.stars.block.HeatGeneratorBlock
     * @since 0.1.0
     */
    val generators by this {
        addAffects(clear = true) { ToStarsDataHelper.isMachine(it.identifier(), "generator") }
        addModule { IdentifierLanguageClientDataCollectionModule { it.replace("solid", "solid-fired").replace("fluid", "liquid-fired") } }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.heat_generators) }
    }

    /**
     * The data description shared by every tier 1 heat generator, including solid and fluid.
     * @see net.jidb.to.stars.block.HeatGeneratorBlock
     * @since 0.1.0
     */
    val generator_tier_1 by this {
        addAffects(clear = true) { ToStarsDataHelper.isMachine(it.identifier(), "generator", 1) }
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

    /**
     * The data description of the copper solid-fired heat generator.
     * @see net.jidb.to.stars.content.ToStarsBlockLibrary.copper_solid_generator
     * @see net.jidb.to.stars.block.SolidGeneratorBlock
     * @since 0.1.0
     */
    val copper_solid_generator by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 1) }
        addModule { ShapedRecipeDataCollectionModule(modify = ToStarsDataHelper.createSolidGeneratorRecipe(ToStarsMod.blocks.copper_machine_enclosure)) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.copper_machines) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.tier_1_solid_generators) }
    }

    /**
     * The data description of the gold solid-fired heat generator.
     * @see net.jidb.to.stars.content.ToStarsBlockLibrary.gold_solid_generator
     * @see net.jidb.to.stars.block.SolidGeneratorBlock
     * @since 0.1.0
     */
    val gold_solid_generator by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 2) }
        addModule { ShapedRecipeDataCollectionModule(
            modify = ToStarsDataHelper.createSolidGeneratorRecipe(ToStarsMod.blocks.gold_machine_enclosure)
        ) }
        addModule { ShapedRecipeDataCollectionModule(
            id = it.entry.identifier().toString() + "_from_upgrade",
            modify = ToStarsDataHelper.createGoldMachineRecipe(ToStarsMod.blocks.copper_solid_generator)
        ) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.gold_machines) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.tier_1_solid_generators) }
    }

    /**
     * The data description shared by every tier 2 heat generator, including solid and fluid.
     * @see net.jidb.to.stars.block.HeatGeneratorBlock
     * @since 0.2.0
     */
    val generator_tier_2 by this {
        addAffects(clear = true) { ToStarsDataHelper.isMachine(it.identifier(), "generator", 2) }
        addModule {
            val unlit = TexturedModel.ORIENTABLE.updateTexture {
                val bottom = it.get(TextureSlot.BOTTOM)
                it.put(TextureSlot.BOTTOM, Material(Identifier.fromNamespaceAndPath(modid, "block/machine_enclosure_2_bottom"), bottom.forceTranslucent))
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

    /**
     * The data description of the steel solid-fired heat generator.
     * @see net.jidb.to.stars.content.ToStarsBlockLibrary.steel_solid_generator
     * @see net.jidb.to.stars.block.SolidGeneratorBlock
     * @since 0.2.0
     */
    val steel_solid_generator by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 2) }
        addModule { ShapedRecipeDataCollectionModule(
            modify = ToStarsDataHelper.createSolidGeneratorRecipe(ToStarsMod.blocks.steel_machine_enclosure)
        ) }
        addModule { ShapedRecipeDataCollectionModule(
            id = it.entry.identifier().toString() + "_from_upgrade",
            modify = ToStarsDataHelper.createSteelMachineRecipe(ToStarsMod.itemTags.tier_1_solid_generators)
        ) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.steel_machines) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.tier_2_solid_generators) }
    }

    /**
     * The data description of the netherite solid-fired heat generator.
     * @see net.jidb.to.stars.content.ToStarsBlockLibrary.netherite_solid_generator
     * @see net.jidb.to.stars.block.SolidGeneratorBlock
     * @since 0.2.0
     */
    val netherite_solid_generator by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 3) }
        addModule { ShapedRecipeDataCollectionModule(modify = ToStarsDataHelper.createSolidGeneratorRecipe(ToStarsMod.blocks.netherite_machine_enclosure)) }
        addModule { SmithingRecipeDataCollectionModule(
            Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
            Ingredient.of(ToStarsMod.blocks.steel_solid_generator),
            Ingredient.of(Items.NETHERITE_INGOT),
            null,
            id = it.entry.identifier().toString() + "_from_upgrade",
        ) { collection, event ->
            event.helper.createHas(this, ToStarsMod.blocks.steel_solid_generator)
            this
        } }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.netherite_machines) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.tier_2_solid_generators) }
    }

    /**
     * The generation of the rotor blades.
     */
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

    /**
     * The data description shared by all turbines.
     * @see net.jidb.to.stars.block.TurbineBlock
     * @since 0.1.0
     */
    val turbines by this {
        addAffects(clear = true) { ToStarsDataHelper.isMachine(it.identifier(), "turbine") }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.turbines) }
    }

    /**
     * The data description shared by tier one turbines.
     * @see net.jidb.to.stars.block.TurbineBlock
     * @since 0.1.0
     */
    val turbine_tier_1 by this {
        addAffects(clear = true) { ToStarsDataHelper.isMachine(it.identifier(), "turbine", 1) }
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
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.tier_1_turbines) }
    }

    /**
     * The data description of the copper turbine.
     * @see net.jidb.to.stars.content.ToStarsBlockLibrary.copper_turbine
     * @since 0.1.0
     */
    val copper_turbine by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 1) }
        addModule { ShapedRecipeDataCollectionModule(modify = ToStarsDataHelper.createTurbineRecipe(ToStarsMod.blocks.copper_machine_enclosure)) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.copper_machines) }
    }

    /**
     * The data description of the gold turbine.
     * @see net.jidb.to.stars.content.ToStarsBlockLibrary.gold_turbine
     * @since 0.1.0
    */
    val gold_turbine by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 2) }
        addModule { ShapedRecipeDataCollectionModule(
            modify = ToStarsDataHelper.createTurbineRecipe(ToStarsMod.blocks.gold_machine_enclosure)
        ) }
        addModule { ShapedRecipeDataCollectionModule(
            id = it.entry.identifier().toString() + "_from_upgrade",
            modify = ToStarsDataHelper.createGoldMachineRecipe(ToStarsMod.blocks.copper_power_bank)
        ) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.gold_machines) }
    }

    /**
     * The data description shared by tier two turbines.
     * @see net.jidb.to.stars.block.TurbineBlock
     * @since 0.2.0
     */
    val turbine_tier_2 by this {
        addAffects(clear = true) { ToStarsDataHelper.isMachine(it.identifier(), "turbine", 2) }
        addModule {
            val unlit = TexturedModel.createDefault({
                val texture = it.identifier.withPrefix("block/")
                TextureMapping()
                    .put(TextureSlot.DOWN, Material(Identifier.fromNamespaceAndPath(modid, "block/machine_enclosure_2_bottom")))
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
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.tier_2_turbines) }
    }

    /**
     * The data description of the steel turbine.
     * @see net.jidb.to.stars.content.ToStarsBlockLibrary.steel_turbine
     * @since 0.2.0
     */
    val steel_turbine by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 2) }
        addModule { ShapedRecipeDataCollectionModule(
            modify = ToStarsDataHelper.createTurbineRecipe(ToStarsMod.blocks.steel_machine_enclosure)
        ) }
        addModule { ShapedRecipeDataCollectionModule(
            id = it.entry.identifier().toString() + "_from_upgrade",
            modify = ToStarsDataHelper.createSteelMachineRecipe(ToStarsMod.itemTags.tier_1_turbines)
        ) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.steel_machines) }
    }

    /**
     * The data description of the netherite turbine.
     * @see net.jidb.to.stars.content.ToStarsBlockLibrary.netherite_turbine
     * @since 0.2.0
     */
    val netherite_turbine by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 3) }
        addModule { ShapedRecipeDataCollectionModule(
            modify = ToStarsDataHelper.createTurbineRecipe(ToStarsMod.blocks.netherite_machine_enclosure)
        ) }
        addModule { SmithingRecipeDataCollectionModule(
            Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
            Ingredient.of(ToStarsMod.blocks.steel_turbine),
            Ingredient.of(Items.NETHERITE_INGOT),
            null,
            id = it.entry.identifier().toString() + "_from_upgrade",
        ) { collection, event ->
            event.helper.createHas(this, ToStarsMod.blocks.steel_turbine)
            this
        } }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.netherite_machines) }
    }

    /**
     * The data description shared by all centrifuges.
     * @see net.jidb.to.stars.block.CentrifugeBlock
     * @since 0.1.0
     */
    val centrifuges by this {
        addAffects(clear = true) { ToStarsDataHelper.isMachine(it.identifier(), "centrifuge") }
        addModule(::EnergySilkBlockLootDataCollectionModule)
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.centrifuges) }
    }

    /**
     * The data description shared by tier one centrifuges.
     * @see net.jidb.to.stars.block.CentrifugeBlock
     * @since 0.1.0
     */
    val centrifuge_tier_1 by this {
        addAffects(clear = true) { ToStarsDataHelper.isMachine(it.identifier(), "centrifuge", 1) }
        addModule {
            val unlit = ToStarsModels.centrifuge.updateTexture {
                it.put(TextureSlot.BOTTOM, Material(Identifier.fromNamespaceAndPath(modid, "block/machine_enclosure_1_bottom")))
            }
            LitMachineBlockModelClientDataCollectionModule(unlit, ToStarsModels.centrifugeLit.updateTexture {
                it.put(TextureSlot.BOTTOM, Material(Identifier.fromNamespaceAndPath(modid, "block/machine_enclosure_1_bottom")))
            })
        }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.tier_1_centrifuges) }
    }

    /**
     * The data description of the copper centrifuge.
     * @see net.jidb.to.stars.content.ToStarsBlockLibrary.copper_centrifuge
     * @since 0.1.0
     */
    val copper_centrifuge by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 1) }
        addModule { ShapedRecipeDataCollectionModule(modify = ToStarsDataHelper.createCentrifugeRecipe(ToStarsMod.blocks.copper_machine_enclosure)) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.copper_machines) }
    }

    /**
     * The data description of the gold centrifuge.
     * @see net.jidb.to.stars.content.ToStarsBlockLibrary.gold_centrifuge
     * @since 0.1.0
     */
    val gold_centrifuge by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 2) }
        addModule { ShapedRecipeDataCollectionModule(
            modify = ToStarsDataHelper.createCentrifugeRecipe(ToStarsMod.blocks.gold_machine_enclosure)
        ) }
        addModule { ShapedRecipeDataCollectionModule(
            id = it.entry.identifier().toString() + "_from_upgrade",
            modify = ToStarsDataHelper.createGoldMachineRecipe(ToStarsMod.blocks.copper_centrifuge)
        ) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.gold_machines) }
    }

    /**
     * The data description shared by tier two centrifuges.
     * @see net.jidb.to.stars.block.CentrifugeBlock
     * @since 0.2.0
     */
    val centrifuge_tier_2 by this {
        addAffects(clear = true) { ToStarsDataHelper.isMachine(it.identifier(), "centrifuge", 2) }
        addModule {
            val unlit = ToStarsModels.centrifuge.updateTexture {
                it.put(TextureSlot.BOTTOM, Material(Identifier.fromNamespaceAndPath(modid, "block/machine_enclosure_2_bottom")))
            }
            LitMachineBlockModelClientDataCollectionModule(unlit, ToStarsModels.centrifugeLit.updateTexture {
                it.put(TextureSlot.BOTTOM, Material(Identifier.fromNamespaceAndPath(modid, "block/machine_enclosure_2_bottom")))
            })
        }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.tier_2_centrifuges) }
    }

    /**
     * The data description of the steel centrifuge.
     * @see net.jidb.to.stars.content.ToStarsBlockLibrary.steel_centrifuge
     * @since 0.2.0
     */
    val steel_centrifuge by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 2) }
        addModule { ShapedRecipeDataCollectionModule(
            modify = ToStarsDataHelper.createCentrifugeRecipe(ToStarsMod.blocks.steel_machine_enclosure)
        ) }
        addModule { ShapedRecipeDataCollectionModule(
            id = it.entry.identifier().toString() + "_from_upgrade",
            modify = ToStarsDataHelper.createSteelMachineRecipe(ToStarsMod.itemTags.tier_1_centrifuges)
        ) }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.steel_machines) }
    }

    /**
     * The data description of the netherite centrifuge.
     * @see net.jidb.to.stars.content.ToStarsBlockLibrary.netherite_centrifuge
     * @since 0.2.0
     */
    val netherite_centrifuge by this {
        addModule { MiningBlockTagDataCollectionModule(ToolType.PICKAXE, 3) }
        addModule { ShapedRecipeDataCollectionModule(
            modify = ToStarsDataHelper.createCentrifugeRecipe(ToStarsMod.blocks.netherite_machine_enclosure)
        ) }
        addModule { SmithingRecipeDataCollectionModule(
            Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
            Ingredient.of(ToStarsMod.blocks.steel_centrifuge),
            Ingredient.of(Items.NETHERITE_INGOT),
            null,
            id = it.entry.identifier().toString() + "_from_upgrade",
        ) { collection, event ->
            event.helper.createHas(this, ToStarsMod.blocks.steel_centrifuge)
            this
        } }
        addModule { SimpleItemTagDataCollectionModule(ToStarsMod.itemTags.netherite_machines) }
    }

    /**
     * The generation of the Gravitational Influence music disc.
     */
    val music_disc_gravitational_influence by this {
        addModule { SimpleLanguageClientDataCollectionModule("Music Disc") }
        addModule { GeneralLootDataCollectionModule(ToStarsMod.lootTables.advancement_nuke_race.identifier(), LootTable.Builder()
            .withPool(LootPool.Builder()
                .add(LootItem.lootTableItem(it.`object` as Item)))) }
    }

    /**
     * The data description of the kiln block.
     * @see [net.jidb.to.stars.content.ToStarsBlockLibrary.kiln]
     * @since 0.2.0
     */
    val kiln by this {
        addModule {
            val unlit = TexturedModel.ORIENTABLE_ONLY_TOP.updateTexture {
                val front = it.get(TextureSlot.FRONT)
                it.put(TextureSlot.FRONT, Material(front.sprite.withPath { it.replace("_front", "") }, front.forceTranslucent))
                val side = it.get(TextureSlot.SIDE)
                it.put(TextureSlot.SIDE, Material(side.sprite, side.forceTranslucent))
                it.put(TextureSlot.TOP, Material(side.sprite, side.forceTranslucent))
            }
            LitMachineBlockModelClientDataCollectionModule(unlit, unlit.updateTexture {
                val front = it.get(TextureSlot.FRONT)
                it.put(TextureSlot.FRONT, Material(front.sprite.withSuffix("_lit"), front.forceTranslucent))
            })
        }
        addModule { ShapedRecipeDataCollectionModule { collection, event ->
            pattern(" b ")
            pattern("bfb")
            pattern("bbb")
            define('b', Blocks.BRICKS)
            define('f', Blocks.FURNACE)
            event.helper.createHas(this, Blocks.BRICKS, Blocks.FURNACE)
            this
        } }
    }

}
