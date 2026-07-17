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

object ToStarsItemLibrary : SimpleRegistryLibrary<Item>(ToStarsMod.MOD_ID) {

    override val registry = BuiltInRegistries.ITEM

    val test_item by this(::i) { entry -> TestItem(Item.Properties()
        .setId(getEntryResourceKey(entry))) }

    val uranium by this { entry -> Item(Item.Properties()
        .setId(getEntryResourceKey(entry))) }
    val uranium_nugget by this { entry -> Item(Item.Properties()
        .setId(getEntryResourceKey(entry))) }
    val enriched_uranium by this { entry -> Item(Item.Properties()
        .setId(getEntryResourceKey(entry))) }
    val enriched_uranium_nugget by this { entry -> Item(Item.Properties()
        .setId(getEntryResourceKey(entry))) }
    val heavy_uranium by this { entry -> Item(Item.Properties()
        .setId(getEntryResourceKey(entry))) }
    val heavy_uranium_nugget by this { entry -> Item(Item.Properties()
        .setId(getEntryResourceKey(entry))) }

    val copper_battery by this { entry -> BatteryItem(MachineTier.ONE, Item.Properties()
        .setId(getEntryResourceKey(entry))
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE)))) }
    val gold_battery by this { entry -> BatteryItem(MachineTier.ONE_5, Item.Properties()
        .setId(getEntryResourceKey(entry))
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE_5)))) }

    val magnetic_iron by this { entry -> Item(Item.Properties()
        .setId(getEntryResourceKey(entry))) }

}