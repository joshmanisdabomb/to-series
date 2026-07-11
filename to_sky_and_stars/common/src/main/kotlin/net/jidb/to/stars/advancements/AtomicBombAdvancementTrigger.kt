package net.jidb.to.stars.advancements

import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.predicates.ContextAwarePredicate
import net.minecraft.advancements.predicates.entity.EntityPredicate
import net.minecraft.advancements.triggers.SimpleCriterionTrigger
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.storage.loot.LootContext
import java.util.*

class AtomicBombAdvancementTrigger : SimpleCriterionTrigger<AtomicBombAdvancementTrigger.TriggerInstance>() {

    override fun codec() = codec

    fun trigger(player: ServerPlayer, bomb: Entity) {
        val context = EntityPredicate.createContext(player, bomb)
        this.trigger(player) { it.matches(context) }
    }

    data class TriggerInstance(val player: Optional<ContextAwarePredicate>, val entity: Optional<ContextAwarePredicate>) : SimpleInstance {

        override fun player() = player

        fun matches(context: LootContext) = this.entity.isEmpty || this.entity.get().matches(context)

    }

    companion object {
        val codec = RecordCodecBuilder.create {
            it.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::component1),
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("entity").forGetter(TriggerInstance::component2),
            )
                .apply(it, ::TriggerInstance)
        }
    }

}