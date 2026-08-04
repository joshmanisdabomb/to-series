package net.jidb.to.base.pub.block

import net.jidb.to.base.api.helper.BlockHelper.horizontalPlayerPlacement
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition

/**
 * A plain block that faces the player who placed it, for content that needs nothing more than a horizontal facing.
 * Vanilla has no such block to use directly, only [HorizontalDirectionalBlock] to extend, so this fills that gap in the same way as vanilla's own generic blocks.
 *
 * @param properties The vanilla block properties.
 * @since 0.1.0
 */
open class HorizontalGenericBlock(properties: Properties) : HorizontalDirectionalBlock(properties) {

    init {
        registerDefaultState(stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) = builder.add(FACING).let {}

    override fun getStateForPlacement(context: BlockPlaceContext) = horizontalPlayerPlacement(context)

    public override fun codec() = codec

    companion object {

        /**
         * The codec vanilla reads and writes this block with, which every block has to declare.
         *
         * @since 0.1.0
         */
        val codec = simpleCodec(::HorizontalGenericBlock)

    }

}
