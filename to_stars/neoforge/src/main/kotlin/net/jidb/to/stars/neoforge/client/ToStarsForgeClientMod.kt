package net.jidb.to.stars.neoforge.client

import net.jidb.to.base.neoforge.client.mod.ToForgeClientMod
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.ToStarsClientMod
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent

@EventBusSubscriber(value = [Dist.CLIENT], modid = ToStarsMod.MOD_ID)
object ToStarsForgeClientMod : ToForgeClientMod() {

    override val client get() = ToStarsClientMod

    @SubscribeEvent
    override fun subscribeStub(event: FMLConstructModEvent) = Unit

}
