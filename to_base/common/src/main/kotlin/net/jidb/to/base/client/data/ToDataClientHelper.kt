package net.jidb.to.base.client.data

import net.jidb.to.base.client.service.ClientServices

object ToDataClientHelper {

    val NUMERIC_TEXTURES = List(16) { ClientServices.platform.data.createTextureSlot(it.toString()) }

}