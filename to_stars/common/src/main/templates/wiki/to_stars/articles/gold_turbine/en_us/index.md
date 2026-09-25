---
flavor: "Full steam ahead."
---

# Introduction

The {{"template": "introduction", "description": "an energy-generating block"}}. Crafted using a {{"of": "to_base:tag / to_stars:machine/enclosure", "text": "gold tier enclosure"}}, it converts the kinetic energy of {{"of": "minecraft:block / to_stars:rotor_blades"}} into {{"of": "to_base:concept / to_base:to_energy"}} at a higher rate than a {{"of": "minecraft:block / to_stars:copper_turbine"}}.

The blades cannot be powered with {{"of": "to_base:concept / minecraft:redstone"}}, only the steam generated from the {{"of": "to_base:concept / to_stars:heat"}} of an active {{"of": "minecraft:block / to_stars:boiler"}} can be converted into {{"of": "to_base:concept / to_base:to_energy"}}.

# Stats

Information about a {{"of": "self"}} can be viewed by hovering over the item in an inventory.

The {{"of": "self"}} will convert 1°C of {{"of": "to_base:concept / to_stars:heat"}} into 0.025 {{"of": "to_base:concept / to_base:to_energy", "text": "TE"}} every tick, meaning:

- For the minimum 100°C of an active {{"of": "minecraft:block / to_stars:boiler"}}, this {{"of": "self", "text": "turbine"}} generates 2.5 {{"of": "to_base:concept / to_base:to_energy", "text": "TE"}} every tick.
- For the maximum 200°C of an active {{"of": "minecraft:block / to_stars:boiler"}}, 5 {{"of": "to_base:concept / to_base:to_energy", "text": "TE"}} is generated every tick.

{{"of": "minecraft:block / to_stars:boiler", "plural": true}} that are supplied more than 200°C are overboiling and are capped at 5 {{"of": "to_base:concept / to_base:to_energy", "text": "TE"}}/t.

Using the turbine on a {{"of": "minecraft:block / to_stars:boiler"}} that is only partially filled with {{"of": "minecraft:fluid / minecraft:water"}} will generate proportionally less {{"of": "to_base:concept / to_base:to_energy"}}.

# Usage

{{"of": "minecraft:block / to_stars:rotor_blades"}} must be mounted to the front face of a {{"of": "self"}} to catch rising steam. The {{"of": "minecraft:block / to_stars:rotor_blades"}} need an active {{"of": "minecraft:block / to_stars:boiler"}} directly beneath it within 3 blocks unobstructed.

Once the {{"of": "minecraft:block / to_stars:rotor_blades"}} are powered by steam, the {{"of": "self"}} lights up and starts producing energy, which is output from the opposite face. {{"of": "minecraft:block / to_stars:power_cable", "plural": true}} can be attached to this output face, or a direct connection to an energy receiving {{"of": "to_base:tag / to_stars:machine"}} can be made.

Since a single {{"of": "minecraft:block / to_stars:boiler", "plural": true}} can only supply between 100°C and 200°C of {{"of": "to_base:concept / to_stars:heat"}} before the excess is lost, multiple {{"of": "self", "plural": true}} are needed to extract the maximum {{"of": "to_base:concept / to_stars:heat"}} from a {{"of": "to_base:tag / to_stars:machine/generator"}}.

# Upgrading

{{"of": "self", "plural": true}} can be directly upgraded into a {{"of": "minecraft:block / to_stars:steel_solid_generator"}}, a {{"of": "to_base:tag / to_stars:machine/tier2", "text": "tier 2 machine"}} with a a higher conversion rate from {{"of": "to_base:concept / to_stars:heat"}} to {{"of": "to_base:concept / to_base:to_energy"}}. The crafting recipe for this involves {{"of": "minecraft:item / to_stars:steel_ingot", "plural": true}}, requiring other machines of at least {{"of": "to_base:tag / to_stars:machine/tier1", "text": "tier 1"}}.
