package net.jidb.to.base.test

import net.jidb.to.base.ToBaseMod
import net.minecraft.SharedConstants
import net.minecraft.server.Bootstrap
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * Starts enough of the game, once per test JVM, for a test to use the built-in registries and the real [net.minecraft.world.level.block.state.BlockState]s that come out of them.
 *
 * Almost anything in `net.minecraft` touches [net.minecraft.core.registries.BuiltInRegistries] on the way, including the reflection a mocking library uses to build a stub of one, and that class cannot initialise until the game version has been worked out.
 *
 * This gets a test as far as anything that only reads a registry. It does not get it a modloader, a level or a save, so a test that needs one of those wants a game test rather than a unit test.
 *
 * @since 0.8.0
 */
object MinecraftBootstrap {

    /**
     * Whether [ensure] has already run in this JVM.
     *
     * @since 0.8.0
     */
    private var started = false

    /**
     * Bootstraps the game and creates the registries the mod adds, doing nothing if that already happened.
     *
     * The registries come last because [TestRegisterService] writes into them as the loaders' own services do, and vanilla freezes the built-in ones on the way through `Bootstrap.bootStrap`.
     *
     * @since 0.8.0
     */
    @Synchronized
    fun ensure() {
        if (started) return
        started = true

        SharedConstants.tryDetectVersion()
        Bootstrap.bootStrap()
        //Anything holding a codec over a registry of the mod's own, such as BlockNetwork, needs it to exist before the class is even loaded.
        ToBaseMod.registries.buildOnce()
    }

    /**
     * The JUnit extension that calls [ensure] before the tests of the class it is applied to, which is how a test asks for this rather than calling it itself.
     *
     * Apply it with [ExtendWith], as [net.jidb.to.base.api.block.network.BlockNetworkTest] does.
     *
     * @since 0.8.0
     */
    class Extension : BeforeAllCallback {

        override fun beforeAll(context: ExtensionContext) = ensure()

    }

}
