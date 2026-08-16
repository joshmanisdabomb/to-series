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

    /**
     * Updates the turbine's state, speed, and energy production based on the surrounding environment and machine tier.
     *
     * The method evaluates the heat and liquid level of boiling cauldrons below the turbine, adjusts the turbine's speed, monitors its powered state, and handles energy transfer to nearby systems.
     *
     * @param level The world level where the turbine is located.
     * @param state The current block state of the turbine block.
     * @param pos The position of the turbine block being ticked.
     * @param facing The direction the turbine is facing.
     * @param turbine The tier of the turbine, which determines energy production parameters.
     * @param powered The current powered state of the turbine block.
     */
    private fun tickTurbine(level: ServerLevel, state: BlockState, pos: BlockPos, facing: Direction, turbine: MachineTier, powered: Boolean) {
        speed = 0f
        for (i in 1..3) {
            val pos2 = pos.below(i)
            val boiler = level.getBlockState(pos2)
            if (boiler.block is BoilingCauldronBlock) {
                val fill = boiler.getValue(LEVEL)
                val heat = level.getBlockEntity(pos2, ToStarsMod.blockEntities.boiler).getOrNull()?.heats?.values?.sum() ?: break
                speed = heat.coerceIn(0f, 200f).div(100f).times(fill.div(3f)).times(turbineSpeed)
                break
            } else if (!boiler.getCollisionShape(level, pos2).isEmpty) {
                break
            }
        }

        if (Mth.equal(speed, 0f) && powered) {
            level.setBlock(pos, state.setValue(POWERED, false), 3)
        } else if (!Mth.equal(speed, 0f) && !powered) {
            level.setBlock(pos, state.setValue(POWERED, true), 3)
        }

        val other = ToBaseMod.transferProviders.to_energy.fromBlock(level, pos.relative(facing.opposite, 2), facing)
        if (other != null) {
            val context = energy[turbine]!!
            context.energy = speed.div(turbineSpeed).times(baseEnergyRate * turbine.turbineRate).toLong()

            TransferContext.moveAny(Unit, context.getTotalAmount(Unit), context, other)
        }
    }

    /**
     * Applies damage to entities within a specified collision area, pushing them based on their position
     * relative to the center and the direction of the rotor blades.
     *
     * @param level The world level where the rotor blades are located.
     * @param facing The direction the rotor blades are facing, influencing the force vector applied to entities.
     * @param center The central position of the rotor blades used for calculating force and damage.
     * @param collision The axis-aligned bounding box defining the collision area to check for entities.
     */
    private fun tickDamage(level: ServerLevel, facing: Direction, center: Vec3, collision: AABB) {
        val entities = level.getEntitiesOfClass(LivingEntity::class.java, collision)
        if (entities.isNotEmpty()) {
            val source = DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(ToStarsMod.damageTypes.rotor_blades), center)
            val damage = 0.5f + ceil(speed.times(30f.div(fanSpeed))).div(10f)
            for (affected in entities) {
                if (affected.hurtServer(level, source, damage)) {
                    val vector = affected.position().subtract(center).normalize()
                        .scale(speed.times(0.8).div(fanSpeed))
                        .with(facing.axis, facing.unitVec3.get(facing.axis).times(1.0))
                        .with(Direction.Axis.Y, 0.5)
                    affected.addDeltaMovement(vector)
                }
            }
        }
    }

    /**
     * Applies a pushing force to entities within a specified collision area based on their position relative to
     * the center and the direction of a facing vector. Entities are pushed away from the rotor blades with a force
     * proportional to their distance from the center, while taking into account environmental and movement factors.
     *
     * @param level The world level where the rotor blades are operating.
     * @param pos The position of the rotor blades in the world.
     * @param facing The direction of the rotor blades, determining the direction of the applied force.
     * @param center The central position of the rotor blades used for calculating the force applied to entities.
     * @param collision The axis-aligned bounding box defining the area to check for entities affected by the pushing force.
     */
    private fun tickPush(level: ServerLevel, pos: BlockPos, facing: Direction, center: Vec3, collision: AABB) {
        val range = 1.0 + (speed * fanRange / fanSpeed)
        val area = collision.expandTowards(facing.stepX.times(range), 0.0, facing.stepZ.times(range))
        val entities = level.getEntities(null, area)
        for (affected in entities) {
            if ((affected as? Player)?.abilities?.flying == true) continue
            if (level.clip(ClipContext(affected.position(), affected.position().with(facing.axis, pos.get(facing.axis).plus(0.5 + facing.unitVec3.get(facing.axis).times(0.5))), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, affected)).type != HitResult.Type.MISS) continue
            val power = 1.2 - (affected.position().distanceTo(center) / range)
            var motion = facing.unitVec3.scale(speed.times(fanMotion.times(fanSpeed)).times(power.coerceAtLeast(0.0)))
            if (affected.isSteppingCarefully) {
                if ((affected as? Player)?.isCreative == true) {
                    continue
                }
                motion = motion.normalize().scale(0.005)
            }
            affected.addDeltaMovement(motion)
        }
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
                    if (slevel != null) {
                        entity.tickTurbine(slevel, state, pos, facing, turbine, powered)
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
            if (turbine == null && slevel != null) {
                entity.tickPush(level, pos, facing, center, collision)
            }

            if (slevel != null && entity.speed > 0.25f.times(fanSpeed)) {
                entity.tickDamage(level, facing, center, collision)
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

        override fun getAmountAt(index: Int) = energy

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
