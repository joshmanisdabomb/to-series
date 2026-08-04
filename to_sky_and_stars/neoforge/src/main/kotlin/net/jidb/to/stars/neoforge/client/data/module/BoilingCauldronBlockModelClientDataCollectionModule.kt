package net.jidb.to.stars.neoforge.client.data.module

import net.jidb.to.base.client.data.api.collection.event.ModelClientDataCollectionEvent
import net.jidb.to.base.client.data.api.collection.module.ClientDataCollectionModule
import net.jidb.to.base.data.api.collection.DataCollection
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator
import net.minecraft.client.data.models.blockstates.PropertyDispatch
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.LayeredCauldronBlock
import net.minecraft.world.level.block.state.properties.BlockStateProperties

/**
 * A [ClientDataCollectionModule] generating the models of the boiler, i.e. one per water level and again for each of those with the water boiling.
 */
class BoilingCauldronBlockModelClientDataCollectionModule : ClientDataCollectionModule() {

    override fun generateBlockModels(collection: DataCollection<Block>, event: ModelClientDataCollectionEvent): Boolean {
        event.block.blockStateOutput.accept(
            MultiVariantGenerator.dispatch(collection.`object`)
                .with(PropertyDispatch.initial(LayeredCauldronBlock.LEVEL, BlockStateProperties.UNSTABLE)
                    .select(1, false, BlockModelGenerators.plainVariant(
                        ModelTemplates.CAULDRON_LEVEL1.createWithSuffix(
                            collection.`object`,
                            "_stable1",
                            TextureMapping.cauldron(TextureMapping.getBlockTexture(collection.`object`)),
                            event.block.modelOutput
                        )
                    ))
                    .select(2, false, BlockModelGenerators.plainVariant(
                        ModelTemplates.CAULDRON_LEVEL2.createWithSuffix(
                            collection.`object`,
                            "_stable2",
                            TextureMapping.cauldron(TextureMapping.getBlockTexture(collection.`object`)),
                            event.block.modelOutput
                        )
                    ))
                    .select(3, false, BlockModelGenerators.plainVariant(
                        ModelTemplates.CAULDRON_FULL.createWithSuffix(
                            collection.`object`,
                            "_stable3",
                            TextureMapping.cauldron(TextureMapping.getBlockTexture(collection.`object`)),
                            event.block.modelOutput
                        )
                    ))
                    .select(1, true, BlockModelGenerators.plainVariant(
                        ModelTemplates.CAULDRON_LEVEL1.createWithSuffix(
                            collection.`object`,
                            "_unstable1",
                            TextureMapping.cauldron(TextureMapping.getBlockTexture(collection.`object`, "_unstable")),
                            event.block.modelOutput
                        )
                    ))
                    .select(2, true, BlockModelGenerators.plainVariant(
                        ModelTemplates.CAULDRON_LEVEL2.createWithSuffix(
                            collection.`object`,
                            "_unstable2",
                            TextureMapping.cauldron(TextureMapping.getBlockTexture(collection.`object`, "_unstable")),
                            event.block.modelOutput
                        )
                    ))
                    .select(3, true, BlockModelGenerators.plainVariant(
                        ModelTemplates.CAULDRON_FULL.createWithSuffix(
                            collection.`object`,
                            "_unstable3",
                            TextureMapping.cauldron(TextureMapping.getBlockTexture(collection.`object`, "_unstable")),
                            event.block.modelOutput
                        )
                    ))
                )
        )
        return true
    }

}
