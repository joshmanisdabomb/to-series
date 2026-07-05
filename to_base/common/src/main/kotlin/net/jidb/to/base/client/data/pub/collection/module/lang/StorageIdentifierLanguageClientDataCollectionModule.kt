package net.jidb.to.base.client.data.pub.collection.module.lang

import net.minecraft.resources.ResourceKey

class StorageIdentifierLanguageClientDataCollectionModule(modifyId: (id: String) -> String = { it }) : IdentifierLanguageClientDataCollectionModule(modifyId) {

    override fun getEntryName(key: ResourceKey<*>): String {
        val before = super.getEntryName(key)
        if (!before.endsWith("_block")) return before
        return before.dropLast(6)
    }

    override fun inflector(name: String): String {
        return "Block of ${super.inflector(name)}"
    }

}