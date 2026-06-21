package net.jidb.to.base.mixin;

import net.jidb.to.base.data.api.ToDataItemHelper;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void construct(Item.Properties properties, CallbackInfo info) {
        ToDataItemHelper.INSTANCE.add((Item)(Object)this, properties);
    }

}