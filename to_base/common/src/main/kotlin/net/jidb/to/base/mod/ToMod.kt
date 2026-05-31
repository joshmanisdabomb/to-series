package net.jidb.to.base.mod

import org.slf4j.LoggerFactory

abstract class ToMod : IToMod, ToContentMod, ToModListener<ToMod> {

    override val logger by lazy { LoggerFactory.getLogger(modid) }

    protected var _initialised = false
    override val initialised get() = _initialised
    protected val initHooks = mutableListOf<(mod: ToMod) -> Unit>()

    protected var _complete = false
    override val complete get() = _complete
    protected val setupHooks = mutableListOf<(mod: ToMod) -> Unit>()

    override fun init() {
        registries?.build()

        configuredFeatures?.build()
        placedFeatures?.build()

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

        sounds?.build()

        events?.build()
        eventHandlers?.build()
        advancementTriggers?.build()
        tickets?.build()
        savedData?.build()

        biomeMods?.build()

        reloadListeners?.build()

        _initialised = true
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

        _complete = true
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