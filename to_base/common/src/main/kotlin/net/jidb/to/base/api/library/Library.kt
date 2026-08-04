package net.jidb.to.base.api.library

import net.minecraft.resources.Identifier
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * Represents a generic class designed to make it easier to lazily declare a collection of entries with inputs that are processed into an output, and can be searched through.
 *
 * @param I The type of the input given to define a [LibraryEntry].
 * @param V The type of the value (output) which can be retrieved from a library property or [LibraryEntry].
 * @property modid The mod ID for this library.
 * @since 0.0.3
 */
sealed class Library<I, V>(val modid: String) {

    /**
     * A mutable map that stores the internal library entries before initialization.
     *
     * @since 0.0.3
     */
    internal val _entries = mutableMapOf<String, LibraryEntry<out I, out V>>()

    /**
     * A map of [LibraryEntry] indexed by their string identifiers. This property represents the collection of all entries registered in the library.
     *
     * This property is initialized during the library's build process and cannot be modified directly once constructed.
     *
     * Access to this property requires it to be initialized through the `build()` or `buildOnce()` method of the containing library. Attempting to access the property prior to initialization will throw.
     *
     * @since 0.0.3
     */
    lateinit var entries: Map<String, LibraryEntry<out I, out V>>
        private set

    /**
     * Lazily initialized map of values extracted from the `entries` map.
     * @since 0.0.3
     */
    val values by lazy { entries.mapValues { it.value.value }.values }

    /**
     * Indicates whether the library has been fully built and initialised.
     * This flag is set to `true` when [build] is called.
     * @since 0.0.3
     */
    var initialised = false
        private set

    /**
     * Builds the library by finalizing all its entries and marking it as initialized.
     *
     * @return A map containing the finalized entries of the library, where the keys are the entry names and the values are the corresponding library entries.
     * @throws LibraryException If the library has already been built.
     * @since 0.0.3
     */
    fun build(): Map<String, LibraryEntry<out I, out V>> {
        if (initialised) {
            throw LibraryException("${this@Library} is already built.")
        }
        beforeBuild()
        for (entry in _entries.values) {
            entry.build()
        }
        entries = _entries.toMap()
        initialised = true
        afterBuild()
        return entries
    }

    /**
     * Ensures that the library is built only once. If the library has already been built, it returns the existing entries.
     * Otherwise, it triggers the library [build] call.
     *
     * @return A map containing the library [entries], where the keys are the entry names and the values are the corresponding library entries.
     * @since 0.0.3
     */
    fun buildOnce(): Map<String, LibraryEntry<out I, out V>> {
        if (initialised) return entries
        return build()
    }

    /**
     * This method is invoked before the library build process begins.
     * It is a hook intended to be overridden by subclasses to provide custom pre-build behavior.
     *
     * @since 0.0.3
     */
    open fun beforeBuild() = Unit

    /**
     * This method is invoked before each [LibraryEntry] is built.
     * It is a hook intended to be overridden by subclasses to provide custom pre-build behavior.
     *
     * @param entry The library entry about to be built.
     * @since 0.0.3
     */
    open fun beforeBuild(entry: LibraryEntry<out I, out V>) = Unit

    /**
     * This method is invoked after the library build process is finished.
     * It is a hook intended to be overridden by subclasses to provide custom post-build behavior.
     *
     * @since 0.0.3
     */
    open fun afterBuild() = Unit

    /**
     * This method is invoked after each [LibraryEntry] is built.
     * It is a hook intended to be overridden by subclasses to provide custom post-build behavior.
     *
     * @param entry The library entry that was just built.
     * @since 0.0.3
     */
    open fun afterBuild(entry: LibraryEntry<out I, out V>) = Unit

    /**
     * Retrieves a list of library entries with the specified value.
     * Example usage would be `Library.getEntries(Library.custom_stone)`
     *
     * @param W The final type of the value, used to cast the returned [LibraryEntry] list.
     * @param value The value to filter the library entries by.
     * @return A [List] of [LibraryEntry] (usually containing one element) whose values match the specified value.
     * @since 0.0.4
     */
    fun <W : V> getEntries(value: W): List<LibraryEntry<out I, W>> = _entries.filter { it.value.value == value }.values.toList() as List<Library<I, V>.LibraryEntry<out I, W>>

    /**
     * Retrieves a list of library entries under the specified Library property.
     * Example usage would be `Library.getEntries(Library::custom_stone)`
     *
     * @param W The final type of the value within the property, used to cast the returned [LibraryEntry] list.
     * @param property The property to filter the library entries by.
     * @return A [List] of [LibraryEntry] (usually containing one element) whose values match the specified value.
     * @since 0.0.4
     */
    fun <W : V> getEntries(property: KProperty<W>): List<LibraryEntry<out I, W>> = _entries.filter { it.value.property == property }.values.toList() as List<Library<I, V>.LibraryEntry<out I, W>>

