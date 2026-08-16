package net.jidb.to.base.client.data.pub.provider.wiki

import net.minecraft.resources.Identifier

interface WikiDataEnforcer {

    fun enforce(created: List<Pair<Identifier, Identifier>>): List<Pair<Identifier, Identifier>>

}
