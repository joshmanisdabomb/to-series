package net.jidb.to.base.api.library

import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

/**
 * [Library] implementation that includes helper [LibraryEntry] providers and getters to simplify the definition of [ResourceKey] entries.
 * @param T The type of resource keys to be defined in this library.
 * @param modid The mod ID for this library.
 * @since 0.3.0
 */
abstract class ResourceKeyLibrary<T : Any>(modid: String) : AdvancedLibrary<Identifier, ResourceKey<T>>(modid), IResourceKeyLibrary<Identifier, ResourceKey<T>, T>, AdvancedLibraryBuilder<Identifier, ResourceKey<T>> {

    override fun <J : Identifier> i(entry: Library<Identifier, ResourceKey<T>>.LibraryEntry<out Identifier, out ResourceKey<T>>, input: () -> J): () -> ResourceKey<T> = { ResourceKey.create(registryKey, input()) }

    /**
     * Helper [LibraryEntry] delegate provider that defines a [ResourceKey] set to the [registryKey] with the [Identifier] as the property name and mod ID.
     * ```kotlin
     * class ExampleLibrary : ResourceKeyLibrary("modid") {
     *     val registryKey = Registries.DAMAGE_TYPE
     *     val custom by this() // ResourceKey[minecraft:damage_type / modid:custom]
     * }
     * ```
     *
     * @return A [LibraryEntry] for the [ResourceKey] built from the mod ID and property name.
     * @since 0.3.0
     */
    operator fun invoke(): Library<Identifier, ResourceKey<T>>.LibraryEntry<out Identifier, out ResourceKey<T>> = invoke(::i) { it.id }

    /**
     * Helper [LibraryEntry] delegate provider that defines a [ResourceKey] set to the [registryKey] with the given [Identifier].
     * ```kotlin
     * class ExampleLibrary : ResourceKeyLibrary("modid") {
     *     val registryKey = Registries.DAMAGE_TYPE
     *     val custom by this(Identifier.from("foo", "bar")) // ResourceKey[minecraft:damage_type / foo:bar]
     * }
     * ```
     *
     * @param id The [Identifier] that the [ResourceKey] will use, independent of the ID of the [LibraryEntry].
     * @return A [LibraryEntry] for the [ResourceKey] built from the given [Identifier].
     * @since 0.3.0
     */
    operator fun invoke(id: Identifier): Library<Identifier, ResourceKey<T>>.LibraryEntry<out Identifier, out ResourceKey<T>> = invoke(::i) { id }

    override fun getEntryIdentifier(entry: Library<Identifier, ResourceKey<T>>.LibraryEntry<out Identifier, out ResourceKey<T>>) = entry.value.identifier()
    override fun getEntryResourceKey(entry: Library<Identifier, ResourceKey<T>>.LibraryEntry<out Identifier, out ResourceKey<T>>) = entry.value

}
