package net.jidb.to.base.data.pub.collection.module.loot

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.GeneralLootDataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.resources.Identifier
import net.minecraft.world.level.storage.loot.LootTable

class GeneralLootDataCollectionModule(val id: Identifier, val builder: LootTable.Builder) : DataCollectionModule() {

    override fun generateGeneralLoot(collection: DataCollection<*>, event: GeneralLootDataCollectionEvent) = mapOf(id to builder)

}
