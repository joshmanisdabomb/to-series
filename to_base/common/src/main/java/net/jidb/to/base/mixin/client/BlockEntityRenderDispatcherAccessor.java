package net.jidb.to.base.mixin.client;

import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockEntityRenderDispatcher.class)
public interface BlockEntityRenderDispatcherAccessor {

    @Accessor("blockModelResolver")
    BlockModelResolver to_base$getBlockModelResolver();

}