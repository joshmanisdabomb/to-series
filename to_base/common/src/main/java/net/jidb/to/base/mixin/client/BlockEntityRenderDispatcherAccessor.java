package net.jidb.to.base.mixin.client;

import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Exposes private fields held by the block entity render dispatcher.
 * Primarily used to access the block model resolver from {@link net.minecraft.client.Minecraft#blockEntityRenderDispatcher}
 *
 * @see net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher
 */
@Mixin(BlockEntityRenderDispatcher.class)
public interface BlockEntityRenderDispatcherAccessor {

    /**
     * Returns the resolver used to turn block states into baked models.
     *
     * @return the dispatcher's block model resolver.
     */
    @Accessor("blockModelResolver")
    BlockModelResolver to_base$getBlockModelResolver();

}
