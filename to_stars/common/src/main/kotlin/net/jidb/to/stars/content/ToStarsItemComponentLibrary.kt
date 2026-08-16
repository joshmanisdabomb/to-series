package net.jidb.to.stars.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.stars.item.component.InfiniteEnergyItemComponent
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries

object ToStarsItemComponentLibrary : SimpleRegistryLibrary<DataComponentType<*>>(ToBaseMod.modid) {

    override val registry = BuiltInRegistries.DATA_COMPONENT_TYPE

    val infinite_energy by this { DataComponentType.Builder<InfiniteEnergyItemComponent>().persistent(InfiniteEnergyItemComponent.codec)
        .build() }

}
