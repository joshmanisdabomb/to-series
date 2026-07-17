package net.jidb.to.stars.block.entity

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.transfer.TransferContext
import net.jidb.to.base.api.transfer.TransferTransaction
import net.jidb.to.base.api.transfer.TransferTransactionJournal
import net.jidb.to.base.pub.transfer.ToEnergyTransferContext
import net.jidb.to.base.service.Services
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.BoilingCauldronBlock
import net.jidb.to.stars.info.MachineTier
import net.jidb.to.stars.network.RotorSyncPayload
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.LayeredCauldronBlock.LEVEL
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING
import net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import kotlin.jvm.optionals.getOrNull
import kotlin.math.absoluteValue
import kotlin.math.ceil
import kotlin.math.min

class RotorBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(ToStarsMod.blockEntities.rotor_blades, pos, state) {

    var speed: Float = 0f

    var clientAngle: Float = 0f
    var clientPrevAngle: Float = 0f

    var syncTime = 0
    var syncLast = 0f

    val energy = MachineTier.entries.associateWith(::RotorToEnergyTransferContext)

    override fun loadAdditional(input: ValueInput) {
        super.loadAdditional(input)
        speed = input.getFloatOr("speed", speed)
    }

    override fun saveAdditional(output: ValueOutput) {
        super.saveAdditional(output)
    }

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag {
        val tag = CompoundTag()
        tag.putFloat("speed", speed)
        return tag
    }

    companion object {
        const val turbineSpeed = 0.04f
        const val baseEnergyRate = 1000L

        const val fanSpeed = 0.14f
        const val fanMotion = 7.5f
        const val fanRange = 7.0f

        fun tick(level: Level, pos: BlockPos, state: BlockState, entity: RotorBlockEntity) {
            val turbine = ToStarsMod.blocks.rotor_blades.getTurbine(state, pos, level)
            val facing = state.getValue(HORIZONTAL_FACING)
            val powered = state.getValue(POWERED)
            val slevel = level as? ServerLevel
            if (level.isClientSide) {
                if (powered) {
                    entity.clientPrevAngle = entity.clientAngle
                    entity.clientAngle = (entity.clientAngle + entity.speed).rem(1f)
                } else {
                    entity.clientPrevAngle = 0f
                    entity.clientAngle = 0f
                }
            } else {
                if (turbine != null) {
                    entity.speed = 0f
                    for (i in 1..3) {
                        val pos2 = pos.below(i)
                        val boiler = level.getBlockState(pos2)
                        if (boiler.block is BoilingCauldronBlock) {
                            val fill = boiler.getValue(LEVEL)
                            val heat = level.getBlockEntity(pos2, ToStarsMod.blockEntities.boiler).getOrNull()?.heats?.values?.sum() ?: break
                            entity.speed = heat.coerceIn(0f, 200f).div(100f).times(fill.div(3f)).times(turbineSpeed)
                            break
                        } else if (!boiler.getCollisionShape(level, pos2).isEmpty) {
                            break
                        }
                    }

                    if (Mth.equal(entity.speed, 0f) && powered) {
                        level.setBlock(pos, state.setValue(POWERED, false), 3)
                    } else if (!Mth.equal(entity.speed, 0f) && !powered) {
                        level.setBlock(pos, state.setValue(POWERED, true), 3)
                    }

                    val other = ToBaseMod.transferProviders.to_energy.fromBlock(level, pos.relative(facing.opposite, 2), facing)
                    if (other != null) {
                        val context = entity.energy[turbine]!!
                        context.energy = entity.speed.div(turbineSpeed).times(baseEnergyRate * turbine.turbineRate).toLong()

                        TransferContext.moveAny(Unit, context.getTotalAmount(Unit), context, other)
                    }
                } else {
                    entity.speed = level.getBestNeighborSignal(pos) * fanSpeed / 15f
                }

                if (entity.syncTime <= 0) {
                    if (!Mth.equal(entity.speed, entity.syncLast)) {
                        if (slevel != null) {
                            Services.platform.networking.sendToPlayersTrackingChunk(slevel, ChunkPos.containing(pos), RotorSyncPayload(pos, entity.speed))
                        }
                        entity.syncTime = 4
                        entity.syncLast = entity.speed
                    }
                } else {
                    entity.syncTime--
                }
            }

            val center = Vec3.atCenterOf(pos)
            val collision = AABB(pos)
                .inflate(facing.stepZ.absoluteValue.times(0.4), 0.0, facing.stepX.absoluteValue.times(0.4))
                .contract(facing.stepX.absoluteValue.times(0.3), 0.0, facing.stepZ.absoluteValue.times(0.3))
            if (turbine == null) {
                val range = 1.0 + (entity.speed * fanRange / fanSpeed)
                val area = collision.expandTowards(facing.stepX.times(range), 0.0, facing.stepZ.times(range))
                val entities = level.getEntities(null, area)
                for (affected in entities) {
                    if ((affected as? Player)?.abilities?.flying == true) continue
                    if (level.clip(ClipContext(affected.position(), affected.position().with(facing.axis, pos.get(facing.axis).plus(0.5 + facing.unitVec3.get(facing.axis).times(0.5))), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, affected)).type != HitResult.Type.MISS) continue
                    val power = 1.2 - (affected.position().distanceTo(center) / range)
                    var motion = facing.unitVec3.scale(entity.speed.times(fanMotion.times(fanSpeed)).times(power.coerceAtLeast(0.0)))
                    if (affected.isSteppingCarefully) {
                        if ((affected as? Player)?.isCreative == true) {
                            continue
                        }
                        motion = motion.normalize().scale(0.005)
                    }
                    affected.addDeltaMovement(motion)
                }
            }

            if (slevel != null && entity.speed > 0.25f.times(fanSpeed)) {
                val entities = level.getEntitiesOfClass(LivingEntity::class.java, collision)
                if (entities.isNotEmpty()) {
                    val source = DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(ToStarsMod.damageTypes.rotor_blades), center)
                    val damage = 0.5f + ceil(entity.speed.times(30f.div(fanSpeed))).div(10f)
                    for (affected in entities) {
                        if (affected.hurtServer(level, source, damage)) {
                            val vector = affected.position().subtract(center).normalize()
                                .scale(entity.speed.times(0.8).div(fanSpeed))
                                .with(facing.axis, facing.unitVec3.get(facing.axis).times(1.0))
                                .with(Direction.Axis.Y, 0.5)
                            affected.addDeltaMovement(vector)
                        }
                    }
                }
            }
        }
    }

    class RotorToEnergyTransferContext(val tier: MachineTier) : ToEnergyTransferContext {
        var energy = 0L

        val journal = object : TransferTransactionJournal<Long>() {
            override fun create() = energy
            override fun rewind(snapshot: Long) {
                energy = snapshot
            }
        }

        override fun getSlotCount() = 1

        override fun getAmountAt(resource: Unit, index: Int) = energy

        override fun getCapacityAt(resource: Unit, index: Int) = baseEnergyRate.times(tier.turbineRate).toLong()

        override fun insert(resource: Unit, amount: Long, transaction: TransferTransaction) = 0L

        override fun extract(resource: Unit, amount: Long, transaction: TransferTransaction): Long {
            val extracted = min(energy, amount)
            if (extracted > 0) {
                journal.store(transaction)
                energy -= extracted
                return extracted
            } else {
                return 0
            }
        }
    }

}
