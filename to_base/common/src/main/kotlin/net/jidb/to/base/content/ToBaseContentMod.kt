package net.jidb.to.base.content

import net.jidb.to.base.pub.mod.ToMod
import org.slf4j.Logger

/**
 * The mod object holding the game content of To Lay the Foundations, such as the research desk.
 * It is a [ToMod] in its own right rather than part of [net.jidb.to.base.ToBaseMod], so that the library the mod offers to other mods stays separate from the content it happens to add itself.
 *
 * @property modid The mod ID, which is the same one the library registers under.
 * @property logger The logger, shared with the mod object that owns this one.
 * @since 0.5.0
 */
class ToBaseContentMod(override val modid: String, override val logger: Logger) : ToMod() {

    override val blocks = ToBaseBlockLibrary
    override val items = ToBaseItemLibrary
    override val blockItems = ToBaseBlockItemLibrary
    override val tabs = ToBaseCreativeTabLibrary

    override val menus = ToBaseMenuLibrary

    override val itemTags = ToBaseItemTagLibrary

    override val gameTests = ToBaseGameTestLibrary

}
