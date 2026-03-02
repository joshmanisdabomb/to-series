package net.jidb.to.stars

import net.jidb.to.base.ToBaseMod
import net.jidb.to.stars.content.ToSkyAndStarsBlockItemLibrary
import net.jidb.to.stars.content.ToSkyAndStarsBlockLibrary
import net.jidb.to.stars.content.ToSkyAndStarsCreativeTabLibrary
import net.jidb.to.stars.content.ToSkyAndStarsItemLibrary
import org.slf4j.LoggerFactory

object ToSkyAndStarsMod {
    const val MOD_ID = "to_sky_and_stars"
    val logger = LoggerFactory.getLogger(MOD_ID)

    val blocks = ToSkyAndStarsBlockLibrary
    val items = ToSkyAndStarsItemLibrary
    val blockItems = ToSkyAndStarsBlockItemLibrary
    val tabs = ToSkyAndStarsCreativeTabLibrary

    fun init() {
        logger.info("Hello common world from ${MOD_ID}!")
        ToBaseMod.logger.info("Hello ${ToBaseMod.MOD_ID} from ${MOD_ID}!")

        blocks.build()
        items.build()
        blockItems.build()
        tabs.build()
    }
}