package net.jidb.to.base.pub.item

import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import net.minecraft.world.item.component.TooltipProvider
import java.util.function.Consumer

/**
 * A registry deciding where in an item's tooltip each [TooltipProvider] component writes its lines.
 * Vanilla writes the components it knows about in a fixed order and ignores any others, so a modded component is registered here and drawn in by the mixin at whichever of the three points it asked for.
 *
 * @since 0.6.0
 */
object TooltipProviderRegistry {

    /**
     * The components written above everything else in the tooltip, before the item's own name-adjacent lines.
     *
     * @since 0.6.0
     */
    private val top = mutableSetOf<DataComponentType<out TooltipProvider>>()

    /**
     * The components written directly after a given vanilla component, indexed by the component they follow.
     *
     * @since 0.6.0
     */
    private val after = mutableMapOf<DataComponentType<*>, MutableSet<DataComponentType<out TooltipProvider>>>()

    /**
     * The components written below everything else in the tooltip.
     *
     * @since 0.6.0
     */
    private val bottom = mutableSetOf<DataComponentType<out TooltipProvider>>()

    /**
     * Registers a component to be written at the top of the tooltip.
     *
     * @param type The component type to register.
     * @since 0.6.0
     */
    fun registerTop(type: DataComponentType<out TooltipProvider>) {
        top.add(type)
    }

    /**
     * Registers a component to be written directly after another component.
     *
     * @param type The component type to register.
     * @param after The component type it is written after.
     * @since 0.6.0
     */
    fun registerAfter(type: DataComponentType<out TooltipProvider>, after: DataComponentType<*>) {
        this.after.computeIfAbsent(after) { mutableSetOf() }.add(type)
    }

    /**
     * Registers a component to be written at the bottom of the tooltip.
     *
     * @param type The component type to register.
     * @since 0.6.0
     */
    fun registerBottom(type: DataComponentType<out TooltipProvider>) {
        bottom.add(type)
    }

    /**
     * Registers a component at the usual place, i.e. after the item's lore, which is where vanilla's own extra lines end.
     *
     * @param type The component type to register.
     * @return [Unit]
     * @since 0.6.0
     */
    fun register(type: DataComponentType<out TooltipProvider>) = registerAfter(type, DataComponents.LORE)

    /**
     * Writes the lines of every component in the given set that the stack actually carries.
     *
     * @param list The components to write.
     * @param stack The item stack the tooltip is for.
     * @param context The tooltip context vanilla supplied.
     * @param display The tooltip display settings of the stack, which decide what is hidden.
     * @param builder The consumer each line is passed to.
     * @param tooltipFlag The tooltip flags vanilla supplied.
     * @since 0.6.0
     */
    private fun display(list: Set<DataComponentType<out TooltipProvider>>, stack: ItemStack, context: Item.TooltipContext, display: TooltipDisplay, builder: Consumer<Component>, tooltipFlag: TooltipFlag) {
        for (provider in list) {
            val value = stack.get(provider)
            if (value != null) {
                stack.addToTooltip(provider, context, display, builder, tooltipFlag)
            }
        }
    }

    /**
     * Writes the lines of every component registered at the top of the tooltip.
     *
     * @param stack The item stack the tooltip is for.
     * @param context The tooltip context vanilla supplied.
     * @param display The tooltip display settings of the stack, which decide what is hidden.
     * @param builder The consumer each line is passed to.
     * @param tooltipFlag The tooltip flags vanilla supplied.
     * @since 0.6.0
     */
    fun displayTop(stack: ItemStack, context: Item.TooltipContext, display: TooltipDisplay, builder: Consumer<Component>, tooltipFlag: TooltipFlag) {
        display(top, stack, context, display, builder, tooltipFlag)
    }

    /**
     * Writes the lines of every component registered to follow the given one.
     *
     * @param type The component that has just been written.
     * @param stack The item stack the tooltip is for.
     * @param context The tooltip context vanilla supplied.
     * @param display The tooltip display settings of the stack, which decide what is hidden.
     * @param builder The consumer each line is passed to.
     * @param tooltipFlag The tooltip flags vanilla supplied.
     * @since 0.6.0
     */
    fun displayAfter(type: DataComponentType<*>, stack: ItemStack, context: Item.TooltipContext, display: TooltipDisplay, builder: Consumer<Component>, tooltipFlag: TooltipFlag) {
        display(after[type] ?: return, stack, context, display, builder, tooltipFlag)
    }

    /**
     * Writes the lines of every component registered at the bottom of the tooltip.
     *
     * @param stack The item stack the tooltip is for.
     * @param context The tooltip context vanilla supplied.
     * @param display The tooltip display settings of the stack, which decide what is hidden.
     * @param builder The consumer each line is passed to.
     * @param tooltipFlag The tooltip flags vanilla supplied.
     * @since 0.6.0
     */
    fun displayBottom(stack: ItemStack, context: Item.TooltipContext, display: TooltipDisplay, builder: Consumer<Component>, tooltipFlag: TooltipFlag) {
        display(bottom, stack, context, display, builder, tooltipFlag)
    }

}
