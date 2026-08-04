package net.jidb.to.base.client.data.pub.provider.wiki

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

/**
 * A [WikiDataEnforcer] expecting an article for every registry entry that answers a test, which is how a mod holds itself to documenting all of its own content.
 *
 * @property check Whether a registry entry is expected to have an article.
 * @since 0.1.0
 */
class RegistryWikiDataEnforcer(val check: (ResourceKey<*>) -> Boolean) : WikiDataEnforcer {

    /**
     * Creates a rule expecting an article for everything a mod registered, other than what a test excuses.
     *
     * @param modid The mod ID whose content is expected to have articles.
     * @param except Whether a registry entry is excused from needing one.
     * @since 0.1.0
     */
    constructor(modid: String, except: (ResourceKey<*>) -> Boolean) : this({ it.identifier().namespace == modid && !except(it) })

    /**
     * Creates a rule expecting an article for everything a mod registered, other than the entries named.
     *
     * @param modid The mod ID whose content is expected to have articles.
     * @param except The registry entries excused from needing one.
     * @since 0.1.0
     */
    constructor(modid: String, except: List<ResourceKey<*>>) : this(modid, { except.contains(it) })

    /**
     * Creates a rule expecting an article for everything a mod registered, with nothing excused.
     *
     * @param modid The mod ID whose content is expected to have articles.
     * @since 0.1.0
     */
    constructor(modid: String) : this(modid, { false })

    override fun enforce(created: List<Pair<Identifier, Identifier>>) = BuiltInRegistries.REGISTRY.flatMap { it.registryKeySet().filter { check(it) }.map { it.registry() to it.identifier() }.filter { !created.contains(it) } }

}
