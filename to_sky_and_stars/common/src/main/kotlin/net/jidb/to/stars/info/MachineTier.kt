package net.jidb.to.stars.info

import net.minecraft.network.chat.TextColor
import net.minecraft.util.StringRepresentable

enum class MachineTier(val number: Float) : StringRepresentable {

    ONE(1.0f) {
        override val maxInput = 256000L
        override val maxOutput = 256000L

        override val batteryStorage = 4000000L
        override val bankStorage = 40000000L

        override val machineBuffer = 4000000L
        override val machineSpeed = 0.5f

        override val generatorHeat = 0.01f
        override val generatorInitial = 100f
        override val generatorRange = 250f
        override val generatorBonus = 50f
        override val generatorCooling = 0.99f

        override val turbineRate = 2f

        override val chatColor = 0xFFE38407.toInt()
    },
    ONE_5(1.5f) {
        override val maxInput = 512000L
        override val maxOutput = 512000L

        override val batteryStorage = 12000000L
        override val bankStorage = 100000000L

        override val machineBuffer = 10000000L
        override val machineSpeed = 0.75f

        override val generatorHeat = 0.015f
        override val generatorInitial = 100f
        override val generatorRange = 300f
        override val generatorBonus = 100f
        override val generatorCooling = 0.994f

        override val turbineRate = 2.5f

        override val chatColor = TextColor.YELLOW.value
    };

    abstract val maxInput: Long
    abstract val maxOutput: Long

    abstract val batteryStorage: Long
    abstract val bankStorage: Long

    abstract val machineBuffer: Long
    abstract val machineSpeed: Float

    abstract val generatorHeat: Float
    abstract val generatorInitial: Float
    abstract val generatorRange: Float
    abstract val generatorBonus: Float
    abstract val generatorCooling: Float

    abstract val turbineRate: Float

    abstract val chatColor: Int

    override fun getSerializedName() = name.lowercase()

    companion object {
        val codec = StringRepresentable.fromEnum(::values)
    }

}