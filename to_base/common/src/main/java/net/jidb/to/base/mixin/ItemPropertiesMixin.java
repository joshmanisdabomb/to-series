package net.jidb.to.base.mixin;

import net.jidb.to.base.data.api.ToDataItemHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentInitializers;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.Properties.class)
public abstract class ItemPropertiesMixin {

    @Inject(method = "component", at = @At("HEAD"))
    public <T> void component(DataComponentType<T> type, T value, CallbackInfoReturnable<Item.Properties> info) {
        ToDataItemHelper.INSTANCE.addComponent((Item.Properties)(Object)this, type, value);
    }

    @Inject(method = "delayedComponent", at = @At("HEAD"))
    public <T> void delayedComponent(DataComponentType<T> type, DataComponentInitializers.SingleComponentInitializer<T> value, CallbackInfoReturnable<Item.Properties> info) {
        ToDataItemHelper.INSTANCE.addDelayedComponent((Item.Properties)(Object)this, type, value);
    }

    @Inject(method = "delayedHolderComponent", at = @At("HEAD"))
    public <T> void delayedHolderComponent(DataComponentType<Holder<T>> type, ResourceKey<T> value, CallbackInfoReturnable<Item.Properties> info) {
        ToDataItemHelper.INSTANCE.addHolderComponent((Item.Properties)(Object)this, type, value);
    }

}
