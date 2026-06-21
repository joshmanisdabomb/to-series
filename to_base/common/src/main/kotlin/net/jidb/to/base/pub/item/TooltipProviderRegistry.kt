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

object TooltipProviderRegistry {
    private val top = mutableSetOf<DataComponentType<out TooltipProvider>>()
    private val after = mutableMapOf<DataComponentType<*>, MutableSet<DataComponentType<out TooltipProvider>>>()
    private val bottom = mutableSetOf<DataComponentType<out TooltipProvider>>()

    fun registerTop(type: DataComponentType<out TooltipProvider>) {
        top.add(type)
    }

    fun registerAfter(type: DataComponentType<out TooltipProvider>, after: DataComponentType<*>) {
        this.after.computeIfAbsent(after) { mutableSetOf() }.add(type)
    }

    fun registerBottom(type: DataComponentType<out TooltipProvider>) {
        bottom.add(type)
    }

    fun register(type: DataComponentType<out TooltipProvider>) = registerAfter(type, DataComponents.LORE)

    private fun display(list: Set<DataComponentType<out TooltipProvider>>, stack: ItemStack, context: Item.TooltipContext, display: TooltipDisplay, builder: Consumer<Component>, tooltipFlag: TooltipFlag) {
        for (provider in list) {
            val value = stack.get(provider)
            if (value != null) {
                stack.addToTooltip(provider, context, display, builder, tooltipFlag)
            }
        }
    }

    fun displayTop(stack: ItemStack, context: Item.TooltipContext, display: TooltipDisplay, builder: Consumer<Component>, tooltipFlag: TooltipFlag) {
        display(top, stack, context, display, builder, tooltipFlag)
    }

    fun displayAfter(type: DataComponentType<*>, stack: ItemStack, context: Item.TooltipContext, display: TooltipDisplay, builder: Consumer<Component>, tooltipFlag: TooltipFlag) {
        display(after[type] ?: return, stack, context, display, builder, tooltipFlag)
    }

    fun displayBottom(stack: ItemStack, context: Item.TooltipContext, display: TooltipDisplay, builder: Consumer<Component>, tooltipFlag: TooltipFlag) {
        display(bottom, stack, context, display, builder, tooltipFlag)
    }
}