package net.jidb.to.stars.neoforge.data.provider

import net.jidb.to.base.ToBaseMod
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.damagesource.DamageType
import java.util.concurrent.CompletableFuture

class ToStarsDamageTypeTagDataProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>) : TagsProvider<DamageType>(output, Registries.DAMAGE_TYPE, provider, ToBaseMod.modid) {

    override fun addTags(provider: HolderLookup.Provider) {
        this.tag(DamageTypeTags.IS_FIRE)
            .add(ToStarsMod.damageTypes.heated)
            .add(ToStarsMod.damageTypes.boiled)
        this.tag(DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES)
            .add(ToStarsMod.damageTypes.heated)
            .add(ToStarsMod.damageTypes.boiled)
        this.tag(DamageTypeTags.PANIC_CAUSES)
            .add(ToStarsMod.damageTypes.rotor_blades)
        this.tag(DamageTypeTags.BURN_FROM_STEPPING)
            .add(ToStarsMod.damageTypes.heated)
        this.tag(DamageTypeTags.BYPASSES_SHIELD)
            .add(ToStarsMod.damageTypes.heated)
            .add(ToStarsMod.damageTypes.boiled)
        this.tag(DamageTypeTags.SULFUR_CUBE_WITH_BLOCK_IMMUNE_TO)
            .add(ToStarsMod.damageTypes.heated)
            .add(ToStarsMod.damageTypes.rotor_blades)
        this.tag(DamageTypeTags.NO_KNOCKBACK)
            .add(ToStarsMod.damageTypes.heated)
            .add(ToStarsMod.damageTypes.boiled)
    }

}