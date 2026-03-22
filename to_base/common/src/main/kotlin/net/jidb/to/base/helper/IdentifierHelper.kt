package net.jidb.to.base.helper

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object IdentifierHelper {

    //fun createWithDefaultNamespace(id: String, namespace: String) = if (!id.contains(':')) Identifier.fromNamespaceAndPath(namespace, id) else Identifier.parse(id)

    /*fun create(path: String, namespace: String, pathMod: (name: String) -> String = { it }) = Identifier.fromNamespaceAndPath(namespace, pathMod(path))
    fun modify(resource: Identifier, pathMod: (name: String) -> String) = create(resource.path, resource.namespace, pathMod)
    fun prefix(id: Identifier, prefix: String?, glue: String = "_", glueWhen: (prefix: String?) -> Boolean = ::defaultGlueWhen) = Identifier.fromNamespaceAndPath(id.namespace, stringPrefix(id.path, prefix, glue, glueWhen))
    fun suffix(id: Identifier, suffix: String?, glue: String = "_", glueWhen: (suffix: String?) -> Boolean = ::defaultGlueWhen) = Identifier.fromNamespaceAndPath(id.namespace, stringSuffix(id.path, suffix, glue, glueWhen))
    fun stringPrefix(path: String, prefix: String?, glue: String = "_", glueWhen: (prefix: String?) -> Boolean = ::defaultGlueWhen) = "${if (glueWhen(prefix)) "$prefix$glue" else ""}$path"
    fun stringSuffix(path: String, suffix: String?, glue: String = "_", glueWhen: (suffix: String?) -> Boolean = ::defaultGlueWhen) = "$path${if (glueWhen(suffix)) "$glue$suffix" else ""}"*/

    //fun defaultGlueWhen(str: String?) = str != null

    /*fun Identifier.modify(pathMod: (name: String) -> String) = modify(this, pathMod)
    fun Identifier.prefix(prefix: String?, glue: String = "_", glueWhen: (prefix: String?) -> Boolean = IdentifierHelper::defaultGlueWhen) = prefix(this, prefix, glue, glueWhen)
    fun Identifier.suffix(suffix: String?, glue: String = "_", glueWhen: (suffix: String?) -> Boolean = IdentifierHelper::defaultGlueWhen) = suffix(this, suffix, glue, glueWhen)*/

    val Block.identifier get() = BuiltInRegistries.BLOCK.getKey(this)
    val Item.identifier get() = BuiltInRegistries.ITEM.getKey(this)

}