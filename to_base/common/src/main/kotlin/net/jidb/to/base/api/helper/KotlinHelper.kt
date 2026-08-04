package net.jidb.to.base.api.helper

/**
 * Utility object that provides a collection of helper methods and extensions for common operations in Kotlin.
 * Includes conditional selection, mathematical calculations, array manipulations, etc.
 *
 * @since 0.1.0
 */
object KotlinHelper {

    /**
     * Evaluates a [Boolean] condition and returns one of the two provided values.
     * Equivalent of a ternary `if (value) true else false`.
     *
     * @param T The type of both `true` and `false` that could be returned.
     * @param value The [Boolean] condition to evaluate.
     * @param true The value to return if the condition is true.
     * @param false The value to return if the condition is false.
     * @return Returns `true` if the condition is true, otherwise `false`.
     * @since 0.1.0
     */
    fun <T> either(value: Boolean, `true`: T, `false`: T) = if (value) `true` else `false`

    /**
     * Evaluates the receiving [Boolean] and returns one of the two provided values.
     * Equivalent of a ternary `if (value) true else false`.
     *
     * @param T The type of both `true` and `false` that could be returned.
     * @param true The value to return if the condition is true.
     * @param false The value to return if the condition is false.
     * @return Returns `true` if the condition is true, otherwise `false`.
     * @since 0.1.0
     */
    @JvmName("eitherExt")
    fun <T> Boolean.either(`true`: T, `false`: T) = either(this, `true`, `false`)

    /**
     * Returns the provided value if the [Boolean] condition is true, otherwise returns null.
     *
     * @param T The type of `true` that could be returned.
     * @param value A [Boolean] condition that determines which value to return.
     * @param true The value to return if the condition is true.
     * @return The provided value if the condition is true, or `null` otherwise.
     * @since 0.1.0
     */
    fun <T> orNull(value: Boolean, `true`: T) = if (value) `true` else null

    /**
     * Returns the provided value if the receiving [Boolean] is true, otherwise returns null.
     *
     * @param T The type of `true` that could be returned.
     * @param true The value to return if the condition is true.
     * @return The provided value if the condition is true, or `null` otherwise.
     * @since 0.1.0
     */
    @JvmName("orNullExt")
    fun <T> Boolean.orNull(`true`: T) = orNull(this, `true`)

    /**
     * Quickly calculates the square of the given [Byte] with [Byte.times] rather than [kotlin.math.pow].
     *
     * @param number The [Byte] value to be squared.
     * @return The square of the input number.
     * @since 0.2.0
     */
    fun squared(number: Byte) = number * number

    /**
     * Quickly calculates the square of the receiver [Byte] with [Byte.times] rather than [kotlin.math.pow].
     *
     * @return The square of the input number.
     * @since 0.2.0
     */
    @JvmName("squaredByte")
    fun Byte.squared() = squared(this)

    /**
     * Quickly calculates the square of the given [Short] with [Short.times] rather than [kotlin.math.pow].
     *
     * @param number The [Short] value to be squared.
     * @return The square of the input number.
     * @since 0.2.0
     */
    fun squared(number: Short) = number * number

    /**
     * Quickly calculates the square of the receiver [Short] with [Short.times] rather than [kotlin.math.pow].
     *
     * @return The square of the input number.
     * @since 0.2.0
     */
    @JvmName("squaredShort")
    fun Short.squared() = squared(this)

    /**
     * Quickly calculates the square of the given [Int] with [Int.times] rather than [kotlin.math.pow].
     *
     * @param number The [Int] value to be squared.
     * @return The square of the input number.
     * @since 0.2.0
     */
    fun squared(number: Int) = number * number

    /**
     * Quickly calculates the square of the receiver [Int] with [Int.times] rather than [kotlin.math.pow].
     *
     * @return The square of the input number.
     * @since 0.2.0
     */
    @JvmName("squaredInt")
    fun Int.squared() = squared(this)

    /**
     * Quickly calculates the square of the given [Long] with [Long.times] rather than [kotlin.math.pow].
     *
     * @param number The [Long] value to be squared.
     * @return The square of the input number.
     * @since 0.2.0
     */
    fun squared(number: Long) = number * number

    /**
     * Quickly calculates the square of the receiver [Long] with [Long.times] rather than [kotlin.math.pow].
     *
     * @return The square of the input number.
     * @since 0.2.0
     */
    @JvmName("squaredLong")
    fun Long.squared() = squared(this)

    /**
     * Quickly calculates the square of the given [Double] with [Double.times] rather than [kotlin.math.pow].
     *
     * @param number The [Double] value to be squared.
     * @return The square of the input number.
     * @since 0.2.0
     */
    fun squared(number: Double) = number * number

