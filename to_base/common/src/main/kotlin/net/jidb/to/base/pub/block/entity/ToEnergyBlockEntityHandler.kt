package net.jidb.to.base.pub.block.entity

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.inventory.data.ContainerDataSchema
import net.jidb.to.base.api.inventory.data.ContainerDataSchemaApplyFunction
import net.jidb.to.base.pub.item.component.ToEnergyItemComponentData
import net.jidb.to.base.pub.transfer.energy.BlockEntityToEnergyTransferContext
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentMap
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.StringRepresentable
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

/**
 * Everything a block entity needs in order to store To Energy, held in one object so that a block entity can gain energy by delegating to it rather than by extending anything.
 * As well as the energy itself it covers saving and loading, syncing to the client, exposing the figures to a menu, and carrying the energy into and out of the item the block drops as.
 *
 * It also keeps a rolling history of what has moved each tick, from which the per-tick input and output limits are enforced by [BlockEntityToEnergyTransferContext] and the rates shown in a tooltip are averaged.
 * The block entity is expected to call [tickAverages] once per tick to roll that history along.
 *
 * @param defaultCapacity How much energy can be stored, before any saved value overrides it.
 * @param defaultMaxInput How much energy can be inserted per tick, before any saved value overrides it.
 * @param defaultMaxOutput How much energy can be extracted per tick, before any saved value overrides it.
 * @property averageLength How many ticks of history the averages are taken over. Defaults to `20`, i.e. one second.
 * @since 0.7.0
 */
class ToEnergyBlockEntityHandler(defaultCapacity: Long, defaultMaxInput: Long, defaultMaxOutput: Long, val averageLength: Int = 20) {

    /**
     * The amount of energy currently stored.
     *
     * @since 0.7.0
     */
    var energy = 0L

    /**
     * How much energy was inserted on each of the last [averageLength] ticks, with the current tick at index zero.
     *
     * @since 0.7.0
     */
    val historyInsert = LongArray(averageLength) { 0 }

    /**
     * How much energy was extracted on each of the last [averageLength] ticks, with the current tick at index zero.
     *
     * @since 0.7.0
     */
    val historyExtract = LongArray(averageLength) { 0 }

    /**
     * The average energy inserted per tick over [historyInsert], recalculated by [tickAverages].
     *
     * @since 0.7.0
     */
    var historyInsertAvg = 0L

    /**
     * The average energy extracted per tick over [historyExtract], recalculated by [tickAverages].
     *
     * @since 0.7.0
     */
    var historyExtractAvg = 0L

    /**
     * How much energy can be stored.
     *
     * @since 0.7.0
     */
    var capacity = defaultCapacity

    /**
     * How much energy can be inserted per tick.
     *
     * @since 0.7.0
     */
    var maxInput = defaultMaxInput

    /**
     * How much energy can be extracted per tick.
     *
     * @since 0.7.0
     */
    var maxOutput = defaultMaxOutput

    /**
     * How many ticks are left before the next sync to the client is allowed, counted down by [shouldNetworkSync].
     *
     * @since 0.7.0
     */
    var nextEnergySync = 0

    /**
     * The transfer context through which other blocks and items move energy into and out of this handler.
     *
     * @since 0.7.0
     */
    val transfer = BlockEntityToEnergyTransferContext(this)

    /**
     * The [ContainerData] a menu syncs these figures to its screen through.
     * Vanilla's container data only carries shorts, so each value is split across several indices by [dataSchema] rather than sent whole.
     *
     * @since 0.7.0
     */
    val dataAccess = object : ContainerData {

        override fun get(key: Int) = dataSchema.getShort(key, ::getFromDataSchema) ?: 0

        override fun set(key: Int, value: Int) {
            dataSchema.set(key, value.toShort(), ::setFromDataSchema)
        }

        override fun getCount() = dataSchema.getDataSize()

    }

