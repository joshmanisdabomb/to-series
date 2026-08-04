package net.jidb.to.stars.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.stars.item.component.InfiniteEnergyItemComponent
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries

/**
 * [SimpleRegistryLibrary] implementation holding the item components of this mod.
 */
object ToStarsItemComponentLibrary : SimpleRegistryLibrary<DataComponentType<*>>(ToBaseMod.modid) {

    override val registry = BuiltInRegistries.DATA_COMPONENT_TYPE

    /**
     * Marks an item as holding endless energy, which is what the creative power source carries.
     */
    val infinite_energy by this { DataComponentType.Builder<InfiniteEnergyItemComponent>().persistent(InfiniteEnergyItemComponent.codec)
        .build() }

}