    /**
     * Quickly calculates the square of the receiver [Double] with [Double.times] rather than [kotlin.math.pow].
     *
     * @return The square of the input number.
     * @since 0.2.0
     */
    @JvmName("squaredDouble")
    fun Double.squared() = squared(this)

    /**
     * Quickly calculates the square of the given [Float] with [Float.times] rather than [kotlin.math.pow].
     *
     * @param number The [Float] value to be squared.
     * @return The square of the input number.
     * @since 0.2.0
     */
    fun squared(number: Float) = number * number

    /**
     * Quickly calculates the square of the receiver [Float] with [Float.times] rather than [kotlin.math.pow].
     *
     * @return The square of the input number.
     * @since 0.2.0
     */
    @JvmName("squaredFloat")
    fun Float.squared() = squared(this)

    /**
     * Creates a new [Array] by repeating the elements of the given [Array] a specified number of times.
     *
     * @param T The type of items in the input [Array].
     * @param array The [Array] whose elements will be repeated.
     * @param times The number of times to repeat the elements of the [Array].
     * @return A new [Array] containing the elements of the input [Array] repeated.
     * @since 0.1.0
     */
    inline fun <reified T> repeat(array: Array<T>, times: Int) = Array(array.size * times) { array[it % array.size] }

    /**
     * Creates a new [Array] by repeating the elements of the receiving [Array] a specified number of times.
     *
     * @param T The type of items in the input [Array].
     * @param times The number of times to repeat the elements of the [Array].
     * @return A new [Array] containing the elements of the input [Array] repeated.
     * @since 0.1.0
     */
    inline operator fun <reified T> Array<T>.times(times: Int) = repeat(this, times)

    /**
     * Creates a new [List] by repeating the elements of the given [Iterable] a specified number of times.
     *
     * @param T The type of items in the input [Iterable].
     * @param list The [Iterable] whose elements will be repeated.
     * @param times The number of times to repeat the elements of the [Iterable].
     * @return A new [List] containing the elements of the input [Iterable] repeated.
     * @since 0.1.0
     */
    inline fun <reified T> repeat(list: Iterable<T>, times: Int): List<T> {
        val list = list.toList()
        return List(list.size * times) { list[it % list.toList().size] }
    }

    /**
     * Creates a new [List] by repeating the elements of the receiving [Iterable] a specified number of times.
     *
     * @param T The type of items in the input [Iterable].
     * @param times The number of times to repeat the elements of the [Iterable].
     * @return A new [List] containing the elements of the input [Iterable] repeated.
     * @since 0.1.0
     */
    inline operator fun <reified T> Iterable<T>.times(times: Int) = repeat(this, times)

    /**
     * Filters out null values from the given [Array] and returns a new [Array] containing only non-null elements.
     * Similar to [kotlin.collections.filterNotNull] but for [Array] types.
     *
     * @param T The type of items in the input [Array].
     * @param array An [Array] of nullable elements of type [T] to be filtered.
     * @return A new [Array] containing only the non-null elements from the input [Array].
     * @since 0.3.0
     */
    inline fun <reified T> filterNotNull(array: Array<T?>): Array<T> = array.mapNotNull { it }.toTypedArray()

    /**
     * Filters out null values from the receiver [Array] and returns a new [Array] containing only non-null elements.
     * Similar to [kotlin.collections.filterNotNull] but for [Array] types.
     *
     * @param T The type of element, excluding null, that can be in the input [Array].
     * @return A new [Array] containing only the non-null elements from the input [Array].
     * @since 0.3.0
     */
    @JvmName("filterNotNullExt")
    inline fun <reified T> Array<T?>.filterNotNull(): Array<T> = filterNotNull(this)

    /**
     * Transposes a two-dimensional [List], converting rows to columns and vice versa.
     *
     * @param T The type of elements in each sublist.
     * @param list A two-dimensional [List] where all sublists have the same length.
     * @return A transposed two-dimensional [List] where rows are converted to columns.
     * @throws IllegalArgumentException if the sublists have different lengths.
     * @since 0.8.0
     */
    fun <T> transpose(list: List<List<T>>): List<List<T>> {
        if (list.isEmpty()) return emptyList()
        val size = list.first().size
        require(list.all { it.size == size }) { "All rows must have the same length." }

        return List(size) { col -> List(list.size) { row -> list[row][col] } }
    }

    /**
     * Transposes the two-dimensional receiver [List], converting rows to columns and vice versa.
     *
     * @param T The type of elements in each sublist.
     * @return A transposed two-dimensional [List] where rows are converted to columns.
     * @throws IllegalArgumentException if the sublists have different lengths.
     * @since 0.8.0
     */
    @JvmName("transposeExt")
    fun <T> List<List<T>>.transpose(): List<List<T>> = transpose(this)

}