    /**
     * Retrieves the first library entry that matches the specified value, or `null` if no match is found.
     * Example usage would be `Library.getEntry(Library.custom_stone)`
     *
     * @param W The final type of the value, used to cast the returned [LibraryEntry].
     * @param value The value used to locate and retrieve the corresponding library entry.
     * @return The first matching [LibraryEntry], or `null` if no matching entry exists.
     * @since 0.0.4
     */
    fun <W : V> getEntry(value: W) = getEntries(value).firstOrNull()

    /**
     * Retrieves the first library entry that matches the specified Library property, or `null` if no match is found.
     * Example usage would be `Library.getEntry(Library::custom_stone)`
     *
     * @param W The final type of the value within the property, used to cast the returned [LibraryEntry].
     * @param property The property used to locate and retrieve the corresponding library entry.
     * @return The first matching [LibraryEntry], or `null` if no matching entry exists.
     * @since 0.0.4
     */
    fun <W : V> getEntry(property: KProperty<W>) = getEntries(property).firstOrNull()

    /**
     * Retrieves the [Identifier] associated with a given [LibraryEntry].
     * Can be overridden by subclasses to provide different implementations of retrieving an identifier from an entry.
     *
     * @param entry The [LibraryEntry] whose identifier is to be retrieved.
     * @return The [Identifier] of the specified [LibraryEntry].
     * @see LibraryEntry.id
     * @since 0.0.3
     */
    open fun getEntryIdentifier(entry: LibraryEntry<out I, out V>) = entry.id

    /**
     * Retrieves a list of tags associated with the given [LibraryEntry] from the given [LibraryTagList].
     *
     * @param T The type of tags stored in the list.
     * @param list The tag list containing mappings between library entries and their respective tags.
     * @param entry The library entry for which the associated tags are to be retrieved.
     * @return A list of tags of type [T] associated with the provided library entry.
     * @since 0.1.0
     */
    open fun <T> getEntryTags(list: LibraryTagList<V, T>, entry: LibraryEntry<out I, out V>): List<T> = list.get(entry)

    /**
     * Returns a string representation of the library and its `modid`, useful for debug printing.
     *
     * @return [String] in the form "$modid $javaClass"
     * @since 0.0.3
     */
    override fun toString() = "$modid ${this.javaClass}"

