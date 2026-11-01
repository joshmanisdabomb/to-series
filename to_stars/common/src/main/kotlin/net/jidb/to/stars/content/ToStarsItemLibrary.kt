package net.jidb.to.stars.content

import net.jidb.to.base.api.info.TooltipEngine
import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.info.MachineTier
import net.jidb.to.stars.info.ToStarsTooltipEngine
import net.jidb.to.stars.item.BatteryItem
import net.jidb.to.stars.item.TestItem
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.util.Unit
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.HoeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.ShovelItem
import net.minecraft.world.item.SmithingTemplateItem
import net.minecraft.world.item.equipment.ArmorType

/**
 * [SimpleRegistryLibrary] implementation holding every item of this mod that is not the item of a block.
 */
object ToStarsItemLibrary : SimpleRegistryLibrary<Item>(ToStarsMod.MOD_ID) {

    override val registry = BuiltInRegistries.ITEM

    /**
     * An item used for testing, which is not part of the mod's content proper.
     */
    val test_item by this(::i) { entry -> TestItem(Item.Properties()
        .setId(getEntryResourceKey(entry))) }

    /**
     * Uranium, smelted from the ore and the material every other form is processed from.
     */
    val uranium by this { entry -> Item(Item.Properties()
        .setId(getEntryResourceKey(entry))) }

    /**
     * A ninth of a uranium ingot.
     */
    val uranium_nugget by this { entry -> Item(Item.Properties()
        .setId(getEntryResourceKey(entry))) }

    /**
     * Enriched uranium, the fuel a reactor runs on, made by processing plain uranium.
     */
    val enriched_uranium by this { entry -> Item(Item.Properties()
        .setId(getEntryResourceKey(entry))) }

    /**
     * A ninth of an enriched uranium ingot.
     */
    val enriched_uranium_nugget by this { entry -> Item(Item.Properties()
        .setId(getEntryResourceKey(entry))) }

    /**
     * Heavy uranium, what is left over from enriching, which is used as shielding.
     */
    val heavy_uranium by this { entry -> Item(Item.Properties()
        .setId(getEntryResourceKey(entry))) }

    /**
     * A ninth of a heavy uranium ingot.
     */
    val heavy_uranium_nugget by this { entry -> Item(Item.Properties()
        .setId(getEntryResourceKey(entry))) }

    /**
     * Magnetised iron ingot, used as a crafting recipe for turbines and magnets.
     */
    val magnetic_iron by this { entry -> Item(Item.Properties()
        .setId(getEntryResourceKey(entry))) }

    /**
     * Steel ingot, a crafting material for tier 2 machines.
     */
    val steel_ingot by this { entry -> Item(Item.Properties()
        .trimMaterial(ToStarsMod.trimMaterials.steel)
        .setId(getEntryResourceKey(entry))) }

    /**
     * Steel nugget, a crafting material from smelting down steel tools. 9 can be combined to make an ingot.
     */
    val steel_nugget by this { entry -> Item(Item.Properties()
        .setId(getEntryResourceKey(entry))) }

    /**
     * A steel sword, made by upgrading an iron sword into steel in a smithing table. Has unlimited durability.
     */
    val steel_sword by this { entry -> Item(Item.Properties()
        .sword(ToStarsMod.toolMaterials.steel, 3.0F, -2.4F)
        .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        .setId(getEntryResourceKey(entry))) }

    /**
     * A steel pickaxe, made by upgrading an iron pickaxe into steel in a smithing table. Has unlimited durability.
     */
    val steel_pickaxe by this { entry -> Item(Item.Properties()
        .pickaxe(ToStarsMod.toolMaterials.steel, 1.0F, -2.8F)
        .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        .setId(getEntryResourceKey(entry))) }

    /**
     * A steel shovel, made by upgrading an iron shovel into steel in a smithing table. Has unlimited durability.
     */
    val steel_shovel by this { entry -> ShovelItem(ToStarsMod.toolMaterials.steel, 1.5F, -3.0F, Item.Properties()
        .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        .setId(getEntryResourceKey(entry))) }

    /**
     * A steel axe, made by upgrading an iron axe into steel in a smithing table. Has unlimited durability.
     */
    val steel_axe by this { entry -> AxeItem(ToStarsMod.toolMaterials.steel, 6.0F, -3.1F, Item.Properties()
        .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        .setId(getEntryResourceKey(entry))) }

    /**
     * A steel hoe, made by upgrading an iron hoe into steel in a smithing table. Has unlimited durability.
     */
    val steel_hoe by this { entry -> HoeItem(ToStarsMod.toolMaterials.steel, -2.0F, -1.0F, Item.Properties()
        .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        .setId(getEntryResourceKey(entry))) }

