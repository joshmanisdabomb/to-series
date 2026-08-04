package net.jidb.to.base.api.inventory.data

import net.minecraft.world.inventory.ContainerData
import kotlin.math.pow

/**
 * A [ContainerDataSchemaType] for transporting a [Double] as multiple [Short] values via [ContainerDataSchema] over the network.
 *
 * The maximum size and precision of this decimal depends on how many [decimals] and how much [shortLength] is dedicated to it.
 * For example `shortLength = 2` and `decimals = 3` means a maximum value of `2147483.647`.
 *
 * @param shortLength The amount of shorts to dedicate to storing the decimal value.
 * @property decimals The amount of decimal places to store for this decimal value.
 * @since 0.7.0
 */

class DecimalContainerDataSchemaType(override val shortLength: Int, val decimals: Int) : ContainerDataSchemaType<Double> {

    /**
     * A computed scaling factor based on the number of decimals, calculated as 10 raised to the power of [decimals].
     *
     * @since 0.7.0
     */
    private val factor = 10.0.pow(decimals.toDouble())

    override fun getValue(data: ContainerData, start: Int): Double {
        val first = data.get(start).toLong() and 0xFFFFL
        val signBit = first and SIGN_MASK
        var magnitude = first and DATA_MASK

        for (i in 1 until shortLength) {
            magnitude = magnitude or ((data.get(start + i).toLong() and 0xFFFFL) shl (15 + (i - 1) * 16))
        }

        val scaled = if (signBit != 0L) -magnitude else magnitude
        return scaled / factor
    }

    override fun getNumberValue(original: Double) = original

    override fun toShort(value: Double, offset: Int): Short {
        val scaled = (value * factor).toLong()
        val isNegative = scaled < 0L
        val absValue = if (isNegative) -scaled else scaled

        return if (offset == 0) {
            val bits = (absValue and DATA_MASK).toInt()
            val sign = if (isNegative) 0x8000 else 0
            (bits or sign).toShort()
        } else {
            ((absValue shr (15 + (offset - 1) * 16)) and 0xFFFFL).toShort()
        }
    }

    override fun applyShort(short: Short, offset: Int, original: Double): Double {
        val first = (original * factor).toLong()
        val signBit = if ((first and SIGN_MASK) != 0L) 0x8000L else 0
        val magnitude = first and DATA_MASK

        val newMagnitude = if (offset == 0) {
            short.toLong() and DATA_MASK
        } else {
            val shift = 15 + (offset - 1) * 16
            val mask = (0xFFFFL shl shift)
            (magnitude and mask.inv()) or ((short.toLong() and 0xFFFFL) shl shift)
        }

        return ((newMagnitude or signBit).toDouble() / factor)
    }

    companion object {

        /**
         * A constant mask used to identify or manipulate the sign bit in a numerical value.
         * This mask typically focuses on the most significant bit to determine whether a value
         * is positive or negative in signed binary representations.
         *
         * @since 0.7.0
         */
        private const val SIGN_MASK = 0x8000L

        /**
         * A constant bitmask used to extract significant bits from an encoded short value.
         * This mask is applied to limit the data to the 15 least significant bits,
         * ensuring compatibility with the defined schema operations.
         *
         * @since 0.7.0
         */
        private const val DATA_MASK = 0x7FFFL

    }

}
