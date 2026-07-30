package net.jidb.to.stars.network

import net.jidb.to.base.api.network.PayloadEntry
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import java.util.*

data class ProcessorSyncPayload(val pos: BlockPos, val lastRecipe: Optional<ResourceKey<Recipe<*>>>, val lastRecipeIcon: Optional<ResourceKey<Item>>) : CustomPacketPayload {

    override fun type() = type

    companion object : PayloadEntry<ProcessorSyncPayload> {
        override val type = CustomPacketPayload.Type<ProcessorSyncPayload>(Identifier.fromNamespaceAndPath(ToStarsMod.MOD_ID, "processor_recipe_sync"))
        override val codec = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ProcessorSyncPayload::pos,
            ResourceKey.streamCodec(Registries.RECIPE).apply(ByteBufCodecs::optional),
            ProcessorSyncPayload::lastRecipe,
            ResourceKey.streamCodec(Registries.ITEM).apply(ByteBufCodecs::optional),
            ProcessorSyncPayload::lastRecipeIcon,
            ::ProcessorSyncPayload
        )

        override val phase = PayloadEntry.Phase.PLAY
        override val side = PayloadEntry.Side.S2C
    }

}