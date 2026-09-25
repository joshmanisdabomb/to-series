package net.jidb.to.stars.client.gui.screens

import net.jidb.to.base.client.pub.gui.components.AbstractBarWidget
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.inventory.menu.CentrifugeMenu
import net.jidb.to.stars.inventory.menu.ProcessorMenu
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.Items

/**
 * The screen a centrifuge is opened into, whose progress is drawn as one arrow down into the drum and then two out of it, one per output slot.
 *
 * @param M The type of the interface being drawn.
 * @param menu The interface being drawn.
 * @param playerInventory The inventory of the player who opened it.
 * @param title The title of the interface.
 */
class CentrifugeScreen<M : CentrifugeMenu>(menu: M, playerInventory: Inventory, title: Component) : ProcessorScreen<M>(menu, Component.translatable("gui.${ToStarsMod.modid}.centrifuge.recipebook.filter"), listOf(RecipeBookComponent.TabInfo(Items.COMPASS, ToStarsMod.recipeCategories.processor_misc), RecipeBookComponent.TabInfo(Items.IRON_INGOT, ToStarsMod.recipeCategories.processor_misc)), playerInventory, title, 209) {

    /**
     * The arrow running out to the left output, or `null` before the screen has been laid out.
     */
    private var progressl: AbstractBarWidget? = null

    /**
     * The arrow running out to the right output, or `null` before the screen has been laid out.
     */
    private var progressr: AbstractBarWidget? = null

    override val texture = Companion.texture

    override fun init() {
        super.init()

        progress = this.addRenderableWidget(
            object : AbstractBarWidget(leftPos + 85, topPos + 71, 5, 13) {

                override val animation = AnimationDirection.TOP_BOTTOM

                override fun getFill(): Float {
                    val value = (ProcessorMenu.dataSchema.getShortValue(menu.data, ProcessorMenu.ProcessorDataKey.PROGRESS) ?: 0) / (ProcessorMenu.dataSchema.getShortValue(menu.data, ProcessorMenu.ProcessorDataKey.MAX_PROGRESS) ?: 0).toFloat()
                    return value.times(1.85f).coerceAtMost(1f)
                }
                override fun getFrontTexture() = Companion.progress
                override fun getBackTexture() = null

            }
        )
        progressl = this.addRenderableWidget(
            object : AbstractBarWidget(leftPos + 70, topPos + 76, 16, 15) {

                override val animation = AnimationDirection.RIGHT_LEFT

                override fun getFill(): Float {
                    val value = (ProcessorMenu.dataSchema.getShortValue(menu.data, ProcessorMenu.ProcessorDataKey.PROGRESS) ?: 0) / (ProcessorMenu.dataSchema.getShortValue(menu.data, ProcessorMenu.ProcessorDataKey.MAX_PROGRESS) ?: 0).toFloat()
                    return value.times(1.85f).minus(0.85f).coerceAtLeast(0f)
                }
                override fun getFrontTexture() = Companion.progressl
                override fun getBackTexture() = null

            }
        )
        progressr = this.addRenderableWidget(
            object : AbstractBarWidget(leftPos + 89, topPos + 76, 16, 15) {

                override val animation = AnimationDirection.LEFT_RIGHT

                override fun getFill(): Float {
                    val value = (ProcessorMenu.dataSchema.getShortValue(menu.data, ProcessorMenu.ProcessorDataKey.PROGRESS) ?: 0) / (ProcessorMenu.dataSchema.getShortValue(menu.data, ProcessorMenu.ProcessorDataKey.MAX_PROGRESS) ?: 0).toFloat()
                    return value.times(1.85f).minus(0.85f).coerceAtLeast(0f)
                }
                override fun getFrontTexture() = Companion.progressr
                override fun getBackTexture() = null

            }
        )
    }

    override fun onRecipeBookButtonClick() {
        super.onRecipeBookButtonClick()
        progressl?.x = leftPos + 70
        progressr?.x = leftPos + 89
    }

    override fun extractProgressTooltip(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, areaX: IntRange?, areaY: IntRange?): Boolean {
        val progress = progress ?: return false
        val progressl = progressl ?: return false
        val progressr = progressr ?: return false

        val areaX = progressl.x..progressr.right
        val areaY = progress.y..progressr.bottom
        if (mouseX in areaX && mouseY in areaY) {
            return super.extractProgressTooltip(graphics, mouseX, mouseY, areaX, areaY)
        }
        return false
    }

    companion object {

        /**
         * The background of the screen.
         */
        val texture = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "textures/gui/centrifuge.png")

        /**
         * The texture of the arrow running down into the drum.
         */
        val progress = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "centrifuge/progress")

        /**
         * The texture of the arrow running out to the left output.
         */
        val progressl = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "centrifuge/progress_l")

        /**
         * The texture of the arrow running out to the right output.
         */
        val progressr = Identifier.fromNamespaceAndPath(ToStarsMod.modid, "centrifuge/progress_r")

    }

}