    /**
     * A steel helmet, made by upgrading an iron helmet into steel in a smithing table. Has unlimited durability.
     */
    val steel_helmet by this { entry -> Item(Item.Properties()
        .humanoidArmor(ToStarsMod.armorMaterials.steel, ArmorType.HELMET)
        .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        .setId(getEntryResourceKey(entry))) }

    /**
     * A steel chestplate, made by upgrading an iron chestplate into steel in a smithing table. Has unlimited durability.
     */
    val steel_chestplate by this { entry -> Item(Item.Properties()
        .humanoidArmor(ToStarsMod.armorMaterials.steel, ArmorType.CHESTPLATE)
        .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        .setId(getEntryResourceKey(entry))) }

    /**
     * Steel leggings, made by upgrading iron leggings into steel in a smithing table. Has unlimited durability.
     */
    val steel_leggings by this { entry -> Item(Item.Properties()
        .humanoidArmor(ToStarsMod.armorMaterials.steel, ArmorType.LEGGINGS)
        .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        .setId(getEntryResourceKey(entry))) }

    /**
     * Steel boots, made by upgrading iron boots into steel in a smithing table. Has unlimited durability.
     */
    val steel_boots by this { entry -> Item(Item.Properties()
        .humanoidArmor(ToStarsMod.armorMaterials.steel, ArmorType.BOOTS)
        .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        .setId(getEntryResourceKey(entry))) }

    /**
     * Steel horse armor, made by upgrading iron horse armor into steel in a smithing table. Has unlimited durability.
     */
    val steel_horse_armor by this { entry -> Item(Item.Properties()
        .horseArmor(ToStarsMod.armorMaterials.steel)
        .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        .setId(getEntryResourceKey(entry))) }

    /**
     * Steel nautilus armor, made by upgrading iron nautilus armor into steel in a smithing table. Has unlimited durability.
     */
    val steel_nautilus_armor by this { entry -> Item(Item.Properties()
        .nautilusArmor(ToStarsMod.armorMaterials.steel)
        .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        .setId(getEntryResourceKey(entry))) }

    /**
     * A steel spear, made by upgrading an iron spear into steel in a smithing table. Has unlimited durability.
     */
    val steel_spear by this { entry -> Item(Item.Properties()
        .spear(ToStarsMod.toolMaterials.steel, 0.95F, 0.95F, 0.6F, 2.5F, 11.0F, 6.75F, 5.1F, 11.25F, 4.6F)
        .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        .setId(getEntryResourceKey(entry))) }

    /**
     * The steel upgrade smithing template, a simple item used in the crafting recipe of upgrading iron equipment into steel.
     */
    val steel_upgrade_smithing_template by this { entry -> SmithingTemplateItem(
        Component.translatable("item.${ToStarsMod.modid}.smithing_template.steel_upgrade.applies_to").withStyle(ChatFormatting.BLUE),
        Component.translatable("item.${ToStarsMod.modid}.smithing_template.steel_upgrade.ingredients").withStyle(ChatFormatting.BLUE),
        Component.translatable("item.${ToStarsMod.modid}.smithing_template.steel_upgrade.base_slot_description"),
        Component.translatable("item.${ToStarsMod.modid}.smithing_template.steel_upgrade.additions_slot_description"),
        (Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE as SmithingTemplateItem).baseSlotEmptyIcons,
        (Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE as SmithingTemplateItem).additionalSlotEmptyIcons,
        Item.Properties().setId(getEntryResourceKey(entry))
    ) }

    /**
     * The tier one battery, a portable form of energy storage.
     */
    val copper_battery by this { entry -> BatteryItem(MachineTier.ONE, Item.Properties()
        .setId(getEntryResourceKey(entry))
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE)))) }

    /**
     * The tier one and a half battery, a portable form of energy storage.
     */
    val gold_battery by this { entry -> BatteryItem(MachineTier.ONE_5, Item.Properties()
        .setId(getEntryResourceKey(entry))
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE_5)))) }

    /**
     * The tier two battery, a portable form of energy storage.
     */
    val steel_battery by this { entry -> BatteryItem(MachineTier.TWO, Item.Properties()
        .setId(getEntryResourceKey(entry))
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.TWO)))) }

    /**
     * The tier two and a half battery, a portable form of energy storage.
     */
    val netherite_battery by this { entry -> BatteryItem(MachineTier.TWO_5, Item.Properties()
        .setId(getEntryResourceKey(entry))
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.TWO_5)))) }

    /**
     * The music disc that plays Gravitational Influence.
     */
    val music_disc_gravitational_influence by this { entry -> Item(Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(ToStarsMod.music.gravitational_influence)
        .setId(getEntryResourceKey(entry))) }

}
