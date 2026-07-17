package net.jidb.to.base.fabric.mixin.client;

import net.jidb.to.base.client.ToBaseClientMod;
import net.jidb.to.base.pub.event.entity.EntityEventContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(Level.class)
public class LevelMixin {

    @Inject(method = "guardEntityTick", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
    public void onLevelEvent(Consumer<Entity> tick, Entity entity, CallbackInfo ci) {
        ToBaseClientMod.INSTANCE.getEvents().getClient_entity_tick_pre().call(new EntityEventContext(entity));
    }

}
