package net.jidb.to.stars.neoforge.data.module

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.BlockLootDataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.advancements.predicates.DataComponentMatchers
import net.minecraft.advancements.predicates.EnchantmentPredicate
import net.minecraft.advancements.predicates.ItemPredicate
import net.minecraft.advancements.predicates.MinMaxBounds
import net.minecraft.core.component.predicates.DataComponentPredicates
import net.minecraft.core.component.predicates.EnchantmentsPredicate
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition
import net.minecraft.world.level.storage.loot.predicates.MatchTool
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue

/**
 * A [DataCollectionModule] generating the loot table of a block that stores To Energy, which keeps whatever it was holding when it is broken with silk touch and loses it otherwise.
 */
class EnergySilkBlockLootDataCollectionModule : DataCollectionModule() {

    override fun generateBlockLoot(collection: DataCollection<Block>, event: BlockLootDataCollectionEvent) = mapOf(collection.`object` to LootTable.lootTable()
        .withPool(
            LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0f))
                .add(
                    AlternativesEntry.alternatives(
                        LootItem.lootTableItem(collection.`object`).`when`(
                            MatchTool.toolMatches(
                                ItemPredicate.Builder.item().withComponents(
                                    DataComponentMatchers.Builder.components()
                                        .partial(
                                            DataComponentPredicates.ENCHANTMENTS, EnchantmentsPredicate.enchantments(listOf(
                                                EnchantmentPredicate(
                                                    event.helper.provider.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH),
                                                    MinMaxBounds.Ints.atLeast(1)
                                                )
                                            ))
                                        )
                                        .build()
                                )
                            )
                        ).apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(ToBaseMod.itemComponents.energy_data)),
                        LootItem.lootTableItem(collection.`object`).`when`(ExplosionCondition.survivesExplosion())
                    )
                )
        ))

}
