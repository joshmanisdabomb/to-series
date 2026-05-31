package net.jidb.to.base.fabric.mixin;

import net.jidb.to.base.ToBaseMod;
import net.jidb.to.base.hooks.event.advancements.AdvancementEventContext;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true, print = true)
@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin {

    @Shadow
    private ServerPlayer player;

    @Inject(method = "award", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", shift = At.Shift.AFTER))
    public void onAdvancementEarnEvent(AdvancementHolder advancement, String criterionKey, CallbackInfoReturnable<Boolean> info) {
        ToBaseMod.INSTANCE.getEvents().getAdvancement_grant_post().call(new AdvancementEventContext(player, advancement));
    }

}
