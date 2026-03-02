package net.jidb.to.base.service

import net.jidb.to.base.platform.Platform

abstract class EnvironmentService {
    abstract val platform: Platform
    abstract val environment: Environment
    abstract val context: Context

    abstract fun isModLoaded(modId: String): Boolean

    enum class Context {
        CLIENT, DEDICATED_SERVER
    }

    enum class Environment {
        DEV, BUILD
    }
}