    /**
     * Reads the value one of the schema's keys stands for, which is how [dataAccess] answers a menu's request for a slice of it.
     *
     * @param K The type of the schema key.
     * @param index The key being read.
     * @return The value behind that key, or `null` where the key belongs to no field of this handler.
     * @since 0.7.0
     */
    fun <K : StringRepresentable> getFromDataSchema(index: K) = when (index.serializedName) {
        EnergyStorageDataKey.TOTAL.serializedName -> energy
        EnergyStorageDataKey.CAPACITY.serializedName -> capacity
        EnergyStorageDataKey.MAX_INPUT.serializedName -> maxInput
        EnergyStorageDataKey.MAX_OUTPUT.serializedName -> maxOutput
        EnergyStorageDataKey.INSERT_CHANGE.serializedName -> historyInsert[1]
        EnergyStorageDataKey.INSERT_AVERAGE.serializedName -> historyInsertAvg
        EnergyStorageDataKey.EXTRACT_CHANGE.serializedName -> historyExtract[1]
        EnergyStorageDataKey.EXTRACT_AVERAGE.serializedName -> historyExtractAvg
        else -> null
    }

    /**
     * Writes the value one of the schema's keys stands for, which is how [dataAccess] applies a slice a menu has received.
     * The value is not given directly; the function is handed the current value and returns the new one, so that only the bits carried by this slice are changed.
     *
     * @param K The type of the schema key.
     * @param index The key being written.
     * @param fn The function producing the new value from the current one.
     * @return Returns `true` if the key belongs to a field of this handler, `false` otherwise.
     * @since 0.7.0
     */
    fun <K : StringRepresentable> setFromDataSchema(index: K, fn: ContainerDataSchemaApplyFunction): Boolean {
        when (index.serializedName) {
            EnergyStorageDataKey.TOTAL.serializedName -> energy = fn(energy)
            EnergyStorageDataKey.CAPACITY.serializedName -> capacity = fn(capacity)
            EnergyStorageDataKey.MAX_INPUT.serializedName -> maxInput = fn(maxInput)
            EnergyStorageDataKey.MAX_OUTPUT.serializedName -> maxOutput = fn(maxOutput)
            EnergyStorageDataKey.INSERT_CHANGE.serializedName -> historyInsert[1] = fn(historyInsert[1])
            EnergyStorageDataKey.INSERT_AVERAGE.serializedName -> historyInsertAvg = fn(historyInsertAvg)
            EnergyStorageDataKey.EXTRACT_CHANGE.serializedName -> historyExtract[1] = fn(historyExtract[1])
            EnergyStorageDataKey.EXTRACT_AVERAGE.serializedName -> historyExtractAvg = fn(historyExtractAvg)
            else -> return false
        }
        return true
    }

    /**
     * Reads the stored energy and its limits back out of saved data, which the block entity calls from its own load.
     * A missing limit keeps whatever default the handler was built with, so raising a default in code raises it for blocks already placed.
     *
     * @param input The saved data to read from.
     * @since 0.7.0
     */
    fun loadAdditional(input: ValueInput) {
        energy = input.getLongOr("energy", 0L)
        capacity = input.getLongOr("energyMaxCapacity", capacity)
        maxInput = input.getLongOr("energyMaxInput", maxInput)
        maxOutput = input.getLongOr("energyMaxOutput", maxOutput)
    }

    /**
     * Writes the stored energy and its limits into saved data, which the block entity calls from its own save.
     *
     * @param output The saved data to write to.
     * @since 0.7.0
     */
    fun saveAdditional(output: ValueOutput) {
        output.putLong("energy", energy)
        output.putLong("energyMaxCapacity", capacity)
        output.putLong("energyMaxInput", maxInput)
        output.putLong("energyMaxOutput", maxOutput)
    }

    /**
     * Writes the parts of this handler the client needs into the block entity's update tag.
     * Only the stored energy and the capacity are sent, as they are all the client draws from; the transfer limits never leave the server.
     *
     * @param tag The update tag to write into.
     * @since 0.7.0
     */
    fun getUpdateTag(tag: CompoundTag) {
        tag.putLong("energy", energy)
        tag.putLong("energyMaxCapacity", capacity)
    }

    /**
     * Takes the energy of the item the block was placed from, so that a machine broken while charged is still charged when put back down.
     *
     * @param components The components of the item the block was placed from.
     * @since 0.7.0
     */
    fun applyImplicitComponents(components: DataComponentGetter) {
        val energy = components.get(ToBaseMod.itemComponents.energy_data)
        if (energy != null) {
            this.energy = energy.energy
            capacity = energy.max
            maxInput = energy.maxInput
            maxOutput = energy.maxOutput
        }
    }

