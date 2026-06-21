package net.jidb.to.base.api.mod

import org.slf4j.Logger

interface IToMod {
    val modid: String
    val logger: Logger

    val initialised: Boolean
    val complete: Boolean

    fun init() = Unit

    fun setup() = Unit
}