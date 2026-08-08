package net.jidb.to.stars.client.content

import net.jidb.to.base.client.pub.library.SpecialModelLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.item.render.EnergyStorageSpecialRenderer
import net.jidb.to.stars.client.item.render.RotorSpecialRenderer

/**
 * [SpecialModelLibrary] implementation holding this mod's items that are drawn by a renderer rather than from a model.
 */
object ToStarsSpecialModelLibrary : SpecialModelLibrary(ToStarsMod.modid) {

    /**
     * Draws a power bank in the hand with its charge showing.
     */
    val power_bank by this(EnergyStorageSpecialRenderer.codec)

    /**
     * Draws the rotor blades in the hand.
     */
    val rotor_blades by this(RotorSpecialRenderer.codec)

}
