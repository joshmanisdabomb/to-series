---
flavor: "Now with more sulfur!"
---

# Introduction
{{"template": "introduction_version", "ordinal": "twelth", "extra": ", and the eighth major 0.x release"}}.

This release mainly adds support for custom recipes, loot tables, damage types and sources, as well as custom payloads for inventory menu syncing.

This version of the mod and all downstream {{"of": "to_base:tag / to_base:to_series"}} mods have been updated to Minecraft 26.2.

# In this Release

## Additions
- Added damage type library with support for caching damage sources.
- ToEnergyItemProvider to get a ToEnergyTransferContext from any item or component, allowing mods to provide energy from Item and DataComponent without using ToEnergyItemComponentData.
- A new wiki data enforcer to check the existence of articles for the current mod version.
- Environment Service can now check mod version of a given mod ID cross-platform.
- Added dedicated IdentifierHelper::get functions to get block and item identifiers, now backing the previous IdentifierHelper::identifier Kotlin extension functions.
- Added RegistryHelper::get dedicated functions and RegistryHelper::resourceKey extension functions.
- Reintroduced DirectionHelper, providing the perpendicular directions of a given direction.
- Client and server pre-tick events for entities, alongside a client-side pre-tick event for levels.
- Use item on block event for player interactions.
- Client mods can now declare an event handler library on Fabric and Neoforge, built during client initialization.
- ParticleBlockModelClientDataCollectionModule for generating block models that only display particles, or that behave like air.
- EmptyToEnergyTransferContext, representing a noop energy transfer context.
- Nano, micro and milli SI unit prefixes to the tooltip engine.
- Rounded decimal formats that retain trailing zeros to the tooltip engine.
- Added jukebox song data library to easily output jukebox songs in datagen.
- InventoryPlatformModule::createExtendedMenu and InventoryPlatformModule::openExtendedMenu for creating inventory menus and syncing data from server to client.
- OutputSlot, a Slot that rejects items being placed inside and CraftingOutputSlot that handles crafting advancements.
- ToEnergyItemContainerListener that tracks per-slot energy changes in a Menu and can inject this information into the item's tooltip in a Screen.
- Added general loot data collection events for arbitrary loot tables created in collection datagen.
- Added KotlinHelper::transpose for transposing lists of lists.

## Changes
- Wiki data enforcers now are based on reporting identifier pairs, rather than resource keys.
- BlockModelResolver is now accessed from BlockEntityRenderDispatcher mixin, rather than the Minecraft client instance.
- {{"of": "to_base:concept / to_base:to_energy"}} is now stored internally in milli-units (mTE), improving precision for small amounts of energy.
- Refactored TooltipEngine::createSIComponent to accept an explicit list of unit components and decimal format, with support for forcing a specific unit tier.
- Renamed InventoryPlatformModule::createMenuType to InventoryPlatformModule::createBasicMenu.
- Renamed classes with Energy prefix to ToEnergy when only about the To Energy system.
- Refactored all bar widgets to be based on AbstractBarWidget.
- ProcessBarWidget now refers to the arrow of a machine, such as a Furnace, rather than the burn icon.

## Fixes
- BlockNetworks previously would not store a connection entry with only one node in the network.