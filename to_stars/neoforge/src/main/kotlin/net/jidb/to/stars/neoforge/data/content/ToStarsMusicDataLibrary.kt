package net.jidb.to.stars.neoforge.data.content

import net.jidb.to.base.data.api.library.JukeboxSongLibrary
import net.jidb.to.stars.ToStarsMod

/**
 * [JukeboxSongLibrary] implementation generating the jukebox songs of this mod.
 */
object ToStarsMusicDataLibrary : JukeboxSongLibrary(ToStarsMod.modid) {

    /**
     * The song Gravitational Influence, which runs for just under two and a half minutes.
     */
    val gravitational_influence by this(ToStarsMod.sounds.gravitational_influence, 149.355f, 1)

}
