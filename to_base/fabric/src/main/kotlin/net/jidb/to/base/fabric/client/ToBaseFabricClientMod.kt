package net.jidb.to.base.fabric.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.resource.v1.ResourceLoader
import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.ToBaseClientMod
import net.jidb.to.base.client.wiki.WikiArticleManager
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.PackType

object ToBaseFabricClientMod : ClientModInitializer {

    override fun onInitializeClient() {
        ToBaseClientMod.init()

        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(Identifier.fromNamespaceAndPath(ToBaseMod.MOD_ID, "wiki_articles"), WikiArticleManager)
    }

}
