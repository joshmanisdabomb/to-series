package net.jidb.to.base.neoforge.client.content.data.module

import net.jidb.to.base.neoforge.client.content.data.ToBaseModels

/**
 * A [CableBlockModelClientDataCollectionModule] for a cable four pixels across, which is the size the base mod's own cables are drawn at.
 *
 * @param bare Whether the cable is drawn without its outer casing, i.e. as the conductor alone. Defaults to `false`.
 * @since 0.6.0
 */
class Cable4BlockModelClientDataCollectionModule(bare: Boolean = false) : CableBlockModelClientDataCollectionModule(
    if (bare) ToBaseModels.cable4CenterBare else ToBaseModels.cable4Center,
    if (bare) ToBaseModels.cable4ConnectionBare else ToBaseModels.cable4Connection,
    ToBaseModels.cable4ItemTemplate
)
