---
flavor: "This release certainly was an event."
---

# Introduction
{{"template": "introduction_version", "ordinal": "ninth", "extra": ", and the fifth major 0.x release"}}.

This release adds a cross-platform event system, and makes code changes to separate "content" from this mod from hooks usable by other mods.

# In this Release

## Additions
- Added a cross-platform event system, with a new AdvancementGrantPostEvent that can be listened to.
- Added events and payload libraries to the ToMod class.
- Added custom registry registration.
- Added a way for other To mods to wait for this (or another) mod to be initialized before running code.

## Changes
- Features in the mod considered "content", such as the {{"of": "minecraft:block / to_base:research_desk"}}, were moved into a content sub-ToMod class.

## Fixes
- Mixins can now be loaded only on the Fabric side.