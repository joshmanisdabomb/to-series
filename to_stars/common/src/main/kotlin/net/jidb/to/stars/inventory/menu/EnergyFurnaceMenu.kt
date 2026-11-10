package net.jidb.to.stars.inventory.menu

import net.jidb.to.base.pub.inventory.slot.CraftingOutputSlot
import net.jidb.to.base.pub.inventory.slot.ToEnergyItemSlot
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.EnergyFurnaceBlock
import net.jidb.to.stars.block.entity.AbstractEnergyFurnaceBlockEntity
import net.jidb.to.stars.info.EnergyFurnaceType
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.recipebook.ServerPlaceRecipe
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.StackedItemContents
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipePropertySet
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import java.util.Optional

class EnergyFurnaceMenu(id: Int, playerInventory: Inventory, container: Container, data: ContainerData) : AbstractProcessorMenu(ToStarsMod.menus.energy_furnace, id, playerInventory, container, data) {

    override val inputSlots = Companion.inputSlots
    override val outputSlots = Companion.outputSlots
    override val batterySlots = Companion.batterySlots

    private var _clientData: EnergyFurnaceMenuData? = null

    private val tester by lazy {
        val level = playerInventory.player.level()
        when (getFurnaceType()) {
            EnergyFurnaceType.KILN -> KilnMenu.createPropertySetTester(level)
            EnergyFurnaceType.SMOKER -> level.recipeAccess().propertySet(RecipePropertySet.SMOKER_INPUT)::test
            EnergyFurnaceType.BLAST_FURNACE -> level.recipeAccess().propertySet(RecipePropertySet.BLAST_FURNACE_INPUT)::test
            else -> level.recipeAccess().propertySet(RecipePropertySet.FURNACE_INPUT)::test
        }
    }

    /**
     * Creates the menu on the client, where the energy furnace itself is not available and what it needs to draw is sent with the opening packet instead.
     *
     * @param id The id of the menu.
     * @param playerInventory The inventory of the player who opened it.
     * @param data What the client needs to know about the energy furnace.
     */
    constructor(id: Int, playerInventory: Inventory, data: EnergyFurnaceMenuData) : this(id, playerInventory, SimpleContainer(allSlots.size), SimpleContainerData(dataSchema.getDataSize())) {
        _clientData = data
    }

    init {
        checkContainerSize(container, allSlots.size)

        container.startOpen(playerInventory.player)

        addSlot(AbstractProcessorInputSlot(container, 0, 56, 49))
        addSlot(CraftingOutputSlot(container, playerInventory.player, inputSlots, 1, 115, 49))
        addSlot(ToEnergyItemSlot(container, 2, 8, 18, ::slotsChanged))

        addStandardInventorySlots(playerInventory, 8, 100)

        checkContainerDataCount(data, dataSchema.getDataSize())

        addDataSlots(data)
    }

    override fun handlePlacement(useMaxItems: Boolean, allowDroppingItemsToClear: Boolean, recipe: RecipeHolder<*>, level: ServerLevel, inventory: Inventory): PostPlaceAction = ServerPlaceRecipe.placeRecipe(object : ServerPlaceRecipe.CraftingMenuAccess<AbstractCookingRecipe> {

        override fun fillCraftSlotsStackedContents(stackedContents: StackedItemContents) = this@EnergyFurnaceMenu.fillCraftSlotsStackedContents(stackedContents)

        override fun clearCraftingContent() {
            inputSlots.forEach { this@EnergyFurnaceMenu.getSlot(it).set(ItemStack.EMPTY) }
        }

        override fun recipeMatches(recipe: RecipeHolder<AbstractCookingRecipe>) = recipe.value().matches(SingleRecipeInput(this@EnergyFurnaceMenu.items[inputSlots.first()]), level)

    }, inputSlots.size, 1, inputSlots.map(::getSlot), inputSlots.map(::getSlot), inventory, recipe as RecipeHolder<AbstractCookingRecipe>, useMaxItems, allowDroppingItemsToClear)

    override fun canPlaceInput(stack: ItemStack, index: Int, level: Level) = tester(stack)

    override fun getClientData() = _clientData

    fun getFurnaceType(): EnergyFurnaceType? {
        val cdata = getClientData()
        if (cdata != null) {
            return cdata.type
        }
        val bentity = container as? AbstractEnergyFurnaceBlockEntity ?: return null
        return (bentity.blockState.block as? EnergyFurnaceBlock)?.furnace
    }

    class EnergyFurnaceMenuData(var type: EnergyFurnaceType, pos: BlockPos, machineSpeed: Float, machineUsage: Float, machineBonus: Float, machineBonusMax: Int, lastRecipe: Optional<ResourceKey<Recipe<*>>>, lastRecipeIcon: Optional<ResourceKey<Item>>) : AbstractProcessorMenuData(pos, machineSpeed, machineUsage, machineBonus, machineBonusMax, lastRecipe, lastRecipeIcon) {

        /**
         * Reads what the client needs to be told straight off a machine.
         *
         * @param entity The machine the menu is being opened on.
         */
        constructor(entity: AbstractEnergyFurnaceBlockEntity) : this((entity.blockState.block as? EnergyFurnaceBlock)?.furnace ?: EnergyFurnaceType.FURNACE, entity.blockPos, entity.machineSpeed, entity.machineUsage, entity.machineBonus, entity.machineBonusMax, Optional.ofNullable(entity.lastRecipe), Optional.ofNullable(entity.lastRecipeIcon))

        companion object {

            /**
             * The codec this is sent to the client through as the menu opens.
             */
            val codec = StreamCodec.composite(
                EnergyFurnaceType.streamCodec,
                EnergyFurnaceMenuData::type,
                BlockPos.STREAM_CODEC,
                EnergyFurnaceMenuData::pos,
                ByteBufCodecs.FLOAT,
                EnergyFurnaceMenuData::machineSpeed,
                ByteBufCodecs.FLOAT,
                EnergyFurnaceMenuData::machineUsage,
                ByteBufCodecs.FLOAT,
                EnergyFurnaceMenuData::machineBonus,
                ByteBufCodecs.INT,
                EnergyFurnaceMenuData::machineBonusMax,
                ResourceKey.streamCodec(Registries.RECIPE).apply(ByteBufCodecs::optional),
                EnergyFurnaceMenuData::lastRecipe,
                ResourceKey.streamCodec(Registries.ITEM).apply(ByteBufCodecs::optional),
                EnergyFurnaceMenuData::lastRecipeIcon,
                ::EnergyFurnaceMenuData
            )

        }

    }

    companion object {

        /**
         * The slot a recipe's ingredients are taken from.
         */
        val inputSlots = intArrayOf(0)

        /**
         * The slots a recipe's results are put into, which are not interchangeable.
         */
        val outputSlots = intArrayOf(1)

        /**
         * The slots that contain energy storage that the machine can draw from.
         */
        val batterySlots = intArrayOf(2)

        /**
         * Every slot of the centrifuge, in the order they are drawn.
         */
        val allSlots = intArrayOf(*inputSlots, *outputSlots, *batterySlots)

    }

}
