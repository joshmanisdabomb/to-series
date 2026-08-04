package net.jidb.to.base.pub.particle

import net.minecraft.core.particles.SimpleParticleType

/**
 * A [SimpleParticleType] that can actually be constructed, as vanilla's own constructor is not public.
 * This is the particle equivalent of what an access widener or access transformer would otherwise be needed for, and it adds nothing else.
 *
 * @param overrideLimiter Whether the particle ignores the client's particle limit setting. Defaults to `false`.
 * @since 0.2.0
 */
class AccessibleSimpleParticleType(overrideLimiter: Boolean = false) : SimpleParticleType(overrideLimiter)
