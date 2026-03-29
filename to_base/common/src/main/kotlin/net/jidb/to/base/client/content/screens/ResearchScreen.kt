package net.jidb.to.base.client.content.screens

import com.mojang.blaze3d.platform.cursor.CursorTypes
import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.gui.ScaledTrackingItemStackRenderState
import net.jidb.to.base.client.wiki.WikiArticleManager
import net.jidb.to.base.content.inventory.menu.ResearchMenu
import net.jidb.to.base.helper.RegistryHelper
import net.jidb.to.base.service.Services
import net.jidb.to.base.wiki.WikiArticle
import net.jidb.to.base.wiki.WikiArticleLink
import net.minecraft.client.gui.ActiveTextCollector
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.*
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.gui.screens.ConfirmLinkScreen
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.input.KeyEvent
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.resources.Identifier
import net.minecraft.util.Mth
import net.minecraft.util.StringDecomposer
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import java.util.*
import kotlin.jvm.optionals.getOrNull

class ResearchScreen(menu: ResearchMenu, playerInventory: Inventory, protected val originalTitle: Component) : AbstractContainerScreen<ResearchMenu>(menu, playerInventory, Component.empty()) {

    protected var mode = ResearchScreenMode.HOME

    private var home: SpriteIconButton? = null
    private var searchText: EditBox? = null
    private var search: SpriteIconButton? = null
    private var openInBrowser: SpriteIconButton? = null

    private var listAll: Button? = null
    private var listBlocks: Button? = null
    private var listItems: Button? = null
    private var listEntities: Button? = null
    private var listToStars: Button? = null

    private var listButtons: List<Button> = emptyList()
    private var articleText: MultiLineTextWidget? = null

    private var currentArticle: WikiArticle? = null
    private var currentList = WikiArticleManager.index.all
    private var listTitle: Component? = null

    private var icons: List<Any> = emptyList()
    private var iconTicks = 0f

    private var scrollY = 0
    private var scrollHeight = 0
    private var scrollDragPoint: Int? = null

    private val fullWidth = 230
    private val fullHeight = 219
    private val toolbarY = 8
    private val toolbarWidth = 20
    private val toolbarHeight = 20
    private val leftToolbarX = 8
    private val rightToolbarX = fullWidth - 8 - toolbarWidth
    private val contentX = 9
    private val contentWidth = 195
    private val contentRight = contentX + contentWidth
    private val scrollbarX = 209
    private val scrollbarWidth = 12
    private val scrollbarRight = scrollbarX + scrollbarWidth - 1
    private val contentPageHeight = 141
    private val contentListHeight = 165
    private val contentPadding = 3
    private val contentLineHeight = 9
    private val contentBottom = fullHeight - 8
    private val listsButtonWidth = 192
    private val listButtonHeight = 26
    private val contentListY = contentBottom - contentListHeight
    private val contentPageY = contentBottom - contentPageHeight
    private val scrollbarThumbHeight = 15
    private val iconOffsetX = 6
    private val iconOffsetY = 33
    private val iconOffsetSize = 30
    private val titleX = 42
    private val titleY = 31
    private val titleWidth = 89
    private val titleHeight = 13
    private val subtitleY = 55
    private val labelX = 8
    private val labelY = 33

    private val contentY
        get() = if (mode == ResearchScreenMode.LIST) contentListY else contentPageY
    private val contentHeight
        get() = if (mode == ResearchScreenMode.LIST) contentListHeight else contentPageHeight

    init {
        imageWidth = fullWidth
        imageHeight = fullHeight

        inventoryLabelX = (leftPos + (imageWidth / 2)) - (18 * 4.5).toInt()
        inventoryLabelY = imageHeight - 94

        val item = minecraft.player?.mainHandItem ?: minecraft.player?.offhandItem
        if (item != null) {
            val article = WikiArticleManager.index[item.item].firstOrNull()
            if (article != null) {
                mode = ResearchScreenMode.PAGE
                currentArticle = article
            }
        }
    }

