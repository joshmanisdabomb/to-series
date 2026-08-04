package net.jidb.to.base.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.base.content.inventory.menu.ResearchMenu
import net.jidb.to.base.service.Services
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.inventory.MenuType

/**
 * [SimpleRegistryLibrary] implementation that registers the [MenuType] content of To Lay the Foundations, and provides access to it in one place.
 *
 * @since 0.1.0
 */
object ToBaseMenuLibrary : SimpleRegistryLibrary<MenuType<*>>(ToBaseMod.modid) {

    override val registry = BuiltInRegistries.MENU

    /**
     * The menu opened by a research desk, which the in-game wiki is read through.
     *
     * @see ResearchMenu
     * @since 0.1.0
     */
    val research by this { Services.platform.inventory.createBasicMenu(::ResearchMenu) }

}
