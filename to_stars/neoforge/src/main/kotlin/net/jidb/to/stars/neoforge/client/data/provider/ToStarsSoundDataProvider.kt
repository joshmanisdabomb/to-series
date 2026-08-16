package net.jidb.to.stars.neoforge.client.data.provider

import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.SoundDefinition
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider
import java.util.concurrent.CompletableFuture

class ToStarsSoundDataProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>) : SoundDefinitionsProvider(output, ToStarsMod.MOD_ID) {

    override fun registerSounds() {
        add(ToStarsMod.sounds.nuke_small, SoundDefinition.definition()
            .with(
                sound(ToStarsMod.sounds.nuke_small.location.withPath { it.replace('.', '/') }, SoundDefinition.SoundType.SOUND)
            )
            .subtitle("subtitles." + ToStarsMod.sounds.nuke_small.location.toString().replace(":", "."))
        )
        add(ToStarsMod.sounds.nuke_large, SoundDefinition.definition()
            .with(
                sound(ToStarsMod.sounds.nuke_large.location.withPath { it.replace('.', '/') }, SoundDefinition.SoundType.SOUND)
            )
            .subtitle("subtitles." + ToStarsMod.sounds.nuke_large.location.toString().replace(":", "."))
        )

        add(ToStarsMod.sounds.atomic_bomb_activate, SoundDefinition.definition()
            .with(
                sound(ToStarsMod.sounds.atomic_bomb_activate.location.withPath { it.replace('.', '/') }, SoundDefinition.SoundType.SOUND)
            )
            .subtitle("subtitles." + ToStarsMod.sounds.atomic_bomb_activate.location.toString().replace(":", "."))
        )
        add(ToStarsMod.sounds.atomic_bomb_timer, SoundDefinition.definition()
            .with(
                sound(ToStarsMod.sounds.atomic_bomb_timer.location.withPath { it.replace('.', '/') }, SoundDefinition.SoundType.SOUND)
            )
            .subtitle("subtitles." + ToStarsMod.sounds.atomic_bomb_timer.location.toString().replace(":", "."))
        )
        add(ToStarsMod.sounds.atomic_bomb_cut, SoundDefinition.definition()
            .with(*Array(3, { k ->
                sound(ToStarsMod.sounds.atomic_bomb_cut.location.withPath { it.replace('.', '/') }.withSuffix("${k + 1}"), SoundDefinition.SoundType.SOUND)
            }))
            .subtitle("subtitles." + ToStarsMod.sounds.atomic_bomb_cut.location.toString().replace(":", "."))
        )

        add(ToStarsMod.sounds.generator_crackle, SoundDefinition.definition()
            .with(*Array(4, { k ->
                sound(ToStarsMod.sounds.generator_crackle.location.withPath { it.replace('.', '/') }.withSuffix("${k + 1}"), SoundDefinition.SoundType.SOUND)
            }))
            .subtitle("subtitles." + ToStarsMod.sounds.generator_crackle.location.toString().replace(":", "."))
        )
        add(ToStarsMod.sounds.generator_empty, SoundDefinition.definition()
            .with(
                sound(ToStarsMod.sounds.generator_empty.location.withPath { it.replace('.', '/') }, SoundDefinition.SoundType.SOUND)
            )
            .subtitle("subtitles." + ToStarsMod.sounds.generator_empty.location.toString().replace(":", "."))
        )

        add(ToStarsMod.sounds.gravitational_influence, SoundDefinition.definition()
            .with(
                sound(ToStarsMod.sounds.gravitational_influence.location.withPath { it.replace("music_disc.", "records/") }, SoundDefinition.SoundType.SOUND).stream()
            )
        )
    }

}
