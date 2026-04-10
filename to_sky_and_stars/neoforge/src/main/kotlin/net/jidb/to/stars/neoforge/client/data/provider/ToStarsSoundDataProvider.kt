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
        )
        add(ToStarsMod.sounds.nuke_large, SoundDefinition.definition()
            .with(
                sound(ToStarsMod.sounds.nuke_large.location.withPath { it.replace('.', '/') }, SoundDefinition.SoundType.SOUND)
            )
        )
    }

}