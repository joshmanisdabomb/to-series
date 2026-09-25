package net.jidb.to.stars.client.gui.screens

import net.jidb.to.base.api.info.TooltipEngine
import net.jidb.to.base.client.pub.gui.components.energy.EnergyBarWidget
import net.jidb.to.base.client.pub.gui.components.energy.EnergyTransferFromStackWidget
import net.jidb.to.base.client.pub.gui.components.energy.EnergyTransferToStackWidget
import net.jidb.to.base.pub.block.entity.ToEnergyBlockEntityHandler
import net.jidb.to.base.pub.info.ToBaseTooltipEngine
import net.jidb.to.base.pub.inventory.ToEnergyItemContainerListener
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.inventory.menu.EnergyStorageMenu
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack

/**
 * The screen a power bank is opened into, which shows its charge and which way energy is moving through each of its two slots.
 *
 * @param menu The interface being drawn.
 * @param playerInventory The inventory of the player who opened it.
 * @param title The title of the interface.
 */
class EnergyStorageScreen(menu: EnergyStorageMenu, playerInventory: Inventory, title: Component) : AbstractContainerScreen<EnergyStorageMenu>(menu, playerInventory, title, 176, 165) {

    /**
     * The charge bar, or `null` before the screen has been laid out.
     */
    private var bar: EnergyBarWidget? = null

    /**
     * Watches the slots so that an item's own charge can be shown in its tooltip.
     */
    private val energyItemListener = ToEnergyItemContainerListener(EnergyStorageMenu.allSlots, minecraft::level)

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

        menu.removeSlotListener(energyItemListener)
        menu.addSlotListener(energyItemListener)
    }

    override fun containerTick() {
        energyItemListener.tick(menu)
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
        run {
            val tooltip = energyItemListener.getTooltipFromContainerItem(stack, minecraft.level ?: return@run, minecraft.player ?: return@run, hoveredSlot?.index ?: return@run, minecraft.hasShiftDown(), minecraft.options.advancedItemTooltips)
            if (tooltip != null) {
                return tooltip
            }
        }
        return super.getTooltipFromContainerItem(stack)
    }

    companion object {

        /**
         * The background of the screen.
         */
        val texture = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "textures/gui/energy_storage.png")

    }

}
