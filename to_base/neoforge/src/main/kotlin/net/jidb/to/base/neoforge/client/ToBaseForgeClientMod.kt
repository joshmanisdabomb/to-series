package net.jidb.to.base.neoforge.client

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.ToBaseClientMod
import net.jidb.to.base.neoforge.client.content.data.ToBaseForgeDataMod
import net.jidb.to.base.neoforge.client.event.ToBaseForgeClientEventHandler
import net.jidb.to.base.neoforge.client.mod.ToForgeClientMod
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent

@EventBusSubscriber(value = [Dist.CLIENT], modid = ToBaseMod.MOD_ID)
object ToBaseForgeClientMod : ToForgeClientMod() {

    override val client get() = ToBaseClientMod
    override val data = ::ToBaseForgeDataMod
    override val eventHandler get() = ToBaseForgeClientEventHandler

    @SubscribeEvent
    override fun subscribeStub(event: FMLConstructModEvent) = Unit

}