package net.jidb.to.base.mod

interface ToPlatformMod {

    val common: IToMod

    fun init() = Unit

    fun setup() = Unit

}