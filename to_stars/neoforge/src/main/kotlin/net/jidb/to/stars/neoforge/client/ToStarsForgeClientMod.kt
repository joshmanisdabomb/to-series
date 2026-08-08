package net.jidb.to.stars.neoforge.client

import net.jidb.to.base.neoforge.client.mod.ToForgeClientMod
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.ToStarsClientMod
import net.jidb.to.stars.neoforge.client.data.ToStarsForgeDataMod
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent

/**
 * [ToForgeClientMod] implementation for the content mod, i.e. the client-side entry point Neoforge loads it through.
 */
@EventBusSubscriber(value = [Dist.CLIENT], modid = ToStarsMod.MOD_ID)
object ToStarsForgeClientMod : ToForgeClientMod() {

    override val client get() = ToStarsClientMod
    override val data = ::ToStarsForgeDataMod

    @SubscribeEvent
    override fun subscribeStub(event: FMLConstructModEvent) = Unit

}
