package net.jidb.to.base.neoforge.pub

import com.mojang.serialization.MapCodec
import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.base.neoforge.level.biome.BiomeModModifier
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.registries.NeoForgeRegistries

object ToBaseBiomeModifierLibrary : SimpleRegistryLibrary<MapCodec<out BiomeModifier>>(ToBaseMod.modid) {

    override val registry = NeoForgeRegistries.BIOME_MODIFIER_SERIALIZERS

    val biome_mod by this { BiomeModModifier.codec }

}
