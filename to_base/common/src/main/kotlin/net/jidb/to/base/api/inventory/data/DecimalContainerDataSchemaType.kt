package net.jidb.to.base.api.inventory.data

import net.minecraft.world.inventory.ContainerData
import kotlin.math.pow

class DecimalContainerDataSchemaType(override val shortLength: Int, val decimals: Int) : ContainerDataSchemaType<Double> {

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
        }
        else {
            ((absValue shr (15 + (offset - 1) * 16)) and 0xFFFFL).toShort()
        }
    }

    override fun applyShort(short: Short, offset: Int, original: Double): Double {
        val first = (original * factor).toLong()
        val signBit = if ((first and SIGN_MASK) != 0L) 0x8000L else 0
        val magnitude = first and DATA_MASK

        val newMagnitude = if (offset == 0) {
            short.toLong() and DATA_MASK
        }
        else {
            val shift = 15 + (offset - 1) * 16
            val mask = (0xFFFFL shl shift)
            (magnitude and mask.inv()) or ((short.toLong() and 0xFFFFL) shl shift)
        }

        return ((newMagnitude or signBit).toDouble() / factor)
    }

    companion object {
        private const val SIGN_MASK = 0x8000L
        private const val DATA_MASK = 0x7FFFL
    }

}
