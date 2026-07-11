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

## Changes
- Wiki data enforcers now are based on reporting identifier pairs, rather than resource keys.
- BlockModelResolver is now accessed from BlockEntityRenderDispatcher mixin, rather than the Minecraft client instance.

## Fixes
- BlockNetworks previously would not store a connection entry with only one node in the network.