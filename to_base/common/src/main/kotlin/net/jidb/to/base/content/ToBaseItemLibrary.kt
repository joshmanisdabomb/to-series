package net.jidb.to.base.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items

object ToBaseItemLibrary : SimpleRegistryLibrary<Item>(ToBaseMod.modid) {

    override val registry = BuiltInRegistries.ITEM

    val test_item by this { entry ->
        Item(Item.Properties().setId(getEntryResourceKey(entry)))
    }

    val icon get() = ItemStackTemplate(Items.BARRIER, DataComponentPatch.builder().set(DataComponents.ITEM_MODEL, Identifier.fromNamespaceAndPath(ToBaseMod.modid, "icon")).build())

}
