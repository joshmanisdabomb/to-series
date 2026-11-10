package net.jidb.to.stars.client.gui.components

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.helper.KotlinHelper.either
import net.jidb.to.base.api.info.TooltipEngine
import net.jidb.to.base.client.pub.gui.components.AbstractBarWidget
import net.jidb.to.base.client.pub.gui.components.energy.AbstractEnergyBarWidget
import net.jidb.to.base.client.pub.gui.components.energy.MiniEnergyBarWidget
import net.jidb.to.base.pub.info.ToBaseTooltipEngine
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.client.gui.screens.IProcessorScreen
import net.jidb.to.stars.info.ToStarsTooltipEngine
import net.jidb.to.stars.inventory.menu.AbstractProcessorMenu
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.util.Mth
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import kotlin.jvm.optionals.getOrNull

open class AbstractProcessorComponents(protected val screen: IProcessorScreen) {

    /**
     * The recipe book widget for the interface, or `null` before the screen has been laid out.
     */
    var recipeBook: RecipeBookComponent<*>? = null
        protected set

    /**
     * The bar showing how much energy the machine has by it, or `null` before the screen has been laid out.
     */
    var energy: AbstractEnergyBarWidget? = null
        protected set

    protected var energyProvider: AbstractProcessorComponents.() -> AbstractEnergyBarWidget? = { null }

    /**
     * The bar showing how efficient the machine has become, or `null` before the screen has been laid out.
     */
    var efficiency: EfficiencyBarWidget? = null
        protected set

    protected var efficiencyProvider: AbstractProcessorComponents.() -> EfficiencyBarWidget? = { null }

    /**
     * The bar showing how far through its recipe the machine is, or `null` before the screen has been laid out.
     */
    var progress: AbstractBarWidget? = null
        protected set

    protected var progressProvider: AbstractProcessorComponents.() -> AbstractBarWidget? = { null }

    open fun init() {
        recipeBook = screen.processorScreen.children().firstNotNullOf { it as? RecipeBookComponent<*> }

        energy = energyProvider()
        efficiency = efficiencyProvider()
        progress = progressProvider()

        val listener = screen.energyListener
        if (listener != null) {
            screen.processorMenu.removeSlotListener(listener)
            screen.processorMenu.addSlotListener(listener)
        }
    }

    open fun onRecipeBookButtonClick() {
        val offset = RecipeBookComponent.IMAGE_WIDTH.div(2).plus(4) * (recipeBook?.isVisible ?: false).either(1, -1)
        energy?.x += offset
        efficiency?.x += offset
        progress?.x += offset
    }

    open fun setEnergyBar(provider: AbstractProcessorComponents.() -> AbstractEnergyBarWidget?): AbstractProcessorComponents {
        energyProvider = provider
        return this
    }

    open fun setEfficiencyBar(provider: AbstractProcessorComponents.() -> EfficiencyBarWidget?): AbstractProcessorComponents {
        efficiencyProvider = provider
        return this
    }

    open fun setProgressBar(provider: AbstractProcessorComponents.() -> AbstractBarWidget?): AbstractProcessorComponents {
        progressProvider = provider
        return this
    }

    fun setDefaults(leftPos: Int? = null, topPos: Int? = null, imageHeight: Int? = null) = this
        .setEnergyBar { defaultEnergyBar(screen.processorMenu, leftPos ?: screen.processorLeftPos, topPos ?: screen.processorTopPos).also { screen.processorAddWidget(it) } }
        .setEfficiencyBar { defaultEfficiencyBar(screen.processorMenu, leftPos ?: screen.processorLeftPos, topPos ?: screen.processorTopPos, imageHeight ?: screen.processorImageHeight).also { screen.processorAddWidget(it) } }

    fun containerTick() {
        screen.energyListener?.tick(screen.processorMenu)
    }

    /**
     * Draws the small icons beside the bars, i.e. whether the machine can afford its recipe, whether it is building efficiency and what it last made.
     *
     * @param graphics What the icons are drawn into.
     */
    open fun extractIcons(graphics: GuiGraphicsExtractor) {
        extractEnergyIcon(graphics)
        extractEfficiencyIcon(graphics)
        extractLastRecipeIcon(graphics)
    }

