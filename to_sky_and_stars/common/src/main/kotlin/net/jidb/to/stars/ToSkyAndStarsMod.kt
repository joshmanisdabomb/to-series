package net.jidb.to.stars

import net.jidb.to.base.ToBaseMod
import org.slf4j.LoggerFactory

object ToSkyAndStarsMod {
    const val mod_id = "to_sky_and_stars"
    val logger = LoggerFactory.getLogger(mod_id)

    fun init() {
        logger.info("Hello common world from ${mod_id}!")
        ToBaseMod.logger.info("Hello ${ToBaseMod.mod_id} from ${mod_id}!")
    }
}