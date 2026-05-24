package net.jidb.to.base.helper

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object IdentifierHelper {

    fun blockPrefix(modid: String, path: String) = Identifier.fromNamespaceAndPath(modid, path).withPrefix("block/")
    fun itemPrefix(modid: String, path: String) = Identifier.fromNamespaceAndPath(modid, path).withPrefix("item/")

    val Block.identifier get() = BuiltInRegistries.BLOCK.getKey(this)
    val Item.identifier get() = BuiltInRegistries.ITEM.getKey(this)

}