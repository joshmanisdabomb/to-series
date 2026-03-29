package net.jidb.to.base.helper

object KotlinHelper {

    fun <T> either(value: Boolean, `true`: T, `false`: T) = if (value) `true` else `false`
    @JvmName("eitherExt")
    fun <T> Boolean.either(`true`: T, `false`: T) = either(this, `true`, `false`)
    fun <T> orNull(value: Boolean, `true`: T) = if (value) `true` else null
    @JvmName("orNullExt")
    fun <T> Boolean.orNull(`true`: T) = orNull(this, `true`)

    inline fun <reified T> repeat(array: Array<T>, times: Int) = Array(array.size * times) { array[it % array.size] }
    inline operator fun <reified T> Array<T>.times(times: Int) = repeat(this, times)
    inline fun <reified T> repeat(list: Iterable<T>, times: Int): List<T> {
        val list = list.toList()
        return List(list.size * times) { list[it % list.toList().size] }
    }
    inline operator fun <reified T> Iterable<T>.times(times: Int) = repeat(this, times)

    fun squared(number: Byte) = number * number
    @JvmName("squaredByte")
    fun Byte.squared() = squared(this)
    fun squared(number: Short) = number * number
    @JvmName("squaredShort")
    fun Short.squared() = squared(this)
    fun squared(number: Int) = number * number
    @JvmName("squaredInt")
    fun Int.squared() = squared(this)
    fun squared(number: Long) = number * number
    @JvmName("squaredLong")
    fun Long.squared() = squared(this)
    fun squared(number: Double) = number * number
    @JvmName("squaredDouble")
    fun Double.squared() = squared(this)
    fun squared(number: Float) = number * number
    @JvmName("squaredFloat")
    fun Float.squared() = squared(this)

}