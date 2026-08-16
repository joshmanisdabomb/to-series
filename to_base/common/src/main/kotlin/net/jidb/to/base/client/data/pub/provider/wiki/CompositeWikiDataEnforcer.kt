package net.jidb.to.base.client.data.pub.provider.wiki

import net.minecraft.resources.Identifier

class CompositeWikiDataEnforcer(vararg val enforcers: WikiDataEnforcer) : WikiDataEnforcer {

    override fun enforce(created: List<Pair<Identifier, Identifier>>) = enforcers.flatMap { it.enforce(created) }.distinct()

}
