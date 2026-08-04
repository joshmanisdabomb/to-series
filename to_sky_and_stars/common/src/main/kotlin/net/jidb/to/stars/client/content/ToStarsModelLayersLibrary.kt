package net.jidb.to.stars.client.content

import net.jidb.to.base.client.pub.library.ModelLayerLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.block.render.model.CentrifugeBlockEntityModel

/**
 * [ModelLayerLibrary] implementation holding the model layers of this mod, i.e. the parts a block entity renderer is built from.
 */
object ToStarsModelLayersLibrary : ModelLayerLibrary(ToStarsMod.modid) {

    /**
     * The moving parts of a centrifuge.
     */
    val centrifuge by this(provider = CentrifugeBlockEntityModel::create)

}
