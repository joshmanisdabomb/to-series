package net.jidb.to.stars.neoforge.client.data

import net.jidb.to.base.neoforge.client.service.ForgeDataModService
import net.jidb.to.stars.ToStarsMod
import net.neoforged.neoforge.data.event.GatherDataEvent

/**
 * [ForgeDataModService] implementation for the content mod, i.e. the entry the data generation run reaches [ToStarsForgeDataMod] through.
 */
class ToStarsForgeDataModService : ForgeDataModService() {

    override val modid = ToStarsMod.MOD_ID

    override fun generate(event: GatherDataEvent.Client) = ToStarsForgeDataMod(event).generate()

}
