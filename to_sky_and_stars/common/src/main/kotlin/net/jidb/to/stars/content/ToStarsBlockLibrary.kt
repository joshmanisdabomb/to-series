package net.jidb.to.stars.content

import net.jidb.to.base.block.properties.ExtendedBlockProperties
import net.jidb.to.base.library.BlockLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.NuclearFireBlock
import net.jidb.to.stars.block.NuclearWasteBlock
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.util.ColorRGBA
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction

object ToStarsBlockLibrary : BlockLibrary(ToStarsMod.MOD_ID) {

    override val registry = BuiltInRegistries.BLOCK

    val nuclear_waste by this { entry -> NuclearWasteBlock(ColorRGBA(0xFF544F4E.toInt()), BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.TERRACOTTA_CYAN)
        .strength(30.0f, 8.0f)
        .randomTicks()
        .sound(SoundType.CORAL_BLOCK)) }
    val nuclear_fire by this { entry -> NuclearFireBlock(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_LIGHT_GREEN)
        .randomTicks()
        .replaceable()
        .noCollision()
        .instabreak()
        .lightLevel { 15 }
        .sound(SoundType.SCULK_SENSOR)
        .pushReaction(PushReaction.DESTROY)) }
        .tag(properties, ExtendedBlockProperties()
            .renderLayer(ExtendedBlockProperties.RenderLayer.CUTOUT))

}