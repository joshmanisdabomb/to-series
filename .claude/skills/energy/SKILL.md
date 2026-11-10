---
name: energy
description: Computing how much To Energy (TE) a solid-generator + boiler + rotor + turbine setup produces from a given fuel in to_stars. Use when asked how much TE a certain tier's heat generator yields from an item and quantity of fuel (e.g. "1 coal in a copper generator with 1 copper turbine", "one gold generator with 3 gold turbines and a block of coal"), including the starting rate, the ramp-up, and the total generated.
---

# To Energy yield calculations (to_stars)

Answer "how much TE does N fuel items give in a <tier> generator with <M> turbines, from rest" questions: the rate at the start (from rest), the rate it ramps up to, how long the ramp takes, and the total TE generated.

## Do this every time the skill is called

**Re-read the tier table before computing.** The tier constants below are a snapshot; the source of truth is:

`to_stars/common/src/main/kotlin/net/jidb/to/stars/info/MachineTier.kt`

Read it, and if any of the values differ from the table below (or a new tier exists), use the file's values and note the discrepancy. Do not trust the table over the file.

## How the energy chain works (what the numbers come from)

The chain: **solid generator (burns fuel) → heat pipe network → boiler (cauldron of water) → rotor blades → turbine → TE into the machine/battery behind the turbine.**

1. **Generator** (`HeatGeneratorBlockEntity.tick`, `SolidGeneratorBlockEntity`):
   - Each fuel item ignites into `heatDuration = ceil(vanillaBurnTicks × machineSpeed)` ticks.
   - While burning, each tick: `heat = max(heat, generatorInitial) + fuelValue × generatorHeat`, then if `heat >= generatorInitial + generatorRange` it snaps to `generatorInitial + generatorRange + generatorBonus` (the cap).
   - When not burning, each tick: `heat = min(heat, initial+range) × generatorCooling`, and if `heat < generatorInitial` it snaps to `0`. So a cold generator is exactly at `heat = 0`, but the **first** burning tick sets it to `max(0, initial) + add = initial + add` — i.e. from rest it always starts at the "rest heat" = `generatorInitial`.
   - Multiple fuel items in the slot burn **contiguously** (the next ignites on the same tick the previous ends; no cooling in between).
2. **Network** (`HeatBlockNetworkType.tick`): total heat of all generators is divided among the **most-active** boilers: `active = clamp(int(totalHeat / 100), 0, #boilers)`, `share = totalHeat / active`. Boilers beyond `active` get nothing. A single generator (heat ≤ ~1000) drives only 1–2 boilers.
3. **Boiler** (`BoilingCauldronBlockEntity`): stores the per-side heat; its effective heat is the sum of the sides the pipes set. The `UNSTABLE` state is purely cosmetic (sum > 200).
4. **Turbine** (`RotorBlockEntity.tickTurbine`): reads the boiler below (up to 3 blocks), clamps heat to **200**, and:
   `raw = clamp(heat,0,200)/100 × (cauldronFill/3) × 1000 × turbineRate` (per tick, `.toLong()` truncation — use `int()`).
   - **"TE" in the answer = raw/1000.** (1.0 "TE/t" = 1000 raw/t.) The boiler's cauldron fill matters: full = 3, half = 1.5, a third = 1.
5. One turbine = one boiler = one rotor. N turbines = N such stacks unless the user asks for generators shared across boilers (then use the network model, below).

## Tier reference (snapshot — verify against MachineTier.kt)

| tier | generatorHeat | initial | range | bonus | cooling | machineSpeed | turbineRate |
|---|---|---|---|---|---|---|---|
| copper (1.0) | 0.010 | 100 | 250 | 50 | 0.99 | 0.5 | 1.5 |
| gold (1.5) | 0.0125 | 100 | 300 | 100 | 0.994 | 0.75 | 1.75 |
| steel (2.0) | 0.015 | 100 | 400 | 100 | 0.996 | 1.0 | 2.0 |
| netherite (2.5) | 0.0175 | 100 | 500 | 100 | 0.9975 | 1.25 | 2.25 |

## Fuel reference (vanilla `FuelValues.vanillaBurnTimes`, baseUnit = 200 ticks)

The generator's `fuelValue` is `2` for items in `to_stars:heat_generator_2x` (currently `#minecraft:coals` + `#c:storage_blocks/coal`, i.e. coal, charcoal, coal block), `0` for items tagged `c:buckets` (buckets do **not** burn in a solid generator at all), else `1`. Burn time = the vanilla furnace burn time in ticks:

| fuel | burn ticks | fuelValue |
|---|---|---|
| coal / charcoal | 1600 | 2 |
| coal block | 16000 | 2 |
| blaze rod | 2400 | 1 |
| planks / log / wooden stairs / chests / bookshelves (1.5× items) | 300 | 1 |
| wooden slab | 150 | 1 |
| stick / wool / sapling / bowl / dead bush | 100 | 1 |
| dried kelp block | 4001 | 1 |
| lava bucket | 20000 | **0 — does not burn** |

For other items use the vanilla furnace burn table (baseUnit 200): e.g. 1.5× items (bookshelves, chests, boats are 6×, etc.). If unsure, ask.

## How to compute (run the simulator, do not hand-wave)

