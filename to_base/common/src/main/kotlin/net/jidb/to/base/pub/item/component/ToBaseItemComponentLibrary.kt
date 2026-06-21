package net.jidb.to.base.pub.item.component

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.base.pub.item.TooltipProviderRegistry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries

object ToBaseItemComponentLibrary : SimpleRegistryLibrary<DataComponentType<*>>(ToBaseMod.modid) {

    override val registry = BuiltInRegistries.DATA_COMPONENT_TYPE

    val energy_data by this { DataComponentType.Builder<ToEnergyItemData>().persistent(ToEnergyItemData.codec)
        .build().also { TooltipProviderRegistry.register(it) } }

}
