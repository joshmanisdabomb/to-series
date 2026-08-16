package net.jidb.to.base.hooks.level.storage

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.level.storage.SavedDataTypeConstructor
import net.minecraft.world.level.saveddata.SavedDataType

object ToBaseSavedDataLibrary : SimpleLibrary<SavedDataType<*>>(ToBaseMod.modid) {

    val block_networks by this { SavedDataTypeConstructor.createType(it.id, ::BlockNetworkSavedData, BlockNetworkSavedData.codec, null) }

}
