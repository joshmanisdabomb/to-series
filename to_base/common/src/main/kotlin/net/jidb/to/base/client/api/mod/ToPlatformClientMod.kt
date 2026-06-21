package net.jidb.to.base.client.api.mod

interface ToPlatformClientMod {

    val client: IToClientMod

    fun clientInit() = Unit

    fun clientSetup() = Unit

}