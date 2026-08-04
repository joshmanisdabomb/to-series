package net.jidb.to.base.api.platform

import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.PreparableReloadListener

/**
 * A [Platform]-specific module that defines a cross-platform contract for handling reload listeners.
 * This module has code for registering a [PreparableReloadListener] to run on game start and F3+R.
 *
 * @since 0.2.0
 */
abstract class ReloadListenerPlatformModule {

    /**
     * Registers a reload listener that will be triggered during the game's loading phase and when the player uses F3+R in-game.
     *
     * @param identifier The unique identifier for the reload listener.
     * @param listener The reload listener to be registered.
     * @since 0.2.0
     */
    abstract fun register(identifier: Identifier, listener: PreparableReloadListener)

}
