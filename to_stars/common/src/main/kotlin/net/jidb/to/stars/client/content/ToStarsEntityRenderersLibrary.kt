package net.jidb.to.stars.client.content

import net.jidb.to.base.client.pub.library.EntityRendererLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.entity.AtomicBombEntityRenderer

/**
 * [EntityRendererLibrary] implementation holding how each of this mod's entities is drawn.
 */
object ToStarsEntityRenderersLibrary : EntityRendererLibrary(ToStarsMod.modid) {

    /**
     * Draws a falling atomic bomb.
     */
    val atomic_bomb by this { EntityRendererEntry({ ToStarsMod.entities.atomic_bomb }, ::AtomicBombEntityRenderer) }

}
