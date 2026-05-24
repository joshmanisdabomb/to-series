package net.jidb.to.base.mod

import org.slf4j.LoggerFactory

abstract class ToMod : IToMod, ToContentMod {

    override val logger by lazy { LoggerFactory.getLogger(modid) }

    override fun init() {
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

        tickets?.build()

        biomeMods?.build()

        reloadListeners?.build()
    }

    override fun setup() {
        blocks?.properties?.build()
    }

}