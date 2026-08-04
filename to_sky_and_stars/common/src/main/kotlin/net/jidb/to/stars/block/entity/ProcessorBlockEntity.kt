package net.jidb.to.stars.block.entity

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.helper.RegistryHelper.resourceKey
import net.jidb.to.base.api.transfer.TransferContext
import net.jidb.to.base.pub.block.entity.ToEnergyBlockEntityHandler
import net.jidb.to.base.pub.transfer.energy.InputToEnergyTransferContext
import net.jidb.to.base.pub.transfer.energy.ToEnergyItemProvider
import net.jidb.to.base.pub.transfer.energy.ToEnergyWorldlyProvider
import net.jidb.to.base.service.Services
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.CentrifugeBlock
import net.jidb.to.stars.inventory.menu.ProcessorMenu
import net.jidb.to.stars.network.ProcessorSyncPayload
import net.jidb.to.stars.recipe.processor.ProcessorRecipe
import net.jidb.to.stars.recipe.processor.ProcessorRecipeInput
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.ContainerHelper
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.player.StackedItemContents
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.RecipeCraftingHolder
import net.minecraft.world.inventory.StackedContentsCompatible
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

/**
 * The block entity of a machine that runs processor recipes, which holds the energy it works from and how far through the current recipe it is.
 *
 * A processor becomes more efficient the longer it is left on one recipe, so how many times the current recipe has been run in a row is kept and reset whenever it changes.
 *
 * @param type The block entity type being built.
 * @param pos The position of the block.
 * @param state The state of the block.
 */
abstract class ProcessorBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : BaseContainerBlockEntity(type, pos, state), WorldlyContainer, ToEnergyWorldlyProvider, StackedContentsCompatible, RecipeCraftingHolder {

    /**
     * The items in the machine, across all of its slots.
     */
    protected abstract var inventory: NonNullList<ItemStack>

    /**
     * Which of the slots a recipe's ingredients are taken from.
     */
    protected abstract val inputSlots: IntArray

    /**
     * Which of the slots a recipe's results are put into.
     */
    protected abstract val outputSlots: IntArray

    /**
     * Which of the slots the machine draws energy out of a battery in.
     */
    protected abstract val batterySlots: IntArray

    /**
     * The energy the machine has by it to work from, along with how much can move in and out of it per tick.
     */
    val energy = ToEnergyBlockEntityHandler(
        (state.block as? CentrifugeBlock)?.machine?.machineBuffer ?: 0L,
        (state.block as? CentrifugeBlock)?.machine?.maxInput ?: 0L,
        (state.block as? CentrifugeBlock)?.machine?.maxOutput ?: 0L,
    )

    /**
     * What the sides of the block offer to something inserting energy, which can only insert, since a machine gives nothing back.
     */
    val transferInput = InputToEnergyTransferContext(energy.transfer)

    /**
     * How far through the current recipe the machine is, in ticks.
     */
    var progress = 0

    /**
     * How long the current recipe takes this machine, in ticks.
     */
    var progressMax = 0

    /**
     * How long the machine takes over a recipe, as a multiple of what the recipe itself asks for.
     */
    var machineSpeed = (state.block as CentrifugeBlock).machine.machineSpeed

    /**
     * How much energy the machine spends on a recipe, as a multiple of what the recipe itself asks for.
     */
    var machineUsage = (state.block as CentrifugeBlock).machine.machineUsage

    /**
     * How many runs of the same recipe the machine keeps gaining efficiency over.
     */
    var machineBonusMax = (state.block as CentrifugeBlock).machine.machineBonusMax

    /**
     * How much more efficient the machine becomes with each run of the same recipe.
     */
    var machineBonus = (state.block as CentrifugeBlock).machine.machineBonus

    /**
     * The recipe the machine is running, or `null` where it is running none.
     */
    var currentRecipe: ResourceKey<Recipe<*>>? = null

    /**
     * The recipe the machine last ran, which is what the current run is compared against to decide whether its efficiency carries over.
     */
    var lastRecipe: ResourceKey<Recipe<*>>? = null

    /**
     * The item shown for the last recipe in the interface, kept separately so that it can be drawn without the recipe itself having to be loaded.
     */
    var lastRecipeIcon: ResourceKey<Item>? = null

