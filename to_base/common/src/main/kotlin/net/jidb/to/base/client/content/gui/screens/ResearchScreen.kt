package net.jidb.to.base.client.content.gui.screens

import com.mojang.blaze3d.platform.cursor.CursorTypes
import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.helper.RegistryHelper
import net.jidb.to.base.api.wiki.WikiArticle
import net.jidb.to.base.api.wiki.WikiArticleLink
import net.jidb.to.base.client.content.wiki.WikiArticleManager
import net.jidb.to.base.client.pub.gui.ScaledTrackingItemStackRenderState
import net.jidb.to.base.content.inventory.menu.ResearchMenu
import net.jidb.to.base.service.Services
import net.minecraft.client.gui.ActiveTextCollector
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.components.MultiLineTextWidget
import net.minecraft.client.gui.components.PlainTextButton
import net.minecraft.client.gui.components.SpriteIconButton
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
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

/**
 * The screen opened by a research desk, through which the in-game wiki is read.
 *
 * It is one screen rather than three, switching between a home page, an article page and a list of articles through [mode]; each mode has its own background texture, its own content height, and its own set of visible widgets, which [changeMode] applies.
 * Opening the desk while holding an item that has an article of its own jumps straight to that article rather than to the home page.
 *
 * Scrolling is done by hand rather than by a vanilla scrolling widget, because the two scrollable modes scroll different things: the list moves its buttons, and the article moves its text widget.
 *
 * @param menu The menu this screen displays.
 * @param playerInventory The inventory of the player who opened the screen.
 * @property originalTitle The title the menu was opened with, kept because the screen passes an empty one to its superclass so that vanilla does not draw it.
 * @since 0.1.0
 */
class ResearchScreen(menu: ResearchMenu, playerInventory: Inventory, protected val originalTitle: Component) : AbstractContainerScreen<ResearchMenu>(menu, playerInventory, Component.empty(), 230, 219) {

    /**
     * Which of the three pages the screen is currently showing.
     *
     * @since 0.1.0
     */
    private var mode = ResearchScreenMode.HOME

    /**
     * The button returning to the home page, shown on every page but the home page itself.
     *
     * @since 0.1.0
     */
    private var home: SpriteIconButton? = null

    /**
     * The box a search term is typed into, shown only on the home page.
     *
     * @since 0.1.0
     */
    private var searchText: EditBox? = null

    /**
     * The button running the typed search, shown only on the home page.
     *
     * @since 0.1.0
     */
    private var search: SpriteIconButton? = null

    /**
     * The button opening the online wiki in a browser.
     *
     * @since 0.1.0
     */
    private var openInBrowser: SpriteIconButton? = null

    /**
     * The home page button listing every article.
     *
     * @since 0.1.0
     */
    private var listAll: Button? = null

    /**
     * The home page button listing the articles about blocks.
     *
     * @since 0.1.0
     */
    private var listBlocks: Button? = null

    /**
     * The home page button listing the articles about items.
     *
     * @since 0.1.0
     */
    private var listItems: Button? = null

    /**
     * The home page button listing the articles about entities.
     *
     * @since 0.1.0
     */
    private var listEntities: Button? = null

    /**
     * The home page button listing the articles of To Sky and Stars.
     *
     * @since 0.1.0
     */
    private var listToStars: Button? = null

    /**
     * The buttons currently making up the list page, one per article in [currentList], rebuilt each time the list changes.
     *
     * @since 0.1.0
     */
    private var listButtons: List<Button> = emptyList()

    /**
     * The widget the body of an article is drawn in, shown only on the article page.
     *
     * @since 0.1.0
     */
    private var articleText: MultiLineTextWidget? = null

    /**
     * The article the article page is showing, or `null` where none has been opened yet.
     *
     * @since 0.1.0
     */
    private var currentArticle: WikiArticle? = null

    /**
     * The articles the list page is showing, which starts as every article there is.
     *
     * @since 0.1.0
     */
    private var currentList = WikiArticleManager.index.all

    /**
     * The heading shown above the list page, naming what was listed or searched for.
     *
     * @since 0.1.0
     */
    private var listTitle: Component? = null

    /**
     * The icons of the article currently open, which are cycled between where an article has more than one.
     *
     * @since 0.1.0
     */
    private var icons: List<Any> = emptyList()

