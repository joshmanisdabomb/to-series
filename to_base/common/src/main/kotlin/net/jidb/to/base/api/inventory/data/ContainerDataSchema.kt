package net.jidb.to.base.api.inventory.data

import net.minecraft.core.BlockPos
import net.minecraft.world.inventory.ContainerData

/**
 * A wrapper for managing data of a [ContainerData], by allowing any [ContainerDataSchemaType] to send any object through one or many [Short] values over the network.
 * Fields are also keyed by a name type [K], which means you no longer need to keep track of the order of fields in [ContainerData].
 *
 * For example, this class can be used for sending [Long], [Float] or complex objects like [BlockPos] using [ContainerData], which typically only supports [Short] values.
 *
 * @param K The type of the key used to identify data fields within the schema, e.g. [String] or an [Enum].
 * @since 0.6.0
 */
class ContainerDataSchema<K : Any> {

    /**
     * Associates keys of type [K] with schema types of [ContainerDataSchemaType].
     *
     * @since 0.6.0
     */
    private val types = mutableMapOf<K, ContainerDataSchemaType<*>>()

    /**
     * Builder function to define a new field, with the given name and type, that can be sent over the network.
     *
     * @param name The name of the field to define in the schema.
     * @param type The type of the field, represented by a [ContainerDataSchemaType] instance.
     * @return The [ContainerDataSchema] instance for further chaining.
     * @since 0.7.0
     */
    fun define(name: K, type: ContainerDataSchemaType<*>): ContainerDataSchema<K> {
        types[name] = type
        return this
    }

    /**
     * Builder function to define a new named [Boolean] that can be sent over the network.
     *
     * @param name The name of the [Boolean] field to define in the schema.
     * @return The [ContainerDataSchema] instance for further chaining.
     * @since 0.8.0
     */
    fun defineBool(name: K) = define(name, BooleanContainerDataSchemaType)

    /**
     * Builder function to define a new named [Short] that can be sent over the network.
     *
     * @param name The name of the [Short] field to define in the schema.
     * @return The [ContainerDataSchema] instance for further chaining.
     * @since 0.6.0
     */
    fun defineShort(name: K) = define(name, ShortContainerDataSchemaType)

    /**
     * Builder function to define a new named [Int] that can be sent over the network.
     *
     * @param name The name of the [Int] field to define in the schema.
     * @return The [ContainerDataSchema] instance for further chaining.
     * @since 0.6.0
     */
    fun defineInt(name: K) = define(name, IntegerContainerDataSchemaType)

    /**
     * Builder function to define a new named [Long] that can be sent over the network.
     *
     * @param name The name of the [Long] field to define in the schema.
     * @return The [ContainerDataSchema] instance for further chaining.
     * @since 0.6.0
     */
    fun defineLong(name: K) = define(name, LongContainerDataSchemaType)

    /**
     * Builder function to define a new named [BlockPos] that can be sent over the network.
     *
     * @param name The name of the [BlockPos] field to define in the schema.
     * @return The [ContainerDataSchema] instance for further chaining.
     * @since 0.6.0
     */
    fun definePos(name: K) = define(name, BlockPosContainerDataSchemaType)

    /**
     * Builder function to define a new named [Double] that can be sent over the network.
     * The maximum size and precision of this decimal depends on how many [shorts] and [decimals] are dedicated to it.
     * For example `shorts = 2` and `decimals = 3` means a maximum value of `2147483.647`.
     *
     * @param name The name of the [Double] field to define in the schema.
     * @param shorts The amount of shorts to dedicate to storing the decimal value.
     * @param decimals The amount of decimal places to store for this decimal value.
     * @return The [ContainerDataSchema] instance for further chaining.
     * @since 0.7.0
     */
    fun defineDecimal(name: K, shorts: Int = 2, decimals: Int = 3) = define(name, DecimalContainerDataSchemaType(shorts, decimals))

