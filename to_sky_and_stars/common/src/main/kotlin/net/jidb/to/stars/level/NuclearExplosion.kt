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
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import java.util.UUID
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * A nuclear explosion, which is far larger than a vanilla one and worked out quite differently.
 *
 * Rather than casting a fixed number of rays, it walks one ray out to every point on the shell of a sphere, so that the blast covers the whole of its radius however large that is.
 * Blocks are cleared, turned to nuclear waste or set alight depending on how far the ray got and what it met; entities are hurt and thrown by how much of the blast they were exposed to.
 *
 * @property level The level the blast goes off in.
 * @property entity What set it off, or `null` where nothing did.
 * @param source What the damage is blamed on, or `null` to blame the blast itself.
 * @property origin Where the blast goes off.
 * @property strength How strong the blast is, which is also its radius in blocks.
 */
class NuclearExplosion(
    val level: ServerLevel,
    val entity: Entity?,
    source: DamageSource?,
    val origin: Vec3,
    val strength: Float
) {

    /**
     * What the damage from the blast is blamed on.
     */
    val source = source ?: Explosion.getDefaultDamageSource(level, entity)

    /**
     * How hard each player was thrown, kept so that it can be sent to them along with the blast itself.
     */
    val knockback = mutableMapOf<UUID, Vec3>()

    /**
     * Sets the blast off, i.e. damages the blocks, hurts the entities and tells the clients about it.
     */
    fun run() {
        doBlockDamage()
        doEntityDamage()
        updateClients()
    }

    /**
     * Clears, wastes or sets alight every block the blast reaches, by walking a ray out to each point on the shell of its sphere.
     */
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

    /**
     * Walks one ray out from the origin, weakening it by what each block it passes through resists, and records what should happen to every block it reached.
     *
     * @param dxEnd Where the ray ends, along x.
     * @param dyEnd Where it ends, along y.
     * @param dzEnd Where it ends, along z.
     * @param maxDistance How far the ray can reach.
     * @param minY The lowest the ray may go, i.e. the bottom of the world.
     * @param maxY The highest it may go, i.e. the top of the world.
     * @param processed The positions already walked over, which is shared between rays so that none is dealt with twice.
     * @param toRemove The positions to clear, which the ray adds to.
     * @param toWaste The positions to turn to nuclear waste, which the ray adds to.
     * @param toFire The positions to set alight, which the ray adds to.
     * @param pos A position the ray reuses as it walks, rather than allocating a fresh one per step.
     */
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

        val stepX = rayStep(dx)
        val stepY = rayStep(dy)
        val stepZ = rayStep(dz)

        val tDeltaX = rayDelta(stepX, dx)
        val tDeltaY = rayDelta(stepY, dy)
        val tDeltaZ = rayDelta(stepZ, dz)

        var tMaxX = rayBound(stepX, dx, origin.x, x)
        var tMaxY = rayBound(stepY, dy, origin.y, y)
        var tMaxZ = rayBound(stepZ, dz, origin.z, z)

        var t = 0.0
        var tMax = maxDistance - level.random.nextDouble().times(maxDistance * 0.4)

        while (t <= tMax) {
            if (y < minY || y >= maxY) break

            pos.set(x, y, z)
            val state = level.getBlockState(pos)
            if (!state.isAir) {
                tMax = markBlock(state, pos, t, tMax, maxDistance, processed, toRemove, toWaste, toFire)
                if (tMax <= 0.0) break
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

    /**
     * Which way a ray steps along one axis, i.e. the sign of its direction along it.
     *
     * @param direction How far the ray travels along the axis, per unit of its length.
     * @return `1` or `-1` where it moves along the axis at all, `0` where it does not.
     */
    private fun rayStep(direction: Double) = when {
        direction > 0.0 -> 1
        direction < 0.0 -> -1
        else -> 0
    }

    /**
     * How far a ray travels between one crossing of an axis' block boundaries and the next.
     *
     * @param step Which way it steps along the axis.
     * @param direction How far it travels along the axis, per unit of its length.
     * @return The distance between crossings, or infinity where the ray never crosses one.
     */
    private fun rayDelta(step: Int, direction: Double) = if (step == 0) Double.POSITIVE_INFINITY else abs(1.0 / direction)

    /**
     * How far a ray travels before it first crosses an axis' block boundary, which is what the walk starts from.
     *
     * @param step Which way it steps along the axis.
     * @param direction How far it travels along the axis, per unit of its length.
     * @param start Where it starts along the axis.
     * @param block The block it starts in along the axis.
     * @return The distance to the first crossing, or infinity where the ray never reaches one.
     */
    private fun rayBound(step: Int, direction: Double, start: Double, block: Int) = when {
        step > 0 -> ((block + 1.0) - start) / direction
        step < 0 -> (start - block) / -direction
        else -> Double.POSITIVE_INFINITY
    }

    /**
     * Weakens a ray by what one block it passed through resists, and records what should become of that block.
     *
     * A block the blast cannot get through stops the ray outright; anything else takes its resistance out of what the ray has left, which is what makes the blast reach further through open air than through stone.
     * What the block becomes is decided by how far out and how near the end of the ray it is: mostly cleared near the origin, and increasingly left as nuclear waste or set alight towards the edge.
     *
     * @param state The block the ray is passing through.
     * @param pos Where that block is.
     * @param t How far the ray has come.
     * @param tMax How far it can still reach.
     * @param maxDistance How far it could reach to begin with.
     * @param processed The positions already dealt with by an earlier ray, which are left alone.
     * @param toRemove The positions to clear, which this may add to.
     * @param toWaste The positions to turn to nuclear waste, which this may add to.
     * @param toFire The positions to set alight, which this may add to.
     * @return How far the ray can reach now, which is `0.0` or less where it is stopped here.
     */
    private fun markBlock(
        state: BlockState,
        pos: BlockPos,
        t: Double,
        tMax: Double,
        maxDistance: Double,
        processed: LongOpenHashSet,
        toRemove: LongOpenHashSet,
        toWaste: LongOpenHashSet,
        toFire: LongOpenHashSet
    ): Double {
        val remaining = t / maxDistance
        if (state.`is`(ToStarsMod.blockTags.nuke_immune)) return -1.0

        var blockResistance = state.block.explosionResistance
        if (blockResistance > 100000) return -1.0

        var reach = tMax
        if (!state.`is`(ToStarsMod.blockTags.nuke_passthrough)) {
            if (state.`is`(ToStarsMod.blockTags.nuke_shielding)) {
                blockResistance = 1000000f
            } else if (state.block is LiquidBlock) {
                blockResistance = 0f
            }
            val fluidResistance = level.getFluidState(pos).amount.div(8f)
            val resistance = maxOf(blockResistance, fluidResistance)
            reach -= resistance.times(0.2 - (remaining * remaining).times(0.175)).coerceAtLeast(0.01) * level.random.nextDouble()
        }

        if (reach <= 0.0) return reach

        val packed = BlockPos.asLong(pos.x, pos.y, pos.z)

        val wasteProgress = remaining * (t / reach)
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

        return reach
    }

    /**
     * Every position on the shell of a sphere, which is where the rays are walked out to.
     *
     * @param radius The radius of the sphere.
     * @return The positions on its shell, relative to the origin.
     */
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

    /**
     * Hurts and throws every entity within reach of the blast, by how much of it they were exposed to and how far away they were.
     *
     * A creative player who is flying is left alone entirely, and one who is not is thrown only gently, so that the blast does not fling them across the world.
     */
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

    /**
     * Tells every player near enough to notice that the blast has gone off, along with how hard they themselves were thrown.
     */
    fun updateClients() {
        val players = level.getPlayers { it.distanceToSqr(origin) <= 1000000.0 }
        for (player in players) {
            Services.platform.networking.sendToPlayer(player, NuclearExplosionPayload(strength, origin.toVector3f(), knockback[player.uuid]?.toVector3f() ?: Vector3f(0f, 0f, 0f)))
        }
    }

}
