package net.jidb.to.base.pub.mod

import net.jidb.to.base.api.mod.IToMod
import net.jidb.to.base.api.mod.ToContentMod
import net.jidb.to.base.api.mod.ToModListener
import org.slf4j.LoggerFactory

/**
 * Implementation of a Minecraft cross-platform mod, with library properties that will be built on mod initialization in a sane order.
 * This object will be encapsulated by a [net.jidb.to.base.api.mod.ToPlatformMod], i.e. ToForgeMod and ToFabricMod.
 *
 * You don't need to be a To Series mod to use this class.
 *
 * @since 0.2.0
 * @see net.jidb.to.base.api.mod.ToPlatformMod
 */
abstract class ToMod : IToMod, ToContentMod, ToModListener<ToMod> {

    override val logger by lazy { LoggerFactory.getLogger(modid) }

    override var initialised = false
        protected set

    /**
     * A collection of initialization hooks that will be executed after the mod is initialised.
     * This stores hooks passed to [onInitialised] from [ToModListener].
     *
     * @since 0.5.0
     */
    protected val initHooks = mutableListOf<(mod: ToMod) -> Unit>()

    override var complete = false
        protected set

    /**
     * A collection of setup hooks that will be executed after the mod is set up.
     * This stores hooks passed to [onSetup] from [ToModListener].
     *
     * @since 0.5.0
     */
    protected val setupHooks = mutableListOf<(mod: ToMod) -> Unit>()

    override fun init() {
        registries?.build()

        configuredFeatures?.build()
        placedFeatures?.build()
        damageTypes?.build()
        lootTables?.build()
        music?.build()

        blockNetworks?.build()
        itemComponents?.build()

        blocks?.build()
        items?.build()
        blockItems?.build()
        tabs?.build()

        entities?.build()
        blockEntities?.build()
        menus?.build()
        particles?.build()

        payloads?.build()
        payloadHandlers?.build()
        payloads?.register(payloadHandlers)

        blockTags?.build()
        itemTags?.build()

        recipeCategories?.build()
        recipeDisplays?.build()
        recipeTypes?.build()
        recipeSerializers?.build()

        sounds?.build()

        events?.build()
        eventHandlers?.build()
        advancementTriggers?.build()
        tickets?.build()
        savedData?.build()
        transferProviders?.build()
        biomeMods?.build()

        reloadListeners?.build()

        gameTests?.build()

        initialised = true
        initHooks.forEach { it(this) }
        logger.info("Initialised ToMod $this")
    }

    override fun onInitialised(listener: (mod: ToMod) -> Unit) {
        if (initialised) {
            listener(this)
        } else {
            initHooks.add(listener)
        }
    }

    override fun setup() {
        blocks?.properties?.build()

        complete = true
        setupHooks.forEach { it(this) }
    }

    override fun onSetup(listener: (mod: ToMod) -> Unit) {
        if (complete) {
            listener(this)
        } else {
            setupHooks.add(listener)
        }
    }

}
