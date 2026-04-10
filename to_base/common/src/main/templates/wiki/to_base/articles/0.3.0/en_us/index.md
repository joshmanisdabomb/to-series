---
flavor: "Make generating data great again."
---

# Introduction
{{"template": "introduction_version", "ordinal": "seventh", "extra": ", and the third major 0.x release"}}.

This release focuses on improving the data generation process, by allowing each mod to define a cascading set of data for single or multiple blocks, items, and other registry types that need data.

# In this Release

## Additions
- Added ToForgeDataMod to simplify writing data generators, similar to the ToMod classes added in {{"of": "to_base:mod_version / to_base:0.2.0"}}.
- Added the Data Collection API, a cascading data generation system that allows mods to define data of multiple categories for multiple entries in a single place.
- Added AutoCopyTagDataProvider, a data provider that automatically scans for vanilla and common block tags, as well as receiving custom modded tags, and copies their entries to corresponding item tags.
- Added MultiLanguageDataProvider, a data provider that generates language files for multiple locales at once.

## Changes
- Data generation split into common and client packages.