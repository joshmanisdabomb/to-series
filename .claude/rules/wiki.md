# Wiki Article Writing Conventions

All wiki articles live under `<modId>/common/src/main/templates/wiki/<modId>/articles/<slug>/`. The final compiled output goes to `<modId>/common/src/main/resources/assets/<modId>/wiki/articles/` — **never write files in the resources directory directly**.

## Article Structure

Every article is a directory containing:

```
articles/<slug>/
  index.json             -- required metadata
  en_us/
    index.md             -- required content (only locale)
  factsheet.json         -- required for blocks/items only
  changelog.json         -- optional
```

## Recipes

**Never write out crafting or smelting recipes in article content.** Recipes are automatically generated when baking articles into the resources folder — they are not static text. Describe *what* can be crafted or *how* an item is used, but do not format recipe ingredient grids or step-by-step crafting instructions. That will be handled by the bake process.

## index.json Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `title` | string or `{ "of": ... }` object | Yes | Literal string for mod/version/concept pages. Object that pulls display name from the game registry for items/blocks. |
| `about` | string[] | Yes | Resource identifiers this article is about. Format: `"type / modId:name"`. For mod references use `"to_base:mod / to_base:<name>"`. |
| `redirect` | string[] | No | Resource aliases that redirect here (obsoleted names, alternate access points). |
| `icon` | string[] | Yes | Display icon. Always single item in practice. |
| `tags` | string[] | Yes | Hierarchical mod-specific tags. Format: `"<modId>:<category>/<subtype>"`. |

### Title patterns

- **Items/blocks**: Use `{ "of": "minecraft:item / <modId>:<name>" }` or `{ "of": "minecraft:block / <modId>:<name>" }` — the title is pulled at runtime from the registry.
- **Concepts**: Use a literal string (e.g., `"Nuclear Explosion"`, `"Radiation"`).
- **Mod pages**: Always use a literal string (e.g., `"To Sky and Stars"`, `"To Lay the Foundations"`). Never use `{ "of": ... }` for mod pages.
- **Version pages**: Always use a literal string (e.g., `"0.6.0"`).

### About field patterns

Use resource identifiers with `type / modId:name` format:
- Blocks: `"minecraft:block / to_stars:copper_power_bank"`
- Items: `"minecraft:item / to_stars:uranium"`
- Concepts: `"to_base:concept / to_stars:nuclear_explosion"`
- Mods: `"to_base:mod / to_base:to_stars"`
- Authors: `"to_base:author / to_base:joshmanisdabomb"`
- Tags: `"to_base:tag / to_stars:machine"`
- Mod versions: `"to_base:mod_version / minecraft:20w14infinity"` (for external mods) or `"to_base:mod_version / to_stars:0.1.0"` (for To mods)

### Icon patterns

Always an array with a single item. Must match the type in `about` — if about references a block, icon should be a block reference. For concepts that don't have a corresponding game object, use the most iconic visual (like `nuclear_fire` for nuclear explosion).

### Tag conventions

Tags are hierarchical and mod-specific. Use patterns found in existing articles:
- Top-level categories: `"to_stars:machine"`, `"to_stars:nuclear"`, `"to_base:resource"`
- Subcategories: `"to_stars:machine/tier1"`, `"to_stars:machine/battery/tier1"`, `"to_base:test/items"`

Include all applicable tags — a copper_power_bank gets both `"to_stars:machine"` and `"to_stars:machine/battery"` for broad + narrow categorization. For concepts, tag by topic: `"to_stars:nuclear"`.

### Redirect patterns

Add redirects for anything that used to be or can reasonably be typed to find the article:
- Items → blocks (and vice versa): always include the item variant of a block's article and the block variant of an item's article
- Block entities: `"minecraft:block_entity_type / <modId>:<name>"`
- Menus/screens: `"minecraft:menu / <modId>:<screen_name>"`
- Sound events: `"minecraft:sound_event / <modId>:entity.<thing>.<event>"`
- Entity types: `"minecraft:entity_type / <modId>:<entity>"`
- Obsolete registry names

## factsheet.json (blocks and items only)

Maps resource identifiers to properties. Currently only `renewable` is supported:

```json
{
  "minecraft:block / modId:name": {
    "renewable": true
  }
}
```

Key matches the full type string from `about`. Only blocks and items need factsheets — not concepts, mods, or versions.

## Markdown Content Structure

### Frontmatter (required for all articles)

Start every `index.md` with YAML frontmatter containing a `flavor` field:

```yaml
---
flavor: "Short flavorful quote related to the topic."
---
```

The flavor text appears prominently in the wiki UI. Make it witty, thematic, or informative — match the tone of existing articles (e.g., `"Electrifying!"` for a power cable, `"Learning has never been so much fun!"` for a research desk).

### Required sections

Every article must have:
- `# Introduction` — always the first section, describes what the object/topic is

### Optional sections (choose based on topic)

- `# Usage` — how to use an item/block (crafting, mechanics, GUI interactions)
- `# Upgrading` — progression paths, tier changes, recipe chains
- `# Generation` — ore generation details (chunk distribution, Y levels, triangular distributions)
- `# Drops` — mining drops, enchantment effects, tool requirements
- `# Compacting` — compression/decompression with other resources
- `# Interaction` — how the concept affects game systems (blocks destroyed, entity damage, etc.)
- `# History` — where this content existed in previous mod iterations
- `# Plans` — future roadmap items for a content mod page
- `# Tips` — gameplay tips or strategies
- `# Technical` — implementation details, formulas, chunk loading behavior

### Section ordering convention

1. Introduction (what is it?)
2. Usage / Generation / Drops (how do you get/use it?)
3. Upgrading / Compacting (progression paths)
4. Interaction / Mechanics (deeper systems detail)
5. History (legacy content lineage)
6. Tips (player advice)
7. Technical (developer/internal details)

## Mustache-like Template Syntax

### Cross-referencing other articles

```markdown
{ {"of": "minecraft:block / to_stars:copper_power_bank"} }
```

Options:
- `"plural": true` — links as plural form (for items, use on singular references that mean multiple)
- `"text": "custom text"` — display custom text instead of the registry name
- `"short": true` — for mod version references, shows short form

### Self-reference

```markdown
{ {"of": "self"} }      -- this article (singular)
{ {"of": "self", "plural": true}}  -- this article (plural)
```

Use `{{"of": "self"}}` instead of hardcoding the registry name so articles remain valid even if names change.

### Templates

- `{ {"template": "introduction"} }` — standard intro boilerplate for items/blocks/concepts. The template generates: "The [type] {name}." Use with a `description` parameter for non-standard types:
  - `{ {"template": "introduction", "description": "an ore"} }` — "The ore Uranium Ore."
  - `{ {"template": "introduction"} }` — "The block Copper Power Bank." (no description needed for standard types)
- `{ {"template": "introduction_version", "ordinal": "tenth", "extra": ", and the sixth major 0.x release"} }` — version changelog intro pattern. `ordinal` is an English ordinal number; `extra` is appended text.
- `{ {"template": "mod_recent_version"} }` — in mod pages, links to the most recent version of that mod.

### Writing patterns for different article types

#### Items (ores, resources, nuggets, ingots)

```markdown
---
flavor: "Catchy one-liner."
---

# Introduction
The {{"template": "introduction", "description": "an ore"}}. It can be obtained by mining { {"of": "minecraft:block / modId:ore_name"} }.

Once processed into { {"of": "minecraft:item / modId:processed_item"} }, it can be used in [recipe/application].

# Compacting
{ {"of": "self"} } can be compacted into a { {"of": "minecraft:block / modId:block_variant"} } or broken down into { {"of": "minecraft:item / modId:nugget", "plural": true}}.
```

#### Blocks (machines, cables, enclosures, special blocks)

```markdown
---
flavor: "Catchy one-liner."
---

# Introduction
The {{"template": "introduction"}}. [What it does in 1-2 sentences.]

All { {"of": "to_base:tag / modId:machine/enclosure", "plural": true}} are an intermediary recipe step that crafts other { {"of": "to_base:tag / modId:machine", "plural": true}} of its tier.

# Usage
[How to activate/use the block, GUI interactions, requirements.]

## Subsection
Detailed mechanics with formulas if applicable. Include specific numbers and ranges.

# Upgrading
{ {"of": "self", "plural": true}} can be upgraded into a { {"of": "minecraft:block / next_tier_name"} }. The recipe requires [materials].

# History
[Previous appearances in legacy mods, only if relevant.]
```