    /**
     * Whether what is in the machine still matches the recipe it built its efficiency up on.
     */
    var sameRecipe: Boolean = false

    /**
     * How much energy the current recipe asks for.
     */
    var recipeEnergy = 0L

    /**
     * How long the current recipe asks for, in ticks.
     */
    var recipeTime = 0

    /**
     * How many times the current recipe has been run in a row, which is what the efficiency bonus is worked out from.
     */
    var completion = 0

    /**
     * The randomness a recipe with a chance in it is decided by, kept so that the outcome does not change if the machine is reloaded partway through.
     */
    var seed: Long? = null

    /**
     * The cached lookup that finds which recipe the current ingredients match, so that every recipe need not be tried each tick.
     */
    private val quickCheck = RecipeManager.createCheck(ToStarsMod.recipeTypes.processor)

    /**
     * How the machine's figures are read by and written from the interface it is opened into.
     */
    val dataAccess = object : ContainerData {

        override fun get(key: Int) = ProcessorMenu.dataSchema.getShort(key) { when (it) {
            ProcessorMenu.ProcessorDataKey.PROGRESS -> progress
            ProcessorMenu.ProcessorDataKey.MAX_PROGRESS -> progressMax
            ProcessorMenu.ProcessorDataKey.RECIPE_SAME -> sameRecipe
            ProcessorMenu.ProcessorDataKey.RECIPE_ENERGY -> recipeEnergy
            ProcessorMenu.ProcessorDataKey.RECIPE_TIME -> recipeTime
            ProcessorMenu.ProcessorDataKey.COMPLETIONS -> completion
            else -> energy.getFromDataSchema(it)
        } } ?: 0

        override fun set(key: Int, value: Int) {
            ProcessorMenu.dataSchema.set(key, value.toShort()) { index, fn -> when (index) {
                ProcessorMenu.ProcessorDataKey.PROGRESS -> progress = fn(progress)
                ProcessorMenu.ProcessorDataKey.MAX_PROGRESS -> progressMax = fn(progressMax)
                ProcessorMenu.ProcessorDataKey.RECIPE_SAME -> sameRecipe = fn(sameRecipe)
                ProcessorMenu.ProcessorDataKey.RECIPE_ENERGY -> recipeEnergy = fn(recipeEnergy)
                ProcessorMenu.ProcessorDataKey.RECIPE_TIME -> recipeTime = fn(recipeTime)
                ProcessorMenu.ProcessorDataKey.COMPLETIONS -> completion = fn(completion)
                else -> energy.setFromDataSchema(index, fn)
            } }
        }

        override fun getCount() = ProcessorMenu.dataSchema.getDataSize()

    }

    /**
     * Takes a recipe's ingredients out of the input slots, drawing each one from as many slots as it needs to.
     *
     * @param recipe The recipe being run.
     */
    open fun inputConsume(recipe: RecipeHolder<out ProcessorRecipe>) {
        val ingredients = recipe.value.ingredients
        val slots = inputSlots.map(::getSlot)

        for ((ingredient, count) in ingredients) {
            var remaining = count
            for (slot in slots) {
                if (remaining <= 0) break
                val stack = slot?.get()
                if (stack?.isEmpty != false) continue
                if (!ingredient.test(stack)) continue

                val toConsume = minOf(remaining, stack.count)
                stack.shrink(toConsume)
                remaining -= toConsume
            }
        }
    }

    /**
     * Puts a recipe's results into the output slots, filling what is already there before starting anything new.
     *
     * @param recipe The recipe being run.
     * @param results What it produced.
     * @return Returns `true` if all of it fitted, otherwise `false`, in which case nothing is put anywhere.
     */
    open fun outputResults(recipe: RecipeHolder<out ProcessorRecipe>, results: List<ItemStack>): Boolean {
        val slots = outputSlots.map(::getSlot)
        val stacks = slots.mapNotNull { it?.get()?.copy() }.toMutableList()

        for (stack in results) {
            if (stack.isEmpty) continue
            var remaining = stack.count

            for (existing in stacks) {
                if (remaining == 0) break
                if (existing.isEmpty || !ItemStack.isSameItemSameComponents(existing, stack)) continue

                val amount = minOf(remaining, existing.maxStackSize - existing.count)
                existing.grow(amount)
                remaining -= amount
            }

            for (index in stacks.indices) {
                if (remaining == 0) break
                if (!stacks[index].isEmpty) continue

                val amount = minOf(remaining, stack.maxStackSize)
                stacks[index] = stack.copyWithCount(amount)
                remaining -= amount
            }

            if (remaining > 0) return false
        }

        stacks.forEachIndexed { index, stack -> slots[index]?.set(stack) }
        return true
    }

