package net.jidb.to.base.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.library.SimpleRegistryLibrary
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item

object ToBaseItemLibrary : SimpleRegistryLibrary<Item>(ToBaseMod.MOD_ID) {

    override val registry = BuiltInRegistries.ITEM

    val test_item by this { entry ->
        Item(Item.Properties().setId(getEntryResourceKey(entry)))
    }

}