package net.jidb.to.base.pub.library

import net.jidb.to.base.api.library.LibraryTagList
import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.base.api.properties.ExtendedBlockProperties
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.Block

open class BlockLibrary(modid: String) : SimpleRegistryLibrary<Block>(modid) {

    override val registry = BuiltInRegistries.BLOCK

    val properties = ExtendedBlockPropertiesList()

    inner class ExtendedBlockPropertiesList : LibraryTagList<Block, ExtendedBlockProperties>() {

        fun build() {
            entries.forEach { (key, entry) ->
                getEntryTags(this, entry).forEach { it.build(entry.value) }
            }
        }

    }

}
