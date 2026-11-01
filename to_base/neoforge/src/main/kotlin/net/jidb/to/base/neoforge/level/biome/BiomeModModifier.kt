package net.jidb.to.base.neoforge.level.biome

import com.mojang.serialization.codecs.RecordCodecBuilder
import net.jidb.to.base.api.level.biome.BiomeMod
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.biome.Biome
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo
import net.neoforged.neoforge.server.ServerLifecycleHooks

data class BiomeModModifier(val mod: BiomeMod) : BiomeModifier {

    override fun modify(biome: Holder<Biome>, phase: BiomeModifier.Phase, builder: ModifiableBiomeInfo.BiomeInfo.Builder) {
        if (phase == BiomeModifier.Phase.ADD) {
            val registryAccess = ServerLifecycleHooks.getCurrentServer()?.registryAccess() ?: error("Tried to get registryAccess from server not up.")
            val pfeatures = registryAccess.lookupOrThrow(Registries.PLACED_FEATURE)

            mod.features.forEach { mf ->
                val settings = builder.generationSettings
                if (mf.biomes.contains(biome.key) || mf.tags.any(biome::`is`)) {
                    mf.features.forEach { settings.addFeature(mf.step, pfeatures.getOrThrow(it)) }
                }
            }
        }
    }

    override fun codec() = codec

    companion object {

        val codec = RecordCodecBuilder.mapCodec {
            it.group(
                BiomeMod.codec.fieldOf("biome_mod").forGetter(BiomeModModifier::mod)
            )
                .apply(it, ::BiomeModModifier)
        }

    }

}
