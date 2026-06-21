package net.jidb.to.base.client.pub.library

import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.client.service.ClientServices
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.MenuAccess
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType

open class ScreenLibrary(modid: String) : SimpleLibrary<ScreenLibrary.ScreenEntry<out AbstractContainerMenu, *>>(modid) {

    override fun afterBuild(entry: Library<ScreenEntry<out AbstractContainerMenu, *>, ScreenEntry<out AbstractContainerMenu, *>>.LibraryEntry<out ScreenEntry<out AbstractContainerMenu, *>, out ScreenEntry<out AbstractContainerMenu, *>>) = entry.value.register()

    inner class ScreenEntry<M : AbstractContainerMenu, S>(private val type: () -> MenuType<M>, private val factory: (menu: M, playerInventory: Inventory, title: Component) -> S) where S : Screen, S : MenuAccess<M> {
        internal fun register() = ClientServices.platform.screens.registerMenuScreen(modid, type, factory)
    }

}
