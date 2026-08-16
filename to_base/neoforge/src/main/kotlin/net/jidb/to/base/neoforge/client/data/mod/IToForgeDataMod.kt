package net.jidb.to.base.neoforge.client.data.mod

import net.neoforged.neoforge.data.event.GatherDataEvent

interface IToForgeDataMod {

    val event: GatherDataEvent.Client

    fun generate()

}
