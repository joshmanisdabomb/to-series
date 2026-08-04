package net.jidb.to.base.hooks.level.storage

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.level.storage.SavedDataTypeConstructor
import net.minecraft.world.level.saveddata.SavedDataType

/**
 * [SimpleLibrary] implementation holding the saved data that the base mod itself keeps on a level.
 *
 * @since 0.6.0
 */
object ToBaseSavedDataLibrary : SimpleLibrary<SavedDataType<*>>(ToBaseMod.modid) {

    /**
     * The block networks of a level, i.e. which blocks are joined to which and what each of those networks currently holds.
     *
     * @since 0.6.0
     */
    val block_networks by this { SavedDataTypeConstructor.createType(it.id, ::BlockNetworkSavedData, BlockNetworkSavedData.codec, null) }

}
