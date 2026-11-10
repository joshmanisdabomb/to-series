package net.jidb.to.stars.block

import com.mojang.serialization.codecs.RecordCodecBuilder
import net.jidb.to.base.api.helper.BlockHelper.horizontalPlayerPlacement
import net.jidb.to.base.service.Services
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.entity.AbstractEnergyFurnaceBlockEntity
import net.jidb.to.stars.info.EnergyFurnaceType
import net.jidb.to.stars.info.MachineTier
import net.jidb.to.stars.inventory.menu.EnergyFurnaceMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING
import net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT
import net.minecraft.world.phys.BlockHitResult

class EnergyFurnaceBlock(val furnace: EnergyFurnaceType, val machine: MachineTier, properties: Properties) : BaseEntityBlock(properties) {

    init {
        registerDefaultState(stateDefinition.any()
            .setValue(HORIZONTAL_FACING, Direction.NORTH)
            .setValue(LIT, false)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) = builder.add(HORIZONTAL_FACING, LIT).let {}

    override fun newBlockEntity(pos: BlockPos, state: BlockState) = furnace.newBlockEntity(pos, state)

    override fun <T : BlockEntity> getTicker(level: Level, blockState: BlockState, type: BlockEntityType<T>) = createTickerHelper(type, furnace.blockEntityType, AbstractEnergyFurnaceBlockEntity::tick)

    override fun useWithoutItem(state: BlockState, level: Level, pos: BlockPos, player: Player, hitResult: BlockHitResult): InteractionResult {
        if (!level.isClientSide) {
            val entity = level.getBlockEntity(pos) as? AbstractEnergyFurnaceBlockEntity ?: return InteractionResult.SUCCESS
            Services.platform.inventory.openExtendedMenu(player, ToStarsMod.menus.energy_furnace, state.getMenuProvider(level, pos), EnergyFurnaceMenu.EnergyFurnaceMenuData(entity))
            //player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE)
        }

        return InteractionResult.SUCCESS
    }

    override fun getStateForPlacement(context: BlockPlaceContext) = horizontalPlayerPlacement(context)

    override fun rotate(state: BlockState, rotation: Rotation) = state.setValue(HORIZONTAL_FACING, rotation.rotate(state.getValue(HORIZONTAL_FACING)))

    override fun mirror(state: BlockState, mirror: Mirror) = state.rotate(mirror.getRotation(state.getValue(HORIZONTAL_FACING)))

    override fun codec() = codec

    companion object {

        /**
         * The codec the block is read from a data pack through, which carries its tier.
         */
        val codec = RecordCodecBuilder.mapCodec {
            it.group(
                EnergyFurnaceType.codec.fieldOf("furnace").forGetter(EnergyFurnaceBlock::furnace),
                MachineTier.codec.fieldOf("machine").forGetter(EnergyFurnaceBlock::machine),
                propertiesCodec()
            )
                .apply(it, ::EnergyFurnaceBlock)
        }

    }

}
