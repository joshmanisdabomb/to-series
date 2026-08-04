package net.jidb.to.base.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Exposes private fields of the fire block, such as flammability tables.
 * This allows mod blocks to register their flammability with {@link net.jidb.to.base.api.properties.ExtendedBlockProperties#flammable}
 *
 * @see net.minecraft.world.level.block.FireBlock
 */
@Mixin(FireBlock.class)
public interface FireBlockAccessor {

    /**
     * Returns the table of each block's chance to catch fire from a neighbour.
     *
     * @return the ignite odds table, keyed by block.
     * @see net.minecraft.world.level.block.FireBlock#igniteOdds
     */
    @Accessor("igniteOdds")
    Object2IntMap<Block> to_base$getIgniteOdds();

    /**
     * Returns the table of each block's chance to be consumed once on fire.
     *
     * @return the burn odds table, keyed by block.
     * @see net.minecraft.world.level.block.FireBlock#burnOdds
     */
    @Accessor("burnOdds")
    Object2IntMap<Block> to_base$getBurnOdds();

    /**
     * Registers a block in both flammability tables.
     *
     * @param block the block to make flammable.
     * @param igniteOdds how quickly the block catches fire from a neighbour.
     * @param burnOdds how quickly the block is consumed once on fire.
     * @see net.minecraft.world.level.block.FireBlock#setFlammable
     */
    @Invoker("setFlammable")
    void to_base$setFlammable(Block block, int igniteOdds, int burnOdds);

}
