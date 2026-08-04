package net.jidb.to.stars.neoforge.client.data.module

import net.jidb.to.base.neoforge.client.content.data.ToBaseModels
import net.jidb.to.base.neoforge.client.content.data.module.CableBlockModelClientDataCollectionModule
import net.jidb.to.stars.neoforge.client.data.ToStarsModels

/**
 * A [net.jidb.to.base.neoforge.client.content.data.module.CableBlockModelClientDataCollectionModule] for the heat pipe, which is drawn ten pixels across with a rim where it meets a machine.
 */
class HeatCableBlockModelClientDataCollectionModule : CableBlockModelClientDataCollectionModule(ToBaseModels.cable10Center, ToBaseModels.cable10Connection, ToStarsModels.heatPipeTemplate, rim = ToBaseModels.cable10End)
