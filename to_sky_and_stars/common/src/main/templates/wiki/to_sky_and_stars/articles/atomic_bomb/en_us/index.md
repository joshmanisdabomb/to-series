---
flavor: "You're going to use it as a deterrent, right?"
---

# Introduction
The {{"template": "introduction", "description": "an explosive device"}}. It can be filled with 1 {{"of": "minecraft:block / minecraft:tnt"}}, 1 {{"of": "minecraft:item / to_sky_and_stars:enriched_uranium_nugget"}} and between 1 and 45 {{"of": "minecraft:item / to_sky_and_stars:enriched_uranium"}} to create a {{"of": "to_base:concept / to_sky_and_stars:nuclear_explosion"}}.

This block is affected by gravity similarly to an {{"of": "minecraft:block / minecraft:anvil"}}, but deals no contact damage to entities.

# Usage
To use an {{"of": "self"}}, right click to open the interface. To enable the detonate button, the following items must be in the bomb's inventory:
- 1 {{"of": "minecraft:block / minecraft:tnt"}}
- 1 {{"of": "minecraft:item / to_sky_and_stars:enriched_uranium_nugget"}}
- Between 1 and 45 {{"of": "minecraft:item / to_sky_and_stars:enriched_uranium"}}, or between 1 and 5 {{"of": "minecraft:block / to_sky_and_stars:enriched_uranium_block", "plural": true}}

Once it is filled, click the red button to activate the bomb. Detonated {{"of": "self", "plural": true}} are entities that function similarly to primed {{"of": "minecraft:entity_type / minecraft:tnt"}}.

If the inventory contents are filled, the bomb can also be activated by a redstone signal.

## Explosion Strength

The explosion strength is determined by the amount of {{"of": "minecraft:item / to_sky_and_stars:enriched_uranium"}} added to the {{"of": "self"}}, with each additional unit giving diminishing returns.

The formula for explosion strength is: `20 + floor(sqrt((uranium - 1) / 44) * 120)`

See the {{"of": "to_base:concept / to_sky_and_stars:nuclear_explosion"}} page for what explosion strength means in block and entity damage.

## Fuse Time

The formula for fuse time (in ticks) is: `160 + floor((uranium - 1) / 44 ^ 1.15 * 124) * 10`

# Disarming

To disarm an active {{"of": "self"}}, simply right click it with {{"of": "minecraft:item / minecraft:shears"}}. The bomb and its contents will immediately drop as an item entity.

# Tips
It's challenging to escape a fully filled {{"of": "self"}} by foot, but you could escape the blast by quickly constructing a {{"of": "to_base:concept / minecraft:nether_portal"}}.

# Technical
The {{"of": "self"}} entity, including the falling and primed variants are chunk loaded, similarly to thrown {{"of": "minecraft:item / minecraft:ender_pearl", "plural": true}}.