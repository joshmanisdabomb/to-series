package net.jidb.to.stars.info

import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.util.ByIdMap
import net.minecraft.util.StringRepresentable

/**
 * Enum that defines the kinds of machine a processor recipe can be run by, which is what decides where a recipe shows up and what will accept it.
 */
enum class ProcessorType : StringRepresentable {

    /**
     * The centrifuge, which separates uranium into its enriched and heavy forms.
     */
    CENTRIFUGE;

    override fun getSerializedName() = name.lowercase()

    companion object {

        /**
         * The codec a processor type is read from a recipe file through, by its own name.
         */
        val codec = StringRepresentable.fromEnum(::values)

        /**
         * The types by their position in this enum, out of which anything unrecognised reads back as the first.
         */
        val byId = ByIdMap.continuous(ProcessorType::ordinal, ProcessorType.entries.toTypedArray(), ByIdMap.OutOfBoundsStrategy.ZERO)

        /**
         * The codec a processor type is sent to the client through, by its position in this enum.
         */
        val streamCodec = ByteBufCodecs.idMapper(byId, ProcessorType::ordinal)

    }

}
