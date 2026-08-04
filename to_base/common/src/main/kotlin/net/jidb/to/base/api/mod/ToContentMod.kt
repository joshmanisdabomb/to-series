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
import net.jidb.to.base.pub.library.BiomeModLibrary
import net.jidb.to.base.pub.library.BlockItemLibrary
import net.jidb.to.base.pub.library.BlockLibrary
import net.jidb.to.base.pub.library.DamageTypeLibrary
import net.jidb.to.base.pub.library.EventHandlerLibrary
import net.jidb.to.base.pub.library.GameTestLibrary
import net.jidb.to.base.pub.library.PayloadHandlerLibrary
import net.jidb.to.base.pub.library.PayloadLibrary
import net.jidb.to.base.pub.library.ReloadListenerLibrary
import net.jidb.to.base.pub.library.SoundEventLibrary
import net.jidb.to.base.pub.library.TagLibrary
import net.minecraft.advancements.triggers.CriterionTrigger
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.particles.ParticleType
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.TicketType
import net.minecraft.tags.TagKey
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

/**
 * Interface containing content libraries usable by an implementing [net.jidb.to.base.pub.mod.ToMod].
 * All the values in each [Library] here have first-class support to be registered cross-platform by To Lay the Foundations code.
 *
 * Each property is `null` by default, and mods can override these values with their own libraries to automatically register their content.
 *
 * @since 0.2.0
 */
interface ToContentMod {

    /**
     * A [Library] for the mod's [Block] objects.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered to the block [net.minecraft.core.Registry].
     *
     * [BlockLibrary] also provides [BlockLibrary.ExtendedBlockPropertiesList] to store [net.jidb.to.base.api.properties.ExtendedBlockProperties] for each [Block].
     *
     * @since 0.2.0
     */
    val blocks: BlockLibrary? get() = null

    /**
     * A [Library] for the mod's [Item] objects.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered to the item [net.minecraft.core.Registry].
     *
     * @since 0.2.0
     */
    val items: SimpleRegistryLibrary<Item>? get() = null

    /**
     * A [Library] for the mod's [net.minecraft.world.item.BlockItem] objects.
     * [BlockItemLibrary] generates block items for blocks by default, but can also specify a custom [net.minecraft.world.item.BlockItem] or even use none for each block.
     *
     * @since 0.2.0
     */
    val blockItems: BlockItemLibrary? get() = null

    /**
     * A [Library] for the mod's [CreativeModeTab] objects.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered to the creative tab [net.minecraft.core.Registry].
     *
     * @since 0.2.0
     */
    val tabs: SimpleRegistryLibrary<CreativeModeTab>? get() = null

    /**
     * A [Library] for the mod's [EntityType] objects.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered to the entity [net.minecraft.core.Registry].
     *
     * @since 0.4.0
     */
    val entities: SimpleRegistryLibrary<EntityType<*>>? get() = null

    /**
     * A [Library] for the mod's [BlockEntityType] objects.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered to the block entity [net.minecraft.core.Registry].
     *
     * @since 0.4.0
     */
    val blockEntities: SimpleRegistryLibrary<BlockEntityType<*>>? get() = null

    /**
     * A [Library] for the mod's [MenuType] objects.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered to the menu [net.minecraft.core.Registry].
     *
     * @since 0.2.0
     */
    val menus: SimpleRegistryLibrary<MenuType<*>>? get() = null

    /**
     * A [Library] for the mod's [ParticleType] objects.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered to the particle [net.minecraft.core.Registry].
     *
     * @since 0.2.0
     */
    val particles: SimpleRegistryLibrary<ParticleType<*>>? get() = null

    /**
     * A [Library] for the mod's server-side payload handlers.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered to each modloader.
     *
     * @since 0.2.0
     */
    val payloadHandlers: PayloadHandlerLibrary<ServerPayloadContext>? get() = null

    /**
     * A [Library] for the mod's network payloads (client and server handled).
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered to each modloader.
     *
     * @since 0.2.0
     */
    val payloads: PayloadLibrary? get() = null

    /**
     * A [Library] for the mod's [net.minecraft.core.Registry] objects.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered to the registry [net.minecraft.core.Registry].
     *
     * @since 0.5.0
     */
    val registries: RegistryRegistryLibrary? get() = null

    /**
     * A [Library] for the mod's block [TagKey] objects.
     *
     * @since 0.2.0
     */
    val blockTags: TagLibrary<Block>? get() = null

    /**
     * A [Library] for the mod's item [TagKey] objects.
     *
     * @since 0.2.0
     */
    val itemTags: TagLibrary<Item>? get() = null

    /**
     * A [Library] for the mod's item [DataComponentType] objects.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered to the item data component [net.minecraft.core.Registry].
     *
     * @since 0.6.0
     */
    val itemComponents: SimpleRegistryLibrary<DataComponentType<*>>? get() = null

    /**
     * A [Library] for the mod's [RecipeBookCategory] objects.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered to the recipe category [net.minecraft.core.Registry].
     *
     * @since 0.8.0
     */
    val recipeCategories: SimpleRegistryLibrary<RecipeBookCategory>? get() = null

    /**
     * A [Library] for the mod's [RecipeDisplay.Type] objects.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered to the recipe display [net.minecraft.core.Registry].
     *
     * @since 0.8.0
     */
    val recipeDisplays: SimpleRegistryLibrary<RecipeDisplay.Type<*>>? get() = null

