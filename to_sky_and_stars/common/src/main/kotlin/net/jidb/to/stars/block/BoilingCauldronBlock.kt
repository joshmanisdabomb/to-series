package net.jidb.to.stars.block

import net.jidb.to.base.ToBaseMod
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.entity.BoilingCauldronBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.cauldron.CauldronInteractions
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.InsideBlockEffectApplier
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.LayeredCauldronBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BlockStateProperties.UNSTABLE
import net.minecraft.world.level.redstone.Orientation
import net.minecraft.world.phys.BlockHitResult
import kotlin.jvm.optionals.getOrNull
import kotlin.math.ceil
import kotlin.math.sqrt

class BoilingCauldronBlock(properties: Properties) : LayeredCauldronBlock(Biome.Precipitation.RAIN, CauldronInteractions.WATER, properties), EntityBlock {

    init {
        registerDefaultState(stateDefinition.any()
            .setValue(LEVEL, 1)
            .setValue(UNSTABLE, false)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) = builder.add(LEVEL).add(UNSTABLE).let {}

    override fun newBlockEntity(pos: BlockPos, state: BlockState) = BoilingCauldronBlockEntity(pos, state)

    fun setHeat(level: ServerLevel, state: BlockState, pos: BlockPos, heat: Float, direction: Direction) {
        val networks = level.dataStorage.computeIfAbsent(ToBaseMod.savedData.block_networks)
        if (state.`is`(ToStarsMod.blocks.boiler)) {
            val boiler = level.getBlockEntity(pos, ToStarsMod.blockEntities.boiler).getOrNull()
            if (boiler != null) {
                boiler.heats[direction] = heat
                if (boiler.heats.values.sum() > 0f) {
                    return
                }
            }
            val fill = state.getValue(LEVEL)
            level.setBlock(pos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LEVEL, fill), 3)
            networks.notify(ToStarsMod.blockNetworks.heat, pos)
        } else if (state.`is`(Blocks.WATER_CAULDRON) && heat > 0f) {
            val fill = state.getValue(LEVEL)
            level.setBlock(pos, ToStarsMod.blocks.boiler.defaultBlockState().setValue(LEVEL, fill).setValue(UNSTABLE, heat > 200f), 3)
            networks.notify(ToStarsMod.blockNetworks.heat, pos)
            level.getBlockEntity(pos, ToStarsMod.blockEntities.boiler).ifPresent {
                it.heats[direction] = heat
            }
        }
    }

    override fun entityInside(state: BlockState, level: Level, pos: BlockPos, entity: Entity, effectApplier: InsideBlockEffectApplier, isPrecise: Boolean) {
        if (level is ServerLevel && !entity.fireImmune()) {
            val source = ToStarsMod.damageTypes.getSource(ToStarsMod.damageTypes.boiled, level.registryAccess())
            val fill = state.getValue(LEVEL)
            val heat = level.getBlockEntity(pos, ToStarsMod.blockEntities.boiler).getOrNull()?.heats?.values?.sum() ?: 0f
            val damage = ceil(sqrt(heat.div(100f)).times(10f)).div(10f).times(fill.div(1.5f))
            entity.hurtServer(level, source, damage)
        }
    }

    override fun useItemOn(stack: ItemStack, state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, hitResult: BlockHitResult): InteractionResult {
        if (!stack.`is`(Items.BUCKET) && !stack.`is`(Items.GLASS_BOTTLE) && !stack.`is`(Items.POTION)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND
        }
        return this.interactions.get(stack).interact(state, level, pos, player, hand, stack)
    }

    override fun getCloneItemStack(level: LevelReader, pos: BlockPos, state: BlockState, includeData: Boolean) = ItemStack(Items.CAULDRON)

    override fun neighborChanged(state: BlockState, level: Level, pos: BlockPos, block: Block, orientation: Orientation?, movedByPiston: Boolean) {
        for (direction in Direction.Plane.HORIZONTAL) {
            val other = level.getBlockState(pos.relative(direction))
            if (other.`is`(ToStarsMod.blocks.heat_pipe)) {
                return
            }
        }
        val below = level.getBlockState(pos.below())
        if (below.block is HeatGeneratorBlock && below.getValue(BlockStateProperties.LIT)) {
            return
        }
        level.setBlock(pos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LEVEL, state.getValue(LEVEL)), 3)
    }

    override fun <T : BlockEntity> getTicker(level: Level, blockState: BlockState, type: BlockEntityType<T>) = if (type == ToStarsMod.blockEntities.boiler) BlockEntityTicker<T> { level, pos, state, entity ->
        BoilingCauldronBlockEntity.tick(level,pos, state, entity as BoilingCauldronBlockEntity)
    } else null

}
