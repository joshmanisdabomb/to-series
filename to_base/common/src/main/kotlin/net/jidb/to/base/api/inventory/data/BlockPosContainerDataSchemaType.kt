package net.jidb.to.base.api.inventory.data

import net.minecraft.core.BlockPos
import net.minecraft.world.inventory.ContainerData

/**
 * A [ContainerDataSchemaType] for transporting a [BlockPos] as four [Short] values via [ContainerDataSchema] over the network.
 *
 * @since 0.7.0
 */
object BlockPosContainerDataSchemaType : ContainerDataSchemaType<BlockPos> {

    override val shortLength = LongContainerDataSchemaType.shortLength

    override fun getValue(data: ContainerData, start: Int) = BlockPos.of(LongContainerDataSchemaType.getValue(data, start))
    override fun getNumberValue(original: BlockPos) = original.asLong()

    override fun toShort(value: BlockPos, offset: Int) = LongContainerDataSchemaType.toShort(value.asLong(), offset)
    override fun applyShort(short: Short, offset: Int, original: BlockPos) = BlockPos.of(LongContainerDataSchemaType.applyShort(short, offset, original.asLong()))

}