The simulator lives next to this file: **`te_sim.py`** (resolve it relative to this skill's directory). Run it with `python3 te_sim.py '<json spec>'`. It implements the tick logic above exactly (integer truncation, snap-to-cap, snap-to-0, contiguous fuel burn, network sharing, multi-generator networks).

If the tier constants in the file no longer match `MachineTier.kt`, fix them in `te_sim.py` (and the table above) before running.

### Specs

**Simple (one generator, one turbine; or N identical independent copies):**
```json
{"tier": "copper", "fuel": "coal", "qty": 1}
{"tier": "gold", "fuel": "coal", "qty": 3, "stacks": 3}   // 3 independent generators, each its own boiler+turbine
```

**Full networks (the general form — anything beyond that uses this):**
```json
{
  "networks": [
    {
      "generators": [{"tier": "copper", "fuel": "blaze_rod", "qty": 3}, {"tier": "steel", "fuel": "blaze_rod", "qty": 3}],
      "turbines": [{"tier": "gold"}, {"tier": "gold"}, {"tier": "copper"}, {"tier": "copper"}]
    }
  ]
}
```
- Each network = one heat-pipe network: any number of generators, any number of boiler+rotor+turbine stacks. Mixed tiers are fine; each entry in `turbines` is one boiler with its rotor and turbine.
- **`turbines` is in the order the network visits the boilers.** `HeatBlockNetworkType` fills boilers in iteration order, so list them in the order the user says they get heat ("the gold turbines run first, then the copper ones" → gold entries first). A boiler beyond `active` gets 0 heat.
- Multiple networks in one `networks` array are simulated **independently** (separate heat networks); report them separately.
- Omitting `"networks"` falls back to the simple form.

### Output fields

Per network (or per simple stack):
- `first_te_t` — the network's total rate right after ignition, i.e. "from rest" (generators start at `initial` heat on the first burning tick). Report as "X TE/t from rest".
- `peak_te_t` — highest total rate ever reached; `ramp_s` — how long the ramp to it took. When the rate goes flat afterwards (a generator hits its heat cap, or a boiler clamps at 200 heat), say the peak is the flat running rate.
- `total_te` — everything generated until all fuel is gone and every generator has cooled to 0.
- `max_active_boilers` — how many boilers ever got heat at once (`int(totalHeat/100)`); boilers past this produced 0.
- `per_turbine[]` — each stack's own `total_te` and `peak_te_t`, in the order given. A turbine with `total_te` of 0 never ran: note that.
- `sim_s` — the full simulated duration in seconds (burn + cool-down). The `burn` time is `qty × ceil(furnaceTicks × machineSpeed)` ticks ÷ 20 per generator; the network ends when the slowest generator's fuel is gone and it has cooled.

Defaults when the user leaves things unstated: full cauldron (fill = 3), 1 boiler per turbine, turbines the same tier as the generator, fuel value as in the table, and separate independent stacks when "N generators with N turbines" is what's described. Use the `network` model when the user explicitly wants **one generator piped to several boilers** sharing one fuel.

## Answer format

Match this style (values to sensible precision, round the total to 1 decimal):

> 1 coal in a copper generator with 1 copper turbine: **2.0 TE/t from rest**, ramping up to **2.32 TE/t** over **~40 s**, generating **1757.8 TE** in total (40 s of burn).

Useful variants:
- "3 gold heat generators, each with one coal block, each connected to one gold turbine" → `{"tier":"gold","fuel":"coal_block","qty":1,"stacks":3}`
- "one gold heat generator piped to 3 boilers with one coal block" → `{"networks":[{"generators":[{"tier":"gold","fuel":"coal_block","qty":1}],"turbines":[{"tier":"gold"},{"tier":"gold"},{"tier":"gold"}]}]}` — note: one generator's heat is split, so this yields *less* per boiler than 3 independent stacks
- "copper + steel generators on one network, gold turbines first then copper" → a `networks` spec with both generators and the turbines listed in that priority order
- Partial cauldron: `{"fill": 1.5}` for a half-full cauldron (halves all rates).

If the fuel does not burn (e.g. lava bucket → `val = 0`), say so and do not pretend there is a ramp: it holds the generator at rest heat only (flat rate while the item sits in the slot, then nothing).

## Reference files (for deeper questions)

- `to_stars/common/.../block/entity/HeatGeneratorBlockEntity.kt` — heat ramp/cool/cap logic
- `to_stars/common/.../block/entity/SolidGeneratorBlockEntity.kt` + `inventory/menu/SolidGeneratorMenu.kt` — fuel value/duration (the 2× tag and bucket exclusion)
- `to_stars/common/.../block/network/HeatBlockNetworkType.kt` — multi-boiler heat sharing
- `to_stars/common/.../block/entity/BoilingCauldronBlockEntity.kt` — per-side heat, boil/UNSTABLE threshold (cosmetic)
- `to_stars/common/.../block/entity/RotorBlockEntity.kt` — `clamp(heat,0,200)/100 × fill/3 × 1000 × turbineRate`, `.toLong()`
- `to_stars/common/.../block/TurbineBlock.kt` — turbine only outputs what the rotor made this tick
- `to_stars/common/src/main/resources/data/to_stars/tags/item/heat_generator_2x.json` — which fuels burn at 2×
