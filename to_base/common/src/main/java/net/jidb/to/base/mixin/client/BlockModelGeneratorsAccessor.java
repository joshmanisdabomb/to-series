package net.jidb.to.base.mixin.client;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Consumer;

@Mixin(BlockModelGenerators.class)
public interface BlockModelGeneratorsAccessor {
    @Accessor("blockStateOutput")
    Consumer<BlockModelDefinitionGenerator> to_base$getBlockStateOutput();

    @Invoker("createHorizontallyRotatedBlock")
    void to_base$createHorizontallyRotatedBlock(Block horizontallyRotatedBlock, TexturedModel.Provider provider);
}