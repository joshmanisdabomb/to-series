package net.jidb.to.stars.item

import net.jidb.to.stars.level.NuclearExplosion
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Explosion
import net.minecraft.world.phys.Vec3

class TestItem(properties: Properties) : Item(properties) {

    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        if (level is ServerLevel) {
            val explosion = NuclearExplosion(level, context.player, Explosion.getDefaultDamageSource(level, context.player), Vec3(context.clickedPos.relative(context.clickedFace)).add(0.5, 0.5, 0.5), 70f)
            explosion.run()
        }

        return InteractionResult.SUCCESS
    }

}
