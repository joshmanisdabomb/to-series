package net.jidb.to.stars.neoforge

import net.jidb.to.base.neoforge.mod.ToForgeMod
import net.jidb.to.stars.ToStarsMod
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent

@Mod(ToStarsMod.MOD_ID)
@EventBusSubscriber
object ToStarsForgeMod : ToForgeMod() {
    override val common get() = ToStarsMod

    @SubscribeEvent
    override fun subscribeStub(event: FMLConstructModEvent) = Unit
}
