---
flavor: "Every machine gets its own book of recipes to be proud of."
---

# Introduction
{{"template": "introduction_version", "ordinal": "fourteenth", "extra": ", and the second 1.x release"}}.

This release adds custom recipe book categories that display all recipes and filter via callback.

# In this Release

## Additions
- Added `CustomRecipeBookCategory` with its library, so a content mod can register a custom category on top of the vanilla recipe book categories and decide for itself which recipes it displays - for instance showing a kiln or processor only what it actually smelts.
- Added the hook to attach such a library from an existing content mod, alongside the rest of the other hooks like blocks and items.
