package net.jidb.to.stars.content

import net.jidb.to.base.library.SimpleRegistryLibrary
import net.jidb.to.base.service.Services
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack

object ToStarsCreativeTabLibrary : SimpleRegistryLibrary<CreativeModeTab>(ToStarsMod.MOD_ID) {

    override val registry = BuiltInRegistries.CREATIVE_MODE_TAB

    val tab by this(::i) { entry ->
        Services.platform.creativeTabs.builder()
            .title(Component.translatable("itemgroup.${modid}.${entry.name}"))
            .icon { ItemStack(ToStarsMod.items.test_item) }
            .displayItems { parameters, output ->
                output.accept(ItemStack(ToStarsMod.blocks.nuclear_waste))
                output.accept(ItemStack(ToStarsMod.items.test_item))
            }
            .build()
    }

}