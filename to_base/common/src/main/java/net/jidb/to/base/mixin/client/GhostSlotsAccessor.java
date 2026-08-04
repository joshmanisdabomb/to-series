package net.jidb.to.base.mixin.client;

import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Exposes functions in recipe ghost slots for modded screens to preview ingredients.
 *
 * @see net.minecraft.client.gui.screens.recipebook.GhostSlots
 */
@Mixin(GhostSlots.class)
public interface GhostSlotsAccessor {

    /**
     * Sets the display contents to show on a slot.
     *
     * @param slot the slot to draw the preview in.
     * @param context the context used to resolve the display into item stacks.
     * @param contents the recipe display describing what to preview.
     * @see net.minecraft.client.gui.screens.recipebook.GhostSlots#setInput
     */
    @Invoker("setInput")
    void to_base$setInput(Slot slot, ContextMap context, SlotDisplay contents);

}
