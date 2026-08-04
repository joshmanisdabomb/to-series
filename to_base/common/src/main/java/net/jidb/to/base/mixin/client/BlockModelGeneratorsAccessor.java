package net.jidb.to.base.mixin.client;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Consumer;

/**
 * Exposes the output consumer and private model helpers of Minecraft's block model generator.
 * This lets the To Lay the Foundations common source set use them.
 *
 * @see net.minecraft.client.data.models.BlockModelGenerators
 */
@Mixin(BlockModelGenerators.class)
public interface BlockModelGeneratorsAccessor {

    /**
     * Returns the sink that finished block state definitions are handed to.
     *
     * @return the block state output consumer.
     * @see net.minecraft.client.data.models.BlockModelGenerators#blockStateOutput
     */
    @Accessor("blockStateOutput")
    Consumer<BlockModelDefinitionGenerator> to_base$getBlockStateOutput();

    /**
     * Generates a plain cube model that takes its texture from the block's own name.
     *
     * @param block the block to generate a blockstate and model for.
     * @see net.minecraft.client.data.models.BlockModelGenerators#createTrivialCube
     */
    @Invoker("createTrivialCube")
    void to_base$createTrivialCube(Block block);

    /**
     * Generates a single model for a block using the supplied model template.
     *
     * @param block the block to generate a blockstate for.
     * @param model the textured model template to build from.
     * @see net.minecraft.client.data.models.BlockModelGenerators#createTrivialBlock
     */
    @Invoker("createTrivialBlock")
    void to_base$createTrivialBlock(Block block, TexturedModel.Provider model);

    /**
     * Generates a block model with one variant per horizontal facing direction.
     *
     * @param horizontallyRotatedBlock the block to generate a blockstate for.
     * @param provider the textured model template to build from.
     * @see net.minecraft.client.data.models.BlockModelGenerators#createHorizontallyRotatedBlock
     */
    @Invoker("createHorizontallyRotatedBlock")
    void to_base$createHorizontallyRotatedBlock(Block horizontallyRotatedBlock, TexturedModel.Provider provider);

    /**
     * Generates an invisible block with particles taken from another existing block.
     *
     * @param block the block to generate a blockstate for.
     * @param donor the block whose texture supplies the particles.
     * @see net.minecraft.client.data.models.BlockModelGenerators#createParticleOnlyBlock
     */
    @Invoker("createParticleOnlyBlock")
    void to_base$createParticleOnlyBlock(Block block, Block donor);

    /**
     * Generates an invisible block with particles using the specified texture.
     *
     * @param block the block to generate a blockstate for.
     * @param particle the material used for the block's particles.
     * @see net.minecraft.client.data.models.BlockModelGenerators#createAirLikeBlock
     */
    @Invoker("createAirLikeBlock")
    void to_base$createAirLikeBlock(Block block, Material particle);

}
