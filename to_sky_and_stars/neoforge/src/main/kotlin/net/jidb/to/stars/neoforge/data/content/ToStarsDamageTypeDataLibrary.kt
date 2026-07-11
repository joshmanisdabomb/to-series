package net.jidb.to.stars.neoforge.data.content

import net.jidb.to.base.data.api.library.DatapackLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.Registries
import net.minecraft.world.damagesource.DamageEffects
import net.minecraft.world.damagesource.DamageType

object ToStarsDamageTypeDataLibrary : DatapackLibrary<DamageType>(ToStarsMod.modid) {

    override val registryKey = Registries.DAMAGE_TYPE

    val heated by this { DamageType("$modid.heated", 0.0F, DamageEffects.BURNING) }
    val boiled by this { DamageType("$modid.boiled", 0.0F, DamageEffects.BURNING) }

}
