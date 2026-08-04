package net.jidb.to.base.api.helper

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * A helper object that provides [Identifier]-related utilities.
 *
 * @since 0.1.0
 */
object IdentifierHelper {

    /**
     * Creates an [Identifier] with a "block/" prefix using the specified mod ID and path.
     *
     * @param modid The namespace or mod ID to be used in the [Identifier].
     * @param path The specific path or key within the namespace.
     * @return an [Identifier] object with the "block/" prefix added.
     * @since 0.4.0
     */
    fun blockPrefix(modid: String, path: String) = Identifier.fromNamespaceAndPath(modid, path).withPrefix("block/")

    /**
     * Creates an [Identifier] with an "item/" prefix using the specified mod ID and path.
     *
     * @param modid The namespace or mod ID to be used in the [Identifier].
     * @param path The specific path or key within the namespace.
     * @return an [Identifier] object with the "item/" prefix added.
     * @since 0.4.0
     */
    fun itemPrefix(modid: String, path: String) = Identifier.fromNamespaceAndPath(modid, path).withPrefix("item/")

    /**
     * Retrieves the [Identifier] for the specified block from its [net.minecraft.resources.ResourceKey] via [BuiltInRegistries].
     *
     * @param block The [Block] for which th [net.minecraft.resources.ResourceKey] is to be retrieved.
     * @return The [Identifier] associated with the given [Block].
     * @since 0.8.0
     */
    operator fun get(block: Block) = BuiltInRegistries.BLOCK.getKey(block)

    /**
     * Retrieves the [Identifier] for the receiver [Block] from its [net.minecraft.resources.ResourceKey] via [BuiltInRegistries].
     *
     * @return The [Identifier] associated with the given [Block].
     * @since 0.1.0
     */
    val Block.identifier get() = get(this)

    /**
     * Retrieves the [Identifier] for the specified [Item] from its [net.minecraft.resources.ResourceKey] via [BuiltInRegistries].
     *
     * @param item The [Item] for which th [net.minecraft.resources.ResourceKey] is to be retrieved.
     * @return The [Identifier] associated with the given [Item].
     * @since 0.8.0
     */
    operator fun get(item: Item) = BuiltInRegistries.ITEM.getKey(item)

    /**
     * Retrieves the [Identifier] for the receiver [Item] from its [net.minecraft.resources.ResourceKey] via [BuiltInRegistries].
     *
     * @return The [Identifier] associated with the given [Item].
     * @since 0.1.0
     */
    val Item.identifier get() = get(this)

}
