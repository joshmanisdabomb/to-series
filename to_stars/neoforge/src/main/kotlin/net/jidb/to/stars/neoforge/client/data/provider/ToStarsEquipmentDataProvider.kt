package net.jidb.to.stars.neoforge.client.data.provider

import net.jidb.to.stars.ToStarsMod
import net.minecraft.client.data.models.EquipmentAssetProvider
import net.minecraft.client.resources.model.EquipmentClientInfo
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.equipment.EquipmentAsset
import java.util.Optional
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

/**
 * Generates the equipment assets of the mod, for how armor renders on entities in-game.
 *
 * @param output Where the generated files are written.
 * @param provider The registries the definitions are built against.
 */
class ToStarsEquipmentDataProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>) : EquipmentAssetProvider(output) {

    override fun registerModels(output: BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo>) {
        output.accept(
            ToStarsMod.armorMaterials.steel.assetId,
            EquipmentClientInfo.builder()
                .addHumanoidLayers(ToStarsMod.armorMaterials.steel.assetId.identifier(), false)
                .addLayers(
                    EquipmentClientInfo.LayerType.HORSE_BODY,
                    EquipmentClientInfo.Layer(
                        ToStarsMod.armorMaterials.steel.assetId.identifier(),
                        Optional.empty(),
                        false,
                    )
                )
                .addLayers(
                    EquipmentClientInfo.LayerType.NAUTILUS_BODY,
                    EquipmentClientInfo.Layer(
                        ToStarsMod.armorMaterials.steel.assetId.identifier(),
                        Optional.empty(),
                        false,
                    )
                )
                .build()
        )
    }

}
