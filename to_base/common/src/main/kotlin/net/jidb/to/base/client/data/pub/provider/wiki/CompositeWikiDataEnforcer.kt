package net.jidb.to.base.client.data.pub.provider.wiki

import net.minecraft.resources.Identifier

/**
 * A [WikiDataEnforcer] applying several rules at once, reporting whatever any of them finds missing.
 *
 * @property enforcers The rules to apply.
 * @since 0.8.0
 */
class CompositeWikiDataEnforcer(vararg val enforcers: WikiDataEnforcer) : WikiDataEnforcer {

    override fun enforce(created: List<Pair<Identifier, Identifier>>) = enforcers.flatMap { it.enforce(created) }.distinct()

}
