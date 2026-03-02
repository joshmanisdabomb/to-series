package net.jidb.to.base

import net.jidb.to.base.content.ToBaseBlockItemLibrary
import net.jidb.to.base.content.ToBaseBlockLibrary
import net.jidb.to.base.content.ToBaseCreativeTabLibrary
import net.jidb.to.base.content.ToBaseItemLibrary
import net.jidb.to.base.service.Services
import org.slf4j.LoggerFactory

object ToBaseMod {
    const val MOD_ID = "to_base"
    val logger = LoggerFactory.getLogger(MOD_ID)

    val blocks = ToBaseBlockLibrary
    val items = ToBaseItemLibrary
    val blockItems = ToBaseBlockItemLibrary
    val tabs = ToBaseCreativeTabLibrary

    fun init() {
        logger.info("Hello common world from ${MOD_ID}!")

        logger.info(Services.environment.platform.toString())
        logger.info(Services.environment.environment.toString())
        logger.info(Services.environment.context.toString())

        blocks.build()
        items.build()
        blockItems.build()
        tabs.build()
    }
}