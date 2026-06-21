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

@Mixin(ItemStack.class)
abstract class ItemStackMixin {

    @Inject(method = "addDetailsToTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V"))
    public void prependTooltip(Item.TooltipContext context, TooltipDisplay display, @Nullable Player player, TooltipFlag tooltipFlag, Consumer<Component> builder, CallbackInfo ci) {
        TooltipProviderRegistry.INSTANCE.displayTop((ItemStack)(Object)this, context, display, builder, tooltipFlag);
    }

    @Inject(method = "addToTooltip", at = @At(value = "TAIL"))
    public <T extends TooltipProvider> void infixTooltip(DataComponentType<T> type, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag, CallbackInfo ci) {
        TooltipProviderRegistry.INSTANCE.displayAfter(type, (ItemStack)(Object)this, context, display, consumer, flag);
    }

    @WrapOperation(method = "addDetailsToTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;addUnitComponentToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/network/chat/Component;Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/util/function/Consumer;)V"))
    public void infixTooltipUnit(ItemStack instance, DataComponentType<?> dataComponentType, Component component, TooltipDisplay display, Consumer<Component> builder, Operation<Void> original, Item.TooltipContext context, TooltipDisplay display2, @Nullable Player player, TooltipFlag tooltipFlag, Consumer<Component> builder2) {
        if (dataComponentType == DataComponents.INTANGIBLE_PROJECTILE) {
            TooltipProviderRegistry.INSTANCE.displayAfter(DataComponents.ATTRIBUTE_MODIFIERS, (ItemStack)(Object)this, context, display, builder, tooltipFlag);
        }
        original.call(instance, dataComponentType, component, display, builder);
        TooltipProviderRegistry.INSTANCE.displayAfter(dataComponentType, (ItemStack)(Object)this, context, display, builder, tooltipFlag);
    }

    @Inject(method = "addDetailsToTooltip", at = @At(value = "TAIL"))
    public void appendTooltip(Item.TooltipContext context, TooltipDisplay display, @Nullable Player player, TooltipFlag tooltipFlag, Consumer<Component> builder, CallbackInfo ci) {
        TooltipProviderRegistry.INSTANCE.displayBottom((ItemStack)(Object)this, context, display, builder, tooltipFlag);
    }

}