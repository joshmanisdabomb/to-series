package net.jidb.to.stars.content

import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.base.service.Services
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.inventory.menu.AtomicBombMenu
import net.jidb.to.stars.inventory.menu.EnergyStorageMenu
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.MenuType

object ToStarsMenuLibrary : SimpleRegistryLibrary<MenuType<*>>(ToStarsMod.modid) {

    override val registry = BuiltInRegistries.MENU

    val atomic_bomb by this { Services.platform.inventory.createMenuType(::AtomicBombMenu, FeatureFlags.DEFAULT_FLAGS) }
    val energy_storage by this { Services.platform.inventory.createMenuType(::EnergyStorageMenu, FeatureFlags.DEFAULT_FLAGS) }

}