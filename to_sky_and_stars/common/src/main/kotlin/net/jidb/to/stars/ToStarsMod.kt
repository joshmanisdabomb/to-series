package net.jidb.to.stars

import net.jidb.to.base.pub.mod.ToMod
import net.jidb.to.stars.content.ToStarsAdvancementTriggerLibrary
import net.jidb.to.stars.content.ToStarsBiomeModLibrary
import net.jidb.to.stars.content.ToStarsBlockEntityLibrary
import net.jidb.to.stars.content.ToStarsBlockItemLibrary
import net.jidb.to.stars.content.ToStarsBlockLibrary
import net.jidb.to.stars.content.ToStarsBlockNetworkLibrary
import net.jidb.to.stars.content.ToStarsBlockTagLibrary
import net.jidb.to.stars.content.ToStarsCreativeTabLibrary
import net.jidb.to.stars.content.ToStarsEntityLibrary
import net.jidb.to.stars.content.ToStarsEventHandlerLibrary
import net.jidb.to.stars.content.ToStarsGameTestLibrary
import net.jidb.to.stars.content.ToStarsItemComponentLibrary
import net.jidb.to.stars.content.ToStarsItemLibrary
import net.jidb.to.stars.content.ToStarsItemTagLibrary
import net.jidb.to.stars.content.ToStarsLootTableLibrary
import net.jidb.to.stars.content.ToStarsMenuLibrary
import net.jidb.to.stars.content.ToStarsMusicLibrary
import net.jidb.to.stars.content.ToStarsParticleLibrary
import net.jidb.to.stars.content.ToStarsPayloadHandlerLibrary
import net.jidb.to.stars.content.ToStarsPayloadLibrary
import net.jidb.to.stars.content.ToStarsRecipeCategoryLibrary
import net.jidb.to.stars.content.ToStarsRecipeDisplayLibrary
import net.jidb.to.stars.content.ToStarsRecipeLibrary
import net.jidb.to.stars.content.ToStarsRecipeSerializerLibrary
import net.jidb.to.stars.content.ToStarsSavedDataLibrary
import net.jidb.to.stars.content.ToStarsSoundLibrary
import net.jidb.to.stars.content.ToStarsTicketLibrary
import net.jidb.to.stars.content.key.ToStarsConfiguredFeatureLibrary
import net.jidb.to.stars.content.key.ToStarsDamageTypeLibrary
import net.jidb.to.stars.content.key.ToStarsPlacedFeatureLibrary

/**
 * [ToMod] implementation for To Sky and Stars, which names every library its content is registered through.
 *
 * See [net.jidb.to.stars.client.ToStarsClientMod] for the libraries that only exist on the client side.
 */
object ToStarsMod : ToMod() {

    /**
     * The mod ID this mod's content is registered under.
     */
    const val MOD_ID = "to_sky_and_stars"
    override val modid = MOD_ID

    override val blocks = ToStarsBlockLibrary
    override val items = ToStarsItemLibrary
    override val blockItems = ToStarsBlockItemLibrary
    override val tabs = ToStarsCreativeTabLibrary
    override val itemComponents = ToStarsItemComponentLibrary

    override val blockEntities = ToStarsBlockEntityLibrary
    override val menus = ToStarsMenuLibrary

    override val entities = ToStarsEntityLibrary

    override val payloads = ToStarsPayloadLibrary
    override val payloadHandlers = ToStarsPayloadHandlerLibrary

    override val blockTags = ToStarsBlockTagLibrary
    override val itemTags = ToStarsItemTagLibrary

    override val particles = ToStarsParticleLibrary
    override val sounds = ToStarsSoundLibrary

    override val recipeTypes = ToStarsRecipeLibrary
    override val recipeSerializers = ToStarsRecipeSerializerLibrary
    override val recipeCategories = ToStarsRecipeCategoryLibrary
    override val recipeDisplays = ToStarsRecipeDisplayLibrary

    override val eventHandlers = ToStarsEventHandlerLibrary
    override val advancementTriggers = ToStarsAdvancementTriggerLibrary
    override val tickets = ToStarsTicketLibrary
    override val savedData = ToStarsSavedDataLibrary
    override val blockNetworks = ToStarsBlockNetworkLibrary
    override val gameTests = ToStarsGameTestLibrary

    override val configuredFeatures = ToStarsConfiguredFeatureLibrary
    override val placedFeatures = ToStarsPlacedFeatureLibrary
    override val biomeMods = ToStarsBiomeModLibrary
    override val damageTypes = ToStarsDamageTypeLibrary
    override val lootTables = ToStarsLootTableLibrary
    override val music = ToStarsMusicLibrary

}
