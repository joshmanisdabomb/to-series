package net.jidb.to.base.api.inventory.data

import net.minecraft.world.inventory.ContainerData

/**
 * A [ContainerDataSchemaType] for transporting an [Long] as four [Short] values via [ContainerDataSchema] over the network.
 *
 * @since 0.7.0
 */
object LongContainerDataSchemaType : ContainerDataSchemaType<Long> {

    override val shortLength = 4

    override fun getValue(data: ContainerData, start: Int) = (data.get(start).toLong() and 0xFFFFL) or ((data.get(start + 1).toLong() and 0xFFFFL) shl 16) or ((data.get(start + 2).toLong() and 0xFFFFL) shl 32) or ((data.get(start + 3).toLong() and 0xFFFFL) shl 48)
    override fun getNumberValue(original: Long) = original

    override fun toShort(value: Long, offset: Int) = ((value shr (offset * 16)) and 0xFFFF).toShort()
    override fun applyShort(short: Short, offset: Int, original: Long): Long {
        val shift = offset * 16
        val mask = 0xFFFFL shl shift
        return (original and mask.inv()) or ((short.toLong() and 0xFFFFL) shl shift)
    }

}
