package net.jidb.to.stars.client.content

import net.jidb.to.base.client.library.EntityRendererLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.entity.AtomicBombEntityRenderer

object ToStarsEntityRenderersLibrary : EntityRendererLibrary(ToStarsMod.modid) {

    val atomic_bomb by this { EntityRendererEntry({ ToStarsMod.entities.atomic_bomb }, ::AtomicBombEntityRenderer) }

}
