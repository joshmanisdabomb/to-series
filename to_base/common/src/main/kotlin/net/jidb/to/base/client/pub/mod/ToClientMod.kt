package net.jidb.to.base.client.pub.mod

import net.jidb.to.base.api.mod.ToContentMod
import net.jidb.to.base.api.mod.ToModListener
import net.jidb.to.base.client.api.mod.IToClientMod
import net.jidb.to.base.client.api.mod.ToContentClientMod
import net.jidb.to.base.client.api.properties.ExtendedClientBlockProperties

abstract class ToClientMod : IToClientMod, ToContentClientMod, ToModListener<ToClientMod> {

    override var initialised = false
        protected set
    protected val initHooks = mutableListOf<(mod: ToClientMod) -> Unit>()

    override var complete = false
        protected set
    protected val setupHooks = mutableListOf<(mod: ToClientMod) -> Unit>()

    override fun clientInit() {
        modelLayers?.build()
        blockEntityRenderers?.build()
        entityRenderers?.build()
        payloadHandlers?.build()
        specialModels?.build()
        blockTints?.build()
        itemTints?.build()
        particles?.build()
        screens?.build()
        events?.build()
        eventHandlers?.build()
        reloadListeners?.build()

        initialised = true
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

        complete = true
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
