package net.jidb.to.stars.block.entity

import com.mojang.serialization.Codec
import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.helper.KotlinHelper.squared
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.HeatGeneratorBlock
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.LayeredCauldronBlock.LEVEL
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties.UNSTABLE
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import kotlin.jvm.optionals.getOrNull

class BoilingCauldronBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(ToStarsMod.blockEntities.boiler, pos, state) {

    var heats = mutableMapOf<Direction, Float>()

    override fun loadAdditional(input: ValueInput) {
        super.loadAdditional(input)
        heats = input.read("heat", heatCodec).getOrNull()?.toMutableMap() ?: mutableMapOf()
    }

    override fun saveAdditional(output: ValueOutput) {
        output.store("heat", heatCodec, heats)
        super.saveAdditional(output)
    }

    companion object {
        val heatCodec = Codec.unboundedMap(Direction.CODEC, Codec.FLOAT)

        fun tick(level: Level, pos: BlockPos, state: BlockState, entity: BoilingCauldronBlockEntity) {
            val fill = state.getValue(LEVEL)
            if (level.isClientSide) {
                if (state.getValue(UNSTABLE)) {
                    if (level.random.nextInt(3) == 0) {
                        level.addParticle(ToStarsMod.particles.steam, pos.x.plus(0.3).plus(level.random.nextDouble().times(0.4)), pos.y.plus(0.25 * fill), pos.z.plus(0.3).plus(level.random.nextDouble().times(0.4)), 0.0, level.random.nextDouble().times(0.1), 0.0)
                    }
                    for (i in 0 .. 2) {
                        level.addParticle(ToStarsMod.particles.foam, pos.x.plus(0.3).plus(level.random.nextDouble().times(0.4)), pos.y.plus(0.25 * fill), pos.z.plus(0.3).plus(level.random.nextDouble().times(0.4)), level.random.nextDouble().minus(0.5).times(2.0), level.random.nextDouble().squared().times(2.0).plus(1.0), level.random.nextDouble().minus(0.5).times(2.0))
                    }
                } else {
                    for (i in 0 .. fill.minus(1)) {
                        level.addParticle(ToStarsMod.particles.steam, pos.x.plus(0.3).plus(level.random.nextDouble().times(0.4)), pos.y.plus(0.25 * fill), pos.z.plus(0.3).plus(level.random.nextDouble().times(0.4)), 0.0, level.random.nextDouble().times(0.1), 0.0)
                    }
                    level.addParticle(ParticleTypes.SPLASH, pos.x.plus(0.2).plus(level.random.nextDouble().times(0.6)), pos.y.plus(0.3 * fill), pos.z.plus(0.2).plus(level.random.nextDouble().times(0.6)), level.random.nextDouble().times(0.5).minus(0.25), level.random.nextDouble().times(0.5), level.random.nextDouble().times(0.5).minus(0.25))
                    level.addParticle(ParticleTypes.BUBBLE_POP, pos.x.plus(0.2).plus(level.random.nextDouble().times(0.6)), pos.y.plus(0.3 * fill * level.random.nextDouble()), pos.z.plus(0.2).plus(level.random.nextDouble().times(0.6)), 0.0, level.random.nextDouble().times(0.2), 0.0)
                }
            } else {
                for (direction in Direction.Plane.HORIZONTAL) {
                    if (!level.getBlockState(pos.relative(direction)).`is`(ToStarsMod.blocks.heat_pipe)) {
                        entity.heats[direction] = 0f
                    }
                }
                if (level.getBlockState(pos.below()).block !is HeatGeneratorBlock) {
                    entity.heats[Direction.DOWN] = 0f
                }

                val slevel = level as? ServerLevel ?: return
                val networks = slevel.dataStorage.computeIfAbsent(ToBaseMod.savedData.block_networks)
                val total = entity.heats.values.sum()
                if (total <= 0f) {
                    if (state.`is`(ToStarsMod.blocks.boiler)) {
                        level.setBlock(pos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LEVEL, fill), 3)
                        networks.notify(ToStarsMod.blockNetworks.heat, pos)
                    }
                } else if (total > 200f) {
                    if (!state.`is`(ToStarsMod.blocks.boiler) || !state.getValue(UNSTABLE)) {
                        level.setBlock(pos, ToStarsMod.blocks.boiler.defaultBlockState().setValue(LEVEL, fill).setValue(UNSTABLE, true), 3)
                        networks.notify(ToStarsMod.blockNetworks.heat, pos)
                    }
                } else {
                    if (!state.`is`(ToStarsMod.blocks.boiler) || state.getValue(UNSTABLE)) {
                        level.setBlock(pos, ToStarsMod.blocks.boiler.defaultBlockState().setValue(LEVEL, fill).setValue(UNSTABLE, false), 3)
                        networks.notify(ToStarsMod.blockNetworks.heat, pos)
                    }
                }
            }
        }
    }

}