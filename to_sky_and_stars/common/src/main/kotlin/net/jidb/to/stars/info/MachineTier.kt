package net.jidb.to.stars.info

import net.minecraft.network.chat.TextColor
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.util.ByIdMap
import net.minecraft.util.StringRepresentable

/**
 * Enum that defines the tiers a machine can be built at, which is what every figure a machine works from is read off.
 * A tier is named for the material its casing is made of, and its number is what the wiki and the tooltips call it.
 *
 * @property number The tier as it is written to a player, i.e. `1.0` or `1.5`.
 */
enum class MachineTier(val number: Float) : StringRepresentable {

    /**
     * The copper tier, which is the first a player can build.
     */
    ONE(1.0f) {

        override val maxInput = 256000L
        override val maxOutput = 256000L

        override val batteryStorage = 4000000L
        override val bankStorage = 40000000L

        override val machineBuffer = 4000000L
        override val machineSpeed = 0.5f
        override val machineUsage = 1.25f
        override val machineBonus = 1.015625f
        override val machineBonusMax = 32

        override val generatorHeat = 0.01f
        override val generatorInitial = 100f
        override val generatorRange = 250f
        override val generatorBonus = 50f
        override val generatorCooling = 0.99f

        override val turbineRate = 2f

        override val chatColor = 0xFFE38407.toInt()

    },

    /**
     * The gold tier, a modest step up from copper in every direction.
     */
    ONE_5(1.5f) {

        override val maxInput = 512000L
        override val maxOutput = 512000L

        override val batteryStorage = 12000000L
        override val bankStorage = 100000000L

        override val machineBuffer = 10000000L
        override val machineSpeed = 0.75f
        override val machineUsage = 1.15f
        override val machineBonus = 1.015625f
        override val machineBonusMax = 64

        override val generatorHeat = 0.015f
        override val generatorInitial = 100f
        override val generatorRange = 300f
        override val generatorBonus = 100f
        override val generatorCooling = 0.994f

        override val turbineRate = 2.5f

        override val chatColor = TextColor.YELLOW.value

    };

    /**
     * How much energy a machine of this tier can be given per tick.
     */
    abstract val maxInput: Long

    /**
     * How much energy a machine of this tier can give out per tick.
     */
    abstract val maxOutput: Long

    /**
     * How much energy a battery of this tier holds.
     */
    abstract val batteryStorage: Long

    /**
     * How much energy a power bank of this tier holds.
     */
    abstract val bankStorage: Long

    /**
     * How much energy a working machine of this tier keeps by it to draw on.
     */
    abstract val machineBuffer: Long

    /**
     * How long a machine of this tier takes over a recipe, as a multiple of what the recipe itself asks for. Below `1` is faster.
     */
    abstract val machineSpeed: Float

    /**
     * How much energy a machine of this tier spends on a recipe, as a multiple of what the recipe itself asks for. Below `1` is cheaper.
     */
    abstract val machineUsage: Float

    /**
     * How much more efficient a machine of this tier becomes with each run of the same recipe.
     */
    abstract val machineBonus: Float

    /**
     * How many runs of the same recipe a machine of this tier keeps gaining efficiency over.
     */
    abstract val machineBonusMax: Int

    /**
     * How much heat a generator of this tier makes per tick from a fuel of ordinary value.
     */
    abstract val generatorHeat: Float

    /**
     * The heat a generator of this tier settles at while it is doing nothing.
     */
    abstract val generatorInitial: Float

    /**
     * How far above [generatorInitial] a generator of this tier can be driven before it is running too hot.
     */
    abstract val generatorRange: Float

    /**
     * The extra heat a generator of this tier is given for running at its limit.
     */
    abstract val generatorBonus: Float

    /**
     * What is left of a generator's heat each tick once it stops being fed, i.e. how slowly it cools.
     */
    abstract val generatorCooling: Float

    /**
     * How much energy a turbine of this tier makes from the rotor blades turning.
     */
    abstract val turbineRate: Float

    /**
     * The colour this tier is written in.
     */
    abstract val chatColor: Int

    override fun getSerializedName() = name.lowercase()

    companion object {

        /**
         * The codec a tier is saved and loaded through, by its own name.
         */
        val codec = StringRepresentable.fromEnum(::values)

        /**
         * The tiers by their position in this enum, out of which anything unrecognised reads back as the first.
         */
        val byId = ByIdMap.continuous(MachineTier::ordinal, MachineTier.entries.toTypedArray(), ByIdMap.OutOfBoundsStrategy.ZERO)

        /**
         * The codec a tier is sent to the client through, by its position in this enum.
         */
        val streamCodec = ByteBufCodecs.idMapper(byId, MachineTier::ordinal)

    }

}
