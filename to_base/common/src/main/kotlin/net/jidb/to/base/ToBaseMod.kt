package net.jidb.to.base

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object ToBaseMod {
    private val logger = LoggerFactory.getLogger("to_base")

    fun init() {
        logger.info("Hello common world!")
    }
}