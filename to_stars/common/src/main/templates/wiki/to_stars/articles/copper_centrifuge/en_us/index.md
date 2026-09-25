---
flavor: "Spin to win!"
---

# Introduction

The {{"template": "introduction", "description": "a processing block"}}. It consumes {{"of": "to_base:concept / to_base:to_energy"}} to refine materials by spinning them apart into their component outputs.

As a {{"of": "to_base:tag / to_stars:machine/processor", "text": "processing"}} machine, the {{"of": "self"}} must be supplied with {{"of": "to_base:concept / to_base:to_energy"}} to begin processing.

# Usage

To use the {{"of": "self"}}, right-click to open the user interface. Add your input material in the middle slot.

You can supply {{"of": "to_base:concept / to_base:to_energy"}} to the centrifuge by attaching {{"of": "minecraft:block / to_stars:power_cable", "plural": true}} or energy providing {{"of": "to_base:tag / to_stars:machine", "plural": true}}. You can also extract energy from a {{"of": "to_base:tag / to_stars:machine/battery"}} placed in the top-left slot.

Once enough {{"of": "to_base:concept / to_base:to_energy"}} is supplied, the {{"of": "self"}} will start processing and spin up, operating similarly to a {{"of": "minecraft:block / minecraft:furnace"}}.

Once the recipe is finished processing, the outputs will be generated in the bottom slots. Recipes with two outputs have a dedicated slot for each item type; if the item's slot is full the centrifuge will wait until the slot is free, even if the other slot is empty.

Recipe progress is paused if the centrifuge no longer has enough energy to continue.

## Uranium Refinement

Currently, the primary use of a {{"of": "self"}} is to refine {{"of": "minecraft:item / to_stars:uranium"}}. Each operation consumes a single {{"of": "minecraft:item / to_stars:uranium"}} and yields either 2 to 5 {{"of": "minecraft:item / to_stars:enriched_uranium_nugget", "plural": true}} or 2 to 5 {{"of": "minecraft:item / to_stars:heavy_uranium_nugget", "plural": true}}, with an equal chance of each.

# Stats

Information about a {{"of": "self"}} can be viewed by hovering over the item in an inventory.

All tier 1 {{"of": "to_base:tag / to_stars:machine/processor", "text": "processing"}} machines, including the {{"of": "self"}}, have a base machine speed multiplier of 0.5x. Each recipe also has a base energy cost multiplier of 1.25x.

A {{"of": "self"}} holds an internal buffer of up to 4,000 {{"of": "to_base:concept / to_base:to_energy", "text": "TE"}}.

The maximum amount of {{"of": "to_base:concept / to_base:to_energy"}} that can be inserted in a single game tick is 256 {{"of": "to_base:concept / to_base:to_energy", "text": "TE"}}.

## Interface Tooltips

To see information on the specific recipe being processed, hover over the progress bar in the GUI.

To see information on how much {{"of": "to_base:concept / to_base:to_energy"}} is currently in the centrifuge, hover over the energy bar in the GUI.

To see information on the current "efficient" recipe and the speed bonus provided (see below), hover over the efficiency bar in the GUI.

## Recipe Efficiency Bonus

Running the same recipe repeatedly builds up a speed bonus that shortens the time required to process that recipe.

The efficiency bonus grows with every consecutive operation, up to a maximum of 32, at which point a {{"of": "self"}} processes at 1.5x its regular speed. This also reduces the total amount of {{"of": "to_base:concept / to_base:to_energy"}} required to process a recipe.

Switching to a different recipe resets the bonus once that recipe has finished processing.

# Preserving Energy

When this block is broken, its buffered energy is lost. To preserve it, mine the block with a {{"of": "minecraft:item / minecraft:pickaxe"}} enchanted with {{"of": "minecraft:enchantment / minecraft:silk_touch"}}.

# Upgrading

{{"of": "self", "plural": true}} can be directly upgraded into a {{"of": "minecraft:block / to_stars:steel_centrifuge"}}, a {{"of": "to_base:tag / to_stars:machine/tier2", "text": "tier 2 machine"}} that processes faster and consumes less {{"of": "to_base:concept / to_base:to_energy"}}. The crafting recipe for this involves {{"of": "minecraft:item / to_stars:steel_ingot", "plural": true}}, requiring other machines of at least {{"of": "to_base:tag / to_stars:machine/tier1", "text": "tier 1"}}.

{{"of": "self", "plural": true}} can also be upgraded into a {{"of": "minecraft:block / to_stars:gold_centrifuge"}}, a {{"of": "to_base:tag / to_stars:machine/tier1.5", "text": "tier 1.5 machine"}} that processes faster and consumes less {{"of": "to_base:concept / to_base:to_energy"}}. This is not required to upgrade to {{"of": "to_base:tag / to_stars:machine/tier2", "text": "tier 2"}}.
