package net.jidb.to.stars.client.gui.screens

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.info.TooltipEngine
import net.jidb.to.base.client.pub.gui.components.EnergyBarWidget
import net.jidb.to.base.client.pub.gui.components.EnergyTransferFromStackWidget
import net.jidb.to.base.client.pub.gui.components.EnergyTransferToStackWidget
import net.jidb.to.base.pub.block.entity.ToEnergyBlockEntityHandler
import net.jidb.to.base.pub.info.ToBaseTooltipEngine
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.inventory.menu.EnergyStorageMenu
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerListener
import net.minecraft.world.item.ItemStack

class EnergyStorageScreen(menu: EnergyStorageMenu, playerInventory: Inventory, title: Component) : AbstractContainerScreen<EnergyStorageMenu>(menu, playerInventory, title, 176, 165) {

    private var bar: EnergyBarWidget? = null

    private val lastEnergySeen = LongArray(EnergyStorageMenu.allSlots.size)
    private val lastEnergyChange = LongArray(EnergyStorageMenu.allSlots.size)
    private val lastEnergyStale = LongArray(EnergyStorageMenu.allSlots.size)
    private val energyListener = object : ContainerListener {
        override fun slotChanged(menu: AbstractContainerMenu, slot: Int, stack: ItemStack) {
            val energy = stack.get(ToBaseMod.itemComponents.energy_data) ?: return
            if (slot !in lastEnergySeen.indices) return
            lastEnergyChange[slot] = energy.energy - lastEnergySeen[slot]
            lastEnergySeen[slot] = energy.energy
            lastEnergyStale[slot] = minecraft.level?.gameTime?.plus(1L) ?: 0L
        }

        override fun dataChanged(menu: AbstractContainerMenu, data: Int, value: Int) = Unit
    }

    init {
        titleLabelY = 23
        inventoryLabelY = imageHeight - 94
    }

    override fun init() {
        super.init()

        bar = this.addRenderableWidget(
            EnergyBarWidget(
                { ToEnergyBlockEntityHandler.dataSchema.getLongValue(menu.data, ToEnergyBlockEntityHandler.EnergyStorageDataKey.TOTAL) ?: 0L },
                { ToEnergyBlockEntityHandler.dataSchema.getLongValue(menu.data, ToEnergyBlockEntityHandler.EnergyStorageDataKey.CAPACITY) ?: 0L },
                leftPos + 28,
                topPos + 6
            )
        )

        this.addRenderableWidget(
            EnergyTransferFromStackWidget(
                menu.getSlot(0),
                { (ToEnergyBlockEntityHandler.dataSchema.getLongValue(menu.data, ToEnergyBlockEntityHandler.EnergyStorageDataKey.TOTAL) ?: 0L) >= (ToEnergyBlockEntityHandler.dataSchema.getLongValue(menu.data, ToEnergyBlockEntityHandler.EnergyStorageDataKey.CAPACITY) ?: 0L) },
                leftPos + 73,
                topPos + 56
            )
        )
        this.addRenderableWidget(
            EnergyTransferToStackWidget(
                menu.getSlot(1),
                { (ToEnergyBlockEntityHandler.dataSchema.getLongValue(menu.data, ToEnergyBlockEntityHandler.EnergyStorageDataKey.TOTAL) ?: 0L) <= 0 },
                leftPos + 95,
                topPos + 56
            )
        )

        menu.removeSlotListener(energyListener)
        menu.addSlotListener(energyListener)
    }

    override fun containerTick() {
        val tick = minecraft.level?.gameTime ?: 0L
        for (i in lastEnergySeen.indices) {
            if (tick > lastEnergyStale[i]) {
                lastEnergyChange[i] = 0L
                lastEnergySeen[i] = menu.items[i].get(ToBaseMod.itemComponents.energy_data)?.energy ?: 0L
            }
        }
    }

    override fun extractBackground(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick)
        val i = leftPos
        val j = (height - imageHeight) / 2
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, i, j, 0.0f, 0.0f, imageWidth, imageHeight, 256, 256)
    }

    override fun extractTooltip(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int) {
        super.extractTooltip(graphics, mouseX, mouseY)
        if (bar?.isHovered == true) {
            val tooltip = TooltipEngine.withLineBreaks(ToBaseTooltipEngine.getEnergyInfo(
                ToEnergyBlockEntityHandler.dataSchema.getLongValue(menu.data, ToEnergyBlockEntityHandler.EnergyStorageDataKey.TOTAL) ?: 0L,
                ToEnergyBlockEntityHandler.dataSchema.getLongValue(menu.data, ToEnergyBlockEntityHandler.EnergyStorageDataKey.CAPACITY) ?: 0L,
                ToEnergyBlockEntityHandler.dataSchema.getLongValue(menu.data, ToEnergyBlockEntityHandler.EnergyStorageDataKey.MAX_INPUT) ?: 0L,
                ToEnergyBlockEntityHandler.dataSchema.getLongValue(menu.data, ToEnergyBlockEntityHandler.EnergyStorageDataKey.MAX_OUTPUT) ?: 0L,
                ToEnergyBlockEntityHandler.dataSchema.getLongValue(menu.data, ToEnergyBlockEntityHandler.EnergyStorageDataKey.INSERT_CHANGE),
                ToEnergyBlockEntityHandler.dataSchema.getLongValue(menu.data, ToEnergyBlockEntityHandler.EnergyStorageDataKey.INSERT_AVERAGE),
                ToEnergyBlockEntityHandler.dataSchema.getLongValue(menu.data, ToEnergyBlockEntityHandler.EnergyStorageDataKey.EXTRACT_CHANGE),
                ToEnergyBlockEntityHandler.dataSchema.getLongValue(menu.data, ToEnergyBlockEntityHandler.EnergyStorageDataKey.EXTRACT_AVERAGE),
                advanced = minecraft.hasShiftDown()
            ))
            graphics.setTooltipForNextFrame(font, font.split(tooltip, 200), mouseX, mouseY)
        }
    }

    override fun getTooltipFromContainerItem(stack: ItemStack): List<Component> {
        val level = minecraft.level ?: return super.getTooltipFromContainerItem(stack)
        val player = minecraft.player ?: return super.getTooltipFromContainerItem(stack)
        val index = hoveredSlot?.index ?: return super.getTooltipFromContainerItem(stack)
        if (index !in lastEnergySeen.indices) return super.getTooltipFromContainerItem(stack)
        val energy = stack.get(ToBaseMod.itemComponents.energy_data) ?: return super.getTooltipFromContainerItem(stack)

        val new = stack.copy()
        new.remove(ToBaseMod.itemComponents.energy_data)

        val info = ToBaseTooltipEngine.getEnergyInfo(energy.energy, energy.max, energy.maxInput, energy.maxOutput, lastEnergyChange[index].coerceAtLeast(0L), null, lastEnergyChange[index].coerceAtMost(0L), null, advanced = minecraft.hasShiftDown())
        return TooltipEngine.injectItemTooltip(new, info, level, player, minecraft.options.advancedItemTooltips)
    }

    companion object {
        val texture = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "textures/gui/energy_storage.png")
    }

}