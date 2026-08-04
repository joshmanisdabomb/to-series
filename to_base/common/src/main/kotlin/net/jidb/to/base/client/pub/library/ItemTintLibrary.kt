package net.jidb.to.base.client.pub.library

import com.mojang.serialization.MapCodec
import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.client.service.ClientServices
import net.minecraft.client.color.item.ItemTintSource
import net.minecraft.resources.Identifier

/**
 * [SimpleLibrary] implementation that registers a type of [ItemTintSource], and provides access to those types in one place.
 * Unlike a block tint, which is registered against the blocks it colours, an item tint is registered as a type that item model JSON then refers to by identifier, so what a library entry holds is the codec that reads one.
 *
 * @param modid The mod ID associated with the library.
 * @since 0.7.0
 */
open class ItemTintLibrary(modid: String) : SimpleLibrary<MapCodec<out ItemTintSource>>(modid) {

    /**
     * Declares a type of [ItemTintSource], registering it with the client platform when the library is built.
     *
     * @param S The type of the tint source being declared.
     * @param source A [MapCodec] that reads the tint source from its JSON definition.
     * @param id The identifier to register the tint source type under, or `null` to use the identifier of the entry. Defaults to `null`.
     * @return The [Library.LibraryEntry] for the declared property.
     * @since 0.7.0
     */
    operator fun <S : ItemTintSource> invoke(source: MapCodec<S>, id: Identifier? = null) = invoke({
        val source = it()
        ClientServices.platform.models.registerItemTint(id ?: this.id, source);
        { source }
    }, { source })

}
