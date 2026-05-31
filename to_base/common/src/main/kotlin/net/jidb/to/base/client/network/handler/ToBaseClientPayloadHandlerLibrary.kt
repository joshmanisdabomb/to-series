package net.jidb.to.base.client.network.handler

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.hooks.network.handler.DistantSoundPayloadHandler
import net.jidb.to.base.client.library.ClientPayloadHandlerLibrary

object ToBaseClientPayloadHandlerLibrary : ClientPayloadHandlerLibrary(ToBaseMod.modid) {

    val distant_sound by this { PayloadHandlerEntry({ ToBaseMod.payloads.distant_sound.type }, DistantSoundPayloadHandler::handle) }

}
