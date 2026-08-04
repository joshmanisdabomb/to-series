package net.jidb.to.base.mixin.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Exposes private fields for GUI graphics.
 * Used by {@link net.jidb.to.base.client.pub.gui.ScaledTrackingItemStackRenderState} to submit a draw call directly.
 *
 * @see net.minecraft.client.gui.GuiGraphicsExtractor
 */
@Mixin(GuiGraphicsExtractor.class)
public interface GuiGraphicsAccessor {

    /**
     * Returns the render state that GUI draw calls are written to.
     *
     * @return the GUI render state.
     * @see net.minecraft.client.gui.GuiGraphicsExtractor#guiRenderState
     */
    @Accessor("guiRenderState")
    GuiRenderState to_base$getGuiRenderState();

}