    /**
     * Includes all types from the given schema into the current schema, transforming their names using a conversion function.
     * This could be useful if you want to include an existing schema as a subset of your new schema, even if it uses different keys.
     *
     * @param O The types of the keys of the schema being copied from.
     * @param schema The source schema containing types to include.
     * @param convert A function to transform the names of the included types from the source schema.
     * @return The current schema instance with the included types for further chaining.
     * @since 0.8.0
     */
    fun <O : Any> defineInclude(schema: ContainerDataSchema<O>, convert: (O) -> K): ContainerDataSchema<K> {
        for ((name, type) in schema.types) {
            define(convert(name), type)
        }
        return this
    }

    /**
     * Includes all types from the given schema into the current schema.
     * This could be useful if you want to include an existing schema as a subset of your new schema.
     *
     * @param schema The source schema containing types to include.
     * @return The current schema instance with the included types for further chaining.
     * @since 0.8.0
     */
    fun defineInclude(schema: ContainerDataSchema<K>) = defineInclude(schema) { it }

    /**
     * Retrieves the [Short] based on the given [ContainerData] index.
     * This is done by:
     * - Using the schema to get the key name with a [Short] at that index.
     * - Getting the full value from the provided lookup function.
     * - Then querying the [Short] at that index for that type using [ContainerDataSchemaType.toShort].
     *
     * Intended to be used as a "wrapper" within [ContainerData.get]:
     * ```kotlin
     * object : ContainerData {
     *     override fun get(key: Int) = dataSchema.getShort(key) { when (it) {
     *         "heat" -> this@BlockEntity.heat // Float
     *         "energy" -> this@BlockEntity.energy // Long
     *     } } ?: 0
     * }
     * ```
     *
     * @param key The index key used to locate the desired value in the schema.
     * @param getter A function that retrieves a full value for a given schema field key. The function takes a key of type [K] and returns a value of any type, which will be cast by the [ContainerDataSchemaType] associated with the key.
     * @return The retrieved value as an [Int] (cast [Short]) if found and valid, or `null` if the value is unavailable or invalid.
     * @since 0.7.0
     */
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

    /**
     * Retrieves a full value from the given [ContainerData], using the specified schema type.
     *
     * @param O The type of the value to retrieve.
     * @param data The [ContainerData] instance from which the value will be extracted.
     * @param key The key used to locate the desired value within the data.
     * @param type The schema type defining the type of the value to retrieve, converted from [Short] values stored in [ContainerData].
     * @return The full value corresponding to the given key and schema type.
     * @since 0.7.0
     */
    fun <O : Any> getValue(data: ContainerData, key: K, type: ContainerDataSchemaType<O>) = type.getValue(data, getDataIndex(key))

    /**
     * Retrieves the full value from the given [ContainerData], using the specified key and previously defined schema type.
     *
     * @param data The [ContainerData] instance from which to retrieve the value.
     * @param key The key used to locate the desired value and corresponding type in the schema.
     * @return The full value associated with the specified key, or `null` if the key has not been defined with a type.
     * @since 0.7.0
     */
    fun getValue(data: ContainerData, key: K) = types[key]?.getValue(data, getDataIndex(key))

    /**
     * Retrieves the full numeric value from the given [ContainerData], using the specified key and previously defined schema type.
     *
     * @param data The [ContainerData] instance from which to retrieve the value.
     * @param key The key used to locate the desired value and corresponding type in the schema.
     * @return The full, numeric value associated with the specified key, or `null` if the key has not been defined with a type.
     * @since 0.7.0
     */
    fun getNumberValue(data: ContainerData, key: K) = types[key]?.getNumberValue(data, getDataIndex(key))

    /**
     * Calculates the starting [Short] index of the given key in a [ContainerData].
     *
     * @param key The key for which to calculate the index within the schema.
     * @return The index corresponding to the given key.
     * @throws IllegalStateException If the key is not defined in the schema.
     * @since 0.6.0
     */
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

    /**
     * Retrieves a [Boolean] value from the given [ContainerData].
     * Any schema type could have been used for this key; the return bool is based on if the full numeric value for this key is > 0.
     *
     * @param data The [ContainerData] instance from which the [Boolean] value will be retrieved.
     * @param key The key used to locate the corresponding full value within the data.
     * @since 0.8.0
     * @return `true` if the retrieved [Short] value is greater than 0, `false` if it is 0 or less, or `null` if the key does not correspond to a defined value.
     */
    fun getBoolValue(data: ContainerData, key: K) = getShortValue(data, key)?.let { it > 0 }

