package net.jidb.to.stars.block

import com.mojang.serialization.codecs.RecordCodecBuilder
import net.jidb.to.base.api.helper.BlockHelper.directionalPlayerPlacement
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.entity.EnergyStorageBlockEntity
import net.jidb.to.stars.info.MachineTier
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DirectionalBlock.FACING
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.phys.BlockHitResult

class EnergyStorageBlock(val machine: MachineTier, properties: Properties) : BaseEntityBlock(properties) {

    init {
        registerDefaultState(stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) = builder.add(FACING).let {}

    override fun newBlockEntity(pos: BlockPos, state: BlockState) = EnergyStorageBlockEntity(pos, state)

    override fun <T : BlockEntity> getTicker(level: Level, blockState: BlockState, type: BlockEntityType<T>) = createTickerHelper(type, ToStarsMod.blockEntities.power_bank, EnergyStorageBlockEntity::tick)

    override fun useWithoutItem(state: BlockState, level: Level, pos: BlockPos, player: Player, hitResult: BlockHitResult): InteractionResult {
        if (!level.isClientSide) {
            player.openMenu(state.getMenuProvider(level, pos))
            //player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE)
        }

        return InteractionResult.SUCCESS
    }

    override fun getStateForPlacement(context: BlockPlaceContext) = directionalPlayerPlacement(context)

    override fun rotate(state: BlockState, rotation: Rotation) = state.setValue(FACING, rotation.rotate(state.getValue(FACING)))

    override fun mirror(state: BlockState, mirror: Mirror) = state.rotate(mirror.getRotation(state.getValue(FACING)))

    override fun codec() = codec

    companion object {

        val codec = RecordCodecBuilder.mapCodec {
            it.group(
                MachineTier.codec.fieldOf("machine").forGetter(EnergyStorageBlock::machine),
                propertiesCodec()
            )
                .apply(it, ::EnergyStorageBlock)
        }

    }

}
