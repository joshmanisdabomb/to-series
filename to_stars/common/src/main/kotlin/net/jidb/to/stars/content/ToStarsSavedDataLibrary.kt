package net.jidb.to.stars.content

import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.level.storage.SavedDataTypeConstructor
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.level.storage.AdvancementRaceSavedData
import net.minecraft.world.level.saveddata.SavedDataType

object ToStarsSavedDataLibrary : SimpleLibrary<SavedDataType<*>>(ToStarsMod.modid) {

    val advancement_race by this { SavedDataTypeConstructor.createType(it.id, ::AdvancementRaceSavedData, AdvancementRaceSavedData.codec, null) }

}
