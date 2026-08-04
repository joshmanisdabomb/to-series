package net.jidb.to.base.data.pub.collection.module.tag

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.BlockTagDataCollectionEvent
import net.jidb.to.base.data.api.collection.module.DataCollectionModule
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

/**
 * A [DataCollectionModule] putting a block into the vanilla tags that decide which tool mines it and how good that tool has to be.
 *
 * The tier is expressed as one number rather than as the tags themselves, because vanilla says it two different ways: up to diamond a block names the tier it *needs*, and beyond that it names every tier that is *incorrect* for it, since there is no `NEEDS_NETHERITE_TOOL`.
 *
 * @property tool The kind of tool that mines the block, or `null` where no tool is faster than any other. Defaults to `null`.
 * @property level How good the tool has to be, from `0` for any tool up to `5` for netherite only. Defaults to `0`.
 * @since 0.3.0
 */
open class MiningBlockTagDataCollectionModule(val tool: ToolType? = null, val level: Int = 0) : DataCollectionModule() {

    override fun generateBlockTags(collection: DataCollection<Block>, event: BlockTagDataCollectionEvent): Map<TagKey<Block>, List<Block>>? {
        val tags = when (tool) {
            ToolType.PICKAXE -> listOf(BlockTags.MINEABLE_WITH_PICKAXE)
            ToolType.SHOVEL -> listOf(BlockTags.MINEABLE_WITH_SHOVEL)
            ToolType.AXE -> listOf(BlockTags.MINEABLE_WITH_AXE)
            ToolType.HOE -> listOf(BlockTags.MINEABLE_WITH_HOE)
            ToolType.SWORD_EFFICIENT -> listOf(BlockTags.SWORD_EFFICIENT)
            ToolType.SWORD_INSTANT -> listOf(BlockTags.SWORD_INSTANTLY_MINES)
            ToolType.SHEARS -> listOf(ToBaseMod.blockTags.shears_efficient)
            else -> emptyList()
        } + when (level) {
            5 -> listOf(
                BlockTags.INCORRECT_FOR_WOODEN_TOOL,
                BlockTags.INCORRECT_FOR_STONE_TOOL,
                BlockTags.INCORRECT_FOR_COPPER_TOOL,
                BlockTags.INCORRECT_FOR_IRON_TOOL,
                BlockTags.INCORRECT_FOR_GOLD_TOOL,
                BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
                BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            )
            4 -> listOf(
                BlockTags.INCORRECT_FOR_WOODEN_TOOL,
                BlockTags.INCORRECT_FOR_STONE_TOOL,
                BlockTags.INCORRECT_FOR_COPPER_TOOL,
                BlockTags.INCORRECT_FOR_IRON_TOOL,
                BlockTags.INCORRECT_FOR_GOLD_TOOL,
                BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            )
            3 -> listOf(BlockTags.NEEDS_DIAMOND_TOOL)
            2 -> listOf(BlockTags.NEEDS_IRON_TOOL)
            1 -> listOf(BlockTags.NEEDS_STONE_TOOL)
            else -> emptyList()
        }
        return tags.associateWith { listOf(collection.`object`) }
    }

    /**
     * Enum that defines the kinds of tool a block can be mined faster with.
     *
     * @see MiningBlockTagDataCollectionModule.tool
     * @since 0.3.0
     */
    enum class ToolType {

        /**
         * The block is mined with a pickaxe.
         *
         * @since 0.3.0
         */
        PICKAXE,

        /**
         * The block is mined with a shovel.
         *
         * @since 0.3.0
         */
        SHOVEL,

        /**
         * The block is mined with an axe.
         *
         * @since 0.3.0
         */
        AXE,

        /**
         * The block is mined with a hoe.
         *
         * @since 0.3.0
         */
        HOE,

        /**
         * The block is mined faster with a sword, as cobwebs and bamboo are.
         *
         * @since 0.3.0
         */
        SWORD_EFFICIENT,

        /**
         * The block is mined instantly by a sword, as vanilla's own plants are.
         *
         * @since 0.3.0
         */
        SWORD_INSTANT,

        /**
         * The block is mined faster with shears.
         *
         * @since 0.3.0
         */
        SHEARS

    }

}
