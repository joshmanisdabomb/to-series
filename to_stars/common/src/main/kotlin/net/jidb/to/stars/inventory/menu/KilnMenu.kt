package net.jidb.to.stars.inventory.menu

import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.entity.KilnBlockEntity
import net.jidb.to.stars.inventory.menu.CentrifugeMenu
import net.jidb.to.stars.inventory.menu.CentrifugeMenu.Companion.allSlots
import net.jidb.to.stars.inventory.menu.ProcessorMenu.Companion.dataSchema
import net.jidb.to.stars.inventory.menu.ProcessorMenu.ProcessorMenuData
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractFurnaceMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.RecipeBookType
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.crafting.RecipePropertySet

class KilnMenu(id: Int, playerInventory: Inventory, container: Container, data: ContainerData) : AbstractFurnaceMenu(ToStarsMod.menus.kiln, RecipePropertySet.FURNACE_INPUT, RecipeBookType.FURNACE, id, playerInventory, container, data) {

    constructor(id: Int, playerInventory: Inventory) : this(id, playerInventory, SimpleContainer(allSlots.size), SimpleContainerData(dataSchema.getDataSize()))

}
