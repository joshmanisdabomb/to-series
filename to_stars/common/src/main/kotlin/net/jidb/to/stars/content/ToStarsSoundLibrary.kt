package net.jidb.to.stars.content

import net.jidb.to.base.pub.library.SoundEventLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.sounds.SoundEvent

/**
 * [SoundEventLibrary] implementation holding the sounds of this mod.
 */
object ToStarsSoundLibrary : SoundEventLibrary(ToStarsMod.MOD_ID) {

    /**
     * A nuclear explosion heard close to, or a small one.
     */
    val nuke_small by this { SoundEvent.createVariableRangeEvent(it.id.withPrefix("entity.generic.")) }

    /**
     * A nuclear explosion heard from far away, which carries much further than an ordinary sound.
     */
    val nuke_large by this { SoundEvent.createVariableRangeEvent(it.id.withPrefix("entity.generic.")) }

    /**
     * An atomic bomb being armed.
     */
    val atomic_bomb_activate by this { SoundEvent.createVariableRangeEvent(it.id.withPrefix("entity.").withPath { it.replace("atomic_bomb_", "atomic_bomb.") }) }

    /**
     * One tick of an armed atomic bomb's countdown.
     */
    val atomic_bomb_timer by this { SoundEvent.createVariableRangeEvent(it.id.withPrefix("entity.").withPath { it.replace("atomic_bomb_", "atomic_bomb.") }) }

    /**
     * An armed atomic bomb's countdown being stopped.
     */
    val atomic_bomb_cut by this { SoundEvent.createVariableRangeEvent(it.id.withPrefix("entity.").withPath { it.replace("atomic_bomb_", "atomic_bomb.") }) }

    /**
     * A generator burning fuel.
     */
    val generator_crackle by this { SoundEvent.createVariableRangeEvent(it.id.withPrefix("block.").withPath { it.replace("generator_", "generator.") }) }

    /**
     * A generator running out of fuel.
     */
    val generator_empty by this { SoundEvent.createVariableRangeEvent(it.id.withPrefix("block.").withPath { it.replace("generator_", "generator.") }) }

    /**
     * The song Gravitational Influence, as a jukebox plays it.
     */
    val gravitational_influence by this { SoundEvent.createVariableRangeEvent(it.id.withPrefix("music_disc.")) }

}
