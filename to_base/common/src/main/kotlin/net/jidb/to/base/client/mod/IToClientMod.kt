package net.jidb.to.base.client.mod

import net.jidb.to.base.mod.IToMod

interface IToClientMod {
    val common: IToMod

    val initialised: Boolean
    val complete: Boolean

    fun clientInit() = Unit

    fun clientSetup() = Unit
}