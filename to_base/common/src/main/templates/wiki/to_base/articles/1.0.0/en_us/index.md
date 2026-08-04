---
flavor: "Feels like Notch pulling the lever."
---

# Introduction
{{"template": "introduction_version", "ordinal": "thirteenth", "extra": ", and the first stable release"}}.

This release adds documentation to the code, and adds support for running unit tests and gameplay tests.

# In this Release

## Additions
- Added ToGameTest and associated library, so mods can declare a single test to set the test function, the test instance, and the structure it runs in.
- Added GameTestStructureDataProvider, which writes the structure of every declared test on a generation run.
- Added ToGameTestHelper, a set of extensions for acting on a scene as a player would - placing, breaking, finding the positions a block went to, and asserting on where its drops landed.
- Added PlaceBreakDropGameTest, a ready-made test for the multi-position blocks used across the {{"of": "to_base:tag / to_base:to_series"}}, covering placement, breaking any one position and the single drop coming from the middle.
- Added a unit test suite for the code that does not need a level, backed by JUnit and MockK, with test service providers and a bootstrap that starts just enough of the game for the built-in registries to be readable.
- Added game tests for the {{"of": "minecraft:block / to_base:research_desk"}}, covering placement, handedness and breaking either half.
- Every type, function, property and enum constant now carries documentation, at every visibility.
- Java is now linted with Checkstyle and Kotlin is now linted with Detekt.
- Added a package for custom Detekt rules, such as brace padding, end-of-line comment spacing, property naming, documentation coverage, and `@since` tags.
- A *.kt file in the Java source set and a *.java file in the Kotlin source set now fail the build.
