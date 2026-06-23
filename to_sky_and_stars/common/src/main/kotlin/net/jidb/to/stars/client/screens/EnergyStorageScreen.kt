package net.jidb.to.stars.client.screens

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.info.TooltipEngine
import net.jidb.to.base.client.pub.gui.components.EnergyBarWidget
import net.jidb.to.base.client.pub.gui.components.EnergyTransferFromStackWidget
import net.jidb.to.base.client.pub.gui.components.EnergyTransferToStackWidget
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
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

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
                { EnergyStorageMenu.dataSchema.getLong(menu.data, EnergyStorageMenu.EnergyStorageDataKey.TOTAL) },
                { EnergyStorageMenu.dataSchema.getLong(menu.data, EnergyStorageMenu.EnergyStorageDataKey.CAPACITY) },
                leftPos + 28,
                topPos + 6
            )
        )

        this.addRenderableWidget(
            EnergyTransferFromStackWidget(
                menu.getSlot(0),
                { EnergyStorageMenu.dataSchema.getLong(menu.data, EnergyStorageMenu.EnergyStorageDataKey.TOTAL) >= EnergyStorageMenu.dataSchema.getLong(menu.data, EnergyStorageMenu.EnergyStorageDataKey.CAPACITY) },
                leftPos + 73,
                topPos + 56
            )
        )
        this.addRenderableWidget(
            EnergyTransferToStackWidget(
                menu.getSlot(1),
                { EnergyStorageMenu.dataSchema.getLong(menu.data, EnergyStorageMenu.EnergyStorageDataKey.TOTAL) <= 0 },
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
                EnergyStorageMenu.dataSchema.getLong(menu.data, EnergyStorageMenu.EnergyStorageDataKey.TOTAL),
                EnergyStorageMenu.dataSchema.getLong(menu.data, EnergyStorageMenu.EnergyStorageDataKey.CAPACITY),
                EnergyStorageMenu.dataSchema.getLong(menu.data, EnergyStorageMenu.EnergyStorageDataKey.MAX_INPUT),
                EnergyStorageMenu.dataSchema.getLong(menu.data, EnergyStorageMenu.EnergyStorageDataKey.MAX_OUTPUT),
                EnergyStorageMenu.dataSchema.getLong(menu.data, EnergyStorageMenu.EnergyStorageDataKey.INSERT_CHANGE),
                EnergyStorageMenu.dataSchema.getLong(menu.data, EnergyStorageMenu.EnergyStorageDataKey.INSERT_AVERAGE),
                EnergyStorageMenu.dataSchema.getLong(menu.data, EnergyStorageMenu.EnergyStorageDataKey.EXTRACT_CHANGE),
                EnergyStorageMenu.dataSchema.getLong(menu.data, EnergyStorageMenu.EnergyStorageDataKey.EXTRACT_AVERAGE),
                advanced = minecraft.hasShiftDown()
            ))
            graphics.setTooltipForNextFrame(font, font.split(tooltip, 200), mouseX, mouseY)
        }
    }

    override fun getTooltipFromContainerItem(stack: ItemStack): List<Component> {
        val index = hoveredSlot?.index ?: return super.getTooltipFromContainerItem(stack)
        if (index !in lastEnergySeen.indices) return super.getTooltipFromContainerItem(stack)
        val energy = stack.get(ToBaseMod.itemComponents.energy_data) ?: return super.getTooltipFromContainerItem(stack)

        val new = stack.copy()
        new.remove(ToBaseMod.itemComponents.energy_data)
        val advanced = new.getTooltipLines(Item.TooltipContext.of(minecraft.level), minecraft.player, if (minecraft.options.advancedItemTooltips) TooltipFlag.Default.ADVANCED else TooltipFlag.Default.NORMAL)
        val normal = new.getTooltipLines(Item.TooltipContext.of(minecraft.level), minecraft.player, TooltipFlag.Default.NORMAL)

        val info = ToBaseTooltipEngine.getEnergyInfo(energy.energy, energy.max, energy.maxInput, energy.maxOutput, lastEnergyChange[index].coerceAtLeast(0L), null, lastEnergyChange[index].coerceAtMost(0L), null, advanced = minecraft.hasShiftDown())
        return normal + info + (advanced - normal)
    }

    companion object {
        val texture = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "textures/gui/energy_storage.png")
    }

}