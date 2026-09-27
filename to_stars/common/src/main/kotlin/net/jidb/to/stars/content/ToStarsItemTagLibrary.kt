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

    /**
     * Every tier two machine.
     */
    val steel_machines by this()

    /**
     * Every tier two and a half machine.
     */
    val netherite_machines by this()

    /**
     * Every machine enclosure, of any tier.
     */
    val machine_enclosures by this()

    /**
     * Every tier 1 machine enclosure, including tier 1.5.
     * @since 0.2.0
     */
    val tier_1_machine_enclosures by this()

    /**
     * Every tier 2 machine enclosure, including tier 1.5.
     * @since 0.2.0
     */
    val tier_2_machine_enclosures by this()

    /**
     * Every tier 1 power bank, including tier 1.5.
     * @since 0.2.0
     */
    val tier_1_power_banks by this()

    /**
     * Every tier 2 power bank, including tier 1.5.
     * @since 0.2.0
     */
    val tier_2_power_banks by this()

    /**
     * Every tier 1 battery, including tier 1.5.
     * @since 0.2.0
     */
    val tier_1_batteries by this()

    /**
     * Every tier 2 battery, including tier 1.5.
     * @since 0.2.0
     */
    val tier_2_batteries by this()

    /**
     * Every tier 1 solid heat generator, including tier 1.5.
     * @since 0.2.0
     */
    val tier_1_solid_generators by this()

    /**
     * Every tier 2 solid heat generator, including tier 1.5.
     * @since 0.2.0
     */
    val tier_2_solid_generators by this()

    /**
     * Every tier 1 turbine, including tier 1.5.
     * @since 0.2.0
     */
    val tier_1_turbines by this()

    /**
     * Every tier 2 turbine, including tier 1.5.
     * @since 0.2.0
     */
    val tier_2_turbines by this()

    /**
     * Every tier 1 centrifuge, including tier 1.5.
     * @since 0.2.0
     */
    val tier_1_centrifuges by this()

    /**
     * Every tier 2 centrifuge, including tier 1.5.
     * @since 0.2.0
     */
    val tier_2_centrifuges by this()

}
