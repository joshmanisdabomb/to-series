package net.jidb.to.base.api.info

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.ItemLore
import net.minecraft.world.level.Level
import java.text.DecimalFormat

/**
 * API class that mods can extend for easy creation of tooltips for cases like items and GUI elements.
 * Contains helper methods for creating properties with values separated by ': ', adding line breaks, and injecting tooltips into item stacks.
 *
 * @param modid The mod ID for this tooltip engine.
 * @since 0.6.0
 */
abstract class TooltipEngine(val modid: String) {

    /**
     * Creates a translatable [MutableComponent] in the form "`$prefix.$modid.$topic[.$type]`: `$prefix.$modid.$topic[.$type].value`"
     *
     * @param topic The core topic or category of the property being represented, used in the translation key.
     * @param type A specific type or subcategory associated with the property, used in the translation key. Can be empty.
     * @param color The integer value for the value [Component] color.
     * @param value Arguments representing the dynamic values to be given to the value [Component].
     * @param prefix A [String] prefix used within the translation key.
     * @return A [MutableComponent] containing the styled tooltip line.
     * @since 0.6.0
     */
    fun createPropertyComponent(topic: String, type: String, color: Int, vararg value: Any, prefix: String = "tooltip"): MutableComponent {
        val suffix = if (type.isNotEmpty()) ".$type" else ""
        return Component.translatable("$prefix.$modid.$topic$suffix",
            Component.translatable("$prefix.$modid.$topic$suffix.value", *value)
                .withStyle(Style.EMPTY.withColor(color)))
            .withStyle(Style.EMPTY.withColor(TextColor.GRAY))
    }

    companion object {

        /**
         * A number formatter instance that will show no decimal places.
         * @since 0.6.0
         */
        val number0dp = DecimalFormat("###,##0")

        /**
         * A number formatter instance that will show up to one optional decimal place.
         * @since 0.6.0
         */
        val number1dp = DecimalFormat("###,##0.#")

        /**
         * A number formatter instance that will always show one decimal place.
         * @since 0.8.0
         */
        val number1rdp = DecimalFormat("###,##0.0")

        /**
         * A number formatter instance that will show up to two optional decimal places.
         * @since 0.6.0
         */
        val number2dp = DecimalFormat("###,##0.##")

        /**
         * A number formatter instance that will always show two decimal places.
         * @since 0.8.0
         */
        val number2rdp = DecimalFormat("###,##0.00")

        /**
         * A number formatter instance that will show up to three optional decimal places.
         * @since 0.7.0
         */
        val number3dp = DecimalFormat("###,##0.###")

        /**
         * A number formatter instance that will always show three decimal places.
         * @since 0.8.0
         */
        val number3rdp = DecimalFormat("###,##0.000")

        /**
         * Constructs a single [MutableComponent] by appending a collection of components, separating each with a line break.
         * The last component is added without a trailing line break.
         *
         * @param components A [List] of [Component] to be concatenated into line breaks.
         * @return A [MutableComponent] containing all the given components.
         * @since 0.6.0
         */
        fun withLineBreaks(components: List<Component>): MutableComponent {
            val ret = Component.empty()
            components.dropLast(1).forEach { ret.append(it).append("\n") }
            ret.append(components.last())
            return ret
        }

        /**
         * Converts a list of tooltip components into an [ItemLore] object using its constructor.
         *
         * @param tooltip A list of [Component] objects to create an [ItemLore] with.
         * @return An [ItemLore] item component with the given tooltip.
         * @since 0.6.0
         */
        fun asItemLore(tooltip: List<Component>) = ItemLore(tooltip, tooltip)

        /**
         * Injects additional tooltip lines into an item stack's existing tooltip and outputs the final tooltip.
         *
         * This is done by getting the normal tooltip for the [ItemStack] and appending the given tooltip, then appending the advanced tooltip (F3+H) which is calculated from the difference between the normal and advanced tooltip of the [ItemStack].
         *
         * @param stack The [ItemStack] for which the tooltip is being generated.
         * @param tooltips A list of additional [Component] tooltip lines to inject.
         * @param level The [Level] where the tooltip is being shown, used for context-based tooltip generation.
         * @param player The [Player] viewing the tooltip, can be used for player-specific information in tooltips.
         * @param advanced A flag indicating whether advanced tooltips (F3+H) should be included or not.
         * @return A [List] of [Component] containing the full tooltip, including default and injected lines.
         * @since 0.7.0
         */
        fun injectItemTooltip(stack: ItemStack, tooltips: List<Component>, level: Level, player: Player, advanced: Boolean): List<Component> {
            val advanced = stack.getTooltipLines(Item.TooltipContext.of(level), player, if (advanced) TooltipFlag.Default.ADVANCED else TooltipFlag.Default.NORMAL)
            val normal = stack.getTooltipLines(Item.TooltipContext.of(level), player, TooltipFlag.Default.NORMAL)

            return normal + tooltips + (advanced - normal)
        }

    }

}