    /**
     * A [Library] for the mod's [RecipeType] objects.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered to the recipe type [net.minecraft.core.Registry].
     *
     * @since 0.8.0
     */
    val recipeTypes: SimpleRegistryLibrary<RecipeType<*>>? get() = null

    /**
     * A [Library] for the mod's [RecipeSerializer] objects.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered to the recipe serializer [net.minecraft.core.Registry].
     *
     * @since 0.8.0
     */
    val recipeSerializers: SimpleRegistryLibrary<RecipeSerializer<*>>? get() = null

    /**
     * A [Library] for the mod's [net.minecraft.sounds.SoundEvent] objects.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered to the sound event [net.minecraft.core.Registry].
     *
     * @since 0.2.0
     */
    val sounds: SoundEventLibrary? get() = null

    /**
     * A [Library] for the mod's [net.jidb.to.base.api.level.biome.BiomeMod] objects.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered to each modloader to edit [net.minecraft.world.level.Level] generation.
     *
     * @since 0.3.0
     */
    val biomeMods: BiomeModLibrary? get() = null

    /**
     * A [Library] for the mod's [ReloadListenerLibrary] objects.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered to each modloader.
     *
     * @since 0.2.0
     */
    val reloadListeners: ReloadListenerLibrary<ReloadListenerPlatformModule>? get() = null

    /**
     * A [Library] for the mod's cross-platform [Event] objects.
     *
     * @since 0.5.0
     */
    val events: SimpleLibrary<Event<*, *>>? get() = null

    /**
     * A [Library] for the mod's cross-platform [net.jidb.to.base.api.event.EventHandler] objects.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered as a callback to each [Event].
     *
     * @since 0.5.0
     */
    val eventHandlers: EventHandlerLibrary? get() = null

    /**
     * A [Library] for the mod's [CriterionTrigger] objects.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered to the advancement criterion trigger [net.minecraft.core.Registry].
     *
     * @since 0.5.0
     */
    val advancementTriggers: SimpleRegistryLibrary<CriterionTrigger<*>>? get() = null

    /**
     * A [Library] for the mod's chunk [TicketType] objects.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered to the chunk ticket [net.minecraft.core.Registry].
     *
     * @since 0.4.0
     */
    val tickets: SimpleRegistryLibrary<TicketType>? get() = null

    /**
     * A [Library] for the mod's [SavedDataType] objects, used to save custom data with a [net.minecraft.world.level.Level] in global space.
     *
     * @since 0.5.0
     */
    val savedData: SimpleLibrary<SavedDataType<*>>? get() = null

    /**
     * A [Library] for the mod's [BlockNetworkType] objects.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object is automatically registered to the To Lay the Foundations block network [net.minecraft.core.Registry].
     *
     * @since 0.6.0
     */
    val blockNetworks: SimpleRegistryLibrary<BlockNetworkType>? get() = null

    /**
     * A [Library] for the mod's [TransferContextProvider] objects, used to get [net.jidb.to.base.api.transfer.TransferContext] from a side of a [Block] or [net.minecraft.world.level.block.entity.BlockEntity] in a [net.minecraft.world.level.Level], or from an [net.minecraft.world.item.ItemStack].
     *
     * @since 0.6.0
     */
    val transferProviders: SimpleLibrary<TransferContextProvider<*>>? get() = null

    /**
     * A [Library] for the mod's game tests, i.e. the checks it runs against a live level rather than against a bare JVM.
     *
     * Content here in a [net.jidb.to.base.pub.mod.ToMod] object has its test function automatically registered to the test function [net.minecraft.core.Registry].
     * The test instance and the structure that go with it are data pack content instead, and are written by naming the same library on [net.jidb.to.base.client.data.api.mod.ToContentDataMod.gameTests].
     *
     * @since 1.0.0
     */
    val gameTests: GameTestLibrary? get() = null

    /**
     * A [Library] of [ResourceKey] objects pointing to the mod's [ConfiguredFeature] objects in the datapack.
     *
     * @since 0.3.0
     */
    val configuredFeatures: Library<*, ResourceKey<ConfiguredFeature<*, *>>>? get() = null

    /**
     * A [Library] of [ResourceKey] objects pointing to the mod's [PlacedFeature] objects in the datapack.
     *
     * @since 0.3.0
     */
    val placedFeatures: Library<*, ResourceKey<PlacedFeature>>? get() = null

    /**
     * A [Library] of [ResourceKey] objects pointing to the mod's [net.minecraft.world.damagesource.DamageType] objects in the datapack.
     * [DamageTypeLibrary] also provides a helper function to get a [net.minecraft.world.damagesource.DamageSource] from these [net.minecraft.world.damagesource.DamageType] objects.
     *
     * @since 0.8.0
     */
    val damageTypes: DamageTypeLibrary? get() = null

    /**
     * A [Library] of [ResourceKey] objects pointing to the mod's [LootTable] objects in the datapack.
     *
     * @since 0.8.0
     */
    val lootTables: Library<*, ResourceKey<LootTable>>? get() = null

    /**
     * A [Library] of [ResourceKey] objects pointing to the mod's [JukeboxSong] objects in the datapack.
     *
     * @since 0.8.0
     */
    val music: Library<*, ResourceKey<JukeboxSong>>? get() = null

}
