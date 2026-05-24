package net.jidb.to.stars.client.screens

import net.jidb.to.base.client.service.ClientServices
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.entity.AtomicBombEntity
import net.jidb.to.stars.inventory.menu.AtomicBombMenu
import net.jidb.to.stars.network.AtomicBombDetonatePayload
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
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

class AtomicBombScreen(menu: AtomicBombMenu, playerInventory: Inventory, title: Component) : AbstractContainerScreen<AtomicBombMenu>(menu, playerInventory, title) {

    private var detonate: Button? = null
    private val detonateListener = object : ContainerListener {
        override fun slotChanged(menu: AbstractContainerMenu, slot: Int, stack: ItemStack) {
            updateCanDetonate()
        }

        override fun dataChanged(menu: AbstractContainerMenu, data: Int, value: Int) = Unit
    }

    private var initiated = false
    private var errors: IntArray = intArrayOf(0, 1, 2)

    init {
        imageWidth = 176
        imageHeight = 171

        inventoryLabelX = 8
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

    override fun renderBg(graphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        val i = leftPos
        val j = (height - imageHeight) / 2
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, i, j, 0.0f, 0.0f, imageWidth, imageHeight, 256, 256)
    }

    override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(graphics, mouseX, mouseY, partialTick)

        this.renderTooltip(graphics, mouseX, mouseY)
    }

    override fun renderTooltip(graphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        super.renderTooltip(graphics, mouseX, mouseY)

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
                tooltip = Component.translatable(
                    "gui.${ToStarsMod.modid}.atomic_bomb.detonate.info",
                    Component.translatable("gui.${ToStarsMod.modid}.atomic_bomb.detonate.info.strength", AtomicBombEntity.getExplosionStrength(count))
                        .withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)),
                    Component.translatable("gui.${ToStarsMod.modid}.atomic_bomb.detonate.info.fuse", AtomicBombEntity.getFuseTime(count) / 20)
                        .withStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)),
                )
                    .withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))
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

    protected fun updateCanDetonate() {
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

    inner class AtomicBombButton(x: Int, y: Int, width: Int, message: Component, onPress: OnPress) : Button.Plain(x, y, width, DEFAULT_HEIGHT, message, onPress, DEFAULT_NARRATION) {

        protected val sprites = WidgetSprites(
            Identifier.fromNamespaceAndPath(ToStarsMod.modid, "atomic_bomb/button"),
            Identifier.fromNamespaceAndPath(ToStarsMod.modid, "atomic_bomb/button_disabled"),
            Identifier.fromNamespaceAndPath(ToStarsMod.modid, "atomic_bomb/button_highlighted")
        )

        override fun renderContents(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprites.get(this.active, this.isHoveredOrFocused), this.x, this.y, this.getWidth(), this.getHeight(), ARGB.white(this.alpha))
            this.renderDefaultLabel(graphics.textRendererForWidget(this, GuiGraphics.HoveredTextEffects.NONE))
        }

    }

    companion object {
        val texture = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "textures/gui/atomic_bomb.png")

        private val bullet = Component.literal("\n - ")
    }

}