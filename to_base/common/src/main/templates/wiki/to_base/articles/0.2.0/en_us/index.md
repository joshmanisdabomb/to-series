---
flavor: "The client is always right!"
---

# Introduction
{{"template": "introduction_version", "ordinal": "sixth", "extra": ", and the second major 0.x release"}}.

This release focuses on providing structure to the {{"of": "to_base:tag / to_base:to_series"}} of mods, with a skeletal structure that they can each implement.

# In this Release

## Additions
- Data generation for tags, loot tables, particle and sound definitions.
- Helper functions for getting LibraryEntry objects easier from a library by value.
- ToMod classes to remove boilerplate code for all To mods.
- Network layer for both Forge and Fabric's networking wrappers.
- RenderType field in ExtendedBlockProperties for rendering blocks with CUTOUT, TRANSLUCENT, etc.
- DeferredForgeEventRegistry for handling registering objects (menu screens, reload listeners, packets) that Forge needs to do in a dedicated bus event.

## Changes
- {{"of": "minecraft:block / to_base:research_desk"}} now drops itself in the middle of the block when broken.