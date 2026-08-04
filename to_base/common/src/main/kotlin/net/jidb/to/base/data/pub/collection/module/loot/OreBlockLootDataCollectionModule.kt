package net.jidb.to.base.data.pub.collection.module.loot

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.BlockLootDataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.core.Holder
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
import net.minecraft.world.level.storage.loot.functions.LootItemFunction
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider

/**
 * A [DataCollectionModule] generating the loot table of an ore, i.e. the block itself under silk touch and its raw drop, affected by fortune, otherwise.
 *
 * @property drop The item the ore drops when mined without silk touch.
 * @property silk The item dropped under silk touch, or `null` for the ore block itself. Defaults to `null`.
 * @property count How many of the drop to give before fortune applies. Defaults to exactly one.
 * @property fortune How fortune multiplies the drop. Defaults to the ore bonus vanilla's own ores use.
 * @since 0.3.0
 */
class OreBlockLootDataCollectionModule(val drop: ItemLike, val silk: ItemLike? = null, val count: NumberProvider = ConstantValue.exactly(1.0f), val fortune: (Holder.Reference<Enchantment>) -> LootItemFunction.Builder = ApplyBonusCount::addOreBonusCount) : DataCollectionModule() {

    override fun generateBlockLoot(collection: DataCollection<Block>, event: BlockLootDataCollectionEvent) = mapOf(collection.`object` to event.helper.oreBlockLoot(silk ?: collection.`object`, drop, count, fortune))

}
