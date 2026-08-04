package net.jidb.to.base.neoforge.client.platform

import net.jidb.to.base.client.api.platform.ScreenClientPlatformModule
import net.jidb.to.base.neoforge.platform.DeferredForgeEventRegistry
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.MenuAccess
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent

/**
 * [ScreenClientPlatformModule] implementation for Neoforge.
 *
 * A screen cannot be registered whenever a mod asks, only while Neoforge is raising its own event, so every declaration is queued in a [DeferredForgeEventRegistry] and played back once that arrives.
 *
 * @since 0.1.0
 */
object ScreenForgeClientPlatformModule : ScreenClientPlatformModule() {

    /**
     * The queued registrations, played back when Neoforge raises the event they are waiting on.
     *
     * @since 0.1.0
     */
    val registry = DeferredForgeEventRegistry(RegisterMenuScreensEvent::class.java)

    override fun <M : AbstractContainerMenu, S> registerMenuScreen(modid: String, type: () -> MenuType<M>, factory: (menu: M, playerInventory: Inventory, title: Component) -> S) where S : Screen, S : MenuAccess<M> {
        registry.register(modid) { event -> event.register(type(), factory) }
    }

}
