package net.jidb.to.base.client.data.pub.collection.module.lang

import net.minecraft.resources.ResourceKey

/**
 * An [IdentifierLanguageClientDataCollectionModule] for a block that a material is compacted into, which reads as `Block of Uranium` rather than `Uranium Block`, matching vanilla's own storage blocks.
 * A `_block` suffix is dropped from the name before the display name is built, so that the word is not said twice.
 *
 * @param modifyId A function adjusting the registered name before it is turned into a display name. Defaults to leaving it as it is.
 * @since 0.3.0
 */
class StorageIdentifierLanguageClientDataCollectionModule(modifyId: (id: String) -> String = { it }) : IdentifierLanguageClientDataCollectionModule(modifyId) {

    override fun getEntryName(key: ResourceKey<*>): String {
        val before = super.getEntryName(key)
        if (!before.endsWith("_block")) return before
        return before.dropLast(6)
    }

    override fun inflector(name: String) = "Block of ${super.inflector(name)}"

}
