package net.jidb.to.base.api.helper

import net.jidb.to.base.api.library.IResourceKeyLibrary
import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.LibraryTagList
import net.jidb.to.base.api.library.TranslatableLibrary

/**
 * Utility object providing helper extension functions for interacting with [Library] objects and their entries.
 * These functions are kept separate from the [Library] because of type issues.
 *
 * @since 0.2.0
 */
object LibraryHelper {

    /**
     * Retrieves a value from the receiver [Library] using the provided [getter] function:
     * ```kotlin
     * ToBaseMod.blocks.getIdentifier { custom_stone } // Block
     * ```
     *
     * @param I The input type for the receiver [Library].
     * @param V The final value type for the receiver [Library].
     * @param C The [Library] type.
     * @param W The final value subtype for the receiver [Library].
     * @param getter A lambda function that defines how to get the desired value from the [Library].
     * @return The value obtained from the instance of type [C] by applying the [getter] function.
     * @since 0.2.0
     */
    fun <I, V, C : Library<I, V>, W : V> C.get(getter: C.() -> W): W = getter()

    /**
     * Retrieves a [List] of [LibraryEntry] from the receiver [Library] using the value from the provided [getter] function:
     * ```kotlin
     * ToBaseMod.blocks.getEntries { custom_stone } // List<LibraryEntry<Block>>
     * ```
     *
     * @param I The input type for the receiver [Library].
     * @param V The final value type for the receiver [Library].
     * @param C The [Library] type.
     * @param W The final value subtype for the receiver [Library].
     * @param getter A lambda function that defines how to get the desired library entries from the [Library].
     * @return The library entries obtained from the instance of type [C] by applying the [getter] function.
     * @since 0.2.0
     */
    fun <I, V, C : Library<I, V>, W : V> C.getEntries(getter: C.() -> W) = getEntries(get(getter))

    /**
     * Retrieves a single [LibraryEntry] from the receiver [Library] using the value from the provided [getter] function:
     * ```kotlin
     * ToBaseMod.blocks.getEntry { custom_stone } // LibraryEntry<Block>
     * ```
     *
     * @param I The input type for the receiver [Library].
     * @param V The final value type for the receiver [Library].
     * @param C The [Library] type.
     * @param W The final value subtype for the receiver [Library].
     * @param getter A lambda function that defines how to get the desired [LibraryEntry] from the [Library].
     * @return The [LibraryEntry] obtained from the instance of type [C] by applying the [getter] function.
     * @since 0.2.0
     */
    fun <I, V, C : Library<I, V>, W : V> C.getEntry(getter: C.() -> W) = getEntry(get(getter))

    /**
     * Retrieves the [net.minecraft.resources.Identifier] from the receiver [Library] using the value from the provided [getter] function:
     * ```kotlin
     * ToBaseMod.blocks.getIdentifier { custom_stone } // Identifier[to_base:custom_stone]
     * ```
     *
     * @param I The input type for the receiver [Library].
     * @param V The final value type for the receiver [Library].
     * @param C The [Library] type.
     * @param W The final value subtype for the receiver [Library].
     * @param getter A lambda function that defines how to get the desired [LibraryEntry] from the [Library].
     * @return The [net.minecraft.resources.Identifier] obtained from the [LibraryEntry] from the instance of type [C] by applying the [getter] function.
     * @since 0.2.0
     */
    fun <I, V, C : Library<I, V>, W : V> C.getIdentifier(getter: C.() -> W) = getEntryIdentifier(getEntry(getter)!!)

    /**
     * Retrieves the tags in a [LibraryTagList] from the receiver [Library] using the value from the provided [getter] function:
     * ```kotlin
     * ToBaseMod.blocks.getTags(ToBaseMod.blocks.properties) { custom_stone } // List<ExtendedBlockProperties>
     * ```
     *
     * @param I The input type for the receiver [Library].
     * @param V The final value type for the receiver [Library].
     * @param C The [Library] type.
     * @param W The final value subtype for the receiver [Library].
     * @param T The tag type of the [LibraryTagList].
     * @param list The list to get tags from for the given value.
     * @param getter A lambda function that defines how to get the desired [LibraryEntry] from the [Library].
     * @return The [List] of [T] tags obtained from the [LibraryEntry] from the instance of type [C] by applying the [getter] function.
     * @since 0.2.0
     */
    fun <I, V, C : Library<I, V>, W : V, T> C.getTags(list: LibraryTagList<V, T>, getter: C.() -> W) = getEntryTags(list, getEntry(getter)!!)

    /**
     * Retrieves the [net.minecraft.resources.ResourceKey] from the receiver [Library] using the value from the provided [getter] function:
     * ```kotlin
     * ToBaseMod.blocks.getResourceKey { custom_stone } // ResourceKey[minecraft:block / to_base:custom_stone]
     * ```
     * The library must implement [IResourceKeyLibrary].
     *
     * @param I The input type for the receiver [Library].
     * @param V The final value type for the receiver [Library].
     * @param C The [Library] type.
     * @param W The final value subtype for the receiver [Library].
     * @param R The resource key type.
     * @param getter A lambda function that defines how to get the desired [LibraryEntry] from the [Library].
     * @return The [net.minecraft.resources.ResourceKey] obtained from the [LibraryEntry] from the instance of type [C] by applying the [getter] function.
     * @since 0.2.0
     */
    fun <I, V, C, W : V, R : Any> C.getResourceKey(getter: C.() -> W) where C : Library<I, V>, C : IResourceKeyLibrary<I, V, R> = getEntryResourceKey(getEntry(getter)!!)

    /**
     * Retrieves the [String] translation key from the receiver [Library] using the value from the provided [getter] function:
     * ```kotlin
     * ToBaseMod.blocks.getTranslationKey { custom_stone } //"block.to_base.custom_stone"
     * ```
     * The library must implement [TranslatableLibrary].
     *
     * @param I The input type for the receiver [Library].
     * @param V The final value type for the receiver [Library].
     * @param C The [Library] type.
     * @param W The final value subtype for the receiver [Library].
     * @param getter A lambda function that defines how to get the desired [LibraryEntry] from the [Library].
     * @return The [String] translation key obtained from the [LibraryEntry] from the instance of type [C] by applying the [getter] function.
     * @since 0.2.0
     */
    fun <I, V, C, W : V> C.getTranslationKey(getter: C.() -> W) where C : Library<I, V>, C : TranslatableLibrary<I, V> = getEntryTranslationKey(getEntry(getter)!!)

    /**
     * Retrieves the [net.minecraft.network.chat.Component] from the receiver [Library] using the value from the provided [getter] function:
     * ```kotlin
     * ToBaseMod.blocks.getEntryComponent { custom_stone } //Component.translatable("block.to_base.custom_stone")
     * ```
     * The library must implement [TranslatableLibrary].
     *
     * @param I The input type for the receiver [Library].
     * @param V The final value type for the receiver [Library].
     * @param C The [Library] type.
     * @param W The final value subtype for the receiver [Library].
     * @param getter A lambda function that defines how to get the desired [LibraryEntry] from the [Library].
     * @return The [net.minecraft.network.chat.Component] obtained from the [LibraryEntry] from the instance of type [C] by applying the [getter] function.
     * @since 0.2.0
     */
    fun <I, V, C, W : V> C.getEntryComponent(getter: C.() -> W) where C : Library<I, V>, C : TranslatableLibrary<I, V> = getEntryComponent(getEntry(getter)!!)

}
