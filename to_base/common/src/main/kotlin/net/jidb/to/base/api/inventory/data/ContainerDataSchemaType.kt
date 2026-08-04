package net.jidb.to.base.api.inventory.data

import net.minecraft.world.inventory.ContainerData

/**
 * Represents a schema type used by [ContainerDataSchema] to define how a field data type should be converted to and from a [Short], required by [ContainerData].
 *
 * This interface is designed to support various primitive types like [Short], [Long], [Boolean], and also complex types like [net.minecraft.core.BlockPos].
 *
 * @param O The type of the object that this schema type processes.
 * @since 0.7.0
 */
interface ContainerDataSchemaType<O> {

    /**
     * The amount of 16-bit [Short] values that this schema type requires to store [O] in a [ContainerData].
     * @since 0.7.0
     */
    val shortLength: Int

    /**
     * Retrieves a value of type [O] from the provided [ContainerData] instance, starting at the specified index.
     *
     * @param data The raw [ContainerData] instance to retrieve the value from.
     * @param start The starting [Short] index within [data] to build the value from, reading up to [shortLength] shorts.
     * @return The value of type [O] reconstructed from the [Short] values in [ContainerData].
     * @since 0.7.0
     */
    fun getValue(data: ContainerData, start: Int): O

    /**
     * Converts the provided object of type [O] into a numeric representation.
     *
     * @param original The object of type [O] to be converted into a [Number] value.
     * @return A [Number] that represents [O].
     * @since 0.7.0
     */
    fun getNumberValue(original: O): Number

    /**
     * Retrieves the [Number] representation of a value from a provided [ContainerData] instance, starting at the specified index.
     *
     * This method first reconstructs a value of type [O] from the given [ContainerData] using [getValue] and then converts this value with [getNumberValue]
     *
     * @param data The raw [ContainerData] instance to retrieve the value from.
     * @param start The starting [Short] index within [data] to build the value from, reading up to [shortLength] shorts.
     * @return A [Number] that represents [O].
     * @since 0.7.0
     */
    fun getNumberValue(data: ContainerData, start: Int) = getNumberValue(getValue(data, start))

    /**
     * Converts the provided value of type [O] into a [Short] based on the specified offset.
     *
     * @param value The object of type [O] to be converted into a [Short].
     * @param offset The offset used to determine which [Short] of the value should be returned.
     * @return The [Short] representation of the value derived from the given offset.
     * @since 0.7.0
     */
    fun toShort(value: O, offset: Int): Short

    /**
     * This method casts the input [value] to the generic type [O] and then applies the conversion logic defined in the [toShort] method.
     *
     * @param value The input value of type [Any] to be converted into a [Short]. This value will be cast to the generic type [O] at runtime.
     * @param offset The offset used to determine which [Short] of the value should be returned.
     * @return The [Short] representation of the value derived from the given offset.
     * @since 0.7.0
     */
    fun toShortGeneric(value: Any, offset: Int) = toShort(value as O, offset)

    /**
     * Edits a [Short] value at the specified offset for the given original object of type [O].
     *
     * @param short The [Short] value to be applied.
     * @param offset The offset indicating where in the original object the [Short] value should be set.
     * @param original The original object of type [O] to which the [Short] value will be applied.
     * @return The modified object of type [O] with the [Short] value applied at the specified offset.
     * @since 0.7.0
     */
    fun applyShort(short: Short, offset: Int, original: O): O

    /**
     * Creates an instance of a [ContainerDataSchemaApplyFunction] that applies a specific [Short] value to an object of type [O] at a given offset.
     *
     * @param short The [Short] value to be applied.
     * @param offset The offset indicating where in the original object the [Short] value should be set.
     * @return A [ContainerDataSchemaApplyFunction] that encapsulates the logic for applying the specified [Short] value at the given offset.
     * @since 0.7.0
     */
    fun createApplyFunction(short: Short, offset: Int) = object : ContainerDataSchemaApplyFunction {

        override fun <R> invoke(original: R) = applyShort(short, offset, original as O) as R

    }

}
