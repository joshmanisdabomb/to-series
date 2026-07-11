package net.jidb.to.stars.content.key

import net.jidb.to.base.pub.library.DamageTypeLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.Registries

object ToStarsDamageTypeLibrary : DamageTypeLibrary(ToStarsMod.modid) {

    override val registryKey = Registries.DAMAGE_TYPE

    val heated by this()
    val boiled by this()

}