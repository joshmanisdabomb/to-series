package net.jidb.to.stars.client.content

import net.jidb.to.base.client.pub.library.SpecialModelLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.render.special.EnergyStorageSpecialRenderer

object ToStarsSpecialModelLibrary : SpecialModelLibrary(ToStarsMod.modid) {

    val power_bank by this(EnergyStorageSpecialRenderer.codec)

}