    /**
     * How long the current icon has been shown for, which is what advances the cycle through [icons].
     *
     * @since 0.1.0
     */
    private var iconTicks = 0f

    /**
     * How far the current page has been scrolled down, in pixels.
     *
     * @since 0.1.0
     */
    private var scrollY = 0

    /**
     * The full height of the current page's content, against which [scrollY] decides how much is left to scroll.
     *
     * @since 0.1.0
     */
    private var scrollHeight = 0

    /**
     * Where within the scrollbar thumb it was grabbed, or `null` where it is not being dragged.
     *
     * @since 0.1.0
     */
    private var scrollDragPoint: Int? = null

    /**
     * The y position of the toolbar buttons along the top of the screen.
     *
     * @since 0.1.0
     */
    private val toolbarY = 8

    /**
     * The width of a toolbar button.
     *
     * @since 0.1.0
     */
    private val toolbarWidth = 20

    /**
     * The height of a toolbar button.
     *
     * @since 0.1.0
     */
    private val toolbarHeight = 20

    /**
     * The x position of the toolbar buttons on the left of the screen.
     *
     * @since 0.1.0
     */
    private val leftToolbarX = 8

    /**
     * The x position of the toolbar buttons on the right of the screen.
     *
     * @since 0.1.0
     */
    private val rightToolbarX = imageWidth - 8 - toolbarWidth

    /**
     * The x position of the scrollable content area.
     *
     * @since 0.1.0
     */
    private val contentX = 9

    /**
     * The width of the scrollable content area.
     *
     * @since 0.1.0
     */
    private val contentWidth = 195

    /**
     * The x position of the right edge of the scrollable content area.
     *
     * @since 0.1.0
     */
    private val contentRight = contentX + contentWidth

    /**
     * The x position of the scrollbar.
     *
     * @since 0.1.0
     */
    private val scrollbarX = 209

    /**
     * The width of the scrollbar.
     *
     * @since 0.1.0
     */
    private val scrollbarWidth = 12

    /**
     * The x position of the right edge of the scrollbar.
     *
     * @since 0.1.0
     */
    private val scrollbarRight = scrollbarX + scrollbarWidth - 1

    /**
     * The height of the content area on the article page, which is shorter than the list page to leave room for the article's heading.
     *
     * @since 0.1.0
     */
    private val contentPageHeight = 141

    /**
     * The height of the content area on the list page.
     *
     * @since 0.1.0
     */
    private val contentListHeight = 165

    /**
     * The gap between the edge of the content area and the content inside it.
     *
     * @since 0.1.0
     */
    private val contentPadding = 3

    /**
     * The height of one line of article text, which a scroll of one step moves by.
     *
     * @since 0.1.0
     */
    private val contentLineHeight = 9

    /**
     * The y position of the bottom of the content area, which both modes share.
     *
     * @since 0.1.0
     */
    private val contentBottom = imageHeight - 8

    /**
     * The width of a button on the list page.
     *
     * @since 0.1.0
     */
    private val listsButtonWidth = 192

    /**
     * The height of a button on the list page.
     *
     * @since 0.1.0
     */
    private val listButtonHeight = 26

    /**
     * The y position of the content area on the list page.
     *
     * @since 0.1.0
     */
    private val contentListY = contentBottom - contentListHeight

    /**
     * The y position of the content area on the article page.
     *
     * @since 0.1.0
     */
    private val contentPageY = contentBottom - contentPageHeight

    /**
     * The height of the scrollbar thumb, which is fixed rather than sized by how much there is to scroll.
     *
     * @since 0.1.0
     */
    private val scrollbarThumbHeight = 15

    /**
     * The x position of the icon shown beside an article's title.
     *
     * @since 0.1.0
     */
    private val iconOffsetX = 6

    /**
     * The y position of the icon shown beside an article's title.
     *
     * @since 0.1.0
     */
    private val iconOffsetY = 33

    /**
     * The size the icon beside an article's title is drawn at, which is larger than an item's usual sixteen pixels.
     *
     * @since 0.1.0
     */
    private val iconOffsetSize = 30

    /**
     * The x position of an article's title.
     *
     * @since 0.1.0
     */
    private val titleX = 42

    /**
     * The y position of an article's title.
     *
     * @since 0.1.0
     */
    private val titleY = 31

    /**
     * The width an article's title is fitted into.
     *
     * @since 0.1.0
     */
    private val titleWidth = 89

