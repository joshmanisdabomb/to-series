# To Series

A collection of Minecraft mods targeting recent versions on both **Fabric** and **Neoforge**.

- [Online Wiki](https://to.jidb.net)
- [Downloads](https://to.jidb.net/builds)

## Table of Contents

1. [To Lay the Foundations](#to-lay-the-foundations)
2. [To Sky and Stars](#to-sky-and-stars)
3. [Future To Mods](#future-to-mods)
4. [Legacy](#legacy)
5. [Development](#development)

---

## To Lay the Foundations

**Base mod for To series mods.**

A multi-loader library mod that serves as the foundation for all other mods in the To Series.

Its primary purpose is to provide shared hooks and infrastructure, a cross-platform event system, cross-platform registering to Minecraft registries, a data-driven wiki using Markdown/JSON templates, and various helper utilities for Kotlin.

### Content

Despite being a library mod, To Lay the Foundations adds the **Researcher's Desk**, which can be used to access the in-game wiki interface. This content mirrors the online wiki at [to.jidb.net](https://to.jidb.net).

---

## To Sky and Stars

A content mod aiming to bring the Space Race era to Minecraft. Currently in its early stages, it introduces nuclear-themed items and mechanics inspired by mid-20th century arms developments.

### Content

- Uranium Ore generation.
- Atomic Bombs made with Enriched Uranium.
- Custom energy transfer system with power cables and power banks.

### Plans

- Steel
- Launching Missiles (Conventional and Nuclear Payloads)
- Launching Satellites (Auto-updating Maps, Track Player Positions, Ion Cannons)
- Launching Rockets into Orbit (Space Stations)
- Launching Rockets onto other bodies
  - Curated Dimensions: Moon, Mars, etc.
  - Other curated solar systems.
  - Other planets in solar systems to be randomly generated, similar to 20w14∞#
- Launching Asteroid Miners to generate ores

## Future To Mods

### To Market To Market

A mod focusing on player and villager economies.

- Shop Blocks to trade with players or villagers, buy and sell backed by an order book.
- Currency Minting
- Automation of Villager Trading

### To Hoard It All

A mod focusing on physical or digitised storage.

- Cardboard Boxes (early-game Shulker Boxes)
- Sawmill (Stonecutter for Wood)
- Computers, Disk Drives, Data Cables

### To Dream of Distant Lands

A mod focusing on adding new worlds to explore. Adding biomes, dimensions, creatures and bosses.

- Rainbow Dimension
- Wasteland
- Doom Gauntlet

### To Look on the Past

Nostalgia mod that adds blocks, items and entities from older versions and other editions of the game.

- A Time Rift can be opened to access content from older versions, such as classic grass, wool, food, bow, etc.
- A Bedrock Rift can be opened to access content from other editions such as Bedrock, Education or Pocket Edition. Blocks like the Nether Reactor and the Cyan Rose.
- Maybe a rift for content from older versions of the To Series.

## Legacy

This mod has been in development since Minecraft 1.7.2. Each rewrite is an improvement on the last, and I hope to release this to the public one day...

| Mod Name                                       | Modloader and Version                        |
|------------------------------------------------|----------------------------------------------|
| [Yet Another Mod](resources/YAM.md)            | Forge 1.7.2                                  |
| [Aimless Agglomeration](resources/AA.md)       | Forge 1.10.2 - 1.12.2                        |
| [Loosely Connected Concepts](resources/LCC.md) | Forge 1.13.2 – 1.15.2                        |
| [Loosely Connected Concepts](resources/LCC.md) | Fabric 1.17 – 1.19.2                         |
| To Series                                      | NeoForge & Fabric 1.21.10+ **(we are here)** |

The plan is to port a lot of the content from Loosely Connected Concepts, Aimless Agglomeration and Yet Another Mod into the To Series.

---

## Development

The To Series is coded in Kotlin, targeting the latest stable version of Minecraft.

The build script is a tweaked version of https://github.com/jaredlll08/MultiLoader-Template for supporting both Fabric and Neoforge.

### Building

```bash
# Build all mods for Neoforge and Fabric.
./gradlew build

# Build a single mod for a specific platform.
./gradlew :to_base:neoforge:build
./gradlew :to_stars:fabric:build
```

Each mod namespace (`to_base`, `to_stars`) has its own `common`, `fabric`, and `neoforge` subdirectories.

### AI Policy

Just to be upfront as I know it's a touchy subject - sometimes AI is used to write code for heavy stuff I don't want to do, such as:

- The code for calculating block networks.
- The code for calculating damage to blocks in a nuclear explosion.
- Creating first drafts for articles, which I then edit and improve manually.

I make use of my locally running AI model where I can, but sometimes I use Codex or Claude.

Creating this mod is how I spend my free time, so rest assured, most of it is written by hand.