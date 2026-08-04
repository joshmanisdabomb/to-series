package net.jidb.to.stars.advancements

import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.predicates.ContextAwarePredicate
import net.minecraft.advancements.predicates.entity.EntityPredicate
import net.minecraft.advancements.triggers.SimpleCriterionTrigger
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.storage.loot.LootContext
import java.util.Optional

/**
 * The criterion granted when an atomic bomb goes off, which an advancement can further narrow by what the bomb itself was.
 */
class AtomicBombAdvancementTrigger : SimpleCriterionTrigger<AtomicBombAdvancementTrigger.TriggerInstance>() {

    override fun codec() = codec

    /**
     * Grants the criterion to a player for a particular bomb.
     *
     * @param player The player being granted it.
     * @param bomb The bomb that went off.
     */
    fun trigger(player: ServerPlayer, bomb: Entity) {
        val context = EntityPredicate.createContext(player, bomb)
        this.trigger(player) { it.matches(context) }
    }

    /**
     * One advancement's use of this criterion, i.e. what it asks of the bomb before it will accept it.
     *
     * @property player What the advancement asks of the player, or empty where it asks nothing.
     * @property entity What it asks of the bomb, or empty where it asks nothing.
     */
    data class TriggerInstance(val player: Optional<ContextAwarePredicate>, val entity: Optional<ContextAwarePredicate>) : SimpleInstance {

        override fun player() = player

        /**
         * Whether a bomb satisfies what this advancement asks of it.
         *
         * @param context The bomb that went off.
         * @return Returns `true` if it satisfies the advancement, otherwise `false`.
         */
        fun matches(context: LootContext) = this.entity.isEmpty || this.entity.get().matches(context)

    }

    companion object {

        /**
         * The codec an advancement's use of this criterion is read through.
         */
        val codec = RecordCodecBuilder.create {
            it.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::component1),
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("entity").forGetter(TriggerInstance::component2),
            )
                .apply(it, ::TriggerInstance)
        }

    }

}
