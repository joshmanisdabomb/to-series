package net.jidb.to.stars.content

import net.jidb.to.base.api.info.TooltipEngine
import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.info.MachineTier
import net.jidb.to.stars.info.ToStarsTooltipEngine
import net.jidb.to.stars.item.BatteryItem
import net.jidb.to.stars.item.TestItem
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity

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
        .setId(getEntryResourceKey(entry))) }

    /**
     * Steel nugget, a crafting material from smelting down steel tools. 9 can be combined to make an ingot.
     */
    val steel_nugget by this { entry -> Item(Item.Properties()
        .setId(getEntryResourceKey(entry))) }

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
