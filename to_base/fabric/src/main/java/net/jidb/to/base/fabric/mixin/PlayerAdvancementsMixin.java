package net.jidb.to.base.fabric.mixin;

import net.jidb.to.base.ToBaseMod;
import net.jidb.to.base.pub.event.advancements.AdvancementEventContext;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin that fires the advancement grant post event.
 * Fabric seemingly has no pre-made equivalent to the Neoforge advancement hook.
 *
 * @see net.minecraft.server.PlayerAdvancements
 */
@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementsMixin {

    /**
     * The player these advancements belong to, shadowed so the event can carry it.
     *
     * @see net.minecraft.server.PlayerAdvancements#player
     */
    @Shadow
    private ServerPlayer player;

    /**
     * Dispatches the post-grant event once the advancement has been awarded.
     *
     * @param advancement the advancement that was awarded.
     * @param criterionKey the criterion that completed the advancement.
     * @param info the mixin callback info.
     * @see net.minecraft.server.PlayerAdvancements#award
     */
    @Inject(method = "award", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", shift = At.Shift.AFTER))
    public void onAdvancementEarnEvent(AdvancementHolder advancement, String criterionKey, CallbackInfoReturnable<Boolean> info) {
        ToBaseMod.INSTANCE.getEvents().getAdvancement_grant_post().call(new AdvancementEventContext(player, advancement));
    }

}
