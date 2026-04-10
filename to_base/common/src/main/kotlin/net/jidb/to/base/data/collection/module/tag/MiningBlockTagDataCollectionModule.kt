package net.jidb.to.base.data.collection.module.tag

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.data.collection.DataCollection
import net.jidb.to.base.data.collection.event.BlockTagDataCollectionEvent
import net.jidb.to.base.data.collection.module.DataCollectionModule
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

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

    enum class ToolType {
        PICKAXE, SHOVEL, AXE, HOE, SWORD_EFFICIENT, SWORD_INSTANT, SHEARS
    }

}
