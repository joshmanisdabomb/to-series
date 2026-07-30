package net.jidb.to.base.api.inventory.data

import net.jidb.to.base.api.helper.KotlinHelper.either
import net.minecraft.world.inventory.ContainerData

object BooleanContainerDataSchemaType : ContainerDataSchemaType<Boolean> {
    override val shortLength = 1

    override fun getValue(data: ContainerData, start: Int) = data.get(start) > 0
    override fun getNumberValue(original: Boolean) = original.either(1, 0).toShort()

    override fun toShort(value: Boolean, offset: Int) = getNumberValue(value)
    override fun applyShort(short: Short, offset: Int, original: Boolean): Boolean = short > 0
}