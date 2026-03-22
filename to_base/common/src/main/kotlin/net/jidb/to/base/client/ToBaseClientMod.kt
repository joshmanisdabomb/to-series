package net.jidb.to.base.client

import net.jidb.to.base.client.content.ToBaseScreenLibrary

object ToBaseClientMod {

    val screens = ToBaseScreenLibrary

    fun init() {
        screens.build()
    }

}
