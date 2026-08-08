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

/**
 * [SimpleRegistryLibrary] implementation holding the menu types of this mod, i.e. the interfaces its blocks can be opened into.
 */
object ToStarsMenuLibrary : SimpleRegistryLibrary<MenuType<*>>(ToStarsMod.modid) {

    override val registry = BuiltInRegistries.MENU

    /**
     * The interface the atomic bomb is armed through.
     */
    val atomic_bomb by this { Services.platform.inventory.createBasicMenu(::AtomicBombMenu) }

    /**
     * The interface a power bank is opened into.
     */
    val energy_storage by this { Services.platform.inventory.createBasicMenu(::EnergyStorageMenu) }

    /**
     * The interface a solid generator is opened into.
     */
    val solid_generator by this { Services.platform.inventory.createBasicMenu(::SolidGeneratorMenu) }

    /**
     * The interface a centrifuge is opened into, which is told which recipes it can run as it opens.
     */
    val centrifuge by this { Services.platform.inventory.createExtendedMenu(::CentrifugeMenu, ProcessorMenu.ProcessorMenuData.codec) }

}
