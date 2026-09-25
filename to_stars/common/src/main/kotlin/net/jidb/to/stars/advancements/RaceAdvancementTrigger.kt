package net.jidb.to.stars.advancements

import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.predicates.ContextAwarePredicate
import net.minecraft.advancements.predicates.entity.EntityPredicate
import net.minecraft.advancements.triggers.SimpleCriterionTrigger
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerPlayer
import java.util.Optional

/**
 * The criterion granted to the first player on the server to earn a given advancement, which an advancement can further narrow by naming which one it means.
 */
class RaceAdvancementTrigger : SimpleCriterionTrigger<RaceAdvancementTrigger.TriggerInstance>() {

    override fun codec() = codec

    /**
     * Grants the criterion to a player for a particular advancement.
     *
     * @param player The player being granted it.
     * @param advancement The advancement they were first to earn.
     */
    fun trigger(player: ServerPlayer, advancement: AdvancementHolder) {
        this.trigger(player) { it.matches(advancement) }
    }

    /**
     * One advancement's use of this criterion, i.e. which advancement it asks the player to have been first to.
     *
     * @property player What the advancement asks of the player, or empty where it asks nothing.
     * @property advancement Which advancement they must have been first to, or empty where any will do.
     */
    data class TriggerInstance(val player: Optional<ContextAwarePredicate>, val advancement: Optional<Identifier>) : SimpleInstance {

        override fun player() = player

        /**
         * Whether an advancement is the one this asks to have been first to.
         *
         * @param advancement The advancement that was earned.
         * @return Returns `true` if it is the one asked for, otherwise `false`.
         */
        fun matches(advancement: AdvancementHolder) = this.advancement.isEmpty || this.advancement.get() == advancement.id()

    }

    companion object {

        /**
         * The codec an advancement's use of this criterion is read through.
         */
        val codec = RecordCodecBuilder.create {
            it.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::component1),
                Identifier.CODEC.optionalFieldOf("advancement").forGetter(TriggerInstance::component2),
            )
                .apply(it, ::TriggerInstance)
        }

    }

}