    override fun init() {
        super.init()

        home = this.addRenderableWidget(
            SpriteIconButton.builder(
                Component.translatable("container.${ToBaseMod.modid}.research.home"),
                { changeMode(ResearchScreenMode.HOME) },
                false
            )
                .width(toolbarWidth)
                .sprite(Identifier.fromNamespaceAndPath(ToBaseMod.modid, "research_home"), 14, 14)
                .withTootip()
                .build()
        ).apply {
            setPosition(leftPos + leftToolbarX, topPos + toolbarY)
        }
        searchText = this.addRenderableWidget(
            EditBox(this.font, imageWidth - 62, toolbarHeight,  Component.translatable("container.${ToBaseMod.modid}.research.search"))
        ).apply {
            setHint(Component.translatable("container.${ToBaseMod.modid}.research.search.hint"))
            setPosition(leftPos + leftToolbarX, topPos + toolbarY)
        }
        search = this.addRenderableWidget(
            SpriteIconButton.builder(
                Component.translatable("container.${ToBaseMod.modid}.research.search.button"),
                { this.startSearch() },
                false
            )
                .width(toolbarWidth)
                .sprite(Identifier.fromNamespaceAndPath(ToBaseMod.modid, "research_search"), 14, 14)
                .build()
        ).apply {
            setPosition(leftPos + imageWidth - 54, topPos + toolbarY)
        }
        openInBrowser = this.addRenderableWidget(
            SpriteIconButton.builder(
                Component.translatable("container.${ToBaseMod.modid}.research.browser"),
                { ConfirmLinkScreen.confirmLink(this, "${URL}/${(if (mode == ResearchScreenMode.PAGE) currentArticle?.id else null) ?: ""}").onPress(it) },
                false
            )
                .width(toolbarWidth)
                .sprite(Identifier.fromNamespaceAndPath(ToBaseMod.modid, "research_browser"), 15, 14)
                .withTootip()
                .build()
        ).apply {
            setPosition(leftPos + rightToolbarX, topPos + 8)
        }

        listAll = this.addRenderableWidget(
            PlainTextButton.builder(
                Component.translatable("container.${ToBaseMod.modid}.research.lists.all"),
                {
                    currentList = WikiArticleManager.index.all
                    listTitle = it.message
                    changeMode(ResearchScreenMode.LIST)
                }
            )
                .width(listsButtonWidth)
                .build()
        ).apply {
            setPosition(leftPos + imageWidth.div(2) - listsButtonWidth.div(2), topPos + 40)
        }
        listBlocks = this.addRenderableWidget(
            PlainTextButton.builder(
                Component.translatable("container.${ToBaseMod.modid}.research.lists.minecraft:block"),
                {
                    currentList = WikiArticleManager.index.byRegistry[Registries.BLOCK.identifier()] ?: emptyList()
                    listTitle = Component.translatable("container.${ToBaseMod.modid}.research.list.list", it.message)
                    changeMode(ResearchScreenMode.LIST)
                }
            )
                .width(listsButtonWidth / 3 - 3)
                .build()
        ).apply {
            setPosition(listAll!!.x, listAll!!.y + listAll!!.height + 4)
        }
        listItems = this.addRenderableWidget(
            PlainTextButton.builder(
                Component.translatable("container.${ToBaseMod.modid}.research.lists.minecraft:item"),
                {
                    currentList = WikiArticleManager.index.byRegistry[Registries.ITEM.identifier()] ?: emptyList()
                    listTitle = Component.translatable("container.${ToBaseMod.modid}.research.list.list", it.message)
                    changeMode(ResearchScreenMode.LIST)
                }
            )
                .width(listsButtonWidth / 3 - 2)
                .build()
        ).apply {
            setPosition(listBlocks!!.x + listBlocks!!.width + 4, listBlocks!!.y)
        }
        listEntities = this.addRenderableWidget(
            PlainTextButton.builder(
                Component.translatable("container.${ToBaseMod.modid}.research.lists.minecraft:entity_type"),
                {
                    currentList = WikiArticleManager.index.byRegistry[Registries.ENTITY_TYPE.identifier()] ?: emptyList()
                    listTitle = Component.translatable("container.${ToBaseMod.modid}.research.list.list", it.message)
                    changeMode(ResearchScreenMode.LIST)
                }
            )
                .width(listsButtonWidth / 3 - 3)
                .build()
        ).apply {
            setPosition(listItems!!.x + listItems!!.width + 4, listBlocks!!.y)
        }
        listToStars = this.addRenderableWidget(
            PlainTextButton.builder(
                Component.translatable("container.${ToBaseMod.modid}.research.lists.to_sky_and_stars"),
                {
                    currentList = WikiArticleManager.index.byMod["to_sky_and_stars"] ?: emptyList()
                    listTitle = it.message
                    changeMode(ResearchScreenMode.LIST)
                }
            )
                .width(listsButtonWidth.minus(4*3) / 4)
                .build()
        ).apply {
            active = Services.environment.isModLoaded("to_sky_and_stars")
            setPosition(listAll!!.x, listBlocks!!.y + listBlocks!!.height + 4)
        }

        articleText = this.addWidget(MultiLineTextWidget(Component.empty(), font)).apply {
            active = true
            setPosition(leftPos + contentX + contentPadding, topPos + contentY + contentPadding)
            setMaxWidth(contentWidth - contentPadding.times(2))
            setComponentClickHandler {
                val event = it.clickEvent?.also { it.action() }
                if (event is WikiArticleLink) {
                    currentArticle = event.article
                    changeMode(ResearchScreenMode.PAGE)
                }
            }
        }

        changeMode(mode)
    }

