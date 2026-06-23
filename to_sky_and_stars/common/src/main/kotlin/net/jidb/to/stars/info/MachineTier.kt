package net.jidb.to.stars.info

import net.minecraft.ChatFormatting
import net.minecraft.util.StringRepresentable

enum class MachineTier(val number: Float) : StringRepresentable {

    ONE(1.0f) {
        override val maxInput = 256L
        override val maxOutput = 256L

        override val batteryStorage = 4000L
        override val bankStorage = 40000L

        override val machineBuffer = 4000L
        override val machineSpeed = 0.5f

        override val chatColor = 0xFFE38407.toInt()
    },
    ONE_5(1.5f) {
        override val maxInput = 512L
        override val maxOutput = 512L

        override val batteryStorage = 12000L
        override val bankStorage = 100000L

        override val machineBuffer = 10000L
        override val machineSpeed = 0.75f

        override val chatColor = ChatFormatting.YELLOW.color!!
    };

    abstract val maxInput: Long
    abstract val maxOutput: Long

    abstract val batteryStorage: Long
    abstract val bankStorage: Long

    abstract val machineBuffer: Long
    abstract val machineSpeed: Float

    abstract val chatColor: Int

    override fun getSerializedName() = name.lowercase()

    companion object {
        val CODEC = StringRepresentable.fromEnum(::values)
    }

}