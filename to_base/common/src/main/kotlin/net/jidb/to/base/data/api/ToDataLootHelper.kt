package net.jidb.to.base.data.api

import net.minecraft.advancements.criterion.*
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.predicates.DataComponentPredicates
import net.minecraft.core.component.predicates.EnchantmentsPredicate
import net.minecraft.core.registries.Registries
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay
import net.minecraft.world.level.storage.loot.functions.LootItemFunction
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.predicates.MatchTool
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider

class ToDataLootHelper(val provider: HolderLookup.Provider) {

    fun simpleBlockLoot(drop: ItemLike, explosionResistant: Boolean = false) = LootTable.lootTable()
        .withPool(
            LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0f))
                .add(LootItem.lootTableItem(drop))
                .let { if (explosionResistant) it else it.`when`(ExplosionCondition.survivesExplosion()) }
        )

    fun silkBlockLoot(silk: ItemLike, drop: ItemLike?, explosionResistant: Boolean = false) = LootTable.lootTable()
        .withPool(
            LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0f))
                .add(
                    AlternativesEntry.alternatives(
                        LootItem.lootTableItem(silk).`when`(
                            MatchTool.toolMatches(
                                ItemPredicate.Builder.item().withComponents(
                                    DataComponentMatchers.Builder.components()
                                        .partial(
                                            DataComponentPredicates.ENCHANTMENTS, EnchantmentsPredicate.enchantments(listOf(
                                                EnchantmentPredicate(
                                                    provider.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH),
                                                    MinMaxBounds.Ints.atLeast(1)
                                                )
                                            ))
                                        )
                                        .build()
                                )
                            )
                        ),
                        *if (drop != null) arrayOf(
                            LootItem.lootTableItem(drop)
                                .let { if (explosionResistant) it else it.`when`(ExplosionCondition.survivesExplosion()) }
                        ) else emptyArray()
                    )
                )
        )

    fun oreBlockLoot(silk: ItemLike, drop: ItemLike, count: NumberProvider = ConstantValue.exactly(1.0f), fortune: (fortune: Holder.Reference<Enchantment>) -> LootItemFunction.Builder = ApplyBonusCount::addOreBonusCount, explosionResistant: Boolean = false) = LootTable.lootTable()
        .withPool(
            LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0f))
                .add(
                    AlternativesEntry.alternatives(
                        LootItem.lootTableItem(silk).`when`(
                            MatchTool.toolMatches(
                                ItemPredicate.Builder.item().withComponents(
                                    DataComponentMatchers.Builder.components()
                                        .partial(
                                            DataComponentPredicates.ENCHANTMENTS, EnchantmentsPredicate.enchantments(listOf(
                                                EnchantmentPredicate(
                                                    provider.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH),
                                                    MinMaxBounds.Ints.atLeast(1)
                                                )
                                            ))
                                        )
                                        .build()
                                )
                            )
                        ),
                        LootItem.lootTableItem(drop)
                            .apply(SetItemCountFunction.setCount(count))
                            .apply(fortune(provider.getOrThrow(Enchantments.FORTUNE)))
                            .let { if (explosionResistant) it else it.apply(ApplyExplosionDecay.explosionDecay()) }
                    )
                )
                .let { if (explosionResistant) it.`when`(ExplosionCondition.survivesExplosion()) else it }
        )

    fun <T> propertyBlockLoot(block: Block, property: Property<T>, value: T, drop: ItemLike = block, explosionResistant: Boolean = false) where T : Comparable<T>, T : StringRepresentable = LootTable.lootTable().withPool(
        LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1.0f))
            .add(
                LootItem.lootTableItem(drop).`when`(
                    LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, value))
                )
            )
            .let { if (explosionResistant) it else it.`when`(ExplosionCondition.survivesExplosion()) }
    )

}