    private fun changeMode(mode: ResearchScreenMode) {
        menu.active = mode == ResearchScreenMode.HOME

        home?.visible = mode != ResearchScreenMode.HOME
        search?.visible = mode == ResearchScreenMode.HOME
        searchText?.visible = mode == ResearchScreenMode.HOME

        listAll?.visible = mode == ResearchScreenMode.HOME
        listBlocks?.visible = mode == ResearchScreenMode.HOME
        listItems?.visible = mode == ResearchScreenMode.HOME
        listEntities?.visible = mode == ResearchScreenMode.HOME
        listToStars?.visible = mode == ResearchScreenMode.HOME

        articleText?.visible = mode == ResearchScreenMode.PAGE

        if (mode == ResearchScreenMode.LIST) {
            listButtons = currentList.mapIndexed { index, article -> this.addWidget(
                ListButton(article, 0, 0, contentWidth, listButtonHeight, { currentArticle = article; changeMode(ResearchScreenMode.PAGE) }, ScreenRectangle(leftPos + contentX, topPos + contentListY, contentWidth, contentListHeight))
            ).apply {
                setPosition(leftPos + contentX, topPos + contentListY + index.times(this.height))
            } }
            scrollHeight = listButtons.sumOf { it.height }
        } else {
            listButtons.forEach(::removeWidget)
            listButtons = emptyList()
        }

        val article = currentArticle
        if (mode == ResearchScreenMode.PAGE && article != null) {
            val component = article.getContent(this.minecraft.languageManager.selected)!!.component
            articleText?.message = component.copy().withStyle(Style.EMPTY.withoutShadow().withColor(0xFF202020.toInt()))
            articleText?.y = topPos + contentPageY + contentPadding - scrollY
            scrollHeight = (articleText?.height ?: 0).plus(contentPadding.times(2))
            icons = getArticleIcons(article)
        }

        this.mode = mode
        iconTicks = 0f
        setScrollPosition(0)
    }