    /**
     * Retrieves a [Short] value from the given [ContainerData].
     *
     * @param data The [ContainerData] instance from which the [Short] value will be retrieved.
     * @param key The key used to locate the corresponding [Short] within the data.
     * @return the [Short] at the given key.
     * @since 0.7.0
     */
    fun getShortValue(data: ContainerData, key: K) = getNumberValue(data, key)?.toShort()

    /**
     * Retrieves a [Int] value from the given [ContainerData].
     *
     * @param data The [ContainerData] instance from which the [Int] value will be retrieved.
     * @param key The key used to locate the corresponding [Int] within the data.
     * @return the [Int] at the given key.
     * @since 0.7.0
     */
    fun getIntValue(data: ContainerData, key: K) = getNumberValue(data, key)?.toInt()

    /**
     * Retrieves a [Long] value from the given [ContainerData].
     *
     * @param data The [ContainerData] instance from which the [Long] value will be retrieved.
     * @param key The key used to locate the corresponding [Long] within the data.
     * @return the [Long] at the given key.
     * @since 0.7.0
     */
    fun getLongValue(data: ContainerData, key: K) = getNumberValue(data, key)?.toLong()

    /**
     * Retrieves a [BlockPos] value from the given [ContainerData].
     *
     * @param data The [ContainerData] instance from which the [BlockPos] value will be retrieved.
     * @param key The key used to locate the corresponding [BlockPos] within the data.
     * @return the [BlockPos] at the given key.
     * @since 0.7.0
     */
    fun getPosValue(data: ContainerData, key: K) = getNumberValue(data, key)?.toLong()?.apply(BlockPos::of)

    /**
     * Retrieves a [Float] value from the given [ContainerData].
     *
     * @param data The [ContainerData] instance from which the [Float] value will be retrieved.
     * @param key The key used to locate the corresponding [Float] within the data.
     * @return the [Float] at the given key.
     * @since 0.7.0
     */
    fun getFloatValue(data: ContainerData, key: K) = getNumberValue(data, key)?.toFloat()

    /**
     * Retrieves a [Double] value from the given [ContainerData].
     *
     * @param data The [ContainerData] instance from which the [Double] value will be retrieved.
     * @param key The key used to locate the corresponding [Double] within the data.
     * @return the [Double] at the given key.
     * @since 0.7.0
     */
    fun getDoubleValue(data: ContainerData, key: K) = getNumberValue(data, key)?.toDouble()

    /**
     * Sets the given [Short] to [ContainerData] based on the given index.
     *
     * This is done by:
     * - Using the schema to get the key name with a [Short] at that index.
     * - Creating an [ContainerDataSchemaApplyFunction] based on the current full value at that key.
     * - Running the apply function in the setter function.
     *
     * Intended to be used as a "wrapper" within [ContainerData.set]:
     * ```kotlin
     * object : ContainerData {
     *     override fun set(key: Int) {
     *         HeatGeneratorMenu.dataSchema.set(key, value.toShort()) { index, fn -> when (index) {
     *             "heat" -> this@BlockEntity.heat = fn(this@BlockEntity.heat) // Float
     *             "energy" -> this@BlockEntity.energy = fn(this@BlockEntity.energy) // Long
     *         }
     *     }
     * }
     * ```
     *
     * @param key The index key used to locate the desired value to set in the schema.
     * @param value The [Short] value to set to your variable.
     * @param setter A function that applies a [Short] with a relative index to a full value.
     * @since 0.6.0
     * @return `true` if the value was successfully set, `false` if the key was outside valid ranges.
     */
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

    /**
     * Calculates the total [ContainerData] size by summing the [ContainerDataSchemaType.shortLength] values of all defined types.
     *
     * @return The total sum of the [ContainerDataSchemaType.shortLength] values for all elements.
     * @since 0.6.0
     */
    fun getDataSize() = types.values.sumOf(ContainerDataSchemaType<*>::shortLength)

}
