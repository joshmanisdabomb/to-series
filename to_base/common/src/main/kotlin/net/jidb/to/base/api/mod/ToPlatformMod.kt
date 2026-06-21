package net.jidb.to.base.api.mod

interface ToPlatformMod {

    val common: IToMod

    fun init() = Unit

    fun setup() = Unit

}