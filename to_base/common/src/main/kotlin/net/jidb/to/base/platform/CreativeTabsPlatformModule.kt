package net.jidb.to.base.platform

import net.minecraft.world.item.CreativeModeTab

abstract class CreativeTabsPlatformModule {

    abstract fun builder(): CreativeModeTab.Builder

}