package net.jidb.to.base.block.properties

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FireBlock

class ExtendedBlockProperties {

    private val _fireIgnite = mutableMapOf<FireBlock, Int>()
    private val _fireBurn = mutableMapOf<FireBlock, Int>()

    fun setFlammable(igniteOdds: Int = 5, burnOdds: Int = 20, block: FireBlock = Blocks.FIRE as FireBlock): ExtendedBlockProperties {
        _fireIgnite[block] = igniteOdds
        _fireBurn[block] = burnOdds
        return this
    }

    fun build(block: Block): ExtendedBlockProperties {
        for ((fire, ignite) in _fireIgnite) {
            fire.setFlammable(block, ignite, _fireBurn[fire]!!)
        }
        return this
    }

}