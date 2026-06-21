package net.jidb.to.stars.level

import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import net.jidb.to.base.api.helper.KotlinHelper.squared
import net.jidb.to.base.service.Services
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.network.NuclearExplosionPayload
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.ServerExplosion
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import java.util.*
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class NuclearExplosion(
    val level: ServerLevel,
    val entity: Entity?,
    source: DamageSource?,
    val origin: Vec3,
    val strength: Float
) {

    val source = source ?: Explosion.getDefaultDamageSource(level, entity)

    val knockback = mutableMapOf<UUID, Vec3>()

    fun run() {
        doBlockDamage()
        doEntityDamage()
        updateClients()
    }

    fun doBlockDamage() {
        val radius = strength.roundToInt().coerceAtLeast(1)
        val endpoints = generateSphereShell(radius)
        if (endpoints.isEmpty()) return

        val processed = LongOpenHashSet(endpoints.size * 8)
        val toRemove = LongOpenHashSet(endpoints.size * 4)
        val toWaste = LongOpenHashSet(endpoints.size * 2)
        val toFire = LongOpenHashSet(endpoints.size * 2)
        val pos = BlockPos.MutableBlockPos()

        val minY = level.minY
        val maxY = level.maxY

        val iterator = endpoints.iterator()
        while (iterator.hasNext()) {
            val endpoint = iterator.nextLong()
            val ex = BlockPos.getX(endpoint)
            val ey = BlockPos.getY(endpoint)
            val ez = BlockPos.getZ(endpoint)

            traceRay(ex.toDouble(), ey.toDouble(), ez.toDouble(), radius.toDouble(), minY, maxY, processed, toRemove, toWaste, toFire, pos)
        }

        if (toRemove.isNotEmpty()) {
            val removeIterator = toRemove.iterator()
            while (removeIterator.hasNext()) {
                val packed = removeIterator.nextLong()
                if (toWaste.contains(packed)) continue
                if (toFire.contains(packed)) continue
                pos.set(BlockPos.getX(packed), BlockPos.getY(packed), BlockPos.getZ(packed))
                val state = level.getBlockState(pos)
                if (!state.isAir) {
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3)
                }
            }
        }

        if (toWaste.isNotEmpty()) {
            val wasteState = ToStarsMod.blocks.nuclear_waste.defaultBlockState()
            val wasteIterator = toWaste.iterator()
            while (wasteIterator.hasNext()) {
                val packed = wasteIterator.nextLong()
                pos.set(BlockPos.getX(packed), BlockPos.getY(packed), BlockPos.getZ(packed))
                level.setBlock(pos, wasteState, 3)
            }
        }

        if (toFire.isNotEmpty()) {
            val fireIterator = toFire.iterator()
            while (fireIterator.hasNext()) {
                val packed = fireIterator.nextLong()
                pos.set(BlockPos.getX(packed), BlockPos.getY(packed), BlockPos.getZ(packed))
                level.setBlock(pos, ToStarsMod.blocks.nuclear_fire.getStateForPlacement(level, pos), 3)
            }
        }
    }

    private fun traceRay(
        dxEnd: Double,
        dyEnd: Double,
        dzEnd: Double,
        maxDistance: Double,
        minY: Int,
        maxY: Int,
        processed: LongOpenHashSet,
        toRemove: LongOpenHashSet,
        toWaste: LongOpenHashSet,
        toFire: LongOpenHashSet,
        pos: BlockPos.MutableBlockPos
    ) {
        val lenSq = dxEnd * dxEnd + dyEnd * dyEnd + dzEnd * dzEnd
        if (lenSq <= 1.0e-8) return
        val invLen = 1.0 / sqrt(lenSq)

        val dx = dxEnd * invLen
        val dy = dyEnd * invLen
        val dz = dzEnd * invLen

        var x = floor(origin.x).toInt()
        var y = floor(origin.y).toInt()
        var z = floor(origin.z).toInt()

        val stepX = when {
            dx > 0.0 -> 1
            dx < 0.0 -> -1
            else -> 0
        }
        val stepY = when {
            dy > 0.0 -> 1
            dy < 0.0 -> -1
            else -> 0
        }
        val stepZ = when {
            dz > 0.0 -> 1
            dz < 0.0 -> -1
            else -> 0
        }

        val tDeltaX = if (stepX == 0) Double.POSITIVE_INFINITY else kotlin.math.abs(1.0 / dx)
        val tDeltaY = if (stepY == 0) Double.POSITIVE_INFINITY else kotlin.math.abs(1.0 / dy)
        val tDeltaZ = if (stepZ == 0) Double.POSITIVE_INFINITY else kotlin.math.abs(1.0 / dz)

        var tMaxX = if (stepX > 0) ((x + 1.0) - origin.x) / dx else if (stepX < 0) (origin.x - x) / -dx else Double.POSITIVE_INFINITY
        var tMaxY = if (stepY > 0) ((y + 1.0) - origin.y) / dy else if (stepY < 0) (origin.y - y) / -dy else Double.POSITIVE_INFINITY
        var tMaxZ = if (stepZ > 0) ((z + 1.0) - origin.z) / dz else if (stepZ < 0) (origin.z - z) / -dz else Double.POSITIVE_INFINITY

        var t = 0.0
        var tMax = maxDistance - level.random.nextDouble().times(maxDistance * 0.4)

        while (t <= tMax) {
            if (y < minY || y >= maxY) break

            pos.set(x, y, z)
            val state = level.getBlockState(pos)
            if (!state.isAir) {
                val remaining = t / maxDistance
                if (state.`is`(ToStarsMod.blockTags.nuke_immune)) {
                    break
                }

                var blockResistance = state.block.explosionResistance
                if (blockResistance > 100000) {
                    break
                }

                if (!state.`is`(ToStarsMod.blockTags.nuke_passthrough) && !state.isAir) {
                    if (state.`is`(ToStarsMod.blockTags.nuke_shielding)) {
                        blockResistance = 1000000f
                    } else if (state.block is LiquidBlock) {
                        blockResistance = 0f
                    }
                    val fluidResistance = level.getFluidState(pos).amount.div(8f)
                    val resistance = maxOf(blockResistance, fluidResistance)
                    tMax -= resistance.times(0.2 - (remaining * remaining).times(0.175)).coerceAtLeast(0.01) * level.random.nextDouble()
                }

                if (tMax <= 0.0) break

                val packed = BlockPos.asLong(x, y, z)

                val wasteProgress = remaining * (t / tMax)
                val wasteChance = wasteProgress.squared().squared()
                val fireChance = wasteChance.times(0.5) + 0.005

                if (!processed.contains(packed)) {
                    if (level.random.nextDouble() < fireChance) {
                        toFire.add(packed)
                    } else if (level.random.nextDouble() < wasteChance) {
                        toWaste.add(packed)
                    } else {
                        toRemove.add(packed)
                    }
                }
            }

            if (tMaxX < tMaxY) {
                if (tMaxX < tMaxZ) {
                    x += stepX
                    t = tMaxX
                    tMaxX += tDeltaX
                } else {
                    z += stepZ
                    t = tMaxZ
                    tMaxZ += tDeltaZ
                }
            } else {
                if (tMaxY < tMaxZ) {
                    y += stepY
                    t = tMaxY
                    tMaxY += tDeltaY
                } else {
                    z += stepZ
                    t = tMaxZ
                    tMaxZ += tDeltaZ
                }
            }
        }
    }

    private fun generateSphereShell(radius: Int): LongOpenHashSet {
        val shell = LongOpenHashSet(radius * radius * 12)
        val r2 = radius * radius

        for (x in -radius..radius) {
            val x2 = x * x
            for (y in -radius..radius) {
                val rem = r2 - x2 - y * y
                if (rem < 0) continue
                val z = sqrt(rem.toDouble()).toInt()
                shell.add(BlockPos.asLong(x, y, z))
                shell.add(BlockPos.asLong(x, y, -z))
            }
        }

        for (x in -radius..radius) {
            val x2 = x * x
            for (z in -radius..radius) {
                val rem = r2 - x2 - z * z
                if (rem < 0) continue
                val y = sqrt(rem.toDouble()).toInt()
                shell.add(BlockPos.asLong(x, y, z))
                shell.add(BlockPos.asLong(x, -y, z))
            }
        }

        for (y in -radius..radius) {
            val y2 = y * y
            for (z in -radius..radius) {
                val rem = r2 - y2 - z * z
                if (rem < 0) continue
                val x = sqrt(rem.toDouble()).toInt()
                shell.add(BlockPos.asLong(x, y, z))
                shell.add(BlockPos.asLong(-x, y, z))
            }
        }

        shell.remove(BlockPos.asLong(0, 0, 0))
        return shell
    }

    fun doEntityDamage() {
        knockback.clear()

        val range = strength * 1.4f
        val distsq = range.squared()
        val entities = level.getEntities(null, AABB(origin.x, origin.y, origin.z, origin.x, origin.y, origin.z).inflate(range.toDouble()))
        for (entity in entities) {
            val distance = entity.distanceToSqr(origin)
            if (distance > distsq) continue

            val seen = ServerExplosion.getSeenPercent(origin, entity)
            var damage = seen.times(5.0f).times(strength).pow(1.3f)
            if (entity is LivingEntity) {
                damage += 3.0f.coerceAtMost(entity.maxHealth - 2f)
            }
            damage *= (1.0 - (distance / distsq)).toFloat()
            damage += 1.0f

            entity.hurtServer(this.level, source, damage)

            val resistance = (entity as? LivingEntity)?.getAttributeValue(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE) ?: 0.35
            var force = sqrt(damage).times(1.1 - resistance)
            if (entity is Player && entity.isCreative) {
                if (entity.abilities.flying) {
                    continue
                }
                force = force.coerceAtMost(2.0)
            }
            var direction = entity.position().subtract(origin).normalize()
            direction = direction.with(Direction.Axis.Y, direction.y.squared().times(0.3))
            val velocity = direction.normalize().scale(force)
            entity.push(velocity)
            if (entity is Player) {
                knockback[entity.uuid] = velocity
            }
        }
    }

    fun updateClients() {
        val players = level.getPlayers { it.distanceToSqr(origin) <= 1000000.0 }
        for (player in players) {
            Services.platform.networking.sendToPlayer(player, NuclearExplosionPayload(strength, origin.toVector3f(), knockback[player.uuid]?.toVector3f() ?: Vector3f(0f, 0f, 0f)))
        }
    }
}
