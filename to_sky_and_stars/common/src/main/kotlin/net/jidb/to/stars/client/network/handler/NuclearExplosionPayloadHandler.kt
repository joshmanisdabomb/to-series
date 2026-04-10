package net.jidb.to.stars.client.network.handler

import net.jidb.to.base.client.network.ClientPayloadContext
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.sound.DistantSoundInstance
import net.jidb.to.stars.network.NuclearExplosionPayload
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.LocalPlayer
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.PowerParticleOption
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.CollisionContext
import org.joml.Vector3fc
import kotlin.math.*

object NuclearExplosionPayloadHandler {
    fun handle(data: NuclearExplosionPayload, context: ClientPayloadContext) {
        playSound(data.strength, data.origin, context.player)
        playParticles(data.strength, data.origin, context.level)
        playKnockback(context.player, data.velocity)
    }

    fun playParticles(strength: Float, origin: Vector3fc, level: ClientLevel) {
        val ox = origin.x().toDouble()
        val oy = origin.y().toDouble()
        val oz = origin.z().toDouble()
        val ov = Vec3(ox, oy, oz)

        val count = ((4.0 / 3.0) * Math.PI * strength * strength * strength * 0.01).roundToInt().coerceIn(1, 13000)
        val pos = BlockPos.MutableBlockPos()
        repeat(count) {
            val ry = level.random.nextDouble() * 2 - 1
            val r = sqrt(1 - ry * ry)
            val long = level.random.nextDouble() * 2 * Math.PI
            val len = level.random.nextDouble().pow(1.0 / 3.0)
            val sx = len * r * sin(long)
            val sy = len * ry
            val sz = len * r * cos(long)
            val pv = Vec3(sx * 0.8 * strength + ox, sy * 0.8 * strength + oy, sz * 0.8 * strength + oz)
            pos.set(pv.x, pv.y, pv.z)

            if (!level.getBlockState(pos).canBeReplaced()) return@repeat

            val result = level.clip(ClipContext(ov, pv, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty()))
            if (result.type != HitResult.Type.MISS) return@repeat

            val sv = Vec3(sx, sy + 0.25, sz)
            val nv = sv.normalize()

            var vx = ((sv.x * strength).div(2.0) + (nv.x * strength).div(2.0)).div(40.0)
            var vy = ((sv.y * strength).div(2.0) + (nv.y * strength).div(2.0)).div(40.0)
            var vz = ((sv.z * strength).div(2.0) + (nv.z * strength).div(2.0)).div(40.0)

            var power = len.toFloat()
            val cy = (1 - abs(sy)).pow(4.0)
            if (cy > 0.5) {
                vx *= 0.5 + cy.times(3.0)
                vy = (level.random.nextDouble() - 0.5).times(strength).div(30.0)
                vz *= 0.5 + cy.times(3.0)
                power += 1f
            } else {
                val ch = sqrt(sx * sx + sz * sz)
                if (ch > 0.2 && sy > 0.0) {
                    vx *= 2.0
                    vy = (sy + 1).times(strength).div(20.0)
                    vz *= 2.0
                    power += 2f
                }
            }

            level.addParticle(PowerParticleOption.create(ToStarsMod.particles.nuclear_explosion, power), true, true, pv.x, pv.y, pv.z, vx, vy, vz)
        }
    }

    fun playSound(strength: Float, origin: Vector3fc, player: LocalPlayer) {
        val large = strength >= 75f
        val soundEvent = if (large) ToStarsMod.sounds.nuke_large else ToStarsMod.sounds.nuke_small
        val soundInstance = DistantSoundInstance(
            soundEvent,
            SoundSource.BLOCKS,
            origin.x().toDouble(),
            origin.y().toDouble(),
            origin.z().toDouble(),
            if (large) 800f else 200f,
            0.9f + player.random.nextFloat() * 0.2f,
            player.random,
        )

        Minecraft.getInstance().soundManager.stop(null, SoundSource.BLOCKS)
        Minecraft.getInstance().soundManager.playDelayed(soundInstance, 2)
    }

    fun playKnockback(player: LocalPlayer, velocity: Vector3fc) {
        if (velocity.lengthSquared() <= 0.0) return
        player.push(velocity.x().toDouble(), velocity.y().toDouble(), velocity.z().toDouble())
    }
}