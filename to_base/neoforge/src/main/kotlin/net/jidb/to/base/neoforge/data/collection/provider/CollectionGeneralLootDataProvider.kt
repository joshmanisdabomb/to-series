package net.jidb.to.base.neoforge.data.collection.provider

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.GeneralLootDataCollectionEvent
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.storage.loot.LootTable
import java.util.function.BiConsumer

/**
 * The [LootTableSubProvider] that generates the loot tables described by a mod's collections which belong to no block, such as those of a chest or an entity.
 *
 * @param collections The collections being generated from.
 * @property provider The registries the loot tables are built against.
 * @since 0.8.0
 */
class CollectionGeneralLootDataProvider(private val collections: Iterable<DataCollection<*>>, protected val provider: HolderLookup.Provider) : LootTableSubProvider {

    override fun generate(output: BiConsumer<ResourceKey<LootTable>, LootTable.Builder>) {
        val event = GeneralLootDataCollectionEvent(provider)
        val tables = event.process(collections)
        tables?.forEach { (id, loot) ->
            output.accept(ResourceKey.create(Registries.LOOT_TABLE, id), loot)
        }
    }

}
