package net.jidb.to.base.pub.network

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.pub.library.PayloadLibrary

object ToBasePayloadLibrary : PayloadLibrary(ToBaseMod.modid) {

    val distant_sound by this { DistantSoundPayload.Companion }

}