    /**
     * Draws the icon beside the energy bar, which is only shown where the machine has enough by it to carry on.
     *
     * @param graphics What the icon is drawn into.
     * @param x The x position of the icon, or `null` to place it beside the bar. Defaults to `null`.
     * @param y The y position of the icon, or `null` to place it beside the bar. Defaults to `null`.
     */
    open fun extractEnergyIcon(graphics: GuiGraphicsExtractor, x: Int? = null, y: Int? = null) {
        val energy = energy?.energy()
        val recipeEnergy = AbstractProcessorMenu.dataSchema.getIntValue(screen.processorMenu.data, AbstractProcessorMenu.AbstractProcessorDataKey.RECIPE_ENERGY) ?: 0
        val recipeTime = AbstractProcessorMenu.dataSchema.getIntValue(screen.processorMenu.data, AbstractProcessorMenu.AbstractProcessorDataKey.RECIPE_TIME) ?: 0
        val speed = screen.processorMenu.getClientData()?.machineUsage ?: 0f
        val usage = screen.processorMenu.getClientData()?.machineSpeed ?: 0f
        val total = Mth.ceil(recipeEnergy * usage)
        val cost = Mth.ceil(total * speed / recipeTime)
        if ((energy ?: 0L) > cost) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, energyIndicator, x ?: this.energy?.x?.minus(12) ?: 0, y ?: this.energy?.y?.minus(3) ?: 0, 9, 10)
        }
    }

    /**
     * Draws the icon beside the efficiency bar, which is only shown where the machine has actually built some up on the recipe it is still running.
     *
     * @param graphics What the icon is drawn into.
     * @param x The x position of the icon, or `null` to place it beside the bar. Defaults to `null`.
     * @param y The y position of the icon, or `null` to place it beside the bar. Defaults to `null`.
     */
    open fun extractEfficiencyIcon(graphics: GuiGraphicsExtractor, x: Int? = null, y: Int? = null) {
        val completions = AbstractProcessorMenu.dataSchema.getIntValue(screen.processorMenu.data, AbstractProcessorMenu.AbstractProcessorDataKey.COMPLETIONS) ?: 0
        val sameRecipe = AbstractProcessorMenu.dataSchema.getBoolValue(screen.processorMenu.data, AbstractProcessorMenu.AbstractProcessorDataKey.RECIPE_SAME) ?: false
        if (completions > 0 && sameRecipe) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, efficiencyIndicator, x ?: this.efficiency?.x?.minus(14) ?: 0, y ?: this.efficiency?.y?.minus(3) ?: 0, 11, 10)
        }
    }

    /**
     * Draws the item the machine last made, which is what its efficiency was built up on.
     *
     * @param graphics What the icon is drawn into.
     * @param x The x position of the icon, or `null` to place it beside the efficiency bar. Defaults to `null`.
     * @param y The y position of the icon, or `null` to place it beside the efficiency bar. Defaults to `null`.
     */
    open fun extractLastRecipeIcon(graphics: GuiGraphicsExtractor, x: Int? = null, y: Int? = null) {
        screen.processorMenu.getClientData()?.lastRecipeIcon?.ifPresent {
            val item = Minecraft.getInstance().level?.holderLookup(Registries.ITEM)?.get(it)?.getOrNull() ?: return@ifPresent
            val stack = ItemStack(item)
            graphics.fakeItem(stack, x ?: this.efficiency?.x?.minus(32) ?: 0, y ?: this.efficiency?.y?.minus(6) ?: 0)
        }
    }

    open fun extractTooltip(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int) {
        if (extractEnergyTooltip(graphics, mouseX, mouseY)) return
        if (extractProgressTooltip(graphics, mouseX, mouseY)) return
        if (extractEfficiencyTooltip(graphics, mouseX, mouseY)) return
    }

    /**
     * Shows the tooltip describing the machine's energy, where the mouse is over it.
     *
     * @param graphics What the tooltip is drawn into.
     * @param mouseX Where the mouse is, along x.
     * @param mouseY Where the mouse is, along y.
     * @param areaX The area the tooltip shows over, along x, or `null` to work it out from the widget itself. Defaults to `null`.
     * @param areaY The area it shows over, along y, or `null` to work it out from the widget itself. Defaults to `null`.
     * @return Returns `true` if a tooltip was shown, otherwise `false`.
     */
    open fun extractEnergyTooltip(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, areaX: IntRange? = null, areaY: IntRange? = null): Boolean {
        val energy = energy ?: return false
        if (mouseX in (areaX ?: energy.x.minus(12)..energy.right) && mouseY in (areaY ?: (energy.y.minus(3) until energy.y.plus(7)))) {
            Companion.extractEnergyTooltip(screen.processorScreen, screen.processorMenu, graphics, mouseX, mouseY)
            return true
        }
        return false
    }

    /**
     * Shows the tooltip describing how efficient the machine has become, where the mouse is over it.
     *
     * @param graphics What the tooltip is drawn into.
     * @param mouseX Where the mouse is, along x.
     * @param mouseY Where the mouse is, along y.
     * @param areaX The area the tooltip shows over, along x, or `null` to work it out from the widget itself. Defaults to `null`.
     * @param areaY The area it shows over, along y, or `null` to work it out from the widget itself. Defaults to `null`.
     * @return Returns `true` if a tooltip was shown, otherwise `false`.
     */
    open fun extractEfficiencyTooltip(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, areaX: IntRange? = null, areaY: IntRange? = null): Boolean {
        val efficiency = efficiency ?: return false
        if (mouseX in (areaX ?: efficiency.x.minus(32)..efficiency.right) && mouseY in (areaY ?: (efficiency.y.minus(7) until efficiency.y.plus(9)))) {
            Companion.extractEfficiencyTooltip(screen.processorScreen, screen.processorMenu, graphics, mouseX, mouseY, screen::getRecipeTranslation)
            return true
        }
        return false
    }

    /**
     * Shows the tooltip describing where the machine is with its recipe, where the mouse is over it.
     *
     * @param graphics What the tooltip is drawn into.
     * @param mouseX Where the mouse is, along x.
     * @param mouseY Where the mouse is, along y.
     * @param areaX The area the tooltip shows over, along x, or `null` to work it out from the widget itself. Defaults to `null`.
     * @param areaY The area it shows over, along y, or `null` to work it out from the widget itself. Defaults to `null`.
     * @return Returns `true` if a tooltip was shown, otherwise `false`.
     */
    open fun extractProgressTooltip(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, areaX: IntRange? = null, areaY: IntRange? = null): Boolean {
        val progress = progress ?: return false
        if (mouseX in (areaX ?: progress.x..progress.right) && mouseY in (areaY ?: progress.y..progress.bottom)) {
            Companion.extractProgressTooltip(screen.processorScreen, screen.processorMenu, graphics, mouseX, mouseY)
            return true
        }
        return false
    }

    companion object {

        /**
         * The icon shown beside the energy bar where the machine can afford to carry on.
         */
        val energyIndicator = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "energy/indicator_full")

        /**
         * The icon shown beside the efficiency bar where the machine is building efficiency.
         */
        val efficiencyIndicator = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "efficiency")

        fun defaultEnergyBar(menu: AbstractProcessorMenu, leftPos: Int, topPos: Int): AbstractEnergyBarWidget = MiniEnergyBarWidget(
            { AbstractProcessorMenu.dataSchema.getLongValue(menu.data, AbstractProcessorMenu.AbstractProcessorDataKey.ENERGY_TOTAL) ?: 0L },
            { AbstractProcessorMenu.dataSchema.getLongValue(menu.data, AbstractProcessorMenu.AbstractProcessorDataKey.ENERGY_CAPACITY) ?: 0L },
            leftPos + 41,
            topPos + 24,
        )

        fun defaultEfficiencyBar(menu: AbstractProcessorMenu, leftPos: Int, topPos: Int, imageHeight: Int): EfficiencyBarWidget = EfficiencyBarWidget(
            { (AbstractProcessorMenu.dataSchema.getShortValue(menu.data, AbstractProcessorMenu.AbstractProcessorDataKey.COMPLETIONS) ?: 0) / (menu.getClientData()?.machineBonusMax?.toFloat() ?: 1f) },
            leftPos + 135,
            topPos + imageHeight - 104,
        )

        fun extractEnergyTooltip(screen: AbstractContainerScreen<out AbstractProcessorMenu>, menu: AbstractProcessorMenu, graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int) {
            val tooltip = TooltipEngine.withLineBreaks(ToBaseTooltipEngine.getEnergyInfo(
                AbstractProcessorMenu.dataSchema.getLongValue(menu.data, AbstractProcessorMenu.AbstractProcessorDataKey.ENERGY_TOTAL) ?: 0L,
                AbstractProcessorMenu.dataSchema.getLongValue(menu.data, AbstractProcessorMenu.AbstractProcessorDataKey.ENERGY_CAPACITY) ?: 0L,
                AbstractProcessorMenu.dataSchema.getLongValue(menu.data, AbstractProcessorMenu.AbstractProcessorDataKey.ENERGY_MAX_INPUT) ?: 0L,
                AbstractProcessorMenu.dataSchema.getLongValue(menu.data, AbstractProcessorMenu.AbstractProcessorDataKey.ENERGY_MAX_OUTPUT) ?: 0L,
                AbstractProcessorMenu.dataSchema.getLongValue(menu.data, AbstractProcessorMenu.AbstractProcessorDataKey.ENERGY_INSERT_CHANGE),
                AbstractProcessorMenu.dataSchema.getLongValue(menu.data, AbstractProcessorMenu.AbstractProcessorDataKey.ENERGY_INSERT_AVERAGE),
                AbstractProcessorMenu.dataSchema.getLongValue(menu.data, AbstractProcessorMenu.AbstractProcessorDataKey.ENERGY_EXTRACT_CHANGE),
                AbstractProcessorMenu.dataSchema.getLongValue(menu.data, AbstractProcessorMenu.AbstractProcessorDataKey.ENERGY_EXTRACT_AVERAGE),
                advanced = Minecraft.getInstance().hasShiftDown()
            ))
            graphics.setTooltipForNextFrame(screen.font, screen.font.split(tooltip, 200), mouseX, mouseY)
        }

        fun extractEfficiencyTooltip(screen: AbstractContainerScreen<out AbstractProcessorMenu>, menu: AbstractProcessorMenu, graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, recipeName: (recipe: ResourceKey<Recipe<*>>, icon: ResourceKey<Item>?) -> Component) {
            val tooltip = mutableListOf<Component>()
            val recipe = menu.getClientData()?.lastRecipe?.getOrNull()?.let { recipeName(it, menu.getClientData()?.lastRecipeIcon?.getOrNull()) }
            tooltip.addAll(ToStarsTooltipEngine.getEfficiencyInfo(
                recipe,
                recipe != null && !(AbstractProcessorMenu.dataSchema.getBoolValue(menu.data, AbstractProcessorMenu.AbstractProcessorDataKey.RECIPE_SAME) ?: false) && (AbstractProcessorMenu.dataSchema.getIntValue(menu.data, AbstractProcessorMenu.AbstractProcessorDataKey.RECIPE_TIME) ?: 0) > 0,
                AbstractProcessorMenu.dataSchema.getIntValue(menu.data, AbstractProcessorMenu.AbstractProcessorDataKey.COMPLETIONS) ?: 0,
                menu.getClientData()?.machineBonus ?: 0f,
                menu.getClientData()?.machineBonusMax ?: 0
            ))
            graphics.setTooltipForNextFrame(screen.font, screen.font.split(TooltipEngine.withLineBreaks(tooltip), 200), mouseX, mouseY)
        }

        fun extractProgressTooltip(screen: AbstractContainerScreen<out AbstractProcessorMenu>, menu: AbstractProcessorMenu, graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int) {
            val tooltip = TooltipEngine.withLineBreaks(ToStarsTooltipEngine.getProcessorProgressInfo(
                AbstractProcessorMenu.dataSchema.getShortValue(menu.data, AbstractProcessorMenu.AbstractProcessorDataKey.PROGRESS) ?: 0,
                AbstractProcessorMenu.dataSchema.getShortValue(menu.data, AbstractProcessorMenu.AbstractProcessorDataKey.MAX_PROGRESS) ?: 0,
                AbstractProcessorMenu.dataSchema.getIntValue(menu.data, AbstractProcessorMenu.AbstractProcessorDataKey.RECIPE_TIME) ?: 0,
                AbstractProcessorMenu.dataSchema.getLongValue(menu.data, AbstractProcessorMenu.AbstractProcessorDataKey.RECIPE_ENERGY) ?: 0,
                menu.getClientData()?.machineUsage ?: 0f,
                menu.getClientData()?.machineSpeed ?: 0f,
                advanced = Minecraft.getInstance().hasShiftDown()
            ))
            graphics.setTooltipForNextFrame(screen.font, screen.font.split(tooltip, 200), mouseX, mouseY)
        }

    }

}
