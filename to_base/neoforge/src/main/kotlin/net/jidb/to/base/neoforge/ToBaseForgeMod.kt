package net.jidb.to.base.neoforge

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.neoforge.event.ToBaseForgeGameEventHandler
import net.jidb.to.base.neoforge.event.ToBaseForgeModEventHandler
import net.jidb.to.base.neoforge.mod.ToForgeMod
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent

@Mod(ToBaseMod.MOD_ID)
@EventBusSubscriber
object ToBaseForgeMod : ToForgeMod() {
    override val common get() = ToBaseMod
    override val gameEvents get() = ToBaseForgeGameEventHandler
    override val modEvents get() = ToBaseForgeModEventHandler

    @SubscribeEvent
    override fun subscribeStub(event: FMLConstructModEvent) = Unit
}
