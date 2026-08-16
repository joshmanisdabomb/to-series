package net.jidb.to.stars.content

import net.jidb.to.base.pub.library.TagLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item

object ToStarsItemTagLibrary : TagLibrary<Item>(ToStarsMod.modid) {

    override val registryKey = Registries.ITEM

    val root_advancement_unlock by this()

    val copper_power_bank_unlock by this()

    val gold_battery_unlock by this()

    val enriched_uranium by this()

    val batteries by this()

    val heat_generators by this()

    val heat_generator_2x by this()

    val turbines by this()

    val power_banks by this()

    val centrifuges by this()

    val copper_machines by this()

    val gold_machines by this()

}