    /**
     * Writes this handler's energy onto the item the block drops as, the other half of [applyImplicitComponents].
     *
     * @param components The components of the item being dropped.
     * @since 0.7.0
     */
    fun collectImplicitComponents(components: DataComponentMap.Builder) {
        components.set(ToBaseMod.itemComponents.energy_data, ToEnergyItemComponentData(energy, capacity, maxInput, maxOutput))
    }

    /**
     * Recalculates the averages and rolls the history along by one tick, which the block entity calls once per tick.
     * Rolling the history is also what resets the running totals the per-tick transfer limits are measured against.
     *
     * @since 0.7.0
     */
    fun tickAverages() {
        historyInsertAvg = historyInsert.average().toLong()
        historyExtractAvg = historyExtract.average().toLong()
        for (i in historyInsert.size - 1 downTo 1) historyInsert[i] = historyInsert[i - 1]
        historyInsert[0] = 0L
        for (i in historyExtract.size - 1 downTo 1) historyExtract[i] = historyExtract[i - 1]
        historyExtract[0] = 0L
    }

    /**
     * Whether the block entity should send its energy to the client this tick.
     * Syncing every tick would be wasteful, so a sync is only asked for where energy has actually moved recently, and then no more often than the given interval.
     *
     * @param lastChanged How many ticks of history count as "recently", and how many ticks to wait between syncs. Zero or less syncs every tick. Defaults to `7`.
     * @return Returns `true` if the energy should be synced this tick, `false` otherwise.
     * @since 0.7.0
     */
    fun shouldNetworkSync(lastChanged: Int = 7): Boolean {
        if (lastChanged <= 0) {
            return true
        } else if (nextEnergySync <= 0) {
            if (historyInsert.take(lastChanged).sum() > 0 || historyExtract.take(lastChanged).sum() > 0) {
                nextEnergySync = lastChanged
                return true
            }
        } else {
            nextEnergySync--
        }
        return false
    }

    companion object {

        /**
         * The schema laying out how each figure of this handler is split across the short-sized slots of a [ContainerData].
         *
         * @since 0.7.0
         */
        val dataSchema = ContainerDataSchema<EnergyStorageDataKey>()
            .defineLong(EnergyStorageDataKey.TOTAL)
            .defineLong(EnergyStorageDataKey.CAPACITY)
            .defineLong(EnergyStorageDataKey.MAX_INPUT)
            .defineLong(EnergyStorageDataKey.MAX_OUTPUT)
            .defineLong(EnergyStorageDataKey.INSERT_CHANGE)
            .defineLong(EnergyStorageDataKey.INSERT_AVERAGE)
            .defineLong(EnergyStorageDataKey.EXTRACT_CHANGE)
            .defineLong(EnergyStorageDataKey.EXTRACT_AVERAGE)

    }

    /**
     * Enum that defines the figures of a [ToEnergyBlockEntityHandler] that are synced to a menu.
     *
     * @see ToEnergyBlockEntityHandler.dataSchema
     * @since 0.7.0
     */
    enum class EnergyStorageDataKey : StringRepresentable {

        /**
         * The amount of energy currently stored.
         *
         * @since 0.7.0
         */
        TOTAL,

        /**
         * The amount of energy that can be stored.
         *
         * @since 0.7.0
         */
        CAPACITY,

        /**
         * The amount of energy that can be inserted per tick.
         *
         * @since 0.7.0
         */
        MAX_INPUT,

        /**
         * The amount of energy that can be extracted per tick.
         *
         * @since 0.7.0
         */
        MAX_OUTPUT,

        /**
         * The amount of energy inserted on the previous tick.
         *
         * @since 0.7.0
         */
        INSERT_CHANGE,

        /**
         * The average amount of energy inserted per tick.
         *
         * @since 0.7.0
         */
        INSERT_AVERAGE,

        /**
         * The amount of energy extracted on the previous tick.
         *
         * @since 0.7.0
         */
        EXTRACT_CHANGE,

        /**
         * The average amount of energy extracted per tick.
         *
         * @since 0.7.0
         */
        EXTRACT_AVERAGE;

        override fun getSerializedName() = name.lowercase()

    }

}
