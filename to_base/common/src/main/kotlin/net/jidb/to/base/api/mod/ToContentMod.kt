package net.jidb.to.base.api.mod

import net.jidb.to.base.api.block.network.BlockNetworkType
import net.jidb.to.base.api.event.Event
import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.RegistryRegistryLibrary
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.base.api.network.ServerPayloadContext
import net.jidb.to.base.api.platform.ReloadListenerPlatformModule
import net.jidb.to.base.api.transfer.TransferContextProvider
import net.jidb.to.base.pub.library.*
import net.minecraft.advancements.triggers.CriterionTrigger
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.particles.ParticleType
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.TicketType
import net.minecraft.world.entity.EntityType
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.JukeboxSong
import net.minecraft.world.item.crafting.RecipeBookCategory
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.display.RecipeDisplay
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.saveddata.SavedDataType
import net.minecraft.world.level.storage.loot.LootTable

interface ToContentMod {
    val blocks: BlockLibrary? get() = null
    val items: SimpleRegistryLibrary<Item>? get() = null
    val blockItems: BlockItemLibrary? get() = null
    val tabs: SimpleRegistryLibrary<CreativeModeTab>? get() = null
    val entities: SimpleRegistryLibrary<EntityType<*>>? get() = null
    val blockEntities: SimpleRegistryLibrary<BlockEntityType<*>>? get() = null
    val menus: SimpleRegistryLibrary<MenuType<*>>? get() = null
    val particles: SimpleRegistryLibrary<ParticleType<*>>? get() = null
    val payloadHandlers: PayloadHandlerLibrary<ServerPayloadContext>? get() = null
    val payloads: PayloadLibrary? get() = null
    val registries: RegistryRegistryLibrary? get() = null
    val blockTags: TagLibrary<Block>? get() = null
    val itemTags: TagLibrary<Item>? get() = null
    val itemComponents: SimpleRegistryLibrary<DataComponentType<*>>? get() = null
    val recipeCategories: SimpleRegistryLibrary<RecipeBookCategory>? get() = null
    val recipeDisplays: SimpleRegistryLibrary<RecipeDisplay.Type<*>>? get() = null
    val recipeTypes: SimpleRegistryLibrary<RecipeType<*>>? get() = null
    val recipeSerializers: SimpleRegistryLibrary<RecipeSerializer<*>>? get() = null
    val sounds: SoundEventLibrary? get() = null
    val biomeMods: BiomeModLibrary? get() = null
    val reloadListeners: ReloadListenerLibrary<ReloadListenerPlatformModule>? get() = null
    val events: SimpleLibrary<Event<*, *>>? get() = null
    val eventHandlers: EventHandlerLibrary? get() = null
    val advancementTriggers: SimpleRegistryLibrary<CriterionTrigger<*>>? get() = null
    val tickets: SimpleRegistryLibrary<TicketType>? get() = null
    val savedData: SimpleLibrary<SavedDataType<*>>? get() = null
    val blockNetworks: SimpleRegistryLibrary<BlockNetworkType>? get() = null
    val transferProviders: SimpleLibrary<TransferContextProvider<*>>? get() = null
    val configuredFeatures: Library<*, ResourceKey<ConfiguredFeature<*, *>>>? get() = null
    val placedFeatures: Library<*, ResourceKey<PlacedFeature>>? get() = null
    val damageTypes: DamageTypeLibrary? get() = null
    val lootTables: Library<*, ResourceKey<LootTable>>? get() = null
    val music: Library<*, ResourceKey<JukeboxSong>>? get() = null
}