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

/**
 * Mixin that fires the client-side entity tick event for each entity in the level.
 * Fabric seemingly has no pre-made equivalent to the Neoforge entity tick hook.
 *
 * @see net.minecraft.world.level.Level
 */
@Mixin(Level.class)
public class LevelMixin {

    /**
     * Dispatches the pre-tick event just before the entity is ticked.
     *
     * @param tick the tick action about to be applied to the entity.
     * @param entity the entity being ticked.
     * @param ci the mixin callback info.
     * @see net.minecraft.world.level.Level#guardEntityTick
     */
    @Inject(method = "guardEntityTick", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
    public void onLevelEvent(Consumer<Entity> tick, Entity entity, CallbackInfo ci) {
        if (((Level)(Object)this).isClientSide()) {
            ToBaseClientMod.INSTANCE.getEvents().getClient_entity_tick_pre().call(new EntityEventContext(entity));
        }
    }

}
