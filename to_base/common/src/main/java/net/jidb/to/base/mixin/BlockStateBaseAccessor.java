package net.jidb.to.base.mixin;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Exposes private fields for block states.
 *
 * @see net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase
 */
@Mixin(BlockBehaviour.BlockStateBase.class)
public interface BlockStateBaseAccessor {

    /**
     * Returns the non-dynamic map colour set for this block state.
     *
     * @return the block state's default map colour.
     * @see net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase#mapColor
     */
    @Accessor("mapColor")
    MapColor to_base$getDefaultMapColor();

}
