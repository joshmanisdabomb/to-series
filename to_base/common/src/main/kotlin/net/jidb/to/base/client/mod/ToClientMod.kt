package net.jidb.to.base.client.mod

import net.jidb.to.base.client.block.ExtendedClientBlockProperties
import net.jidb.to.base.mod.ToContentMod

abstract class ToClientMod : IToClientMod, ToContentClientMod {

    override fun clientInit() {
        entityRenderers?.build()
        payloadHandlers?.build()
        particles?.build()
        screens?.build()
        reloadListeners?.build()
    }

    override fun clientSetup() {
        val content = common as? ToContentMod
        if (content != null) {
            val properties = content.blocks?.properties
            if (properties != null) {
                ExtendedClientBlockProperties.handle(properties)
            }
        }
    }

}
