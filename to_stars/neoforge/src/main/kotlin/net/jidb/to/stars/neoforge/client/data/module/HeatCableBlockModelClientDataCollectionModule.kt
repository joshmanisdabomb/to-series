package net.jidb.to.stars.neoforge.client.data.module

import net.jidb.to.base.neoforge.client.content.data.ToBaseModels
import net.jidb.to.base.neoforge.client.content.data.module.CableBlockModelClientDataCollectionModule
import net.jidb.to.stars.neoforge.client.data.ToStarsModels

class HeatCableBlockModelClientDataCollectionModule : CableBlockModelClientDataCollectionModule(ToBaseModels.cable10Center, ToBaseModels.cable10Connection, ToStarsModels.heatPipeTemplate, rim = ToBaseModels.cable10End)
