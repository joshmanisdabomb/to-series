package net.jidb.to.base.client.data.pub.provider.wiki

import net.jidb.to.base.service.Services
import net.minecraft.resources.Identifier

class ModWikiDataEnforcer(val modid: String) : WikiDataEnforcer {

    override fun enforce(created: List<Pair<Identifier, Identifier>>) = listOf(
        Identifier.fromNamespaceAndPath("to_base", "mod_version") to Identifier.fromNamespaceAndPath(modid, Services.environment.getModVersion(modid)!!)
    ).filter { !created.contains(it) }

}
