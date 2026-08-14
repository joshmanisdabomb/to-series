package net.jidb.to.base.neoforge.client.content.data

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.neoforge.client.service.ForgeDataModService
import net.neoforged.neoforge.data.event.GatherDataEvent

/**
 * [ForgeDataModService] implementation for the base mod itself, i.e. the entry the data generation run reaches [ToBaseForgeDataMod] through.
 *
 * @since 1.0.0
 */
class ToBaseForgeDataModService : ForgeDataModService() {

    override val modid = ToBaseMod.MOD_ID

    override fun generate(event: GatherDataEvent.Client) = ToBaseForgeDataMod(event).generate()

}
