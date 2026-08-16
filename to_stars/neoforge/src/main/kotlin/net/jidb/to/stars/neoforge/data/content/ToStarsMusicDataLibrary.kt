package net.jidb.to.stars.neoforge.data.content

import net.jidb.to.base.data.api.library.JukeboxSongLibrary
import net.jidb.to.stars.ToStarsMod

object ToStarsMusicDataLibrary : JukeboxSongLibrary(ToStarsMod.modid) {

    val gravitational_influence by this(ToStarsMod.sounds.gravitational_influence, 149.355f, 1)

}
