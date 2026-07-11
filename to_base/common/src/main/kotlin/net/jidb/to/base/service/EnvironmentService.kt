package net.jidb.to.base.service

import net.jidb.to.base.api.platform.Platform
import net.jidb.to.base.api.side.Side

abstract class EnvironmentService {
    abstract val platform: Platform
    abstract val environment: Environment
    abstract val side: Side

    abstract fun isModLoaded(modId: String): Boolean
    abstract fun getModVersion(modId: String): String?

    enum class Environment {
        DEV, BUILD
    }
}