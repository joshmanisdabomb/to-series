package net.jidb.to.stars.block

import com.mojang.serialization.codecs.RecordCodecBuilder
import net.jidb.to.base.api.helper.BlockHelper.horizontalPlayerPlacement
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.entity.CentrifugeBlockEntity
import net.jidb.to.stars.info.MachineTier
import net.jidb.to.stars.info.ProcessorType
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING
import net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes

/**
 * The centrifuge, which processes uranium into its enriched and heavy forms and lights up while it is working.
 *
 * @param machine The tier the centrifuge is built at.
 * @param properties The block's own properties.
 */
class CentrifugeBlock(machine: MachineTier, properties: Properties) : ProcessorBlock(ProcessorType.CENTRIFUGE, machine, properties) {

    init {
        registerDefaultState(stateDefinition.any()
            .setValue(HORIZONTAL_FACING, Direction.NORTH)
            .setValue(LIT, false)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) = builder.add(HORIZONTAL_FACING, LIT).let {}

    override fun newBlockEntity(pos: BlockPos, state: BlockState) = CentrifugeBlockEntity(pos, state)

    override fun <T : BlockEntity> getTicker(level: Level, blockState: BlockState, type: BlockEntityType<T>) = createTickerHelper(type, ToStarsMod.blockEntities.centrifuge, CentrifugeBlockEntity::tick)

    override fun getStateForPlacement(context: BlockPlaceContext) = horizontalPlayerPlacement(context)

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext) = shapes[state.getValue(HORIZONTAL_FACING)]!!

    override fun rotate(state: BlockState, rotation: Rotation) = state.setValue(HORIZONTAL_FACING, rotation.rotate(state.getValue(HORIZONTAL_FACING)))

    override fun mirror(state: BlockState, mirror: Mirror) = state.rotate(mirror.getRotation(state.getValue(HORIZONTAL_FACING)))

    override fun codec() = codec

    companion object {

        /**
         * The codec the block is read from a data pack through, which carries its tier.
         */
        val codec = RecordCodecBuilder.mapCodec {
            it.group(
                MachineTier.codec.fieldOf("machine").forGetter(CentrifugeBlock::machine),
                propertiesCodec()
            )
                .apply(it, ::CentrifugeBlock)
        }

        /**
         * The collision shape of the centrifuge, for each of the four directions it can face.
         */
        val shapes = Shapes.rotateHorizontal(Shapes.or(
            box(0.0, 0.0, 0.0, 16.0, 7.0, 16.0),
            box(0.0, 7.0, 4.0, 16.0, 16.0, 16.0),
            box(2.0, 7.0, 0.0, 14.0, 8.0, 2.0),
            box(0.0, 7.0, 0.0, 2.0, 13.0, 4.0),
            box(14.0, 7.0, 0.0, 16.0, 13.0, 4.0),
            box(0.0, 13.0, 1.0, 2.0, 14.0, 4.0),
            box(14.0, 13.0, 1.0, 16.0, 14.0, 4.0),
            box(0.0, 14.0, 2.0, 2.0, 15.0, 4.0),
            box(14.0, 14.0, 2.0, 16.0, 15.0, 4.0),
            box(0.0, 15.0, 3.0, 2.0, 16.0, 4.0),
            box(14.0, 15.0, 3.0, 16.0, 16.0, 4.0),
        ))

    }

}
