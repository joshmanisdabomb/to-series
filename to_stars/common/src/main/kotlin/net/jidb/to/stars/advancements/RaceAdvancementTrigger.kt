package net.jidb.to.stars.advancements

import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.predicates.ContextAwarePredicate
import net.minecraft.advancements.predicates.entity.EntityPredicate
import net.minecraft.advancements.triggers.SimpleCriterionTrigger
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerPlayer
import java.util.Optional

class RaceAdvancementTrigger : SimpleCriterionTrigger<RaceAdvancementTrigger.TriggerInstance>() {

    override fun codec() = codec

    fun trigger(player: ServerPlayer, advancement: AdvancementHolder) {
        this.trigger(player) { it.matches(advancement) }
    }

    data class TriggerInstance(val player: Optional<ContextAwarePredicate>, val advancement: Optional<Identifier>) : SimpleInstance {

        override fun player() = player

        fun matches(advancement: AdvancementHolder) = this.advancement.isEmpty || this.advancement.get() == advancement.id()

    }

    companion object {

        val codec = RecordCodecBuilder.create {
            it.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::component1),
                Identifier.CODEC.optionalFieldOf("advancement").forGetter(TriggerInstance::component2),
            )
                .apply(it, ::TriggerInstance)
        }

    }

}
