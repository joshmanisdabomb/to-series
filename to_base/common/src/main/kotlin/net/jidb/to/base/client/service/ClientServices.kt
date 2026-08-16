package net.jidb.to.base.client.service

import net.jidb.to.base.service.Services

object ClientServices {

    private val _platform = this(ClientPlatformService::class.java)
    val platform get() = _platform.platform

    operator fun <T> invoke(clazz: Class<T>) = Services.load(clazz)

}
