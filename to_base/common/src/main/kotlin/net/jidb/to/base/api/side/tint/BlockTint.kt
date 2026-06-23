package net.jidb.to.base.api.side.tint

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockAndLightGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Property

interface BlockTint {
    fun color(state: BlockState?): Int

    fun color(state: BlockState?, level: BlockAndLightGetter?, pos: BlockPos?) = color(state)

    fun colorTerrainParticles(state: BlockState?, level: BlockAndLightGetter?, pos: BlockPos?) = color(state)

    fun relevantProperties() = mutableSetOf<Property<*>>()
}