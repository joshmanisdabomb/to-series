package net.jidb.to.base.fabric.platform

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab
import net.jidb.to.base.platform.CreativeTabsPlatformModule
import net.minecraft.world.item.CreativeModeTab

object CreativeTabsFabricPlatformModule : CreativeTabsPlatformModule() {

    override fun builder(display: ((params: CreativeModeTab.ItemDisplayParameters, output: DisplayItemsConsumer) -> Unit)?): CreativeModeTab.Builder {
        val builder = FabricCreativeModeTab.builder()
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
