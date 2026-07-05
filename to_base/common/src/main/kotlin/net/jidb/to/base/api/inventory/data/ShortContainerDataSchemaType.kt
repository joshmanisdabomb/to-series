package net.jidb.to.base.api.inventory.data

import net.minecraft.world.inventory.ContainerData

object ShortContainerDataSchemaType : ContainerDataSchemaType<Short> {
    override val shortLength = 1

    override fun getValue(data: ContainerData, start: Int) = data.get(start).toShort()
    override fun getNumberValue(original: Short) = original

    override fun toShort(value: Short, offset: Int) = value
    override fun applyShort(short: Short, offset: Int, original: Short): Short = short
}