package net.jidb.to.base

import org.slf4j.LoggerFactory

object ToBaseMod {
    const val mod_id = "to_base"
    val logger = LoggerFactory.getLogger(mod_id)

    fun init() {
        logger.info("Hello common world from ${mod_id}!")
    }
}