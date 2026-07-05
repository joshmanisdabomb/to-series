package net.jidb.to.base.api.inventory.data

import net.minecraft.world.inventory.ContainerData

interface ContainerDataSchemaType<O> {
    val shortLength: Int

    fun getValue(data: ContainerData, start: Int): O
    fun getNumberValue(original: O): Number
    fun getNumberValue(data: ContainerData, start: Int) = getNumberValue(getValue(data, start))

    fun toShort(value: O, offset: Int): Short
    fun toShortGeneric(value: Any, offset: Int) = toShort(value as O, offset)

    fun applyShort(short: Short, offset: Int, original: O): O
    fun createApplyFunction(short: Short, offset: Int) = object : ContainerDataSchemaApplyFunction {
        override fun <R> invoke(original: R): R {
            return applyShort(short, offset, original as O) as R
        }
    }
}