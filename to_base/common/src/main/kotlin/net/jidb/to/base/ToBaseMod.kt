package net.jidb.to.base

import net.jidb.to.base.content.ToBaseContentMod
import net.jidb.to.base.hooks.event.ToBaseEventLibrary
import net.jidb.to.base.hooks.network.ToBasePayloadLibrary
import net.jidb.to.base.hooks.tags.ToBaseBlockTagLibrary
import net.jidb.to.base.mod.ToMod

object ToBaseMod : ToMod() {
    const val MOD_ID = "to_base"

    override val modid = MOD_ID

    val content by lazy { ToBaseContentMod(modid, logger) }

    override val payloads = ToBasePayloadLibrary
    override val events = ToBaseEventLibrary
    override val blockTags = ToBaseBlockTagLibrary

    override fun init() {
        super.init()
        content.init()
    }

    override fun setup() {
        super.setup()
        content.setup()
    }
}