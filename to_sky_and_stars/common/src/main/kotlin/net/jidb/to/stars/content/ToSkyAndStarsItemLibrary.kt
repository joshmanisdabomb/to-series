package net.jidb.to.stars.content

import net.jidb.to.base.library.SimpleRegistryLibrary
import net.jidb.to.stars.ToSkyAndStarsMod
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item

object ToSkyAndStarsItemLibrary : SimpleRegistryLibrary<Item>(ToSkyAndStarsMod.MOD_ID) {

    override val registry = BuiltInRegistries.ITEM

    val test_item by this(::i) { entry ->
        Item(Item.Properties().setId(getEntryResourceKey(entry)))
    }

}