---
flavor: "Absolute flames from start to finish."
---

# Introduction

The {{"template": "introduction", "description": "a heat generating block"}}. It burns solid fuel to produce {{"of": "to_base:concept / to_stars:heat"}}, which can be transferred to {{"of": "minecraft:block / to_stars:boiler", "plural": true}} directly, or with {{"of": "minecraft:block / to_stars:heat_pipe", "plural": true}}.

Solid fuels include all fuels usable in a {{"of": "minecraft:block / minecraft:furnace"}}, excluding fluid buckets such as a {{"of": "minecraft:item / minecraft:lava_bucket"}}.

# Fuel

You can insert any valid fuel into the fuel slot by right-clicking the block to open the GUI. Once inserted, the {{"of": "self"}} begins providing {{"of": "to_base:concept / to_stars:heat"}} at an initial 100°C.

Each item of fuel consumed gradually increases the temperature, lasting as long as the fuel's burn duration. For a {{"of": "self"}}, any fuel adds +0.015°C per tick but lasts 0.75x as long as it would in a {{"of": "minecraft:block / minecraft:furnace"}}.

For more detail, you can view a tooltip for a valid fuel in the GUI by hovering over the item.

{{"of": "minecraft:item / minecraft:coal"}}, {{"of": "minecraft:item / minecraft:charcoal"}} and {{"of": "minecraft:block / minecraft:coal_block", "plural": true}} receive a 2x multiplier to its temperature, adding +0.03°C per tick.

Once the temperature has climbed to 400°C, a bonus 100°C is added, resulting in a maximum temperature of 500°C.

# Cooling

When {{"of": "to_base:concept / to_stars:heat"}} is stored in a {{"of": "self"}} but no more fuel is remaining, the temperature cools down at a rate of 0.6% per tick, until it reaches 100°C. After this, the temperature is instantly reset to 0°C.

# Upgrading

{{"of": "self", "plural": true}} can be directly upgraded into a {{"of": "minecraft:block / to_stars:steel_solid_generator"}}, a {{"of": "to_base:tag / to_stars:machine/tier2", "text": "tier 2 machine"}} with an increased fuel rate, longer fuel duration and higher maximum temperature. The crafting recipe for this involves {{"of": "minecraft:item / to_stars:steel_ingot", "plural": true}}, requiring other machines of at least {{"of": "to_base:tag / to_stars:machine/tier1", "text": "tier 1"}}.