#### Concepts (nuclear explosions, radiation effects)

```markdown
---
flavor: "Catchy one-liner."
---

# Introduction
The {{"template": "introduction"}}. It can be triggered by [mechanism].

# Interaction
## Blocks
Describe block destruction mechanics, blast resistance thresholds, affected radius.

## Entities
Describe entity damage calculations, knockback, special effects.

# History
[Previous appearances in legacy mods.]
```

#### Mod pages (overview of a To mod)

```markdown
---
flavor: "Catchy one-liner."
---

# Introduction
{ {"of": "self"}} is a [content/library] mod in the { {"of": "to_base:tag / to_base:to_series"} }, aiming to [mod goal]. The mod targets recent versions of Minecraft and both Fabric and Neoforge.

{ {"of": "self"}} was created by { {"of": "to_base:author / to_base:joshmanisdabomb"}}, starting in early 2026.

The most recent version is { {"template": "mod_recent_version"} }.

# Content
[What content this mod adds — reference other articles with {{"of": ...}}.]

# Plans
[Roadmap items as bullet list.]

# History
[Previous appearances in legacy mods, with the standard format:]
This mod contains content that could formerly be found in:
- { {"of": "to_base:mod / to_base:lcc"}} for Fabric 1.17 - 1.19.2.
- { {"of": "to_base:mod / to_base:lcc_forge"}} for Forge 1.13.2 - 1.15.2.
- { {"of": "to_base:mod / to_base:aimagg"}} for Forge 1.10.2 and 1.12.2.
- { {"of": "to_base:mod / to_base:yam"}} for Forge 1.7.2.
```

#### Version changelog pages

```markdown
---
flavor: "Catchy one-liner."
---

# Introduction
{ {"template": "introduction_version", "ordinal": "tenth", "extra": ", and the sixth major 0.x release"}}.

[One paragraph overview of what this release focuses on.]

# In this Release

## Additions
- Added [feature].
- Added [mechanic].

## Changes
- [Refactoring, reorganization, or behavior changes.]
```

### Important: Cross-mod reference rules

**Articles in `to_base` must NEVER reference objects from other To mods** (like items, blocks, concepts, or tags from `to_stars`). The to_base mod is a library mod that serves as the foundation — its wiki articles should only reference:
- Vanilla Minecraft objects (`minecraft:item`, `minecraft:block`, etc.)
- Objects registered in to_base itself (`to_base:research_desk`, `to_base:test_block`, etc.)
- Mod references (`to_base:mod / to_base:lcc`)
- Author references (`to_base:author / to_base:joshmanisdabomb`)
- Concepts and tags that are part of to_base's own API

**Articles in `to_stars` CAN reference to_base objects**, because to_stars depends on to_base. This includes:
- `{ {"of": "to_base:tag / to_stars:machine"}}` — referencing to_base's tag system
- `{ {"of": "minecraft:block / to_base:research_desk"}}` — referencing base mod content

## Slug Conventions

- All slugs use underscores (snake_case), matching Minecraft registry naming conventions
- For items/blocks, use the exact Minecraft registry name (e.g., `copper_power_bank` matches `"minecraft:block / to_stars:copper_power_bank"`)
- For mods, use the display name slug: `to_stars`, `to_base`
- For versions, use semver format without prefix: `0.6.0`
- For concepts that don't have registry names, use a descriptive snake_case slug: `nuclear_explosion`

## Tone and Style Guidelines

- Write in clear, direct prose. Avoid filler words.
- Use second person for gameplay instructions ("right-click to open the interface")
- Be specific with numbers — include exact formulas, ranges, Y-levels, tick counts when available
- Reference other articles liberally using `{ {"of": ...}}` syntax instead of naming objects in plain text when a link is possible
- Keep paragraphs short (2-4 sentences max)
- Use code formatting for mathematical formulas: `20 + floor(sqrt((uranium - 1) / 44) * 120)`
- For lists of requirements or steps, use numbered or bulleted sub-lists under subsection headings