    /**
     * Represents an entry in an outer [Library], which encapsulates the logic for initialization, deferred building, and value evaluation.
     * Each [LibraryEntry] has a name and single input ([J]) supplier, which is converted into a single value ([W]) supplier when the [Library] is built.
     * The input or value may or may be not be resolved at build time, depending on the build process implementation of the outer [Library].
     *
     * @param J The type for the input value used to build the entry.
     * @param W The type for the final value of the entry.
     * @property builder A function responsible for transforming the input supplier into the value supplier.
     * @property initial A function that generates the initial input value of the entry, before transformation by the builder.
     * @since 0.0.3
     */
    inner class LibraryEntry<J : I, W : V>(val builder: LibraryEntry<out I, out V>.(() -> J) -> () -> W, val initial: (LibraryEntry<J, W>) -> J) {

        /**
         * Represents the [Library] property that this [LibraryEntry] provides a delegate value for.
         * The property is initialized later at runtime when it is assigned via delegation.
         *
         * @since 0.0.3
         */
        lateinit var property: KProperty<*>
            private set

        /**
         * Provides the name of this [LibraryEntry], derived from the associated property name.
         * The name is used as a unique identifier within the library's entry collection.
         *
         * @since 0.0.3
         */
        val name get() = property.name

        /**
         * The [Identifier] for the current [LibraryEntry], lazily created upon request using the [modid] of the [Library].
         *
         * @since 0.0.3
         */
        val id by lazy { Identifier.fromNamespaceAndPath(modid, name) }

        /**
         * The `index` property represents the numeric index of this entry within the [Library], i.e. in which order this entry was assigned.
         * This property is initialized when the entry is added to a library via the `provideDelegate` operator.
         *
         * @since 0.0.3
         */
        var index by Delegates.notNull<Int>()
            private set

        /**
         * Indicates whether the [LibraryEntry] has been initialized, i.e. the [Library] has already been built with this entry.
         * This variable is used to track the initialization state of a library entry to ensure that the build process is executed only once.
         *
         * @since 0.0.3
         */
        var initialised = false
            private set

        /**
         * Indicates whether the [LibraryEntry] itself will immediately resolve the input supplier at build time.
         * Note that the [Library] may have its own build implementation that resolves the input. This just controls whether the entry itself resolves it.
         *
         * @since 0.0.3
         */
        var evalInput = false
            private set

        /**
         * Indicates whether the [LibraryEntry] itself will immediately resolve the value supplier at build time.
         * Note that the [Library] may have its own build implementation that resolves the value. This just controls whether the entry itself resolves it.
         *
         * @since 0.0.3
         */
        var evalValue = false
            private set

        /**
         * A function consumer that, if set, the [LibraryEntry] will pass the function that builds the value from the input.
         * Example usage would be to wait for another mod to be initialized before building a [LibraryEntry] that depends on it.
         *
         * @since 0.5.0
         */
        private var defer: ((() -> Unit) -> Unit)? = null

        /**
         * Indicates whether the [value] of this [LibraryEntry]  has been evaluated.
         *
         * @since 0.0.3
         */
        var evaluated = false
            private set

        /**
         * Getter function that supplies the final value associated with this `LibraryEntry`.
         * [value] should be used to access the final value, since it is lazily computed and also marks as [evaluated].
         *
         * @since 0.0.3
         */
        lateinit var getter: () -> W
            private set

        /**
         * Lazy-initialized final value for the `LibraryEntry`.
         *
         * @since 0.0.3
         */
        val value by lazy { evaluated = true; getter() }

        /**
         * Provides a delegate for managing the association of a [Library] property with a [LibraryEntry].
         *
         * @param library The [Library] to which the delegate is associated.
         * @param property The property for which the delegate is being provided.
         * @return The current instance of the [LibraryEntry] as a delegate.
         * @since 0.0.3
         */
        operator fun provideDelegate(library: Library<I, V>, property: KProperty<*>): LibraryEntry<J, W> {
            this.property = property
            this.index = library._entries.size
            library._entries[name] = this
            return this
        }

        /**
         * Retrieves the final value associated with the given [Library] property.
         *
         * @param library The [Library] instance to which this property belongs.
         * @param property The property being accessed.
         * @return The value of the property.
         * @since 0.0.3
         */
        operator fun getValue(library: Library<I, V>, property: KProperty<*>) = value

        /**
         * Constructs and finalizes the state of the current [LibraryEntry]. This method uses [builder] to transform the input supplier [initial] to the final [value] supplier.
         * It also runs callbacks [beforeBuild] and [afterBuild], handles deferred building if set for the entry, and also marks [initialised].
         *
         * @throws LibraryException if the current library entry has already been built.
         * @since 0.0.3
         */
        fun build() {
            if (initialised) {
                throw LibraryException("${property.name} in ${this@Library} is already built.")
            }
            beforeBuild(this)

            val input = if (evalInput) {
                val resolved = initial(this)
                ({ resolved })
            } else {
                ({ initial(this) })
            }

            val defer = defer
            if (defer != null) {
                defer {
                    getter = builder(this, input)
                    if (evalValue) value
                    initialised = true
                }
            } else {
                getter = builder(this, input)
                if (evalValue) value
                initialised = true
            }

            afterBuild(this)
        }

        /**
         * Builder function that sets that the input supplier [initial] will be resolved to get its value at [build] time.
         *
         * @return The current instance of [LibraryEntry] for further chaining.
         * @since 0.0.3
         */
        fun evaluateInput(): Library<I, V>.LibraryEntry<J, W> {
            evalInput = true
            return this
        }

        /**
         * Builder function that sets that the final value supplier [value] will be resolved to get its value at [build] time, rather than lazily.
         *
         * @return The current instance of [LibraryEntry] for further chaining.
         * @since 0.0.3
         */
        fun evaluateValue(): Library<I, V>.LibraryEntry<J, W> {
            evalValue = true
            return this
        }

        /**
         * Builder function that adds a tag value to be associated with this [LibraryEntry] in the given [LibraryTagList].
         *
         * @param T The type of tags stored in the list.
         * @param list The tag list where the association will be added.
         * @param value The tag value to associate with the current library entry.
         * @return The current instance of [LibraryEntry] for further chaining.
         * @since 0.0.3
         */
        fun <T> tag(list: LibraryTagList<V, T>, value: T): Library<I, V>.LibraryEntry<J, W> {
            list.add(this, value)
            return this
        }

        /**
         * Builder function to set a deferred build function for the current [LibraryEntry].
         * The provided consumer will be invoked during the build phase with a callback function that builds the [LibraryEntry], instead of being built immediately at [build] time.
         * Example usage would be to wait for another mod to be initialized before building a [LibraryEntry] that depends on it.
         *
         * @param consumer A lambda that accepts a callback to perform deferred execution logic.
         * @return The current instance of [LibraryEntry] for further chaining.
         * @since 0.5.0
         */
        fun deferBuild(consumer: (() -> Unit) -> Unit): Library<I, V>.LibraryEntry<J, W> {
            defer = consumer
            return this
        }

    }

}
