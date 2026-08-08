package net.jidb.to.stars.client.gui.screens

import net.jidb.to.base.api.info.TooltipEngine
import net.jidb.to.base.client.service.ClientServices
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.entity.AtomicBombEntity
import net.jidb.to.stars.info.ToStarsTooltipEngine
import net.jidb.to.stars.inventory.menu.AtomicBombMenu
import net.jidb.to.stars.network.AtomicBombDetonatePayload
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.WidgetSprites
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.resources.Identifier
import net.minecraft.util.ARGB
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerListener
import net.minecraft.world.item.ItemStack

/**
 * The screen an atomic bomb is loaded and armed through.
 *
 * The arming button stays disabled until all three slots hold the right thing, and its tooltip either names what is missing or describes the blast that is about to happen.
 * Once it has been pressed it stays disabled, since the bomb is already on its way and there is nothing further to say.
 *
 * @param menu The interface being drawn.
 * @param playerInventory The inventory of the player who opened it.
 * @param title The title of the interface.
 */
class AtomicBombScreen(menu: AtomicBombMenu, playerInventory: Inventory, title: Component) : AbstractContainerScreen<AtomicBombMenu>(menu, playerInventory, title, 176, 171) {

    /**
     * The arming button, or `null` before the screen has been laid out.
     */
    private var detonate: Button? = null

    /**
     * Watches the slots so that the arming button is re-checked whenever what is in the bomb changes.
     */
    private val detonateListener = object : ContainerListener {

        override fun slotChanged(menu: AbstractContainerMenu, slot: Int, stack: ItemStack) {
            updateCanDetonate()
        }

        override fun dataChanged(menu: AbstractContainerMenu, data: Int, value: Int) = Unit

    }

    /**
     * Whether the bomb has already been armed from this screen.
     */
    private var initiated = false

    /**
     * Which slots hold the wrong thing, which is what the button's tooltip lists.
     */
    private var errors: IntArray = intArrayOf(0, 1, 2)

    init {
        inventoryLabelY = imageHeight - 94
    }

    override fun init() {
        super.init()

        detonate = this.addRenderableWidget(
            AtomicBombButton(
                leftPos + imageWidth / 4,
                topPos + 48,
                imageWidth / 2,
                Component.literal("")
            ) {
                initiated = true
                updateCanDetonate()
                ClientServices.platform.networking.sendToServer(AtomicBombDetonatePayload.instance)
            }
        )
        updateCanDetonate()

        menu.removeSlotListener(detonateListener)
        menu.addSlotListener(detonateListener)
    }

    override fun extractBackground(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick)
        val i = leftPos
        val j = (height - imageHeight) / 2
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, i, j, 0.0f, 0.0f, imageWidth, imageHeight, 256, 256)
    }

    override fun extractTooltip(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int) {
        super.extractTooltip(graphics, mouseX, mouseY)

        if (initiated) {
            return
        }

        var tooltip: MutableComponent? = null
        val slot = hoveredSlot
        if (slot?.hasItem() == false) {
            when (slot.index) {
                in AtomicBombMenu.explosiveSlots -> tooltip = Component.translatable("gui.${ToStarsMod.modid}.atomic_bomb.explosive")
                in AtomicBombMenu.bulletSlots -> tooltip = Component.translatable("gui.${ToStarsMod.modid}.atomic_bomb.bullet")
                in AtomicBombMenu.fuelSlots -> tooltip = Component.translatable(
                    "gui.${ToStarsMod.modid}.atomic_bomb.fuel",
                    AtomicBombMenu.getMaxStackSize(ItemStack(ToStarsMod.items.enriched_uranium)),
                    AtomicBombMenu.getMaxStackSize(ItemStack(ToStarsMod.blocks.enriched_uranium_block))
                )
            }
        } else if (detonate?.isHovered == true) {
            if (errors.isEmpty()) {
                val count = AtomicBombEntity.getUraniumCount(menu.items[2])
                tooltip = TooltipEngine.withLineBreaks(ToStarsTooltipEngine.getAtomicBombInfo(count))
            } else {
                tooltip = Component.translatable("gui.${ToStarsMod.modid}.atomic_bomb.detonate.error")
                    .withStyle(Style.EMPTY.withBold(true))
                if (errors.contains(0)) {
                    tooltip.append(bullet).append(Component.translatable("gui.${ToStarsMod.modid}.atomic_bomb.detonate.error.explosive"))
                }
                if (errors.contains(1)) {
                    tooltip.append(bullet).append(Component.translatable("gui.${ToStarsMod.modid}.atomic_bomb.detonate.error.bullet"))
                }
                if (errors.contains(2)) {
                    tooltip.append(bullet).append(Component.translatable("gui.${ToStarsMod.modid}.atomic_bomb.detonate.error.fuel"))
                }
            }
        }

        tooltip?.also {
            graphics.setTooltipForNextFrame(font, font.split(it, 200), mouseX, mouseY)
        }
    }

    /**
     * Re-checks whether the bomb can be armed, and sets the button's state and wording to suit.
     */
    private fun updateCanDetonate() {
        val button = detonate ?: return
        if (initiated) {
            button.active = false
            return
        }

        errors = menu.getErroredSlots()

        button.active = errors.isEmpty()
        button.message = Component.translatable("gui.${ToStarsMod.modid}.atomic_bomb.detonate")
            .withStyle(Style.EMPTY.withColor(if (errors.isEmpty()) 0xFFFFFF00.toInt() else 0xFF777700.toInt()))
    }

    override fun removed() {
        super.removed()
        menu.removeSlotListener(detonateListener)
    }

    /**
     * The arming button, which is drawn from the bomb's own textures rather than as an ordinary button.
     *
     * @param x The x position of the button.
     * @param y The y position of the button.
     * @param width The width of the button.
     * @param message What the button says.
     * @param onPress What happens when it is pressed.
     */
    inner class AtomicBombButton(x: Int, y: Int, width: Int, message: Component, onPress: OnPress) : Button.Plain(x, y, width, DEFAULT_HEIGHT, message, onPress, DEFAULT_NARRATION) {

        /**
         * The textures of the button, in each of its three states.
         */
        private val sprites = WidgetSprites(
            Identifier.fromNamespaceAndPath(ToStarsMod.modid, "atomic_bomb/button"),
            Identifier.fromNamespaceAndPath(ToStarsMod.modid, "atomic_bomb/button_disabled"),
            Identifier.fromNamespaceAndPath(ToStarsMod.modid, "atomic_bomb/button_highlighted")
        )

        override fun extractContents(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprites.get(this.active, this.isHoveredOrFocused), this.x, this.y, this.getWidth(), this.getHeight(), ARGB.white(this.alpha))
            this.extractDefaultLabel(graphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE))
        }

    }

    companion object {

        /**
         * The background of the screen.
         */
        val texture = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "textures/gui/atomic_bomb.png")

        /**
         * What each line of the button's error tooltip is prefixed with.
         */
        private val bullet = Component.literal("\n - ")

    }

}
