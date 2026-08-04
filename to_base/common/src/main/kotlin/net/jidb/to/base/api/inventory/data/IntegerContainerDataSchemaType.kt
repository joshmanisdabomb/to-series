package net.jidb.to.base.api.inventory.data

import net.minecraft.world.inventory.ContainerData

/**
 * A [ContainerDataSchemaType] for transporting an [Integer] as two [Short] values via [ContainerDataSchema] over the network.
 *
 * @since 0.7.0
 */
object IntegerContainerDataSchemaType : ContainerDataSchemaType<Int> {

    override val shortLength = 2

    override fun getValue(data: ContainerData, start: Int) = (data.get(start) and 0xFFFF) or ((data.get(start + 1) and 0xFFFF) shl 16)
    override fun getNumberValue(original: Int) = original

    override fun toShort(value: Int, offset: Int) = ((value shr (offset * 16)) and 0xFFFF).toShort()
    override fun applyShort(short: Short, offset: Int, original: Int): Int {
        val shift = offset * 16
        val mask = 0xFFFF shl shift
        return (original and mask.inv()) or ((short.toInt() and 0xFFFF) shl shift)
    }

}
