package net.jidb.to.base.client.data.pub.provider.wiki

import net.jidb.to.base.service.Services
import net.minecraft.resources.Identifier

/**
 * A [WikiDataEnforcer] expecting an article for the version of a mod that is currently being built, so that a release cannot go out without its changelog.
 *
 * @property modid The mod ID whose current version is expected to have an article.
 * @since 0.8.0
 */
class ModWikiDataEnforcer(val modid: String) : WikiDataEnforcer {

    override fun enforce(created: List<Pair<Identifier, Identifier>>) = listOf(
        Identifier.fromNamespaceAndPath("to_base", "mod_version") to Identifier.fromNamespaceAndPath(modid, Services.environment.getModVersion(modid)!!)
    ).filter { !created.contains(it) }

}
