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

object ToBaseMod : ToMod() {
    const val MOD_ID = "to_base"

    override val modid = MOD_ID

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