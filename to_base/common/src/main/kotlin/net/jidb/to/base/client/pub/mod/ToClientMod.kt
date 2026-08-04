package net.jidb.to.base.client.pub.mod

import net.jidb.to.base.api.mod.ToContentMod
import net.jidb.to.base.api.mod.ToModListener
import net.jidb.to.base.client.api.mod.IToClientMod
import net.jidb.to.base.client.api.mod.ToContentClientMod
import net.jidb.to.base.client.api.properties.ExtendedClientBlockProperties

/**
 * The base class for the client-side object of a To mod, the client counterpart of [net.jidb.to.base.pub.mod.ToMod].
 * Building every client library the mod declares is done here, so a mod only has to say which libraries it has rather than when to build each one.
 *
 * Client startup happens in two passes: [clientInit] builds the libraries, and [clientSetup] runs once the common side has finished registering, which is when the client half of a block's properties can be applied.
 * A listener can be attached to either through [onInitialised] and [onSetup], and one attached after the fact runs immediately rather than never.
 *
 * @since 0.2.0
 */
abstract class ToClientMod : IToClientMod, ToContentClientMod, ToModListener<ToClientMod> {

    /**
     * The backing property of [initialised].
     *
     * @since 0.5.0
     */
    override var initialised = false
        protected set

    /**
     * The listeners waiting for [clientInit] to finish, attached through [onInitialised].
     *
     * @since 0.5.0
     */
    protected val initHooks = mutableListOf<(mod: ToClientMod) -> Unit>()

    /**
     * The backing property of [complete].
     *
     * @since 0.5.0
     */
    override var complete = false
        protected set

    /**
     * The listeners waiting for [clientSetup] to finish, attached through [onSetup].
     *
     * @since 0.5.0
     */
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
