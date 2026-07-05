package net.jidb.to.base.api.inventory.data

import net.minecraft.core.BlockPos
import net.minecraft.world.inventory.ContainerData

class ContainerDataSchema<K : Any> {

    private val types = mutableMapOf<K, ContainerDataSchemaType<*>>()

    fun define(name: K, type: ContainerDataSchemaType<*>): ContainerDataSchema<K> {
        types[name] = type
        return this
    }

    fun defineShort(name: K) = define(name, ShortContainerDataSchemaType)
    fun defineInt(name: K) = define(name, IntegerContainerDataSchemaType)
    fun defineLong(name: K)  = define(name, LongContainerDataSchemaType)
    fun definePos(name: K) = define(name, BlockPosContainerDataSchemaType)
    fun defineDecimal(name: K, shorts: Int = 2, decimals: Int = 3) = define(name, DecimalContainerDataSchemaType(shorts, decimals))

    fun getShort(key: Int, getter: (index: K) -> Any?): Int? {
        var currentIndex = 0
        for ((k, type) in types) {
            if (key >= currentIndex && key < currentIndex + type.shortLength) {
                val raw = getter(k)
                if (raw is Unit || raw == null) return null
                return type.toShortGeneric(raw, key - currentIndex).toInt()
            }
            currentIndex += type.shortLength
        }
        return null
    }

    fun <O : Any> getValue(data: ContainerData, key: K, type: ContainerDataSchemaType<O>) = type.getValue(data, getDataIndex(key))
    fun getValue(data: ContainerData, key: K) = types[key]?.getValue(data, getDataIndex(key))
    fun getNumberValue(data: ContainerData, key: K) = types[key]?.getNumberValue(data, getDataIndex(key))

    private fun getDataIndex(key: K): Int {
        var currentIndex = 0
        for ((k, type) in types) {
            if (k == key) {
                return currentIndex
            }
            currentIndex += type.shortLength
        }
        error("Container data key $key is not defined.")
    }

    fun getShortValue(data: ContainerData, key: K) = getNumberValue(data, key)?.toShort()
    fun getIntValue(data: ContainerData, key: K) = getNumberValue(data, key)?.toInt()
    fun getLongValue(data: ContainerData, key: K) = getNumberValue(data, key)?.toLong()
    fun getPosValue(data: ContainerData, key: K) = getNumberValue(data, key)?.toLong()?.apply(BlockPos::of)
    fun getFloatValue(data: ContainerData, key: K) = getNumberValue(data, key)?.toFloat()
    fun getDoubleValue(data: ContainerData, key: K) = getNumberValue(data, key)?.toDouble()

    fun set(key: Int, value: Short, setter: (index: K, fn: ContainerDataSchemaApplyFunction) -> Unit): Boolean {
        var currentIndex = 0
        for ((k, type) in types) {
            if (key >= currentIndex && key < currentIndex + type.shortLength) {
                val offset = key - currentIndex
                setter(k, type.createApplyFunction(value, offset))
                return true
            }
            currentIndex += type.shortLength
        }
        return false
    }

    fun getDataSize() = types.values.sumOf(ContainerDataSchemaType<*>::shortLength)

}
