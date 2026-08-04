package net.jidb.to.base.client.api.properties

import net.jidb.to.base.api.helper.IdentifierHelper.identifier
import net.jidb.to.base.api.properties.ExtendedBlockProperties
import net.jidb.to.base.client.service.ClientServices
import net.jidb.to.base.pub.library.BlockLibrary
import net.minecraft.client.color.block.BlockTintSource
import net.minecraft.client.renderer.block.BlockAndTintGetter
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

/**
 * A helper object that applies the client-side half of an [ExtendedBlockProperties] to the blocks a [BlockLibrary] registered.
 * The properties themselves are declared in common code alongside the block, and the parts of them that only mean something on the client, such as a tint, are handled here once the client platform is available.
 *
 * @see ExtendedBlockProperties
 * @since 0.2.0
 */
object ExtendedClientBlockProperties {

    /**
     * Applies the client-side properties of every entry in the given list, which a [BlockLibrary] collects as its blocks are declared.
     *
     * @param properties The list of extended properties, indexed by the library entry that declared them.
     * @return [Unit]
     * @since 0.2.0
     */
    fun handle(properties: BlockLibrary.ExtendedBlockPropertiesList) = properties.list.forEach { (entry, properties) -> properties.forEach { handle(entry.value, it) } }

    /**
     * Applies the client-side properties of a single [ExtendedBlockProperties] to the block that declared them.
     * Currently that means registering a [BlockTintSource] where the properties carry a tint, which adapts the mod's own [net.jidb.to.base.api.side.tint.BlockTint] to the vanilla interface.
     *
     * @param block The block the properties belong to.
     * @param properties The extended properties to apply.
     * @since 0.2.0
     */
    fun handle(block: Block, properties: ExtendedBlockProperties) {
        val tintProvider = properties.tint
        if (tintProvider != null) {
            val tint = tintProvider()
            ClientServices.platform.models.registerBlockTint(block.identifier.namespace, object : BlockTintSource {

                override fun color(state: BlockState) = tint.color(state)
                override fun colorInWorld(state: BlockState, level: BlockAndTintGetter, pos: BlockPos) = tint.color(state, level, pos)
                override fun colorAsTerrainParticle(state: BlockState, level: BlockAndTintGetter, pos: BlockPos) = tint.colorTerrainParticles(state, level, pos)
                override fun relevantProperties() = tint.relevantProperties()

            }, block)
        }
    }

}
