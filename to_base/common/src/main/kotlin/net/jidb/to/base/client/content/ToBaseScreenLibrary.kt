package net.jidb.to.base.client.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.content.gui.screens.ResearchScreen
import net.jidb.to.base.client.pub.library.ScreenLibrary
import net.jidb.to.base.content.ToBaseMenuLibrary

object ToBaseScreenLibrary : ScreenLibrary(ToBaseMod.modid) {

    val research_desk by this { ScreenEntry({ ToBaseMenuLibrary.research }, ::ResearchScreen) }

}
