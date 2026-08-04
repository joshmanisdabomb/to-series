package net.jidb.to.base.pub.item.component

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.base.pub.item.TooltipProviderRegistry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries

/**
 * [SimpleRegistryLibrary] implementation that registers the [DataComponentType] content of To Lay the Foundations, and provides access to it in one place.
 *
 * @since 0.6.0
 */
object ToBaseItemComponentLibrary : SimpleRegistryLibrary<DataComponentType<*>>(ToBaseMod.modid) {

    override val registry = BuiltInRegistries.DATA_COMPONENT_TYPE

    /**
     * The component storing To Energy on an item stack, which also registers itself for a tooltip as it is built.
     *
     * @see ToEnergyItemComponentData
     * @since 0.6.0
     */
    val energy_data by this { DataComponentType.Builder<ToEnergyItemComponentData>().persistent(ToEnergyItemComponentData.codec)
        .build().also { TooltipProviderRegistry.register(it) } }

}
