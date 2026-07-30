package net.jidb.to.stars.info

import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.util.ByIdMap
import net.minecraft.util.StringRepresentable

enum class ProcessorType : StringRepresentable {

    CENTRIFUGE;

    override fun getSerializedName() = name.lowercase()

    companion object {
        val codec = StringRepresentable.fromEnum(::values)
        val byId = ByIdMap.continuous(ProcessorType::ordinal, ProcessorType.entries.toTypedArray(), ByIdMap.OutOfBoundsStrategy.ZERO)
        val streamCodec = ByteBufCodecs.idMapper(byId, ProcessorType::ordinal)
    }

}