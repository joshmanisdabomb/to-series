package net.jidb.to.stars.content

import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.base.service.Services
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.inventory.menu.AtomicBombMenu
import net.jidb.to.stars.inventory.menu.CentrifugeMenu
import net.jidb.to.stars.inventory.menu.EnergyStorageMenu
import net.jidb.to.stars.inventory.menu.ProcessorMenu
import net.jidb.to.stars.inventory.menu.SolidGeneratorMenu
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.inventory.MenuType

object ToStarsMenuLibrary : SimpleRegistryLibrary<MenuType<*>>(ToStarsMod.modid) {

    override val registry = BuiltInRegistries.MENU

    val atomic_bomb by this { Services.platform.inventory.createBasicMenu(::AtomicBombMenu) }

    val energy_storage by this { Services.platform.inventory.createBasicMenu(::EnergyStorageMenu) }

    val solid_generator by this { Services.platform.inventory.createBasicMenu(::SolidGeneratorMenu) }

    val centrifuge by this { Services.platform.inventory.createExtendedMenu(::CentrifugeMenu, ProcessorMenu.ProcessorMenuData.codec) }

}
