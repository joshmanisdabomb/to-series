package net.jidb.to.base.neoforge.client.platform

import net.jidb.to.base.client.platform.ScreenClientPlatformModule
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.MenuAccess
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent

object ScreenForgeClientPlatformModule : ScreenClientPlatformModule() {

    private val registers = mutableMapOf<String, MutableList<(event: RegisterMenuScreensEvent) -> Unit>>()

    override fun <M : AbstractContainerMenu, S> registerMenuScreen(modid: String, type: () -> MenuType<M>, factory: (menu: M, playerInventory: Inventory, title: Component) -> S) where S : Screen, S : MenuAccess<M> {
        registers.getOrPut(modid) { mutableListOf() }.add { event -> event.register(type(), factory) }
    }

    fun listener(modid: String, event: RegisterMenuScreensEvent) {
        registers[modid]!!.forEach { it(event) }
    }

    fun registerMod(modid: String, bus: IEventBus) {
        bus.addListener { event: RegisterMenuScreensEvent -> listener(modid, event) }
    }

}
