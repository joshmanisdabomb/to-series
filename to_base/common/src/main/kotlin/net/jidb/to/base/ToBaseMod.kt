package net.jidb.to.base

import net.jidb.to.base.content.ToBaseContentMod
import net.jidb.to.base.hooks.event.handler.ToBaseEventHandlerLibrary
import net.jidb.to.base.hooks.level.storage.ToBaseSavedDataLibrary
import net.jidb.to.base.pub.ToBaseRegistryLibrary
import net.jidb.to.base.pub.block.network.energy.ToBaseBlockNetworkLibrary
import net.jidb.to.base.pub.event.ToBaseEventLibrary
import net.jidb.to.base.pub.item.component.ToBaseItemComponentLibrary
import net.jidb.to.base.pub.mod.ToMod
import net.jidb.to.base.pub.network.ToBasePayloadLibrary
import net.jidb.to.base.pub.tags.ToBaseBlockTagLibrary
import net.jidb.to.base.pub.transfer.ToBaseTransferProviderLibrary

/**
 * The mod object for To Lay the Foundations. Extends [ToMod].
 *
 * @since 0.0.1
 */
object ToBaseMod : ToMod() {

    /**
     * The mod ID for To Lay the Foundations, defined as a constant for Forge @Mod and @EventBusSubscriber annotations.
     */
    const val MOD_ID = "to_base"

    override val modid = MOD_ID

    /**
     * A sub mod object that holds the "content" of To Lay the Foundations, such as the research desk.
     * This is here to keep the content of the mod separate from the API available to modders.
     */
    val content by lazy { ToBaseContentMod(modid, logger) }

    override val itemComponents = ToBaseItemComponentLibrary
    override val payloads = ToBasePayloadLibrary
    override val events = ToBaseEventLibrary
    override val eventHandlers = ToBaseEventHandlerLibrary
    override val blockTags = ToBaseBlockTagLibrary
    override val blockNetworks = ToBaseBlockNetworkLibrary
    override val savedData = ToBaseSavedDataLibrary
    override val registries = ToBaseRegistryLibrary
    override val transferProviders = ToBaseTransferProviderLibrary

    override fun init() {
        super.init()
        content.init()
    }

    override fun setup() {
        super.setup()
        content.setup()
    }

}
