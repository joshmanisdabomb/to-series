package net.jidb.to.stars.content

import net.jidb.to.base.pub.library.TagLibrary
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item

/**
 * [TagLibrary] implementation holding the item tags of this mod.
 */
object ToStarsItemTagLibrary : TagLibrary<Item>(ToStarsMod.modid) {

    override val registryKey = Registries.ITEM

    /**
     * The items that grant the first advancement of this mod when obtained.
     */
    val root_advancement_unlock by this()

    /**
     * The items that unlock the tier one power bank's advancement.
     */
    val copper_power_bank_unlock by this()

    /**
     * The items that unlock the tier one and a half battery's advancement.
     */
    val gold_battery_unlock by this()

    /**
     * Enriched uranium in any of its forms.
     */
    val enriched_uranium by this()

    /**
     * Every battery, of any tier.
     */
    val batteries by this()

    /**
     * Every machine that makes heat.
     */
    val heat_generators by this()

    /**
     * The heat generators that make twice as much heat as the tag alone would suggest.
     */
    val heat_generator_2x by this()

    /**
     * Every turbine, of any tier.
     */
    val turbines by this()

    /**
     * Every power bank, of any tier.
     */
    val power_banks by this()

    /**
     * Every centrifuge, of any tier.
     */
    val centrifuges by this()

    /**
     * Every tier one machine.
     */
    val copper_machines by this()

    /**
     * Every tier one and a half machine.
     */
    val gold_machines by this()

}
