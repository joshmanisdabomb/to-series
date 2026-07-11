package net.jidb.to.base.api.helper

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object IdentifierHelper {

    fun blockPrefix(modid: String, path: String) = Identifier.fromNamespaceAndPath(modid, path).withPrefix("block/")
    fun itemPrefix(modid: String, path: String) = Identifier.fromNamespaceAndPath(modid, path).withPrefix("item/")

    operator fun get(block: Block) = BuiltInRegistries.BLOCK.getKey(block)
    val Block.identifier get() = get(this)
    operator fun get(item: Item) = BuiltInRegistries.ITEM.getKey(item)
    val Item.identifier get() = get(this)

}