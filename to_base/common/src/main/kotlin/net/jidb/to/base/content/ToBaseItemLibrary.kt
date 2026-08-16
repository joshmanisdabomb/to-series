package net.jidb.to.base.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item

object ToBaseItemLibrary : SimpleRegistryLibrary<Item>(ToBaseMod.modid) {

    override val registry = BuiltInRegistries.ITEM

    val test_item by this { entry ->
        Item(Item.Properties().setId(getEntryResourceKey(entry)))
    }

}
