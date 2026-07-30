package net.jidb.to.base.pub.block.entity

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.inventory.data.ContainerDataSchema
import net.jidb.to.base.api.inventory.data.ContainerDataSchemaApplyFunction
import net.jidb.to.base.pub.item.component.ToEnergyItemComponentData
import net.jidb.to.base.pub.transfer.energy.BlockEntityToEnergyTransferContext
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentMap
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.StringRepresentable
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

class ToEnergyBlockEntityHandler(defaultCapacity: Long, defaultMaxInput: Long, defaultMaxOutput: Long, val averageLength: Int = 20) {

    var energy = 0L

    val historyInsert = LongArray(averageLength) { 0 }
    val historyExtract = LongArray(averageLength) { 0 }
    var historyInsertAvg = 0L
    var historyExtractAvg = 0L

    var capacity = defaultCapacity
    var maxInput = defaultMaxInput
    var maxOutput = defaultMaxOutput

    var nextEnergySync = 0

    val transfer = BlockEntityToEnergyTransferContext(this)

    val dataAccess = object : ContainerData {

        override fun get(key: Int) = dataSchema.getShort(key, ::getFromDataSchema) ?: 0

        override fun set(key: Int, value: Int) {
            dataSchema.set(key, value.toShort(), ::setFromDataSchema)
        }

        override fun getCount() = dataSchema.getDataSize()

    }

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

    fun loadAdditional(input: ValueInput) {
        energy = input.getLongOr("energy", 0L)
        capacity = input.getLongOr("energyMaxCapacity", capacity)
        maxInput = input.getLongOr("energyMaxInput", maxInput)
        maxOutput = input.getLongOr("energyMaxOutput", maxOutput)
    }

    fun saveAdditional(output: ValueOutput) {
        output.putLong("energy", energy)
        output.putLong("energyMaxCapacity", capacity)
        output.putLong("energyMaxInput", maxInput)
        output.putLong("energyMaxOutput", maxOutput)
    }

    fun getUpdateTag(tag: CompoundTag, registries: HolderLookup.Provider) {
        tag.putLong("energy", energy)
        tag.putLong("energyMaxCapacity", capacity)
    }

    fun applyImplicitComponents(components: DataComponentGetter) {
        val energy = components.get(ToBaseMod.itemComponents.energy_data)
        if (energy != null) {
            this.energy = energy.energy
            capacity = energy.max
            maxInput = energy.maxInput
            maxOutput = energy.maxOutput
        }
    }

    fun collectImplicitComponents(components: DataComponentMap.Builder) {
        components.set(ToBaseMod.itemComponents.energy_data, ToEnergyItemComponentData(energy, capacity, maxInput, maxOutput))
    }

    fun tickAverages() {
        historyInsertAvg = historyInsert.average().toLong()
        historyExtractAvg = historyExtract.average().toLong()
        for (i in historyInsert.size - 1 downTo 1) historyInsert[i] = historyInsert[i - 1]
        historyInsert[0] = 0L
        for (i in historyExtract.size - 1 downTo 1) historyExtract[i] = historyExtract[i - 1]
        historyExtract[0] = 0L
    }

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


    enum class EnergyStorageDataKey : StringRepresentable {
        TOTAL,
        CAPACITY,
        MAX_INPUT,
        MAX_OUTPUT,
        INSERT_CHANGE,
        INSERT_AVERAGE,
        EXTRACT_CHANGE,
        EXTRACT_AVERAGE;

        override fun getSerializedName() = name.lowercase()
    }

}
