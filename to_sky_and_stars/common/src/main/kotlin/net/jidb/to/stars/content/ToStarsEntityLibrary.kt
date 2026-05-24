package net.jidb.to.stars.content

import net.jidb.to.base.library.SimpleRegistryLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.entity.AtomicBombEntity
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory

object ToStarsEntityLibrary : SimpleRegistryLibrary<EntityType<*>>(ToStarsMod.modid) {

    override val registry = BuiltInRegistries.ENTITY_TYPE

    val atomic_bomb by this { EntityType.Builder.of(::AtomicBombEntity, MobCategory.MISC)
        .noLootTable()
        .fireImmune()
        .sized(0.98f, 0.98f)
        .eyeHeight(0.15f)
        .clientTrackingRange(100)
        .updateInterval(10)
        .build(getEntryResourceKey(it)) }

}
