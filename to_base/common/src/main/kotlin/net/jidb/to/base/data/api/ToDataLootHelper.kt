package net.jidb.to.base.data.api

import net.minecraft.advancements.predicates.DataComponentMatchers
import net.minecraft.advancements.predicates.EnchantmentPredicate
import net.minecraft.advancements.predicates.ItemPredicate
import net.minecraft.advancements.predicates.MinMaxBounds
import net.minecraft.advancements.predicates.StatePropertiesPredicate
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

/**
 * A helper that builds the loot tables a block usually wants, so that a module need not assemble a common one by hand.
 * Vanilla's own equivalents live on its block loot provider, which a cross-platform generator cannot reach, so the shapes that are actually used are rebuilt here.
 *
 * Every table survives an explosion by default, matching vanilla, which is why each function takes an `explosionResistant` flag rather than the other way round.
 *
 * @property provider The registries the loot tables are built against, which the silk touch and fortune enchantments are looked up in.
 * @since 0.3.0
 */
class ToDataLootHelper(val provider: HolderLookup.Provider) {

    /**
     * A table dropping a single item, which is what an ordinary block wants.
     *
     * @param drop The item the block drops.
     * @param explosionResistant Whether the drop survives being blown up. Defaults to `false`.
     * @return The loot table builder.
     * @since 0.3.0
     */
    fun simpleBlockLoot(drop: ItemLike, explosionResistant: Boolean = false) = LootTable.lootTable()
        .withPool(
            LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0f))
                .add(LootItem.lootTableItem(drop))
                .let { if (explosionResistant) it else it.`when`(ExplosionCondition.survivesExplosion()) }
        )

    /**
     * A table dropping one item when broken with silk touch and another otherwise, as glass and leaves do.
     *
     * @param silk The item dropped when the tool has silk touch.
     * @param drop The item dropped otherwise, or `null` to drop nothing without silk touch.
     * @param explosionResistant Whether the drop survives being blown up. Defaults to `false`.
     * @return The loot table builder.
     * @since 0.3.0
     */
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

    /**
     * A table for an ore: the block itself when broken with silk touch, and its raw drop affected by fortune otherwise.
     *
     * @param silk The item dropped when the tool has silk touch, usually the ore block.
     * @param drop The item dropped otherwise.
     * @param count How many of the drop to give before fortune applies. Defaults to exactly one.
     * @param fortune How fortune multiplies the drop. Defaults to the ore bonus vanilla's own ores use.
     * @param explosionResistant Whether the drop survives being blown up. Defaults to `false`.
     * @return The loot table builder.
     * @since 0.3.0
     */
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

    /**
     * A table that only drops where the block is in a particular state, which is how a multi-block structure drops once rather than once per part.
     *
     * @param T The type of the block state property being matched.
     * @param block The block the property belongs to.
     * @param property The property deciding whether anything drops.
     * @param value The value of the property that drops.
     * @param drop The item to drop. Defaults to the block itself.
     * @param explosionResistant Whether the drop survives being blown up. Defaults to `false`.
     * @return The loot table builder.
     * @since 0.3.0
     */
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
