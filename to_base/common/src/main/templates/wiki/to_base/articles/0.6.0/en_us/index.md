---
flavor: "We've got the energy, I'll tell you all about it!"
---

# Introduction
{{"template": "introduction_version", "ordinal": "tenth", "extra": ", and the sixth major 0.x release"}}.

This release adds features focused on allowing mods to transfer energy and items between blocks and items.

This version of the mod and all downstream {{"of": "to_base:tag / to_base:to_series"}} mods have been updated to Minecraft 26.1.2.

# In this Release

## Additions
- Added multi-platform transfer interface, including transaction handling, which is powered by the modloader's implementation.
- Added item transfer system.
- Added platform (Neoforge and Fabric via TechReborn API) energy transfer system.
- Added To energy transfer system.
- Added an energy item data component that items can use to store and transfer To energy.
- Added reusable widgets that screens can render to display information on To energy.
- Added registering of block entity and special item renderers.
- Added a tooltip provider registry that can register a tooltip renderer for a data component.
- Added a tooltip engine utility class for generating property-based tooltips.
- Added a schema class for container data that supports syncing larger integer and long values over the network with only shorts.
- Added a block network system where network changes are cached in saved level data.
- Added an abstract cable block that mods can use to create networks with specific nodes.
- Added a side class, similar to the platform class, that mods can use to query side-specific behavior safely on both sides of the network.

## Changes
- Most non-content classes were refactored into three subfolders - `api` (public interfaces), `hooks` (in-game code for TLtF's internal use), and `pub` (implementations usable by other mods).
- Sprites were moved into their own dedicated category folders.
- The wiki data enforcer now writes an error file instead of crashing data generation, read by CI to block merges instead of interrupting local development.
