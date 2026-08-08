package net.jidb.to.stars.block.entity

import net.jidb.to.base.pub.network.DistantSoundPayload
import net.jidb.to.base.service.Services
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.entity.AtomicBombEntity
import net.jidb.to.stars.inventory.menu.AtomicBombMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.NonNullList
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.world.ContainerHelper
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.phys.Vec3

/**
 * The block entity of the atomic bomb, which holds the uranium it has been loaded with and turns the whole three-block structure into a falling entity when it is set off.
 *
 * @param pos The position of the block.
 * @param state The state of the block.
 */
class AtomicBombBlockEntity(pos: BlockPos, state: BlockState) : BaseContainerBlockEntity(ToStarsMod.blockEntities.atomic_bomb, pos, state), WorldlyContainer {

    /**
     * What the bomb has been loaded with, i.e. the uranium deciding how strong the blast will be.
     */
    private var inventory = NonNullList.withSize(AtomicBombMenu.allSlots.size, ItemStack.EMPTY)

    /**
     * Arms the bomb, replacing it with a falling entity whose fuse is already burning and playing the arming sound to everyone nearby.
     *
     * @param owner The entity to blame the explosion on, or `null` where nothing set it off. Defaults to `null`.
     */
    fun detonate(owner: LivingEntity? = null) {
        val level = this.level as? ServerLevel ?: return

        val facing = blockState.getValue(HorizontalDirectionalBlock.FACING)
        removeAll(facing)

        val entity = AtomicBombEntity(level, blockPos.x + 0.5, blockPos.y.toDouble(), blockPos.z + 0.5, facing, inventory, true, owner)
        level.addFreshEntity(entity)

        Services.platform.networking.sendToPlayersTrackingPos(level, blockPos, DistantSoundPayload(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(ToStarsMod.sounds.atomic_bomb_activate), SoundSource.BLOCKS, Vec3.atCenterOf(blockPos).toVector3f(), 120f, 0.8f + level.random.nextFloat().times(0.4f), 0))
    }

    /**
     * Turns the bomb into a falling entity without arming it, which is what happens when the ground is taken out from under it.
     */
    fun fall() {
        val level = this.level as? ServerLevel ?: return

        val facing = blockState.getValue(HorizontalDirectionalBlock.FACING)

        removeAll(facing)

        val entity = AtomicBombEntity(level, blockPos.x + 0.5, blockPos.y.toDouble(), blockPos.z + 0.5, facing, inventory, false)
        level.addFreshEntity(entity)
    }

    /**
     * Takes all three segments of the bomb out of the world, which is done before the falling entity replaces them.
     *
     * @param facing The direction the bomb faces, from which its other two segments are found.
     */
    private fun removeAll(facing: Direction) {
        setRemoved()
        level?.setBlock(blockPos.relative(facing.clockWise), Blocks.AIR.defaultBlockState(), 18)
        level?.setBlock(blockPos.relative(facing.counterClockWise), Blocks.AIR.defaultBlockState(), 18)
        level?.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 18)
    }

    override fun getDefaultName() = blockState.block.name

    override fun getItems() = inventory

    public override fun setItems(items: NonNullList<ItemStack>) {
        inventory = items
    }

    override fun createMenu(id: Int, player: Inventory) = AtomicBombMenu(id, player, this)

    override fun getContainerSize() = inventory.size

    override fun getMaxStackSize() = 1

    override fun getMaxStackSize(stack: ItemStack) = AtomicBombMenu.getMaxStackSize(stack)

    override fun getSlotsForFace(side: Direction) = AtomicBombMenu.allSlots

    override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
        val current = inventory[slot]
        if (current.count > AtomicBombMenu.getMaxStackSize(current)) {
            return false
        }

        return AtomicBombMenu.canPlaceItem(slot, stack)
    }

    override fun canPlaceItemThroughFace(slot: Int, stack: ItemStack, side: Direction?) = canPlaceItem(slot, stack)

    override fun canTakeItemThroughFace(slot: Int, stack: ItemStack, side: Direction) = true

    override fun loadAdditional(input: ValueInput) {
        super.loadAdditional(input)
        items.clear()
        ContainerHelper.loadAllItems(input, items)
    }

    override fun saveAdditional(output: ValueOutput) {
        ContainerHelper.saveAllItems(output, items)
        super.saveAdditional(output)
    }

}
