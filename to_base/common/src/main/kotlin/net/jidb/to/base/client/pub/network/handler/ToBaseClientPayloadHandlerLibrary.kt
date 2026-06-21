package net.jidb.to.base.client.pub.network.handler

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.pub.library.ClientPayloadHandlerLibrary

object ToBaseClientPayloadHandlerLibrary : ClientPayloadHandlerLibrary(ToBaseMod.modid) {

    val distant_sound by this { PayloadHandlerEntry({ ToBaseMod.payloads.distant_sound.type }, DistantSoundPayloadHandler::handle) }

}
