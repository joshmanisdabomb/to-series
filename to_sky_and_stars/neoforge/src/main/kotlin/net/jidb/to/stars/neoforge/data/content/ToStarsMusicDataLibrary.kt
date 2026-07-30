package net.jidb.to.stars.neoforge.data.content

import net.jidb.to.base.data.api.library.DatapackLibrary
import net.jidb.to.base.data.api.library.JukeboxSongLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.damagesource.DamageEffects
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.item.JukeboxSong

object ToStarsMusicDataLibrary : JukeboxSongLibrary(ToStarsMod.modid) {

    val gravitational_influence by this(ToStarsMod.sounds.gravitational_influence, 149.355f, 1)

}
