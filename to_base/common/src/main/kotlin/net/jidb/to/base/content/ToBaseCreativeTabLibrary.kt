package net.jidb.to.base.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.library.SimpleRegistryLibrary
import net.jidb.to.base.service.Services
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack

object ToBaseCreativeTabLibrary : SimpleRegistryLibrary<CreativeModeTab>(ToBaseMod.MOD_ID) {

    override val registry = BuiltInRegistries.CREATIVE_MODE_TAB

    val tab by this(::i) { entry ->
        Services.environment.platform.creativeTabs.builder()
            .title(Component.translatable("itemgroup.${modid}.${entry.name}"))
            .icon { ItemStack(ToBaseMod.items.test_item) }
            .displayItems { parameters, output ->
                output.accept(ItemStack(ToBaseMod.blocks.test_block))
                output.accept(ItemStack(ToBaseMod.blocks.test_block_2))
                output.accept(ItemStack(ToBaseMod.items.test_item))
            }
            .build()
    }

}