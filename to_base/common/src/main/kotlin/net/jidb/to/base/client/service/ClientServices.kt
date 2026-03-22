package net.jidb.to.base.client.service

import net.jidb.to.base.service.Services

object ClientServices {

    val platform = this(ClientPlatformService::class.java)

    operator fun <T> invoke(clazz: Class<T>) = Services.load(clazz)

}