package net.jidb.to.base.pub.network

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.pub.library.PayloadLibrary

/**
 * [PayloadLibrary] implementation that declares the network payloads To Lay the Foundations sends, and provides access to them in one place.
 *
 * @since 0.5.0
 */
object ToBasePayloadLibrary : PayloadLibrary(ToBaseMod.modid) {

    /**
     * Tells a client to play a sound audible far beyond vanilla's usual range.
     *
     * @see DistantSoundPayload
     * @since 0.5.0
     */
    val distant_sound by this { DistantSoundPayload.Companion }

}
