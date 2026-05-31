package net.jidb.to.base.hooks.network

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.library.PayloadLibrary

object ToBasePayloadLibrary : PayloadLibrary(ToBaseMod.modid) {

    val distant_sound by this { DistantSoundPayload.Companion }

}