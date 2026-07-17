package net.jidb.to.stars.client.content

import net.jidb.to.base.client.pub.library.SpecialModelLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.item.render.EnergyStorageSpecialRenderer
import net.jidb.to.stars.client.item.render.RotorSpecialRenderer

object ToStarsSpecialModelLibrary : SpecialModelLibrary(ToStarsMod.modid) {

    val power_bank by this(EnergyStorageSpecialRenderer.codec)
    val rotor_blades by this(RotorSpecialRenderer.codec)

}
