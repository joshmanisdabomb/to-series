package net.jidb.to.stars.client.event.handler

import net.jidb.to.base.api.event.EventHandler
import net.jidb.to.base.pub.event.entity.EntityEventContext
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.item.render.RotorSpecialRenderer
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import kotlin.math.absoluteValue
import kotlin.math.max

/**
 * Turns rotor blades held or worn by an entity at the speed that entity is moving, so that carrying them about spins them up.
 */
class RotorMovementEventHandler : EventHandler<EntityEventContext, Unit>() {

    override fun invoke(context: EntityEventContext) {
        val living = context.entity as? LivingEntity ?: return
        for (slot in EquipmentSlot.entries) {
            val stack = living.getItemBySlot(slot)
            if (stack.item == ToStarsMod.blocks.rotor_blades.asItem()) {
                val speed = living.knownSpeed.length().absoluteValue.times(2f).toFloat()
                RotorSpecialRenderer.rotationSpeeds[stack] = max(RotorSpecialRenderer.rotationSpeeds[stack] ?: 0f, speed)
            }
        }
    }

}
