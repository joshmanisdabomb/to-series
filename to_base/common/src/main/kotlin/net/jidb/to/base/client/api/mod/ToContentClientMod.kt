package net.jidb.to.base.client.api.mod

import net.jidb.to.base.api.event.Event
import net.jidb.to.base.api.event.EventHandler
import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.client.api.platform.ReloadListenerClientPlatformModule
import net.jidb.to.base.client.pub.library.BlockEntityRendererLibrary
import net.jidb.to.base.client.pub.library.BlockTintLibrary
import net.jidb.to.base.client.pub.library.ClientPayloadHandlerLibrary
import net.jidb.to.base.client.pub.library.EntityRendererLibrary
import net.jidb.to.base.client.pub.library.ItemTintLibrary
import net.jidb.to.base.client.pub.library.ModelLayerLibrary
import net.jidb.to.base.client.pub.library.ParticleLibrary
import net.jidb.to.base.client.pub.library.ScreenLibrary
import net.jidb.to.base.client.pub.library.SpecialModelLibrary
import net.jidb.to.base.pub.library.ReloadListenerLibrary
import net.minecraft.world.level.block.Block

/**
 * Interface containing client content libraries usable by an implementing [net.jidb.to.base.client.pub.mod.ToClientMod].
 * All the values in each [Library] here have first-class support to be registered cross-platform by To Lay the Foundations client-sided code.
 *
 * Each property is `null` by default, and mods can override these values with their own libraries to automatically register their content.
 *
 * @since 0.2.0
 */
interface ToContentClientMod {

    /**
     * A [Library] for the mod's [net.minecraft.client.renderer.blockentity.BlockEntityRenderer] objects.
     *
     * Content here in a [net.jidb.to.base.client.pub.mod.ToClientMod] object is automatically registered to each modloader.
     *
     * @since 0.6.0
     */
    val blockEntityRenderers: BlockEntityRendererLibrary? get() = null

    /**
     * A [Library] for the mod's [net.minecraft.client.renderer.entity.EntityRenderer] objects.
     *
     * Content here in a [net.jidb.to.base.client.pub.mod.ToClientMod] object is automatically registered to each modloader.
     *
     * @since 0.4.0
     */
    val entityRenderers: EntityRendererLibrary? get() = null

    /**
     * A [Library] for the mod's client-side payload handlers.
     *
     * Content here in a [net.jidb.to.base.client.pub.mod.ToClientMod] object is automatically registered to each modloader.
     *
     * @since 0.2.0
     */
    val payloadHandlers: ClientPayloadHandlerLibrary? get() = null

    /**
     * A [Library] for the mod's [net.minecraft.client.model.geom.ModelLayerLocation] objects.
     *
     * Content here in a [net.jidb.to.base.client.pub.mod.ToClientMod] object is automatically registered to each modloader.
     *
     * @since 0.6.0
     */
    val modelLayers: ModelLayerLibrary? get() = null

    /**
     * A [Library] for the mod's [net.minecraft.client.renderer.special.SpecialModelRenderer] objects for custom [net.minecraft.world.item.Item] renderers.
     *
     * Content here in a [net.jidb.to.base.client.pub.mod.ToClientMod] object is automatically registered to each modloader.
     *
     * @since 0.6.0
     */
    val specialModels: SpecialModelLibrary? get() = null

    /**
     * A [Library] for the mod's [net.minecraft.client.color.block.BlockTintSource] objects for custom [Block] tints.
     *
     * Content here in a [net.jidb.to.base.client.pub.mod.ToClientMod] object is automatically registered to each modloader.
     *
     * @since 0.7.0
     */
    val blockTints: BlockTintLibrary? get() = null

    /**
     * A [Library] for the mod's [net.minecraft.client.color.item.ItemTintSource] objects for custom [net.minecraft.world.item.Item] tints.
     *
     * Content here in a [net.jidb.to.base.client.pub.mod.ToClientMod] object is automatically registered to each modloader.
     *
     * @since 0.7.0
     */
    val itemTints: ItemTintLibrary? get() = null

    /**
     * A [Library] for the mod's [net.minecraft.client.particle.Particle] objects.
     *
     * Content here in a [net.jidb.to.base.client.pub.mod.ToClientMod] object is automatically registered to each modloader.
     *
     * @since 0.2.0
     */
    val particles: ParticleLibrary? get() = null

    /**
     * A [Library] for the mod's [net.minecraft.client.gui.screens.Screen] objects that need to be linked to a [net.minecraft.world.inventory.MenuType].
     *
     * Content here in a [net.jidb.to.base.client.pub.mod.ToClientMod] object is automatically registered to each modloader.
     *
     * @since 0.2.0
     */
    val screens: ScreenLibrary? get() = null

    /**
     * A [Library] for the mod's cross-platform client-side [Event] objects.
     *
     * @since 0.5.0
     */
    val events: SimpleLibrary<Event<*, *>>? get() = null

    /**
     * A [Library] for the mod's cross-platform client-side [net.jidb.to.base.api.event.EventHandler] objects.
     *
     * Content here in a [net.jidb.to.base.client.pub.mod.ToClientMod] object is automatically registered as a callback to each [Event].
     *
     * @since 0.5.0
     */
    val eventHandlers: SimpleLibrary<EventHandler<*, *>>? get() = null

    /**
     * A [Library] for the mod's client-side [ReloadListenerLibrary] objects.
     *
     * Content here in a [net.jidb.to.base.client.pub.mod.ToClientMod] object is automatically registered to each modloader.
     *
     * @since 0.2.0
     */
    val reloadListeners: ReloadListenerLibrary<ReloadListenerClientPlatformModule>? get() = null

}
