package net.jidb.to.stars.neoforge.client.data.module

import net.jidb.to.base.neoforge.client.content.data.ToBaseModels
import net.jidb.to.base.neoforge.client.content.data.module.CableBlockModelClientDataCollectionModule
import net.jidb.to.stars.neoforge.client.data.ToStarsModels

class HeatCableBlockModelClientDataCollectionModule : CableBlockModelClientDataCollectionModule(ToBaseModels.CABLE10_CENTER, ToBaseModels.CABLE10_CONNECTION, ToStarsModels.TEMPLATE_HEAT_PIPE, rim = ToBaseModels.CABLE10_END)