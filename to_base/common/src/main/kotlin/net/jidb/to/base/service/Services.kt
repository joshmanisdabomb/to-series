package net.jidb.to.base.service

import java.util.*

object Services {
    val environment = this(EnvironmentService::class.java)
    val register = this(RegisterService::class.java)
    val platform get() = environment.platform
    val side get() = environment.side

    operator fun <T> invoke(clazz: Class<T>) = load(clazz)

    fun <T> load(clazz: Class<T>) = ServiceLoader.load(clazz).findFirst()
        .orElseThrow { IllegalStateException("Failed to load service for ${clazz.name}.") }
}