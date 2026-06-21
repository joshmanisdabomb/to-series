package net.jidb.to.base.api.block.properties

import net.jidb.to.base.mixin.FireBlockAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FireBlock

class ExtendedBlockProperties {

    private val _fireIgnite = mutableMapOf<FireBlock, Int>()
    val fireIgnite: Map<FireBlock, Int> get() = _fireIgnite
    private val _fireBurn = mutableMapOf<FireBlock, Int>()
    val fireBurn: Map<FireBlock, Int> get() = _fireIgnite

    var renderLayer = RenderLayer.SOLID
        private set

    fun flammable(igniteOdds: Int = 5, burnOdds: Int = 20, block: FireBlock = Blocks.FIRE as FireBlock): ExtendedBlockProperties {
        _fireIgnite[block] = igniteOdds
        _fireBurn[block] = burnOdds
        return this
    }

    fun renderLayer(type: RenderLayer): ExtendedBlockProperties {
        renderLayer = type
        return this
    }

    fun build(block: Block): ExtendedBlockProperties {
        for ((fire, ignite) in _fireIgnite) {
            (fire as FireBlockAccessor).`to_base$setFlammable`(block, ignite, _fireBurn[fire]!!)
        }
        return this
    }

    enum class RenderLayer {
        SOLID,
        CUTOUT,
        TRANSLUCENT,
        TRIPWIRE
    }

}