package net.jidb.to.stars.event.handler

import net.jidb.to.base.api.event.EventHandler
import net.jidb.to.base.pub.event.advancements.AdvancementEventContext
import net.jidb.to.stars.ToStarsMod

/**
 * Records which player was first on the server to earn each advancement, and grants them the criterion for having been first.
 */
class AdvancementRaceEventHandler : EventHandler<AdvancementEventContext, Unit>() {

    override fun invoke(context: AdvancementEventContext) {
        val overworld = context.player.level().server.overworld()
        val data = overworld.dataStorage.computeIfAbsent(ToStarsMod.savedData.advancement_race)
        if (data[context.advancement.id] == null) {
            ToStarsMod.advancementTriggers.race.trigger(context.player, context.advancement)
            data[context.advancement.id] = context.player
        }
    }

}
