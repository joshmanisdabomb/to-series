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

abstract class ProcessorBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : BaseContainerBlockEntity(type, pos, state), WorldlyContainer, ToEnergyWorldlyProvider, StackedContentsCompatible, RecipeCraftingHolder {

    protected abstract var inventory: NonNullList<ItemStack>

    protected abstract val inputSlots: IntArray

    protected abstract val outputSlots: IntArray

    protected abstract val batterySlots: IntArray

    val energy = ToEnergyBlockEntityHandler(
        (state.block as? CentrifugeBlock)?.machine?.machineBuffer ?: 0L,
        (state.block as? CentrifugeBlock)?.machine?.maxInput ?: 0L,
        (state.block as? CentrifugeBlock)?.machine?.maxOutput ?: 0L,
    )

    val transferInput = InputToEnergyTransferContext(energy.transfer)

    var progress = 0

    var progressMax = 0

    var machineSpeed = (state.block as CentrifugeBlock).machine.machineSpeed

    var machineUsage = (state.block as CentrifugeBlock).machine.machineUsage

    var machineBonusMax = (state.block as CentrifugeBlock).machine.machineBonusMax

    var machineBonus = (state.block as CentrifugeBlock).machine.machineBonus

    var currentRecipe: ResourceKey<Recipe<*>>? = null

    var lastRecipe: ResourceKey<Recipe<*>>? = null

    var lastRecipeIcon: ResourceKey<Item>? = null

    var sameRecipe: Boolean = false

    var recipeEnergy = 0L

    var recipeTime = 0

    var completion = 0

    var seed: Long? = null

    private val quickCheck = RecipeManager.createCheck(ToStarsMod.recipeTypes.processor)

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

        val itemCodec = ResourceKey.codec(Registries.ITEM)

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
