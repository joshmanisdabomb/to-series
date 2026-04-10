package net.jidb.to.stars.content

import net.jidb.to.base.library.SimpleRegistryLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.item.TestItem
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

}