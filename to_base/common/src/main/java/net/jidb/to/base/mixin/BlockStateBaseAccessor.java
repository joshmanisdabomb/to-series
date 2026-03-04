package net.jidb.to.base.mixin;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockBehaviour.BlockStateBase.class)
public interface BlockStateBaseAccessor {

    @Accessor(value = "mapColor")
    MapColor to_base$getDefaultMapColor();

}