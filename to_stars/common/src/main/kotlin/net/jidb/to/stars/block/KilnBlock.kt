package net.jidb.to.stars.block

import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.entity.KilnBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.AbstractFurnaceBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import kotlin.jvm.optionals.getOrNull

/**
 * Represents a kiln [net.minecraft.world.level.block.Block], a specialized type of furnace used for smelting unsmokables and unblastables in-game.
 * This class is mostly boilerplate that extends [AbstractFurnaceBlock].
 *
 * @constructor Initializes a new kiln [net.minecraft.world.level.block.Block] with the specified [net.minecraft.world.level.block.state.BlockBehaviour.Properties].
 * @param properties The [net.minecraft.world.level.block.state.BlockBehaviour.Properties] used to define the characteristics of this [net.minecraft.world.level.block.Block].
 *
 * @see KilnBlockEntity
 * @see net.jidb.to.stars.inventory.menu.KilnMenu
 * @see net.jidb.to.stars.client.gui.screens.KilnScreen
 * @since 0.2.0
 */
class KilnBlock(properties: Properties) : AbstractFurnaceBlock(properties) {

    override fun codec() = codec

    override fun openContainer(level: Level, pos: BlockPos, player: Player) {
        val kiln = level.getBlockEntity(pos, ToStarsMod.blockEntities.kiln).getOrNull() ?: return
        player.openMenu(kiln)
        //player.awardStat(Stats.INTERACT_WITH_FURNACE)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState) = KilnBlockEntity(pos, state)

    override fun <T : BlockEntity> getTicker(level: Level, blockState: BlockState, type: BlockEntityType<T>) = createFurnaceTicker(level, type, ToStarsMod.blockEntities.kiln)

    companion object {

        /**
         * Codec responsible for serializing and deserializing instances of the `KilnBlock` class.
         */
        val codec = simpleCodec(::KilnBlock)

    }

}
