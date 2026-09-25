package net.jidb.to.stars.content.key

import net.jidb.to.base.pub.library.DamageTypeLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.Registries

/**
 * [DamageTypeLibrary] implementation holding the ways this mod can kill a player.
 */
object ToStarsDamageTypeLibrary : DamageTypeLibrary(ToStarsMod.modid) {

    override val registryKey = Registries.DAMAGE_TYPE

    /**
     * Burned by a machine that has run too hot.
     */
    val heated by this()

    /**
     * Scalded by standing in a boiler.
     */
    val boiled by this()

    /**
     * Cut by a turbine's rotor blades while they are turning.
     */
    val rotor_blades by this()

}
