package net.jidb.to.base.neoforge.client.content.data.module

import net.jidb.to.base.neoforge.client.content.data.ToBaseModels

class Cable4BlockModelClientDataCollectionModule(bare: Boolean = false) : CableBlockModelClientDataCollectionModule(
    if (bare) ToBaseModels.cable4CenterBare else ToBaseModels.cable4Center,
    if (bare) ToBaseModels.cable4ConnectionBare else ToBaseModels.cable4Connection,
    ToBaseModels.cable4ItemTemplate
)
