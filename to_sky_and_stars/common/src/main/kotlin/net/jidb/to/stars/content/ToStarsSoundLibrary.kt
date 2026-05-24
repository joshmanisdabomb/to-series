package net.jidb.to.stars.content

import net.jidb.to.base.library.SoundEventLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.sounds.SoundEvent

object ToStarsSoundLibrary : SoundEventLibrary(ToStarsMod.MOD_ID) {

    val nuke_small by this { SoundEvent.createVariableRangeEvent(it.id.withPrefix("entity.generic.")) }
    val nuke_large by this { SoundEvent.createVariableRangeEvent(it.id.withPrefix("entity.generic.")) }

    val atomic_bomb_activate by this { SoundEvent.createVariableRangeEvent(it.id.withPrefix("entity.").withPath { it.replace("atomic_bomb_", "atomic_bomb.")}) }
    val atomic_bomb_timer by this { SoundEvent.createVariableRangeEvent(it.id.withPrefix("entity.").withPath { it.replace("atomic_bomb_", "atomic_bomb.")}) }
    val atomic_bomb_cut by this { SoundEvent.createVariableRangeEvent(it.id.withPrefix("entity.").withPath { it.replace("atomic_bomb_", "atomic_bomb.")}) }

}