package net.jidb.to.base.neoforge.client.content.data.module

import net.jidb.to.base.neoforge.client.content.data.ToBaseModels

class Cable4BlockModelClientDataCollectionModule(bare: Boolean = false) : CableBlockModelClientDataCollectionModule(
    if (bare) ToBaseModels.BARE_CABLE4_CENTER else ToBaseModels.CABLE4_CENTER,
    if (bare) ToBaseModels.BARE_CABLE4_CONNECTION else ToBaseModels.CABLE4_CONNECTION,
    ToBaseModels.TEMPLATE_CABLE4_ITEM
)
