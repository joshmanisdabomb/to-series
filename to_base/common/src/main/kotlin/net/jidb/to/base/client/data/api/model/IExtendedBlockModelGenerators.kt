package net.jidb.to.base.client.data.api.model

import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.world.level.block.Block

/**
 * The block model shapes that vanilla's own generator does not provide, mixed into it so that they can be called the same way as the shapes it does.
 *
 * @since 0.3.0
 */
interface IExtendedBlockModelGenerators {

    /**
     * Writes the blockstate of a block that faces any of the six directions, turning the one model to suit each of them.
     *
     * @param block The block to generate for.
     * @param model The model each rotation is drawn from. Defaults to a plain cube.
     * @return [Unit]
     * @since 0.3.0
     */
    fun createFullRotatedVariantBlock(block: Block, model: TexturedModel.Provider = TexturedModel.CUBE): Unit

    /**
     * Writes the blockstate of a block that faces any of the six directions and is drawn from a different model for each of the three axes, so that one placed against a ceiling is not simply the upright model turned over.
     *
     * @param block The block to generate for.
     * @param top The model drawn where the block faces up.
     * @param side The model drawn where the block faces horizontally.
     * @param bottom The model drawn where the block faces down.
     * @return [Unit]
     * @since 0.6.0
     */
    fun createUprightDirectionalBlock(block: Block, top: TexturedModel.Provider, side: TexturedModel.Provider, bottom: TexturedModel.Provider): Unit

    /**
     * Writes the model of a fire-like block, i.e. the floor and wall quads that vanilla's own fire is drawn from.
     *
     * @param block The block to generate for.
     * @return [Unit]
     * @since 0.3.0
     */
    fun createFire(block: Block): Unit

}
