package net.jidb.to.stars.block

import net.jidb.to.base.api.helper.BlockHelper.horizontalPlayerPlacement
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.entity.HeatGeneratorBlockEntity
import net.jidb.to.stars.info.MachineTier
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.AbstractFurnaceBlock
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING
import net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT
import net.minecraft.world.phys.BlockHitResult
import kotlin.math.ceil
import kotlin.math.sqrt

abstract class HeatGeneratorBlock(val machine: MachineTier, properties: Properties) : BaseEntityBlock(properties) {

    init {
        registerDefaultState(stateDefinition.any()
            .setValue(HORIZONTAL_FACING, Direction.NORTH)
            .setValue(LIT, false)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) = builder.add(HORIZONTAL_FACING, LIT).let {}

    override fun useWithoutItem(state: BlockState, level: Level, pos: BlockPos, player: Player, hitResult: BlockHitResult): InteractionResult {
        if (!level.isClientSide) {
            player.openMenu(state.getMenuProvider(level, pos))
            //player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE)
        }

        return InteractionResult.SUCCESS
    }

    override fun getStateForPlacement(context: BlockPlaceContext) = horizontalPlayerPlacement(context)

    override fun rotate(state: BlockState, rotation: Rotation) = state.setValue(HORIZONTAL_FACING, rotation.rotate(state.getValue(HORIZONTAL_FACING)))

    override fun mirror(state: BlockState, mirror: Mirror) = state.rotate(mirror.getRotation(state.getValue(HORIZONTAL_FACING)))

    override fun animateTick(state: BlockState, level: Level, pos: BlockPos, random: RandomSource) {
        if (state.getValue(LIT)) {
            val x = pos.x + 0.5
            val y = pos.y.toDouble()
            val z = pos.z + 0.5
            if (random.nextDouble() < 0.1) {
                level.playLocalSound(x, y, z, ToStarsMod.sounds.generator_crackle, SoundSource.BLOCKS, 1.0f, 1.0f, false)
            }

            val direction: Direction = state.getValue(AbstractFurnaceBlock.FACING)
            val axis = direction.axis
            val ss = random.nextDouble() * 0.6 - 0.3
            val dx = if (axis == Direction.Axis.X) direction.stepX * 0.52 else ss
            val dy = random.nextDouble() * 6.0 / 16.0
            val dz = if (axis == Direction.Axis.Z) direction.stepZ * 0.52 else ss
            level.addParticle(ParticleTypes.SMOKE, x + dx, y + dy, z + dz, 0.0, 0.0, 0.0)
            level.addParticle(ParticleTypes.FLAME, x + dx, y + dy, z + dz, 0.0, 0.0, 0.0)
        }
    }

    override fun stepOn(level: Level, pos: BlockPos, state: BlockState, entity: Entity) {
        if (!state.getValue(LIT) || entity.isSteppingCarefully || entity.fireImmune() || level !is ServerLevel) return super.stepOn(level, pos, state, entity)
        val source = ToStarsMod.damageTypes.getSource(ToStarsMod.damageTypes.heated, level.registryAccess())
        val heat = (level.getBlockEntity(pos) as? HeatGeneratorBlockEntity)?.heat ?: 0f
        val damage = ceil(sqrt(heat.div(100f)).times(10f)).div(10f)
        entity.hurtServer(level, source, damage)
    }

}
