package net.jidb.to.base.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.properties.ExtendedBlockProperties
import net.jidb.to.base.content.block.ResearchDeskBlock
import net.jidb.to.base.pub.block.HorizontalGenericBlock
import net.jidb.to.base.pub.library.BlockLibrary
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor

/**
 * [BlockLibrary] implementation that registers the [Block] content of To Lay the Foundations, and provides access to it in one place.
 *
 * @since 0.0.3
 */
object ToBaseBlockLibrary : BlockLibrary(ToBaseMod.modid) {

    override val registry = BuiltInRegistries.BLOCK

    /**
     * A plain block used while developing, which exists to have something to place.
     *
     * @since 0.0.3
     */
    val test_block by this { entry -> Block(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_YELLOW)
        .strength(0.5f)
        .sound(SoundType.SCAFFOLDING)) }

    /**
     * A test block that faces the player who placed it, which exists to exercise [HorizontalGenericBlock].
     *
     * @since 0.0.3
     */
    val test_block_2 by this { entry -> HorizontalGenericBlock(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.COLOR_YELLOW)
        .strength(0.5f)
        .sound(SoundType.SCAFFOLDING)) }

    /**
     * The research desk, the two-block-wide table the in-game wiki is read at.
     *
     * @see ResearchDeskBlock
     * @since 0.1.0
     */
    val research_desk by this { entry -> ResearchDeskBlock(BlockBehaviour.Properties.of()
        .setId(getEntryResourceKey(entry))
        .mapColor(MapColor.WOOD)
        .forceSolidOn()
        .instrument(NoteBlockInstrument.BASS)
        .strength(2.5F)
        .sound(SoundType.WOOD)
        .ignitedByLava()) }
        .tag(properties, ExtendedBlockProperties()
            .flammable())

}
