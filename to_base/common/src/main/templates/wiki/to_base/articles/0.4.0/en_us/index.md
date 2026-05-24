---
flavor: "Yes, this API breaking change was completely necessary!"
---

# Introduction
{{"template": "introduction_version", "ordinal": "eighth", "extra": ", and the fourth major 0.x release"}}.

This release adds libraries and registration for entities and their renderers, block entities, and chunk loading tickets.

# In this Release

## Additions
- Added entities, block entities and chunkload ticket library properties to ToMod.
- Added entity renderer library property to ToMod.
- Added DistantSoundInstance, a way to play sound with proper attenuation with distances greater than 16 blocks.
- Added DistantSoundPayload for the server to send sound packets for clients to play DistantSoundInstance with any sound event.

## Fixes
- PayloadLibrary and PayloadHandlerLibrary should now correctly register S2C and C2S packets.