package net.jidb.to.base.mixin;

import net.jidb.to.base.pub.item.DefaultItemComponentRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentInitializers;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin that records the data components declared on item properties as they are added.
 * This is used by {@link net.jidb.to.base.pub.item.DefaultItemComponentRegistry} to get the default components of any item.
 *
 * @see net.minecraft.world.item.Item.Properties
 */
@Mixin(Item.Properties.class)
public abstract class ItemPropertiesMixin {

    /**
     * Records a component supplied as a plain value.
     *
     * @param <T> the component's value type.
     * @param type the component being set.
     * @param value the value the component is set to.
     * @param info the mixin callback info.
     * @see net.minecraft.world.item.Item.Properties#component
     */
    @Inject(method = "component", at = @At("HEAD"))
    public <T> void component(DataComponentType<T> type, T value, CallbackInfoReturnable<Item.Properties> info) {
        DefaultItemComponentRegistry.INSTANCE.addComponent((Item.Properties)(Object)this, type, value);
    }

    /**
     * Records a component supplied with a lazily resolved value.
     *
     * @param <T> the component's value type.
     * @param type the component being set.
     * @param value the initialiser that produces the component's value.
     * @param info the mixin callback info.
     * @see net.minecraft.world.item.Item.Properties#delayedComponent
     */
    @Inject(method = "delayedComponent", at = @At("HEAD"))
    public <T> void delayedComponent(DataComponentType<T> type, DataComponentInitializers.SingleComponentInitializer<T> value, CallbackInfoReturnable<Item.Properties> info) {
        DefaultItemComponentRegistry.INSTANCE.addDelayedComponent((Item.Properties)(Object)this, type, value);
    }

    /**
     * Records a component supplied with a registry holder resource key value.
     *
     * @param <T> the type the holder points at.
     * @param type the component being set.
     * @param value the resource key the holder is resolved from.
     * @param info the mixin callback info.
     * @see net.minecraft.world.item.Item.Properties#delayedHolderComponent
     */
    @Inject(method = "delayedHolderComponent", at = @At("HEAD"))
    public <T> void delayedHolderComponent(DataComponentType<Holder<T>> type, ResourceKey<T> value, CallbackInfoReturnable<Item.Properties> info) {
        DefaultItemComponentRegistry.INSTANCE.addHolderComponent((Item.Properties)(Object)this, type, value);
    }

}
