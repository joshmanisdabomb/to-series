package net.jidb.to.base.content

import net.jidb.to.base.pub.mod.ToMod
import org.slf4j.Logger

class ToBaseContentMod(override val modid: String, override val logger: Logger) : ToMod() {

    override val blocks = ToBaseBlockLibrary
    override val items = ToBaseItemLibrary
    override val blockItems = ToBaseBlockItemLibrary
    override val tabs = ToBaseCreativeTabLibrary

    override val menus = ToBaseMenuLibrary

    override val itemTags = ToBaseItemTagLibrary

}
