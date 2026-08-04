package net.jidb.to.base.client.data.pub.provider.wiki

import net.minecraft.resources.Identifier

/**
 * A rule deciding which wiki articles a mod is expected to have, checked while the articles are baked so that content added without an article is reported rather than quietly left undocumented.
 *
 * @since 0.1.0
 */
interface WikiDataEnforcer {

    /**
     * Works out what this rule expects an article for but has not been given one.
     *
     * @param created The subjects that an article was in fact written for, each as its registry and then its own name.
     * @return The subjects that are missing an article, in the same form.
     * @since 0.1.0
     */
    fun enforce(created: List<Pair<Identifier, Identifier>>): List<Pair<Identifier, Identifier>>

}
