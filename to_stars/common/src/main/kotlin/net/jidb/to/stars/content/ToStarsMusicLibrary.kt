package net.jidb.to.stars.content

import net.jidb.to.base.api.library.ResourceKeyLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.JukeboxSong

object ToStarsMusicLibrary : ResourceKeyLibrary<JukeboxSong>(ToStarsMod.modid) {

    override val registryKey = Registries.JUKEBOX_SONG

    val gravitational_influence by this()

}
