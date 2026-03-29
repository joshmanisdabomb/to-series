---
flavor: "BIG BADA BOOM"
---

# Introduction

The {{"template": "introduction"}}. It can be triggered by setting off an {{"of": "minecraft:block / to_sky_and_stars:atomic_bomb"}}.

# Interaction

## Blocks

A sphere is determined at the center of the explosion with the radius depending on the strength of the explosion. All blocks within the sphere will be destroyed if their blast resistance is not high enough.

Blocks that are destroyed will mostly be replaced with {{"of": "minecraft:block / minecraft:air"}}, with blocks further out from the center being replaced with {{"of": "minecraft:block / to_sky_and_stars:nuclear_waste"}} and {{"of": "minecraft:block / to_sky_and_stars:nuclear_fire"}} instead.

Blocks that have too high a blast resistance, such as {{"of": "minecraft:block / minecraft:obsidian"}} and {{"of": "minecraft:block / minecraft:bedrock"}}, will not be destroyed and will remain intact. Blocks behind unbreakable blocks will also be protected, but may be vulnerable to {{"of": "minecraft:block / to_sky_and_stars:nuclear_fire"}} damage after the initial blast.

## Entities

Entities that are caught directly in the blast will receive high, often fatal, damage (typically ~50-200 hearts of damage). A line of sight check, similar to the one used for the vanilla {{"of": "to_base:concept / minecraft:explosion"}}, is performed to determine if the entity is damaged by the blast.

Entities that are fully behind unbreakable blocks are still damaged, but with a more forgiving range between half a heart to 2 hearts total.

Entities are also given knockback proportional to the amount of damage they received.

# History

{{"of": "self", "plural": true}} have been present in:
- {{"of": "to_base:mod / to_base:yam"}}, triggered by detonating a {{"of": "minecraft:block / yam:nuke"}}.
- {{"of": "to_base:mod / to_base:aimagg"}}, triggered by launching a Nuclear Missile.
- {{"of": "to_base:mod / to_base:lcc_forge"}} and {{"of": "to_base:mod / to_base:lcc"}}, triggered by detonating a {{"of": "minecraft:block / to_sky_and_stars:atomic_bomb"}}.

In {{"of": "to_base:mod / to_base:lcc"}}, the blast would additionally inflict nearby entities with {{"of": "minecraft:mob_effect / to_sky_and_stars:radiation", "text": "radiation poisoning"}}, with the effect getting stronger and longer the closer the entity was to the center.
