package net.jidb.to.base.client.pub.mod

import net.jidb.to.base.api.mod.ToContentMod
import net.jidb.to.base.api.mod.ToModListener
import net.jidb.to.base.client.api.block.properties.ExtendedClientBlockProperties
import net.jidb.to.base.client.api.mod.IToClientMod
import net.jidb.to.base.client.api.mod.ToContentClientMod

abstract class ToClientMod : IToClientMod, ToContentClientMod, ToModListener<ToClientMod> {

    protected var _initialised = false
    override val initialised get() = _initialised
    protected val initHooks = mutableListOf<(mod: ToClientMod) -> Unit>()

    protected var _complete = false
    override val complete get() = _complete
    protected val setupHooks = mutableListOf<(mod: ToClientMod) -> Unit>()

    override fun clientInit() {
        blockEntityRenderers?.build()
        entityRenderers?.build()
        payloadHandlers?.build()
        modelLayers?.build()
        specialModels?.build()
        particles?.build()
        screens?.build()
        events?.build()
        eventHandlers?.build()
        reloadListeners?.build()

        _initialised = true
        initHooks.forEach { it(this) }
        common.logger.info("Initialised ToClientMod $this")
    }

    override fun onInitialised(listener: (mod: ToClientMod) -> Unit) {
        if (initialised) {
            listener(this)
        } else {
            initHooks.add(listener)
        }
    }

    override fun clientSetup() {
        val content = common as? ToContentMod
        if (content != null) {
            val properties = content.blocks?.properties
            if (properties != null) {
                ExtendedClientBlockProperties.handle(properties)
            }
        }

        _complete = true
        setupHooks.forEach { it(this) }
    }

    override fun onSetup(listener: (mod: ToClientMod) -> Unit) {
        if (complete) {
            listener(this)
        } else {
            setupHooks.add(listener)
        }
    }

}
