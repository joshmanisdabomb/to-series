package net.jidb.to.base.client.mod

interface ToPlatformClientMod {

    val client: IToClientMod

    fun clientInit() = Unit

    fun clientSetup() = Unit

}