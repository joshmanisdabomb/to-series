package net.jidb.to.base.neoforge.platform

import net.jidb.to.base.api.platform.CreativeTabsPlatformModule
import net.minecraft.world.item.CreativeModeTab

object CreativeTabsForgePlatformModule : CreativeTabsPlatformModule() {

    override fun builder(display: ((params: CreativeModeTab.ItemDisplayParameters, output: DisplayItemsConsumer) -> Unit)?): CreativeModeTab.Builder {
        val builder = CreativeModeTab.builder()
        if (display != null) {
            builder.displayItems { params, output ->
                display(params) { stack, visibility ->
                    output.accept(stack, when (visibility) {
                        TabVisibility.ALWAYS -> CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                        TabVisibility.SEARCH_ONLY -> CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY
                        TabVisibility.PARENT_ONLY -> CreativeModeTab.TabVisibility.PARENT_TAB_ONLY
                    })
                }
            }
        }
        return builder
    }

}
