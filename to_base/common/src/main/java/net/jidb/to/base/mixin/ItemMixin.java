package net.jidb.to.base.mixin;

import net.jidb.to.base.data.api.ToDataItemHelper;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin that records the construction of every item along with its properties.
 * This is used by {@link net.jidb.to.base.data.api.ToDataItemHelper} to get the default components of any item.
 *
 * @see net.minecraft.world.item.Item
 */
@Mixin(Item.class)
public abstract class ItemMixin {

    /**
     * Registers the item being constructed with the data item helper.
     *
     * @param properties the properties the item was built from.
     * @param info the mixin callback info.
     *
     * @see net.minecraft.world.item.Item#Item
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    public void construct(Item.Properties properties, CallbackInfo info) {
        ToDataItemHelper.INSTANCE.add((Item)(Object)this, properties);
    }

}
