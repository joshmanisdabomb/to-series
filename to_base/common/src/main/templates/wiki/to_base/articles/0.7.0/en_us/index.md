---
flavor: "We'll look back on this update with rose-tinted goggles."
---

# Introduction
{{"template": "introduction_version", "ordinal": "eleventh", "extra": ", and the seventh major 0.x release"}}.

This release adds a cross-platform system for tinting blocks and items.

# In this Release

## Additions
- Item and block tinting API and libraries.
- Generic progress bar for container GUIs, such as furnace flames.
- Classes for abstracting handling the transfer and storage of energy for block entities.
- Added Item interface extension for displaying stored energy within items.
- Helper function to inject arbitrary tooltips into items just before the component count.

## Changes
- Updated ContainerDataSchema to work with any type of data over the network, including decimals backed by shorts.
- Moved packages for extended block properties and moved `screens` into `gui` package.

## Removals
- Fully removed render layer from extended block properties, Minecraft now infers this from textures.