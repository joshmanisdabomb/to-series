package net.jidb.to.base.api.inventory

import net.minecraft.core.BlockPos
import net.minecraft.world.inventory.ContainerData

class ContainerDataSchema<K : Any> {

    private val types = mutableMapOf<K, Type>()

    fun defineShort(name: K): ContainerDataSchema<K> {
        types[name] = Type.SHORT
        return this
    }

    fun defineInt(name: K): ContainerDataSchema<K> {
        types[name] = Type.INT
        return this
    }

    fun defineLong(name: K): ContainerDataSchema<K> {
        types[name] = Type.LONG
        return this
    }

    fun definePos(name: K): ContainerDataSchema<K> {
        types[name] = Type.POS
        return this
    }

    fun get(key: Int, getter: (index: K) -> Any): Int {
        var currentIndex = 0
        for ((k, type) in types) {
            if (key >= currentIndex && key < currentIndex + type.size) {
                return when (type) {
                    Type.SHORT -> (getter(k) as Short).toInt()
                    Type.INT -> {
                        val value = getter(k) as Int
                        val offset = key - currentIndex
                        (value shr (offset * 16)) and 0xFFFF
                    }
                    Type.LONG -> {
                        val value = getter(k) as Long
                        val offset = key - currentIndex
                        ((value shr (offset * 16)) and 0xFFFF).toInt()
                    }
                    Type.POS -> {
                        val value = getter(k) as BlockPos
                        val offset = key - currentIndex
                        ((value.asLong() shr (offset * 16)) and 0xFFFF).toInt()
                    }
                }
            }
            currentIndex += type.size
        }
        return 0
    }

    fun getShort(data: ContainerData, key: K) = data.get(getDataIndex(key, Type.SHORT)).toShort()

    fun getInt(data: ContainerData, key: K): Int {
        val index = getDataIndex(key, Type.INT)
        return (data.get(index) and 0xFFFF) or ((data.get(index + 1) and 0xFFFF) shl 16)
    }

    fun getLong(data: ContainerData, key: K): Long {
        val index = getDataIndex(key, Type.LONG, Type.POS)
        return (data.get(index).toLong() and 0xFFFFL) or ((data.get(index + 1).toLong() and 0xFFFFL) shl 16) or ((data.get(index + 2).toLong() and 0xFFFFL) shl 32) or ((data.get(index + 3).toLong() and 0xFFFFL) shl 48)
    }

    fun getPos(data: ContainerData, key: K) = BlockPos.of(getLong(data, key))

    fun set(key: Int, value: Short, setter: (index: K, apply: TypeApplyFunction) -> Unit) {
        var currentIndex = 0
        for ((k, type) in types) {
            if (key >= currentIndex && key < currentIndex + type.size) {
                val offset = key - currentIndex
                setter(k, object : TypeApplyFunction {
                    override fun invoke(original: Short) = value
                    override fun invoke(original: Int): Int {
                        val shift = offset * 16
                        val mask = 0xFFFF shl shift
                        return (original and mask.inv()) or ((value.toInt() and 0xFFFF) shl shift)
                    }
                    override fun invoke(original: Long): Long {
                        val shift = offset * 16
                        val mask = 0xFFFFL shl shift
                        return (original and mask.inv()) or ((value.toLong() and 0xFFFFL) shl shift)
                    }
                    override fun invoke(original: BlockPos): BlockPos {
                        val shift = offset * 16
                        val mask = 0xFFFFL shl shift
                        return BlockPos.of((original.asLong() and mask.inv()) or ((value.toLong() and 0xFFFFL) shl shift))
                    }
                })
                return
            }
            currentIndex += type.size
        }
    }

    private fun getDataIndex(key: K, vararg expectedTypes: Type): Int {
        var currentIndex = 0
        for ((k, type) in types) {
            if (k == key) {
                require(type in expectedTypes) { "Container data key $key is $type, not ${expectedTypes.joinToString()}." }
                return currentIndex
            }
            currentIndex += type.size
        }
        error("Container data key $key is not defined.")
    }

    fun getDataSize() = types.values.sumOf(Type::size)

    enum class Type(val size: Int) {
        SHORT(1), INT(2), LONG(4), POS(4)
    }

    interface TypeApplyFunction {
        operator fun invoke(original: Short): Short
        operator fun invoke(original: Int): Int
        operator fun invoke(original: Long): Long
        operator fun invoke(original: BlockPos): BlockPos
    }

}
