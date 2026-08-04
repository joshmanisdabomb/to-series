package net.jidb.to.base.client.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.content.gui.screens.ResearchScreen
import net.jidb.to.base.client.pub.library.ScreenLibrary
import net.jidb.to.base.content.ToBaseMenuLibrary

/**
 * [ScreenLibrary] implementation that binds the screens of To Lay the Foundations to the menus that open them, and provides access to them in one place.
 *
 * @since 0.1.0
 */
object ToBaseScreenLibrary : ScreenLibrary(ToBaseMod.modid) {

    /**
     * The screen the research desk opens, through which the in-game wiki is read.
     *
     * @see ResearchScreen
     * @since 0.1.0
     */
    val research_desk by this { ScreenEntry({ ToBaseMenuLibrary.research }, ::ResearchScreen) }

}
