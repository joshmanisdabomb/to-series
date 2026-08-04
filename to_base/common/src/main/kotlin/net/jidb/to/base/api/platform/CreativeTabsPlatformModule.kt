package net.jidb.to.base.api.platform

import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack

/**
 * A [Platform]-specific module that defines a cross-platform contract for building and changing settings on [CreativeModeTab].
 * This module has code for creating a [CreativeModeTab.Builder] with a custom [DisplayItemsConsumer] to set tab visibility.
 *
 * @since 0.0.3
 */
abstract class CreativeTabsPlatformModule {

    /**
     * Creates a new instance of a [CreativeModeTab.Builder]. A display callback can be optionally provided here to add [ItemStack]s to the tab with custom tab visibility.
     *
     * @param display An optional lambda function that allows customization of the creative mode tab's item display.
     * @return A [CreativeModeTab.Builder] instance that can be used to further configure and build the creative tab.
     *
     * @since 0.0.3
     */
    abstract fun builder(display: ((params: CreativeModeTab.ItemDisplayParameters, output: DisplayItemsConsumer) -> Unit)? = null): CreativeModeTab.Builder

    /**
     * Functional interface representing a consumer that adds [ItemStack]s to a [CreativeModeTab], with optional tab visibility settings.
     * Accessible version of [CreativeModeTab.DisplayItemsGenerator] with [CreativeModeTab.TabVisibility] parameter.
     *
     * @since 0.6.0
     */
    fun interface DisplayItemsConsumer {

        /**
         * Adds an [ItemStack] to a creative mode tab with the specified [TabVisibility] settings.
         *
         * @param stack The [ItemStack] to be added to the creative mode tab.
         * @param visibility The [TabVisibility] setting that controls the visibility of the item in the tab.
         * @since 0.6.0
         */
        operator fun invoke(stack: ItemStack, visibility: TabVisibility)

        /**
         * Adds an [ItemStack] to a creative mode tab with a default tab visibility of [TabVisibility.ALWAYS].
         *
         * @param stack The [ItemStack] to be added to the creative mode tab.
         * @return [Unit]
         * @since 0.6.0
         */
        operator fun invoke(stack: ItemStack) = invoke(stack, TabVisibility.ALWAYS)

    }

    /**
     * Represents the visibility states for an [ItemStack] within a [CreativeModeTab].
     *
     * @since 0.6.0
     */
    enum class TabVisibility {

        /**
         * Specifies that an [ItemStack] is always visible in the tab.
         *
         * @since 0.6.0
         */
        ALWAYS,

        /**
         * Specifies that an [ItemStack] is only visible when the user is in the global Search tab.
         *
         * @since 0.6.0
         */
        SEARCH_ONLY,

        /**
         * Specifies that an [ItemStack] is only visible when the user is in its tab, not in the global Search tab.
         *
         * @since 0.6.0
         */
        PARENT_ONLY

    }

}
