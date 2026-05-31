package net.jidb.to.base.platform

import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack

abstract class CreativeTabsPlatformModule {

    abstract fun builder(display: ((params: CreativeModeTab.ItemDisplayParameters, output: DisplayItemsConsumer) -> Unit)? = null): CreativeModeTab.Builder

    fun interface DisplayItemsConsumer {
        operator fun invoke(stack: ItemStack, visibility: TabVisibility)
        operator fun invoke(stack: ItemStack) = invoke(stack, TabVisibility.ALWAYS)
    }

    enum class TabVisibility {
        ALWAYS,
        SEARCH_ONLY,
        PARENT_ONLY
    }

}