    override fun getDefaultName() = blockState.block.name

    override fun getItems() = inventory

    public override fun setItems(items: NonNullList<ItemStack>) {
        inventory = items
    }

    override fun getContainerSize() = inventory.size

    override fun getSlotsForFace(side: Direction) = when (side) {
        Direction.UP -> inputSlots + batterySlots
        Direction.DOWN -> outputSlots
        else -> batterySlots
    }

    override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
        val current = inventory[slot]
        if (current.count > getMaxStackSize(stack)) {
            return false
        }

        if (slot in batterySlots) {
            val itemTransfer = ToBaseMod.transferProviders.items.fromContainer(this, null) ?: return false
            val energy = ToBaseMod.transferProviders.to_energy.fromItemStack(stack, itemTransfer, slot)
            return energy != null
        }

        return slot in inputSlots
    }

    override fun canPlaceItemThroughFace(slot: Int, stack: ItemStack, side: Direction?) = canPlaceItem(slot, stack)

    override fun canTakeItemThroughFace(slot: Int, stack: ItemStack, side: Direction): Boolean {
        val energy = ToEnergyItemProvider.getTransferContext(stack)
        if (energy != null) {
            if (slot in batterySlots) {
                return energy.getTotalAmount(Unit) <= 0L
            }
            return false
        }
        return true
    }

    override fun fillStackedContents(contents: StackedItemContents) {
        items.forEach(contents::accountStack)
    }

    override fun setRecipeUsed(recipeUsed: RecipeHolder<*>?) = Unit

    override fun getRecipeUsed() = null

    override fun awardUsedRecipes(player: Player, stacks: MutableList<ItemStack>) = Unit

    override fun loadAdditional(input: ValueInput) {
        super.loadAdditional(input)
        energy.loadAdditional(input)

        progress = input.getIntOr("progress", progress)
        progressMax = input.getIntOr("progressMax", progressMax)
        machineSpeed = input.getFloatOr("machineSpeed", machineSpeed)
        machineUsage = input.getFloatOr("machineUsage", machineUsage)
        completion = input.getIntOr("completion", completion)
        machineBonusMax = input.getIntOr("completionMax", machineBonusMax)
        machineBonus = input.getFloatOr("completionBonus", machineBonus)
        seed = input.getLong("seed").getOrNull()
        currentRecipe = input.read("currentRecipe", Recipe.KEY_CODEC).getOrNull()
        lastRecipe = input.read("lastRecipe", Recipe.KEY_CODEC).getOrNull()
        lastRecipeIcon = input.read("lastRecipeIcon", itemCodec).getOrNull()
        sameRecipe = currentRecipe == lastRecipe
        recipeEnergy = input.getLongOr("recipeEnergy", recipeEnergy)
        recipeTime = input.getIntOr("recipeTime", recipeTime)

        items.clear()
        ContainerHelper.loadAllItems(input, items)
    }

    override fun saveAdditional(output: ValueOutput) {
        energy.saveAdditional(output)

        output.putInt("progress", progress)
        output.putInt("progressMax", progressMax)
        output.putFloat("machineSpeed", machineSpeed)
        output.putFloat("machineUsage", machineUsage)
        output.putInt("completion", completion)
        output.putInt("completionMax", machineBonusMax)
        output.putFloat("completionBonus", machineBonus)
        if (seed != null) output.putLong("seed", seed!!)
        output.storeNullable("currentRecipe", Recipe.KEY_CODEC, currentRecipe)
        output.storeNullable("lastRecipe", Recipe.KEY_CODEC, lastRecipe)
        output.storeNullable("lastRecipeIcon", itemCodec, lastRecipeIcon)
        output.putLong("recipeEnergy", recipeEnergy)
        output.putInt("recipeTime", recipeTime)

        ContainerHelper.saveAllItems(output, items)
        super.saveAdditional(output)
    }

    override fun getTransferContext(level: LevelReader, pos: BlockPos, side: Direction?) = transferInput

    override fun applyImplicitComponents(components: DataComponentGetter) {
        super.applyImplicitComponents(components)
        energy.applyImplicitComponents(components)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        energy.collectImplicitComponents(components)
    }

    companion object {

        /**
         * The codec the icon of the last recipe is saved and loaded through.
         */
        val itemCodec = ResourceKey.codec(Registries.ITEM)

        /**
         * Ticks the block entity.
         *
         * @param level The level it is in.
         * @param pos Its position.
         * @param state Its state.
         * @param entity The block entity being ticked.
         */
        fun tick(level: Level, pos: BlockPos, state: BlockState, entity: ProcessorBlockEntity) {
            if (level.isClientSide) return

            entity.energy.tickAverages()

            val itemTransfer = Services.platform.transfer.itemProvider.fromContainer(entity, null)
            if (itemTransfer != null) {
                for (i in entity.batterySlots) {
                    val item = ToBaseMod.transferProviders.to_energy.fromItemStack(entity.inventory[i], itemTransfer, i)
                    if (item != null) {
                        TransferContext.moveAny(Unit, entity.energy.maxInput, item, entity.energy.transfer)
                    }
                }
            }

            val slevel = level as? ServerLevel ?: return
            val input = ProcessorRecipeInput(entity.blockState, entity.inputSlots.map(entity::getItem))
            val recipe = entity.quickCheck.getRecipeFor(input, slevel).getOrNull()
            entity.recipeEnergy = recipe?.value?.energy ?: 0L
            entity.recipeTime = recipe?.value?.time ?: 0
            var nextLit = false
            if (recipe != null) {
                val progressBase = Mth.ceil(entity.recipeTime / entity.machineSpeed)
                if (entity.seed == null) entity.seed = level.random.nextLong()

                if (recipe.id != entity.currentRecipe) {
                    entity.progress = 0
                }
                if (recipe.id == entity.lastRecipe) {
                    entity.progressMax = (progressBase / entity.machineBonus.minus(1).times(entity.completion).plus(1)).toInt()
                } else {
                    entity.progressMax = progressBase
                }

                val cost = Mth.ceil(Mth.ceil(entity.recipeEnergy * entity.machineUsage) / progressBase.toFloat())
                if (entity.energy.energy >= cost) {
                    nextLit = true
                    if (entity.progress >= entity.progressMax) {
                        val results = recipe.value.assembleAll(recipe.id.identifier(), entity, slevel, entity.seed!!)
                        if (entity.outputResults(recipe, results)) {
                            entity.inputConsume(recipe)
                            if (recipe.id == entity.lastRecipe) {
                                entity.completion = entity.completion.plus(1).coerceAtMost(entity.machineBonusMax)
                            } else {
                                entity.lastRecipe = recipe.id
                                entity.lastRecipeIcon = recipe.value.getFirstOutput().item.resourceKey
                                entity.completion = 1
                                Services.platform.networking.sendToPlayersTrackingChunk(slevel, ChunkPos.containing(pos), ProcessorSyncPayload(pos, Optional.ofNullable(entity.lastRecipe), Optional.ofNullable(entity.lastRecipeIcon)))
                            }
                            entity.progress = 0
                            entity.progressMax = (progressBase / entity.machineBonus.minus(1).times(entity.completion).plus(1)).toInt()
                            entity.seed = level.random.nextLong()
                        }
                    } else {
                        entity.energy.energy -= cost
                        entity.energy.historyExtract[0] = cost.toLong()
                        entity.progress += 1
                    }
                }
            } else {
                entity.progress = 0
                entity.progressMax = 0
            }
            entity.currentRecipe = recipe?.id
            entity.sameRecipe = entity.currentRecipe == entity.lastRecipe

            val lit = state.getValue(BlockStateProperties.LIT)
            if (lit && !nextLit) {
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, false), 3)
                setChanged(level, pos, state)
            } else if (!lit && nextLit) {
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), 3)
                setChanged(level, pos, state)
            }
        }

    }

}
