package net.jidb.to.base.neoforge.client

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.ToBaseClientMod
import net.jidb.to.base.client.wiki.WikiArticleManager
import net.jidb.to.base.neoforge.client.platform.ScreenForgeClientPlatformModule
import net.minecraft.resources.Identifier
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent

@EventBusSubscriber(value = [Dist.CLIENT], modid = ToBaseMod.MOD_ID)
object ToBaseForgeClientMod {

    init {
        ToBaseClientMod.init()
    }

    @SubscribeEvent
    fun onRegisterScreens(event: RegisterMenuScreensEvent) {
        ScreenForgeClientPlatformModule.listener(ToBaseMod.MOD_ID, event)
    }

    @SubscribeEvent
    fun resourceReloadListeners(event: AddClientReloadListenersEvent) {
        event.addListener(Identifier.fromNamespaceAndPath(ToBaseMod.MOD_ID, "wiki_articles"), WikiArticleManager)
    }

}