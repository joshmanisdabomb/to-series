---
flavor: "Now with more sulfur!"
---

# Introduction
{{"template": "introduction_version", "ordinal": "twelth", "extra": ", and the eighth major 0.x release"}}.

This release mainly adds support for custom damage types and sources.

This version of the mod and all downstream {{"of": "to_base:tag / to_base:to_series"}} mods has been updated to Minecraft 26.2.

# In this Release

## Additions
- Added damage type library with support for caching damage sources.
- A new wiki data enforcer to check the existence of articles for the current mod version.
- Environment Service can now check mod version of a given mod ID cross-platform.
- Added dedicated IdentifierHelper::get functions to get block and item identifiers, now backing the previous IdentifierHelper::identifier Kotlin extension functions.
- Added RegistryHelper::get dedicated functions and RegistryHelper::resourceKey extension functions.
- Reintroduced DirectionHelper, providing the perpendicular directions of a given direction.
- Client and server pre-tick events for entities, alongside a client-side pre-tick event for levels.
- Use item on block event for player interactions.
- Client mods can now declare an event handler library on Fabric and Neoforge, built during client initialisation.
- ParticleBlockModelClientDataCollectionModule for generating block models that only display particles, or that behave like air.
- EmptyToEnergyTransferContext, representing a noop energy transfer context.
- Nano, micro and milli SI unit prefixes to the tooltip engine.
- Rounded decimal formats that retain trailing zeros to the tooltip engine.

## Changes
- Wiki data enforcers now are based on reporting identifier pairs, rather than resource keys.
- BlockModelResolver is now accessed from BlockEntityRenderDispatcher mixin, rather than the Minecraft client instance.
- {{"of": "to_base:concept / to_base:to_energy"}} is now stored internally in milli-units (mTE), improving precision for small amounts of energy.
- Refactored TooltipEngine::createSIComponent to accept an explicit list of unit components and decimal format, with support for forcing a specific unit tier.

## Fixes
- BlockNetworks previously would not store a connection entry with only one node in the network.