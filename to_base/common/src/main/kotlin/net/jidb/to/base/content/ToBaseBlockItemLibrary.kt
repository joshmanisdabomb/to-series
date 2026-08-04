package net.jidb.to.base.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.pub.library.BlockItemLibrary

/**
 * [BlockItemLibrary] implementation that registers a [net.minecraft.world.item.BlockItem] for each block of To Lay the Foundations.
 * Nothing is declared here, as every block of the mod is happy with the default item the library builds for it.
 *
 * @since 0.0.3
 */
object ToBaseBlockItemLibrary : BlockItemLibrary(ToBaseMod.modid, ToBaseMod.content.blocks)
