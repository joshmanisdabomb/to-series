package net.jidb.to.base.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item

/**
 * [SimpleRegistryLibrary] implementation that registers the [Item] content of To Lay the Foundations, and provides access to it in one place.
 *
 * @since 0.0.3
 */
object ToBaseItemLibrary : SimpleRegistryLibrary<Item>(ToBaseMod.modid) {

    override val registry = BuiltInRegistries.ITEM

    /**
     * A plain item used while developing, which exists to have something to hold.
     *
     * @since 0.0.3
     */
    val test_item by this { entry ->
        Item(Item.Properties().setId(getEntryResourceKey(entry)))
    }

}
