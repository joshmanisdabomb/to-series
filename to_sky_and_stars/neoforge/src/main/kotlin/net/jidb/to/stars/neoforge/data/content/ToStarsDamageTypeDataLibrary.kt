package net.jidb.to.stars.neoforge.data.content

import net.jidb.to.base.data.api.library.DatapackLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.Registries
import net.minecraft.world.damagesource.DamageEffects
import net.minecraft.world.damagesource.DamageType

/**
 * [DatapackLibrary] implementation generating the damage types of this mod.
 */
object ToStarsDamageTypeDataLibrary : DatapackLibrary<DamageType>(ToStarsMod.modid) {

    override val registryKey = Registries.DAMAGE_TYPE

    /**
     * Burned by a machine that has run too hot.
     */
    val heated by this { DamageType("$modid.heated", 0.0F, DamageEffects.BURNING) }

    /**
     * Scalded by standing in a boiler.
     */
    val boiled by this { DamageType("$modid.boiled", 0.0F, DamageEffects.BURNING) }

    /**
     * Cut by a turbine's rotor blades while they are turning.
     */
    val rotor_blades by this { DamageType("$modid.rotor_blades", 0.0F) }

}
