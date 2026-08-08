package net.jidb.to.stars.content

import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.level.storage.SavedDataTypeConstructor
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.level.storage.AdvancementRaceSavedData
import net.minecraft.world.level.saveddata.SavedDataType

/**
 * [SimpleLibrary] implementation holding the saved data this mod keeps on a level.
 */
object ToStarsSavedDataLibrary : SimpleLibrary<SavedDataType<*>>(ToStarsMod.modid) {

    /**
     * Which player was first on the server to earn each advancement.
     */
    val advancement_race by this { SavedDataTypeConstructor.createType(it.id, ::AdvancementRaceSavedData, AdvancementRaceSavedData.codec, null) }

}
