package net.jidb.to.stars.info

import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.entity.AbstractEnergyFurnaceBlockEntity
import net.jidb.to.stars.block.entity.EnergyBlastFurnaceBlockEntity
import net.jidb.to.stars.block.entity.EnergyFurnaceBlockEntity
import net.jidb.to.stars.block.entity.EnergyKilnBlockEntity
import net.jidb.to.stars.block.entity.EnergySmokerBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.util.ByIdMap
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

enum class EnergyFurnaceType : StringRepresentable {

    FURNACE {
        override val blockEntityType get() = ToStarsMod.blockEntities.energy_furnace

        override fun newBlockEntity(pos: BlockPos, state: BlockState) = EnergyFurnaceBlockEntity(pos, state)
    },
    SMOKER {
        override val blockEntityType get() = ToStarsMod.blockEntities.energy_smoker

        override fun newBlockEntity(pos: BlockPos, state: BlockState) = EnergySmokerBlockEntity(pos, state)
    },
    BLAST_FURNACE {
        override val blockEntityType get() = ToStarsMod.blockEntities.energy_blast_furnace

        override fun newBlockEntity(pos: BlockPos, state: BlockState) = EnergyBlastFurnaceBlockEntity(pos, state)
    },
    KILN {
        override val blockEntityType get() = ToStarsMod.blockEntities.energy_kiln

        override fun newBlockEntity(pos: BlockPos, state: BlockState) = EnergyKilnBlockEntity(pos, state)
    };

    abstract val blockEntityType: BlockEntityType<out AbstractEnergyFurnaceBlockEntity>

    override fun getSerializedName() = name.lowercase()

    abstract fun newBlockEntity(pos: BlockPos, state: BlockState): AbstractEnergyFurnaceBlockEntity

    companion object {

        val codec = StringRepresentable.fromEnum(::values)

        /**
         * The types by their position in this enum, out of which anything unrecognised reads back as the first.
         */
        val byId = ByIdMap.continuous(EnergyFurnaceType::ordinal, EnergyFurnaceType.entries.toTypedArray(), ByIdMap.OutOfBoundsStrategy.ZERO)

        /**
         * The codec an electric furnace type is sent to the client through, by its position in this enum.
         */
        val streamCodec = ByteBufCodecs.idMapper(byId, EnergyFurnaceType::ordinal)

    }

}
