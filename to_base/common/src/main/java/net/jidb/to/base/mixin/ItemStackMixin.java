package net.jidb.to.base.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.jidb.to.base.pub.item.TooltipProviderRegistry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

/**
 * Mixin that injects the {@link net.jidb.to.base.pub.item.TooltipProviderRegistry} into tooltip functions.
 *
 * @see net.minecraft.world.item.ItemStack
 */
@Mixin(ItemStack.class)
abstract class ItemStackMixin {

    /**
     * Runs {@link net.jidb.to.base.pub.item.TooltipProviderRegistry#displayTop} to show custom data component tooltips before the item's tooltip text.
     *
     * @param context the tooltip context the stack is being rendered in.
     * @param display the stack's tooltip display component.
     * @param player the player viewing the tooltip.
     * @param tooltipFlag whether advanced tooltips (F3+H) are enabled.
     * @param builder receives the tooltip lines.
     * @param ci the mixin callback info.
     * @see net.minecraft.world.item.ItemStack#addDetailsToTooltip
     */
    @Inject(method = "addDetailsToTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V"))
    public void prependTooltip(Item.TooltipContext context, TooltipDisplay display, @Nullable Player player, TooltipFlag tooltipFlag, Consumer<Component> builder, CallbackInfo ci) {
        TooltipProviderRegistry.INSTANCE.displayTop((ItemStack)(Object)this, context, display, builder, tooltipFlag);
    }

    /**
     * Runs {@link net.jidb.to.base.pub.item.TooltipProviderRegistry#displayAfter} to show custom data component tooltips after another data component's tooltip text.
     *
     * @param <T> the component's value type, which supplies its tooltip lines.
     * @param type the component whose lines have just been written.
     * @param context the tooltip context the stack is being rendered in.
     * @param display the stack's tooltip display component.
     * @param consumer receives the tooltip lines.
     * @param flag whether advanced tooltips (F3+H) are enabled.
     * @param ci the mixin callback info.
     * @see net.minecraft.world.item.ItemStack#addToTooltip
     */
    @Inject(method = "addToTooltip", at = @At("TAIL"))
    public <T extends TooltipProvider> void infixTooltip(DataComponentType<T> type, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag, CallbackInfo ci) {
        TooltipProviderRegistry.INSTANCE.displayAfter(type, (ItemStack)(Object)this, context, display, consumer, flag);
    }

    /**
     * Runs {@link net.jidb.to.base.pub.item.TooltipProviderRegistry#displayAfter} to show custom data component tooltips after components that render as simple labels.
     *
     * @param instance the stack the wrapped call was made on.
     * @param dataComponentType the component whose label is being written.
     * @param component the label itself.
     * @param display the stack's tooltip display component.
     * @param builder receives the tooltip lines.
     * @param original the wrapped vanilla call.
     * @param context the tooltip context the stack is being rendered in.
     * @param display2 the tooltip display component, as passed to the enclosing method.
     * @param player the player viewing the tooltip.
     * @param tooltipFlag whether advanced tooltips (F3+H) are enabled.
     * @param builder2 the line consumer, as passed to the enclosing method.
     * @see net.minecraft.world.item.ItemStack#addDetailsToTooltip
     */
    @WrapOperation(method = "addDetailsToTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;addUnitComponentToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/network/chat/Component;Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/util/function/Consumer;)V"))
    public void infixTooltipUnit(ItemStack instance, DataComponentType<?> dataComponentType, Component component, TooltipDisplay display, Consumer<Component> builder, Operation<Void> original, Item.TooltipContext context, TooltipDisplay display2, @Nullable Player player, TooltipFlag tooltipFlag, Consumer<Component> builder2) {
        //This is here so there doesn't need to create another mixin for ItemStack.addAttributeTooltips.
        if (dataComponentType == DataComponents.INTANGIBLE_PROJECTILE) {
            TooltipProviderRegistry.INSTANCE.displayAfter(DataComponents.ATTRIBUTE_MODIFIERS, (ItemStack)(Object)this, context, display, builder, tooltipFlag);
        }

        original.call(instance, dataComponentType, component, display, builder);
        TooltipProviderRegistry.INSTANCE.displayAfter(dataComponentType, (ItemStack)(Object)this, context, display, builder, tooltipFlag);
    }

    /**
     * Runs {@link net.jidb.to.base.pub.item.TooltipProviderRegistry#displayBottom} to show custom data component tooltips after the item's tooltip text.
     *
     * @param context the tooltip context the stack is being rendered in.
     * @param display the stack's tooltip display component.
     * @param player the player viewing the tooltip.
     * @param tooltipFlag whether advanced tooltips (F3+H) are enabled.
     * @param builder receives the tooltip lines.
     * @param ci the mixin callback info.
     * @see net.minecraft.world.item.ItemStack#addDetailsToTooltip
     */
    @Inject(method = "addDetailsToTooltip", at = @At("TAIL"))
    public void appendTooltip(Item.TooltipContext context, TooltipDisplay display, @Nullable Player player, TooltipFlag tooltipFlag, Consumer<Component> builder, CallbackInfo ci) {
        TooltipProviderRegistry.INSTANCE.displayBottom((ItemStack)(Object)this, context, display, builder, tooltipFlag);
    }

}