    /**
     * The height an article's title is fitted into.
     *
     * @since 0.1.0
     */
    private val titleHeight = 13

    /**
     * The y position of an article's subtitle.
     *
     * @since 0.1.0
     */
    private val subtitleY = 55

    /**
     * The x position of the screen's own label, such as the heading of a list.
     *
     * @since 0.1.0
     */
    private val labelX = 8

    /**
     * The y position of the screen's own label, such as the heading of a list.
     *
     * @since 0.1.0
     */
    private val labelY = 33

    /**
     * The y position of the content area for the mode currently showing.
     *
     * @since 0.1.0
     */
    private val contentY
        get() = if (mode == ResearchScreenMode.LIST) contentListY else contentPageY

    /**
     * The height of the content area for the mode currently showing.
     *
     * @since 0.1.0
     */
    private val contentHeight
        get() = if (mode == ResearchScreenMode.LIST) contentListHeight else contentPageHeight

    init {
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
                Component.translatable("gui.${ToBaseMod.modid}.research.home"),
                { changeMode(ResearchScreenMode.HOME) },
                false
            )
                .width(toolbarWidth)
                .sprite(Identifier.fromNamespaceAndPath(ToBaseMod.modid, "research/home"), 14, 14)
                .withTootip()
                .build()
        ).apply {
            setPosition(leftPos + leftToolbarX, topPos + toolbarY)
        }
        searchText = this.addRenderableWidget(
            EditBox(this.font, imageWidth - 62, toolbarHeight, Component.translatable("gui.${ToBaseMod.modid}.research.search"))
        ).apply {
            setHint(Component.translatable("gui.${ToBaseMod.modid}.research.search.hint"))
            setPosition(leftPos + leftToolbarX, topPos + toolbarY)
        }
        search = this.addRenderableWidget(
            SpriteIconButton.builder(
                Component.translatable("gui.${ToBaseMod.modid}.research.search.button"),
                { this.startSearch() },
                false
            )
                .width(toolbarWidth)
                .sprite(Identifier.fromNamespaceAndPath(ToBaseMod.modid, "research/search"), 14, 14)
                .build()
        ).apply {
            setPosition(leftPos + imageWidth - 54, topPos + toolbarY)
        }
        openInBrowser = this.addRenderableWidget(
            SpriteIconButton.builder(
                Component.translatable("gui.${ToBaseMod.modid}.research.browser"),
                { ConfirmLinkScreen.confirmLink(this, "$URL/${(if (mode == ResearchScreenMode.PAGE) currentArticle?.id else null) ?: ""}").onPress(it) },
                false
            )
                .width(toolbarWidth)
                .sprite(Identifier.fromNamespaceAndPath(ToBaseMod.modid, "research/browser"), 15, 14)
                .withTootip()
                .build()
        ).apply {
            setPosition(leftPos + rightToolbarX, topPos + 8)
        }

        listAll = this.addRenderableWidget(
            PlainTextButton.builder(
                Component.translatable("gui.${ToBaseMod.modid}.research.lists.all"),
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
                Component.translatable("gui.${ToBaseMod.modid}.research.lists.minecraft:block"),
                {
                    currentList = WikiArticleManager.index.byRegistry[Registries.BLOCK.identifier()] ?: emptyList()
                    listTitle = Component.translatable("gui.${ToBaseMod.modid}.research.list.list", it.message)
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
                Component.translatable("gui.${ToBaseMod.modid}.research.lists.minecraft:item"),
                {
                    currentList = WikiArticleManager.index.byRegistry[Registries.ITEM.identifier()] ?: emptyList()
                    listTitle = Component.translatable("gui.${ToBaseMod.modid}.research.list.list", it.message)
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
                Component.translatable("gui.${ToBaseMod.modid}.research.lists.minecraft:entity_type"),
                {
                    currentList = WikiArticleManager.index.byRegistry[Registries.ENTITY_TYPE.identifier()] ?: emptyList()
                    listTitle = Component.translatable("gui.${ToBaseMod.modid}.research.list.list", it.message)
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
                Component.translatable("gui.${ToBaseMod.modid}.research.lists.to_stars"),
                {
                    currentList = WikiArticleManager.index.byMod["to_stars"] ?: emptyList()
                    listTitle = it.message
                    changeMode(ResearchScreenMode.LIST)
                }
            )
                .width(listsButtonWidth.minus(4 * 3) / 4)
                .build()
        ).apply {
            active = Services.environment.isModLoaded("to_stars")
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

    /**
     * Switches the screen to the given page, showing the widgets that belong to it and hiding the rest.
     * The player's own inventory slots are only shown on the home page, as the other two cover them.
     *
     * @param mode The page to switch to.
     * @since 0.1.0
     */
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

    override fun extractBackground(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick)

        val i = leftPos
        val j = (height - imageHeight) / 2
        graphics.blit(RenderPipelines.GUI_TEXTURED, mode.texture, i, j, 0.0f, 0.0f, imageWidth, imageHeight, 256, 256)

        if (mode == ResearchScreenMode.PAGE) {
            val icon = getArticleIcon(icons, iconTicks)
            if (icon != null) {
                extractArticleIcon(icon, graphics, i + iconOffsetX, j + iconOffsetY, 2f)
            }
        }
        if (mode != ResearchScreenMode.HOME) {
            val scrollPosition = getScrollPosition()
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, if (scrollHeight - contentHeight <= 0) scrollbarThumbDisabled else scrollbarThumb, leftPos + scrollbarX, topPos + contentY + scrollPosition, scrollbarWidth, scrollbarThumbHeight)
        }
    }

    override fun extractContents(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.extractContents(graphics, mouseX, mouseY, partialTick)

        graphics.enableScissor(leftPos + contentX, topPos + contentY, leftPos + contentRight, topPos + contentBottom)
        if (mode == ResearchScreenMode.PAGE) {
            articleText?.extractRenderState(graphics, mouseX, mouseY, partialTick)
        } else if (mode == ResearchScreenMode.LIST) {
            listButtons.forEach { it.extractRenderState(graphics, mouseX, mouseY, partialTick) }
        }
        graphics.disableScissor()
    }

    override fun extractTooltip(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int) {
        super.extractTooltip(graphics, mouseX, mouseY)
        if (mode == ResearchScreenMode.PAGE) {
            if (mouseX - leftPos - iconOffsetX in 0..iconOffsetSize && mouseY - topPos - iconOffsetY in 0..iconOffsetSize) {
                val icon = getArticleIcon(icons, iconTicks)
                if (icon is ItemLike) {
                    graphics.setTooltipForNextFrame(this.font, ItemStack(icon), mouseX, mouseY)
                }
            }
        }
    }

    override fun extractLabels(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int) {
        when (mode) {
            ResearchScreenMode.PAGE -> {
                val title = Component.empty().append(currentArticle!!.short).withoutShadow()
                val tx = if (icons.isNotEmpty()) titleX else contentX
                graphics.pose().pushMatrix()
                graphics.pose().scaleAround(2F, 2F, tx.toFloat(), titleY.toFloat())
                graphics.textRenderer(GuiGraphicsExtractor.HoveredTextEffects.NONE).acceptScrolling(title.copy().withColor(0xFF3E3E3E.toInt()), 0, tx + 1, tx + titleWidth + 1, titleY + 1, titleY + titleHeight + 1)
                graphics.textRenderer(GuiGraphicsExtractor.HoveredTextEffects.NONE).acceptScrolling(title, 0, tx, titleX + titleWidth, titleY, titleY + titleHeight)
                graphics.pose().popMatrix()

                val subtitle = getArticleSubtitle(currentArticle!!)
                graphics.text(this.font, subtitle, tx, subtitleY, 0xFF808080.toInt(), false)
            }
            ResearchScreenMode.HOME -> {
                graphics.text(this.font, Component.translatable("gui.${ToBaseMod.modid}.research.inventory"), inventoryLabelX, inventoryLabelY, 0xFF404040.toInt(), false)
            }
            ResearchScreenMode.LIST -> {
                graphics.text(this.font, listTitle!!, labelX, labelY, 0xFF404040.toInt(), false)
            }
        }
    }

    override fun extractSlot(graphics: GuiGraphicsExtractor, slot: Slot, mouseX: Int, mouseY: Int) {
        super.extractSlot(graphics, slot, mouseX, mouseY)
        if (getSlotArticle(slot) != null) {
            if (slot == hoveredSlot) {
                graphics.requestCursor(CursorTypes.POINTING_HAND)
            }
        } else {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, slotLocked, slot.x - 4, slot.y - 4, 24, 24)
        }
    }

    override fun getTitle() = originalTitle

    /**
     * Searches every article for the term currently typed and shows the matches as a list.
     * Only an article's title is searched, and matching is a case-insensitive substring rather than anything cleverer.
     *
     * @since 0.1.0
     */
    private fun startSearch() {
        currentList = WikiArticleManager.index.all.filter {
            StringDecomposer.getPlainText(it.title).lowercase().contains(searchText!!.value.lowercase())
        }
        listTitle = Component.translatable("gui.${ToBaseMod.modid}.research.list.search", searchText!!.value)
        changeMode(ResearchScreenMode.LIST)
    }

    /**
     * Where the scrollbar thumb sits, worked out from how far the content has been scrolled.
     *
     * @return The offset of the thumb from the top of the scrollbar, in pixels, or `0` where there is nothing to scroll.
     * @since 0.1.0
     */
    private fun getScrollPosition(): Int {
        val height = scrollHeight - contentHeight
        if (height <= 0) return 0
        return (scrollY.toDouble() / height).times(contentHeight - scrollbarThumbHeight).toInt()
    }

    /**
     * Scrolls the current page to the given offset, clamped to what there is left to scroll.
     * The widgets themselves are moved rather than the drawing being offset, as each mode scrolls a different set of them.
     *
     * @param pos The offset to scroll to, in pixels from the top of the content.
     * @since 0.1.0
     */
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
                if (event.y.toInt() - topPos in contentY + scrollPosition until contentY + scrollPosition + scrollbarThumbHeight) {
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

        /**
         * The address of the online wiki, which the browser button opens.
         *
         * @since 0.1.0
         */
        private const val URL = "https://to.jidb.net"

        /**
         * The sprite of the scrollbar thumb when there is something to scroll.
         *
         * @since 0.1.0
         */
        val scrollbarThumb = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "scroll/thumb")

        /**
         * The sprite of the scrollbar thumb when the content already fits.
         *
         * @since 0.1.0
         */
        val scrollbarThumbDisabled = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "scroll/thumb_disabled")

        /**
         * The sprite drawn over an inventory slot whose item has no article to open.
         *
         * @since 0.1.0
         */
        val slotLocked = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "research/slot")

        /**
         * The x position of the icon within a list button.
         *
         * @since 0.1.0
         */
        private const val listButtonIconX = 5

        /**
         * The y position of the icon within a list button.
         *
         * @since 0.1.0
         */
        private const val listButtonIconY = 5

        /**
         * How far a list button's label is indented to leave room for its icon.
         *
         * @since 0.1.0
         */
        private const val listButtonIconPadding = 24

        /**
         * The gap between one list button and the next.
         *
         * @since 0.1.0
         */
        private const val listButtonPadding = 2

        /**
         * The icons an article declares, keeping only those that name something with a model to draw.
         *
         * @param article The article to read the icons of.
         * @return The registry objects the article's icons point at.
         * @since 0.1.0
         */
        private fun getArticleIcons(article: WikiArticle): List<Any> = article.icon.mapNotNull {
            when (val resource = RegistryHelper.getResource(it)) {
                is Block, is Item, is EntityType<*> -> resource
                else -> null
            }
        }

        /**
         * Picks which of an article's icons to show, cycling through them every two seconds where there is more than one.
         *
         * @param icons The icons of the article.
         * @param iconTicks How long the article has been shown for.
         * @return The icon to draw, or `null` where the article has none.
         * @since 0.1.0
         */
        private fun getArticleIcon(icons: List<Any>, iconTicks: Float): Any? {
            if (icons.isEmpty()) return null
            return icons[iconTicks.toInt().div(40) % icons.size]
        }

        /**
         * Draws an article's icon at the given position and scale.
         * Only an icon that can be held as an item is drawn; an entity icon has no way to be drawn in a GUI yet.
         *
         * @param icon The icon to draw.
         * @param graphics The graphics to draw into.
         * @param x The x position to draw the icon at.
         * @param y The y position to draw the icon at.
         * @param scale The factor to draw the icon at, where `1` is an item's usual size. Defaults to `1`.
         * @param scissors The rectangle to clip the icon to, or `null` for no clipping. Defaults to `null`.
         * @since 0.6.0
         */
        private fun extractArticleIcon(icon: Any, graphics: GuiGraphicsExtractor, x: Int, y: Int, scale: Float = 1f, scissors: ScreenRectangle? = null) {
            when (icon) {
                is ItemLike -> {
                    ScaledTrackingItemStackRenderState.extractItem(graphics, ItemStack(icon), x, y, scale, scissors)
                }
            }
        }

        /**
         * The subtitle shown under an article's title.
         * An article that declares one uses it; otherwise the subtitle names what kind of thing the article is about, or the mod it belongs to where the article is a version's changelog.
         *
         * @param article The article to build a subtitle for.
         * @return The subtitle to display.
         * @since 0.1.0
         */
        private fun getArticleSubtitle(article: WikiArticle): Component {
            if (article.subtitle != null) {
                return article.subtitle
            }
            val first = article.about.first()
            if (article.about.size == 1 && first.first.toString() == "to_base:mod_version") {
                return WikiArticleManager.index.byResource[article.parent]!!.first().title
            }
            val token = with(article.about.map { it.first }.toSet()) {
                if (this.size > 1) "mixed" else first.first.toString()
            }
            return Component.translatable("gui.${ToBaseMod.modid}.research.type.$token")
        }

        /**
         * The article about the item in an inventory slot, which is what clicking that slot on the home page opens.
         *
         * @param slot The slot to read the item of.
         * @return The article about that item, or `null` where the slot is empty or its item has no article.
         * @since 0.1.0
         */
        private fun getSlotArticle(slot: Slot): WikiArticle? {
            val item = slot.item.item
            val key = BuiltInRegistries.ITEM.getResourceKey(item).getOrNull() ?: return null
            return WikiArticleManager.index.byResource[key.registry() to key.identifier()]?.firstOrNull()
        }

    }

    /**
     * Enum that defines the three pages the research screen can show, each carrying the background it is drawn on.
     *
     * @see ResearchScreen.mode
     * @since 0.1.0
     */
    enum class ResearchScreenMode {

        /**
         * The home page, which offers the search box, the category buttons and the player's own inventory.
         *
         * @since 0.1.0
         */
        HOME,

        /**
         * An article, showing its title, icon and body text.
         *
         * @since 0.1.0
         */
        PAGE,

        /**
         * A list of articles, whether a whole category or the results of a search.
         *
         * @since 0.1.0
         */
        LIST;

        /**
         * The background texture this page is drawn on, named after the page itself.
         *
         * @since 0.1.0
         */
        val texture = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "textures/gui/research/${name.lowercase()}.png")

    }

    /**
     * One row of the list page, a button labelled with an article's title and drawn with its icon.
     * The icon cycles like the one on an article page does, but only while the button is not hovered, so that the icon under the cursor holds still.
     *
     * @property article The article this button opens.
     * @param x The x position of the button.
     * @param y The y position of the button.
     * @param width The width of the button.
     * @param height The height of the button.
     * @param onPress The action run when the button is pressed.
     * @param scissors The rectangle the icon is clipped to, so that a button scrolled past the edge of the list does not draw its icon outside it.
     * @since 0.1.0
     */
    private class ListButton(val article: WikiArticle, x: Int, y: Int, width: Int, height: Int, onPress: OnPress, private val scissors: ScreenRectangle) : Button(x, y, width, height, article.title, onPress, DEFAULT_NARRATION) {

        /**
         * The icons of this button's article, which are cycled between where there is more than one.
         *
         * @since 0.1.0
         */
        val icons = getArticleIcons(article)

        /**
         * How long the current icon has been shown for, which is what advances the cycle through [icons].
         *
         * @since 0.1.0
         */
        var iconTicks = 0f

        override fun extractContents(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
            this.extractDefaultSprite(graphics)
            this.extractDefaultLabel(graphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE))

            val icon = getArticleIcon(icons, iconTicks)
            if (icon != null) {
                extractArticleIcon(icon, graphics, x + listButtonIconX, y + listButtonIconY, scissors = scissors.transformAxisAligned(graphics.pose()))
            }
            if (!isHovered) {
                iconTicks += partialTick
            }
        }

        override fun extractScrollingStringOverContents(output: ActiveTextCollector, message: Component, margin: Int) {
            val i = this.getX() + margin + (if (icons.isNotEmpty()) listButtonIconPadding else listButtonPadding)
            val j = this.getX() + this.getWidth() - margin
            val k = this.getY()
            val l = this.getY() + this.getHeight()
            output.acceptScrolling(message, 0, i, j, k, l)
        }

    }

}
