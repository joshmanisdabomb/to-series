package net.jidb.to.base.pub.event.item

import net.minecraft.core.component.DataComponentMap
import net.minecraft.world.level.ItemLike

/**
 * The context given to a handler of [ModifyDefaultItemComponentsEvent], through which the default components of an item are patched.
 * Both modloaders offer this as a builder that is only valid while the event is being fired, so the context wraps their calls rather than handing the builder itself out.
 *
 * @param one The platform's function for patching the components of a single item.
 * @param multiple The platform's function for patching the components of every item matching a predicate.
 * @since 0.6.0
 */
class ModifyItemComponentEventContext(private val one: (item: ItemLike, patch: (components: DataComponentMap.Builder, item: ItemLike) -> Unit) -> Unit, private val multiple: (item: (item: ItemLike) -> Boolean, patch: (components: DataComponentMap.Builder, item: ItemLike) -> Unit) -> Unit) {

    /**
     * Patches the default data components of a single item.
     *
     * @param item The item to patch the components of.
     * @param patch A function that adds to or overwrites the components, given the builder holding them and the item being patched.
     * @return [Unit]
     * @since 0.6.0
     */
    fun modify(item: ItemLike, patch: (components: DataComponentMap.Builder, item: ItemLike) -> Unit) = one(item, patch)

    /**
     * Patches the default data components of every item matching a predicate.
     *
     * @param predicate A function deciding whether an item should be patched.
     * @param patch A function that adds to or overwrites the components, given the builder holding them and the item being patched.
     * @return [Unit]
     * @since 0.6.0
     */
    fun modify(predicate: (item: ItemLike) -> Boolean, patch: (components: DataComponentMap.Builder, item: ItemLike) -> Unit) = multiple(predicate, patch)

}
