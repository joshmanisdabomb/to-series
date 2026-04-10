package net.jidb.to.base.mod

import net.jidb.to.base.library.*
import net.jidb.to.base.network.ServerPayloadContext
import net.jidb.to.base.platform.ReloadListenerPlatformModule
import net.minecraft.core.particles.ParticleType
import net.minecraft.resources.ResourceKey
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.PlacedFeature

interface ToContentMod {
    val blocks: BlockLibrary? get() = null
    val items: SimpleRegistryLibrary<Item>? get() = null
    val blockItems: BlockItemLibrary? get() = null
    val tabs: SimpleRegistryLibrary<CreativeModeTab>? get() = null
    val menus: SimpleRegistryLibrary<MenuType<*>>? get() = null
    val particles: SimpleRegistryLibrary<ParticleType<*>>? get() = null
    val payloadHandlers: PayloadHandlerLibrary<ServerPayloadContext>? get() = null
    val payloads: PayloadLibrary? get() = null
    val blockTags: TagLibrary<Block>? get() = null
    val itemTags: TagLibrary<Item>? get() = null
    val sounds: SoundEventLibrary? get() = null
    val biomeMods: BiomeModLibrary? get() = null
    val reloadListeners: ReloadListenerLibrary<ReloadListenerPlatformModule>? get() = null
    val configuredFeatures: Library<*, ResourceKey<ConfiguredFeature<*, *>>>? get() = null
    val placedFeatures: Library<*, ResourceKey<PlacedFeature>>? get() = null
}