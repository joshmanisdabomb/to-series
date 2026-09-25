---
name: history
description: Tracing how a block, item, entity or similar in-game object was introduced, changed and resurfaced across the mod's history (yam → lcc/yam → aimagg → to_base/to_stars). Use when asked what an object is/did, which version added it, how its mechanics worked in a past iteration, or whether something was removed / renamed / reintroduced.
---

# Tracing Object History Through the Monorepo

This single repository holds several distinct mod eras as **git tags** (not branches). Answering "what is X / when did X appear" means working across those tags. The authoritative written record of changes lives in generated *knowledge* data, and only ever existed for the LCC fabric era — fall back to raw source / commit history outside it.

## Mod identity ↔ tag prefix

Different eras were published under different names/namespaces. `X` is the version:

| Era | Purpose | Tags |
|------|---------|------|
| **yam** | "Yet Another Mod" — earliest era, Minecraft 1.7.2 (reports itself as Beta 1.3) | `yam/u*`, `yam/latest` (u-number tags, e.g. `u12`) |
| **lcc_forge** | "Loosely Connected Concepts" Forge line (~MC 1.8 → 1.15.2). Same project family as lcc but a *different codebase*; mod versions share numbers with the fabric line and must not be conflated | `lcc_forge/*` (prealpha, a0.x–a1.0, b1.0) |
| **lcc** | "Loosely Connected Concepts" Fabric line — this is where most of the generated knowledge data lives | `lcc/0.1.x` … `lcc/latest`, plus snapshot tags like `lcc/branch/rainbow`, `lcc/branch/computing` |
| **aimagg** | "Aimless Agglomeration" — 1.12.2 branch of the same project, continued after LCC (mod ids use namespace `aimagg`; late-YAM objects were reintroduced here) | `aimagg/prealpha`, `aimagg/a*` |
| **to_base** | "To Lay the Foundations" — current library mod in this monorepo proper; modern rewrite lineage of the same concept space, but newer objects here are not automatically continuations of LCC-era ones (verify via code) | `to_base/0.x.y` |
| **to_stars / to_sky_and_stars** | "To Sky and Stars" — current content mod in this monorepo. Note: folder is `to_stars/`, the older tags use prefix `to_sky_and_stars/*`. Some objects (e.g. Kiln) were reimplemented here by cherry-picking/carrying over LCC-era code | `to_sky_and_stars/0.x.y` |

The full era/version grouping with release dates, mc versions and branch names is encoded in `LCCVersionGroup.kt` (`knowledge/LCCVersionGroup.kt`) — read it when a question needs the precise mod title or MC version of an old tag.

**Naming traps:**
- "LCC" appears in two sibling linesages (fabric vs forge). Version numbers like `0.4.3` exist in BOTH; check which loader/era the user means. Default to fabric unless stated otherwise.
- YAM is NOT a subset of LCC — it's an older independent alpha era that LCC inherited some content from. `LCCVersion.kt` covers both (its first entries are `YAM_*`).
- aimagg is its own branch, not lcc under a different name.

## The authoritative roadmap of what changed and when

The generated *knowledge* data model exists only in the **lcc** era tags:

- `lcc-content-data/src/main/kotlin/com/joshmanisdabomb/lcc/data/knowledge/LCCVersion.kt`
  Enum. Every entry = one released version of LCC (yam u1 → current). Each carries `modVersion`, `mcVersion`, a human `description` paragraph, and an optional `generateChangelog(map)` that maps specific objects to a text fragment (`introduced`, "Introduced as …", mechanic notes, etc.).
- `lcc-content-data/src/main/kotlin/com/joshmanisdabomb/lcc/data/directory/LCCKnowledgeData.kt`
  One large object full of hand-written wiki-style articles. Each article references objects via `LCCBlocks.<field>`, `LCCEntities.<field>`, etc., and cites versions with `addLink(LCCVersion.X)`.

### Searching these for an object

```bash
# Find the version(s) that mention "kiln" in LCC's own changelog descriptions:
git show lcc/latest:lcc-content-data/src/main/kotlin/com/joshmanisdabomb/lcc/data/knowledge/LCCVersion.kt \
  | grep -in kiln

# Same, but across all tags to see if wording evolved:
for t in $(git tag --list 'lcc/*'); do
  echo "== $t"
  git show "$t:lcc-content-data/src/main/kotlin/com/joshmanisdabomb/lcc/data/knowledge/LCCVersion.kt" 2>/dev/null | grep -in kiln
done

# The article itself (mechanics, notes):
git show lcc/latest:…/LCCKnowledgeData.kt | grep -n -A2 'LCCBlocks\.kiln\b'
```

Note the file path lives under `lcc-content-data/...`. Older tags may have it in a different layout — if the exact path fails, find it generically:

```bash
git ls-tree --name-only <tag>          # top-level dirs at that tag
# locate LCCVersion.kt anywhere: git ls-tree -r --name-only $t | grep 'LCCVersion.kt'
```

## Working with specific tags/versions elsewhere

When the object lives outside LCC (e.g. a `to_stars` addition like "Kiln", or an older yam-only feature not covered by LCCVersion entries):

1. **List the tree at the tag** and search for identifiers in source, lang files, block/item registrations:
   ```bash
   git ls-tree -r --name-only <tag> | grep -i kiln
   # content under a specific path prefix:
   git show <tag>:<path/to/file.kt> | grep -in kiln
   ```
2. **`git log --all -S"ident"`** — the string-pickaxe finds when an identifier first appeared or was deleted across commits, spanning tag boundaries. Combine with `-i` for case insensitivity:
   ```bash
   git log --all -S"kiln" --oneline | head
   ```
3. **Tag containment**: `git tag --contains <sha>` names every release/branch that includes a given commit — useful to state "this was present from vX onward."

## Answering pattern (example: "Tell me about the Kiln")

Assemble, in this order:
1. From `LCCVersion.kt` / knowledge data: what version first introduced it and how its mechanics were described at release (`"introduced"` tag + description text).
2. From later tags' versions of source (or from `to_stars/*` HEAD if carried forward): current/actual behavior — classes, recipe handling, speed multipliers.
3. If it was **renamed or reimplemented** across eras: list each era's introduction tag and a one-line summary pulled from that era's description strings.

Report the answer in terms of *mod identity + mod version*, not raw tags (users care about "LCC 0.4.3", "To Sky and Stars 0.2 current"), with the underlying git refs only where clarity is helped ("see tag `lcc/0.4.3`").

## Style

- Cite specific versions in prose ("added in LCC 0.4.3 (Mar 2021, targeting MC 21w11a)") using date if present on the tag (`LocalDateTime.of(...)` values are readable right out of `LCCVersion.kt`).
- Distinguish *originally added* from *re-added / reworked*: use each version's own description strings rather than inventing continuity. An object that was removed and repurposed is not "the same" unless the commit/log chain shows it — prefer saying what code actually carried over (`git log --follow`, `-S` pickaxe).
- Prefer quoting brief fragments (1 line) of the version's description string as the evidence, rather than paraphrasing loosely. If nothing documents a change explicitly in knowledge data or commits, say "no record" instead of speculating.
