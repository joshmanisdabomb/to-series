package net.jidb.to.base.client.pub.network.handler

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.pub.library.ClientPayloadHandlerLibrary

/**
 * [ClientPayloadHandlerLibrary] implementation that declares the client-side payload handlers of To Lay the Foundations, and provides access to them in one place.
 *
 * @since 0.5.0
 */
object ToBaseClientPayloadHandlerLibrary : ClientPayloadHandlerLibrary(ToBaseMod.modid) {

    /**
     * Handles [net.jidb.to.base.pub.network.DistantSoundPayload] by playing the sound it describes.
     *
     * @since 0.5.0
     */
    val distant_sound by this { PayloadHandlerEntry({ ToBaseMod.payloads.distant_sound.type }, DistantSoundPayloadHandler::handle) }

}