    override fun renderBg(graphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        val i = leftPos
        val j = (height - imageHeight) / 2
        graphics.blit(RenderPipelines.GUI_TEXTURED, mode.texture, i, j, 0.0f, 0.0f, imageWidth, imageHeight, 256, 256)

        if (mode == ResearchScreenMode.PAGE) {
            val icon = getArticleIcon(icons, iconTicks)
            if (icon != null) {
                renderArticleIcon(icon, graphics, i + iconOffsetX, j + iconOffsetY, 2f)
            }
        }
        if (mode != ResearchScreenMode.HOME) {
            val scrollPosition = getScrollPosition()
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, if (scrollHeight - contentHeight <= 0) scrollbar_thumb_disabled else scrollbar_thumb, leftPos + scrollbarX, topPos + contentY + scrollPosition, scrollbarWidth, scrollbarThumbHeight)
        }
    }

    override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(graphics, mouseX, mouseY, partialTick)

        graphics.enableScissor(leftPos + contentX, topPos + contentY, leftPos + contentRight, topPos + contentBottom)
        if (mode == ResearchScreenMode.PAGE) {
            articleText?.render(graphics, mouseX, mouseY, partialTick)
        } else if (mode == ResearchScreenMode.LIST) {
            listButtons.forEach { it.render(graphics, mouseX, mouseY, partialTick) }
        }
        graphics.disableScissor()

        this.renderTooltip(graphics, mouseX, mouseY)
    }

    override fun renderTooltip(graphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        super.renderTooltip(graphics, mouseX, mouseY)
        if (mode == ResearchScreenMode.PAGE) {
            if (mouseX-leftPos-iconOffsetX in 0 .. iconOffsetSize && mouseY-topPos-iconOffsetY in 0 .. iconOffsetSize) {
                val icon = getArticleIcon(icons, iconTicks)
                if (icon is ItemLike) {
                    graphics.setTooltipForNextFrame(this.font, ItemStack(icon), mouseX, mouseY)
                }
            }
        }
    }

    override fun renderLabels(graphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        when (mode) {
            ResearchScreenMode.PAGE -> {
                val title = Component.empty().append(currentArticle!!.short).withoutShadow()
                val tx = if (icons.isNotEmpty()) titleX else contentX
                graphics.pose().pushMatrix()
                graphics.pose().scaleAround(2F, 2F, tx.toFloat(), titleY.toFloat())
                graphics.textRenderer(GuiGraphics.HoveredTextEffects.NONE).acceptScrolling(title.copy().withColor(0xFF3E3E3E.toInt()), 0, tx + 1, tx + titleWidth + 1, titleY + 1, titleY + titleHeight + 1)
                graphics.textRenderer(GuiGraphics.HoveredTextEffects.NONE).acceptScrolling(title, 0, tx, titleX + titleWidth, titleY, titleY + titleHeight)
                graphics.pose().popMatrix()

                val subtitle = getArticleSubtitle(currentArticle!!)
                graphics.drawString(this.font, subtitle, tx, subtitleY, 0xFF808080.toInt(), false)
            }
            ResearchScreenMode.HOME -> {
                graphics.drawString(this.font, Component.translatable("container.${ToBaseMod.modid}.research.inventory"), inventoryLabelX, inventoryLabelY, 0xFF404040.toInt(), false)
            }
            ResearchScreenMode.LIST -> {
                graphics.drawString(this.font, listTitle!!, labelX, labelY, 0xFF404040.toInt(), false)
            }
        }
    }

    override fun renderSlot(guiGraphics: GuiGraphics, slot: Slot, mouseX: Int, mouseY: Int) {
        super.renderSlot(guiGraphics, slot, mouseX, mouseY)
        if (getSlotArticle(slot) != null) {
            if (slot == hoveredSlot) {
                guiGraphics.requestCursor(CursorTypes.POINTING_HAND)
            }
        } else {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, slot_locked, slot.x - 4, slot.y - 4, 24, 24)
        }
    }

    override fun getTitle() = originalTitle

    private fun startSearch() {
        currentList = WikiArticleManager.index.all.filter {
            StringDecomposer.getPlainText(it.title).lowercase().contains(searchText!!.value.lowercase())
        }
        listTitle = Component.translatable("container.${ToBaseMod.modid}.research.list.search", searchText!!.value)
        changeMode(ResearchScreenMode.LIST)
    }

    private fun getScrollPosition(): Int {
        val height = scrollHeight - contentHeight
        if (height <= 0) return 0
        return (scrollY.toDouble() / height).times(contentHeight - scrollbarThumbHeight).toInt()
    }

    private fun setScrollPosition(pos: Int) {
        val new = pos.coerceAtMost(scrollHeight - contentHeight).coerceAtLeast(0)
        val delta = new - scrollY
        if (mode == ResearchScreenMode.LIST) {
            listButtons.forEach { button ->
                button.y -= delta
            }
        } else if (mode == ResearchScreenMode.PAGE) {
            articleText?.y -= delta
        }
        scrollY = new
    }

    override fun keyPressed(event: KeyEvent): Boolean {
        if (searchText?.isFocused == true) {
            if (event.isConfirmation) {
                searchText?.isFocused = false
                startSearch()
                return false
            } else if (event.isEscape) {
                searchText?.isFocused = false
                return false
            } else if (!event.isCycleFocus) {
                searchText?.keyPressed(event)
                return false
            }
        }
        val cancel = super.keyPressed(event)
        if (event.isCycleFocus && mode == ResearchScreenMode.LIST) {
            val element = focused
            if (element != null && listButtons.contains(element) && (element.rectangle.top() < topPos + contentY || element.rectangle.bottom() > topPos + contentBottom)) {
                val first = listButtons.first()
                if (event.hasShiftDown()) {
                    setScrollPosition(element.rectangle.top() - first.y)
                } else {
                    setScrollPosition(element.rectangle.top() - first.y - (contentHeight - element.rectangle.height()))
                }
            }
        }
        return cancel
    }

    override fun checkHotbarKeyPressed(event: KeyEvent) = false

    override fun mouseScrolled(mouseX: Double, mouseY: Double, scrollX: Double, scrollY: Double): Boolean {
        if (!super.mouseScrolled(mouseX, mouseY, scrollX, scrollY)) {
            if (mode != ResearchScreenMode.HOME) {
                setScrollPosition(this.scrollY - scrollY.toInt().times(6))
                return true
            }
        }
        return false
    }

    override fun getChildAt(mouseX: Double, mouseY: Double): Optional<GuiEventListener> {
        val optional = super.getChildAt(mouseX, mouseY)
        val child = optional.orElse(null) ?: return optional
        if (listButtons.contains(child)) {
            if (mouseX.toInt() !in leftPos + contentX until leftPos + contentRight || mouseY.toInt() !in topPos + contentY until topPos + contentBottom) {
                return Optional.empty()
            }
        }
        return optional
    }

    override fun mouseClicked(event: MouseButtonEvent, isDoubleClick: Boolean): Boolean {
        if (mode == ResearchScreenMode.HOME) {
            val slot = hoveredSlot
            if (slot != null) {
                val article = getSlotArticle(slot)
                if (article != null) {
                    currentArticle = article
                    changeMode(ResearchScreenMode.PAGE)
                }
                return true
            }
        }
        val ret = super.mouseClicked(event, isDoubleClick)
        if (mode != ResearchScreenMode.HOME) {
            if (event.x.toInt() - leftPos in scrollbarX until scrollbarRight && event.y.toInt() - topPos in contentY until contentBottom) {
                val scrollPosition = getScrollPosition()
                if (event.y.toInt() - topPos in contentY + scrollPosition until  contentY + scrollPosition + scrollbarThumbHeight) {
                    scrollDragPoint = event.y.toInt() - topPos - contentY - scrollPosition
                } else {
                    val y = Mth.ceil(scrollbarThumbHeight.div(2f))
                    setScrollPosition((event.y.toInt() - y - topPos - contentY).div(contentHeight.toDouble() - y.times(2)).times(scrollHeight - contentHeight).toInt())
                    scrollDragPoint = y
                }
            }
        }
        return ret
    }

    override fun mouseReleased(event: MouseButtonEvent): Boolean {
        scrollDragPoint = null
        return super.mouseReleased(event)
    }

    override fun mouseDragged(event: MouseButtonEvent, dragX: Double, dragY: Double): Boolean {
        val dy = scrollDragPoint
        if (dy != null) {
            setScrollPosition((event.y.toInt() - dy - topPos - contentY).div(contentHeight.toDouble() - Mth.ceil(scrollbarThumbHeight.div(2f)).times(2)).times(scrollHeight - contentHeight).toInt())
            return true
        }
        return super.mouseDragged(event, dragX, dragY)
    }

    companion object {
        private const val URL = "https://to.jidb.net"

        val scrollbar_thumb = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "scrollbar_thumb")
        val scrollbar_thumb_disabled = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "scrollbar_thumb_disabled")
        val slot_locked = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "research_slot")

        private const val listButtonIconX = 5
        private const val listButtonIconY = 5
        private const val listButtonIconPadding = 24
        private const val listButtonPadding = 2

        private fun getArticleIcons(article: WikiArticle): List<Any> = article.icon.mapNotNull {
            when (val resource = RegistryHelper.getResource(it)) {
                is Block, is Item, is EntityType<*> -> resource
                else -> null
            }
        }

        private fun getArticleIcon(icons: List<Any>, iconTicks: Float): Any? {
            if (icons.isEmpty()) return null
            return icons[iconTicks.toInt().div(40) % icons.size]
        }

        private fun renderArticleIcon(icon: Any, graphics: GuiGraphics, x: Int, y: Int, scale: Float = 1f, scissors: ScreenRectangle? = null) {
            when (icon) {
                is ItemLike -> {
                    ScaledTrackingItemStackRenderState.renderItem(graphics, ItemStack(icon), x, y, scale, scissors)
                }
            }
        }

        private fun getArticleSubtitle(article: WikiArticle): Component {
            if (article.subtitle != null) {
                return article.subtitle
            }
            val first = article.about.first()
            if (article.about.size == 1 && first.first.toString() == "to_base:mod_version") {
                return WikiArticleManager.index.byResource[article.parent]!!.first().title
            }
            val token = with (article.about) {
                if (this.size > 1) "mixed" else first.first.toString()
            }
            return Component.translatable("container.${ToBaseMod.modid}.research.type.$token")
        }

        private fun getSlotArticle(slot: Slot): WikiArticle? {
            val item = slot.item.item
            val key = BuiltInRegistries.ITEM.getResourceKey(item).getOrNull() ?: return null
            return WikiArticleManager.index.byResource[key.registry() to key.identifier()]?.firstOrNull()
        }
    }

    enum class ResearchScreenMode {
        HOME,
        PAGE,
        LIST;

        val texture = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "textures/gui/research/${name.lowercase()}.png")
    }

    private class ListButton(val article: WikiArticle, x: Int, y: Int, width: Int, height: Int, onPress: OnPress, private val scissors: ScreenRectangle) : Button(x, y, width, height, article.title, onPress, DEFAULT_NARRATION) {
        val icons = getArticleIcons(article)
        var iconTicks = 0f

        override fun renderContents(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
            this.renderDefaultSprite(graphics)
            this.renderDefaultLabel(graphics.textRendererForWidget(this, GuiGraphics.HoveredTextEffects.NONE))

            val icon = getArticleIcon(icons, iconTicks)
            if (icon != null) {
                renderArticleIcon(icon, graphics, x + listButtonIconX, y + listButtonIconY, scissors = scissors.transformAxisAligned(graphics.pose()))
            }
            if (!isHovered) {
                iconTicks += partialTick
            }
        }

        override fun renderScrollingStringOverContents(activeTextCollector: ActiveTextCollector, text: Component, padding: Int) {
            val i = this.getX() + padding + (if (icons.isNotEmpty()) listButtonIconPadding else listButtonPadding)
            val j = this.getX() + this.getWidth() - padding
            val k = this.getY()
            val l = this.getY() + this.getHeight()
            activeTextCollector.acceptScrolling(text, 0, i, j, k, l)
        }
    }

}
