package net.jidb.to.base.mod

import org.slf4j.Logger

interface IToMod {
    val modid: String
    val logger: Logger

    fun init() = Unit

    fun setup() = Unit
}