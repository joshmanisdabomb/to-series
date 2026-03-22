package net.jidb.to.base.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.content.inventory.menu.ResearchMenu
import net.jidb.to.base.library.SimpleRegistryLibrary
import net.jidb.to.base.service.Services
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.MenuType

object ToBaseMenuLibrary : SimpleRegistryLibrary<MenuType<*>>(ToBaseMod.MOD_ID) {

    override val registry = BuiltInRegistries.MENU

    val research by this { Services.environment.platform.inventory.createMenuType(::ResearchMenu, FeatureFlags.DEFAULT_FLAGS) }

}