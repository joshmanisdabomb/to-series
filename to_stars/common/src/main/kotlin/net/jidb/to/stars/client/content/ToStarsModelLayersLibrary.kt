package net.jidb.to.stars.client.content

import net.jidb.to.base.client.pub.library.ModelLayerLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.block.render.model.CentrifugeBlockEntityModel

object ToStarsModelLayersLibrary : ModelLayerLibrary(ToStarsMod.modid) {

    val centrifuge by this(provider = CentrifugeBlockEntityModel::create)

}
