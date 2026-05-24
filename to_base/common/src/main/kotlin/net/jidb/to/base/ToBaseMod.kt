package net.jidb.to.base

import net.jidb.to.base.content.*
import net.jidb.to.base.mod.ToMod
import net.jidb.to.base.network.payload.DistantSoundPayload
import net.jidb.to.base.service.Services

object ToBaseMod : ToMod() {
    const val MOD_ID = "to_base"

    override val modid = MOD_ID

    override val blocks = ToBaseBlockLibrary
    override val items = ToBaseItemLibrary
    override val blockItems = ToBaseBlockItemLibrary
    override val tabs = ToBaseCreativeTabLibrary

    override val menus = ToBaseMenuLibrary

    override val blockTags = ToBaseBlockTagLibrary
    override val itemTags = ToBaseItemTagLibrary

    override fun init() {
        super.init()

        Services.platform.networking.register(DistantSoundPayload.Companion)
    }
}