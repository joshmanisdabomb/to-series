package net.jidb.to.base.block

import net.jidb.to.base.helper.BlockHelper.horizontalPlacement
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition

open class HorizontalBlock(properties: Properties) : HorizontalDirectionalBlock(properties) {

    init {
        registerDefaultState(stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) = builder.add(FACING).let {}

    override fun getStateForPlacement(context: BlockPlaceContext) = horizontalPlacement(context)

    public override fun codec() = CODEC

    companion object {
        val CODEC = simpleCodec(::HorizontalBlock)
    }

}