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

/**
 * The item library for To Lay the Foundations, which automatically registers items to Minecraft's [net.minecraft.core.Registry].
 *
 * This mod only includes the [test_item], [net.minecraft.world.item.BlockItem]s can be generated and exclusively declared in [ToBaseBlockItemLibrary].
 *
 * @since 0.0.3
 */
object ToBaseItemLibrary : SimpleRegistryLibrary<Item>(ToBaseMod.modid) {

    override val registry = BuiltInRegistries.ITEM

    val test_item by this { entry ->
        Item(Item.Properties().setId(getEntryResourceKey(entry)))
    }

    /**
     * The item stack icon that represents this mod, which renders a fake client item generated in ToBaseModelsDataProvider.
     *
     * May move to [net.jidb.to.base.pub.mod.ToMod] in future.
     *
     * @since 1.0.0
     */
    val icon get() = ItemStackTemplate(Items.BARRIER, DataComponentPatch.builder().set(DataComponents.ITEM_MODEL, Identifier.fromNamespaceAndPath(ToBaseMod.modid, "icon")).build())

}
