package net.jidb.to.base.api.inventory.data

import net.minecraft.world.inventory.ContainerData

/**
 * Interface created by [ContainerDataSchemaType.createApplyFunction] that runs [ContainerDataSchemaType.applyShort] to modify a given object.
 *
 * This is typically created internally in [ContainerDataSchema] and called for each of your variables within [ContainerData.set]:
 * ```kotlin
 * object : ContainerData {
 *     override fun set(key: Int) {
 *         HeatGeneratorMenu.dataSchema.set(key, value.toShort()) { index, fn: ContainerDataSchemaApplyFunction -> when (index) {
 *             "heat" -> this@BlockEntity.heat = fn(this@BlockEntity.heat) // Float
 *             "energy" -> this@BlockEntity.energy = fn(this@BlockEntity.energy) // Long
 *         }
 *     }
 * }
 * ```
 * @since 0.7.0
 */
interface ContainerDataSchemaApplyFunction {

    /**
     * Runs [ContainerDataSchemaType.applyShort] for the given data provided in [ContainerDataSchemaType.createApplyFunction].
     *
     * @param R The type passed and returned. Types are casted, so the wrong type supplied and returned will error if it doesn't match the parent [ContainerDataSchemaType].
     * @param original The original value.
     * @return The modified value after [ContainerDataSchemaType.applyShort].
     * @since 0.7.0
     */
    operator fun <R> invoke(original: R): R

}
