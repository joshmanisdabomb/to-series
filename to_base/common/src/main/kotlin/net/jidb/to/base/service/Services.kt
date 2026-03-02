package net.jidb.to.base.service

import java.util.ServiceLoader

object Services {
    val environment = this(EnvironmentService::class.java)
    val register = this(RegisterService::class.java)

    operator fun <T> invoke(clazz: Class<T>): T = ServiceLoader.load(clazz).findFirst()
        .orElseThrow { IllegalStateException("Failed to load service for ${clazz.name}.") }
}