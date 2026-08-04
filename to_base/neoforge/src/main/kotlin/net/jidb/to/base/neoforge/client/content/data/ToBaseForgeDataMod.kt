package net.jidb.to.base.neoforge.client.content.data

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.data.pub.provider.wiki.CompositeWikiDataEnforcer
import net.jidb.to.base.client.data.pub.provider.wiki.ModWikiDataEnforcer
import net.jidb.to.base.client.data.pub.provider.wiki.RegistryWikiDataEnforcer
import net.jidb.to.base.content.ToBaseGameTestLibrary
import net.jidb.to.base.neoforge.client.content.data.provider.ToBaseLanguageDataProvider
import net.jidb.to.base.neoforge.client.data.mod.ToForgeDataMod
import net.jidb.to.base.neoforge.content.data.provider.ToBaseItemTagDataProvider
import net.minecraft.core.registries.BuiltInRegistries
import net.neoforged.neoforge.data.event.GatherDataEvent

/**
 * [ToForgeDataMod] implementation for the base mod itself, i.e. what its data generation run goes through.
 *
 * Every registry entry it adds is expected to have a wiki article, other than a creative tab, which is not something a player would look up.
 *
 * @param event The event the providers are registered against.
 * @since 0.3.0
 */
class ToBaseForgeDataMod(event: GatherDataEvent.Client) : ToForgeDataMod(event) {

    override val collections = ToBaseDataLibrary

    override val language = ::ToBaseLanguageDataProvider
    override val tags = listOf(::ToBaseItemTagDataProvider)
    override val gameTests = ToBaseGameTestLibrary
    override val wiki = listOf(CompositeWikiDataEnforcer(RegistryWikiDataEnforcer(ToBaseMod.modid, {
        it.registry() == BuiltInRegistries.CREATIVE_MODE_TAB.key().identifier() || it.registry() == BuiltInRegistries.TEST_FUNCTION.key().identifier()
    }), ModWikiDataEnforcer(ToBaseMod.modid)))

}
