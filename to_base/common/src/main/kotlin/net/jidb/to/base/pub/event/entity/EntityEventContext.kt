package net.jidb.to.base.pub.event.entity

import net.minecraft.world.entity.Entity

/**
 * The context given to a handler of an entity event, naming the entity it concerns.
 *
 * @property entity The entity the event fired for.
 * @since 0.8.0
 */
data class EntityEventContext(val entity: Entity)
