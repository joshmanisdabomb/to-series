package net.jidb.to.stars.block

import com.mojang.serialization.codecs.RecordCodecBuilder
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.entity.HeatGeneratorBlockEntity
import net.jidb.to.stars.block.entity.SolidGeneratorBlockEntity
import net.jidb.to.stars.info.MachineTier
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

/**
 * The solid generator, which burns fuel to make heat and lights up while it does.
 *
 * @param machine The tier the generator is built at.
 * @param properties The block's own properties.
 */
class SolidGeneratorBlock(machine: MachineTier, properties: Properties) : HeatGeneratorBlock(machine, properties) {

    override fun newBlockEntity(pos: BlockPos, state: BlockState) = SolidGeneratorBlockEntity(pos, state)

    override fun <T : BlockEntity> getTicker(level: Level, blockState: BlockState, type: BlockEntityType<T>) = createTickerHelper(type, ToStarsMod.blockEntities.solid_generator, HeatGeneratorBlockEntity::tick)

    override fun codec() = codec

    companion object {

        /**
         * The codec the block is read from a data pack through, which carries its tier.
         */
        val codec = RecordCodecBuilder.mapCodec {
            it.group(
                MachineTier.codec.fieldOf("machine").forGetter(SolidGeneratorBlock::machine),
                propertiesCodec()
            )
                .apply(it, ::SolidGeneratorBlock)
        }

    }

}
