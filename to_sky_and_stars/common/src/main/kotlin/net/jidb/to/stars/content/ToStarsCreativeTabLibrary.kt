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
        Services.platform.creativeTabs.builder { parameters, output ->
            output(ItemStack(ToStarsMod.blocks.uranium_ore))
            output(ItemStack(ToStarsMod.blocks.deepslate_uranium_ore))
            output(ItemStack(ToStarsMod.items.uranium_nugget))
            output(ItemStack(ToStarsMod.items.uranium))
            output(ItemStack(ToStarsMod.blocks.uranium_block))
            output(ItemStack(ToStarsMod.items.enriched_uranium_nugget))
            output(ItemStack(ToStarsMod.items.enriched_uranium))
            output(ItemStack(ToStarsMod.blocks.enriched_uranium_block))
            output(ItemStack(ToStarsMod.items.heavy_uranium_nugget))
            output(ItemStack(ToStarsMod.items.heavy_uranium))
            output(ItemStack(ToStarsMod.blocks.heavy_uranium_block))
            output(ItemStack(ToStarsMod.blocks.heavy_uranium_shielding))
            output(ItemStack(ToStarsMod.blocks.atomic_bomb))
            output(ItemStack(ToStarsMod.blocks.nuclear_waste))
            output(ItemStack(ToStarsMod.items.test_item))
        }
            .title(Component.translatable("itemgroup.${modid}.${entry.name}"))
            .icon { ItemStack(ToStarsMod.items.test_item) }
            .build()
    }

}