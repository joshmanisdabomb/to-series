package net.jidb.to.base.library

import net.minecraft.resources.Identifier
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

sealed class Library<I, V>(val modid: String) {

    internal val _entries = mutableMapOf<String, LibraryEntry<out I, out V>>()
    lateinit var entries: Map<String, LibraryEntry<out I, out V>>
        private set
    val values by lazy { entries.mapValues { it.value.value }.values }

    var initialised = false
        private set

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

    fun buildOnce(): Map<String, LibraryEntry<out I, out V>> {
        if (initialised) return entries
        return build()
    }

    open fun beforeBuild() = Unit

    open fun beforeBuild(entry: LibraryEntry<out I, out V>) = Unit

    open fun afterBuild() = Unit

    open fun afterBuild(entry: LibraryEntry<out I, out V>) = Unit

    fun <W : V> getEntries(value: W): List<LibraryEntry<out I, W>> = _entries.filter { it.value.value == value }.values.toList() as List<Library<I, V>.LibraryEntry<out I, W>>
    fun <W : V> getEntries(property: KProperty<W>): List<LibraryEntry<out I, W>> = _entries.filter { it.value.property == property }.values.toList() as List<Library<I, V>.LibraryEntry<out I, W>>
    fun <W : V> getEntry(value: W) = getEntries(value).firstOrNull()
    fun <W : V> getEntry(property: KProperty<W>) = getEntries(property).firstOrNull()

    open fun getEntryIdentifier(entry: LibraryEntry<out I, out V>) = entry.id

    open fun <T> getEntryTags(list: LibraryTagList<V, T>, entry: LibraryEntry<out I, out V>): List<T> = list.get(entry)

    override fun toString() = "$modid ${this.javaClass}"

    inner class LibraryEntry<J : I, W : V>(val builder: LibraryEntry<out I, out V>.(() -> J) -> () -> W, val initial: (LibraryEntry<J, W>) -> J) {

        lateinit var property: KProperty<*>
            private set
        val name get() = property.name
        val id by lazy { Identifier.fromNamespaceAndPath(modid, name) }
        var index by Delegates.notNull<Int>()
            private set

        var initialised = false
            private set
        var evalInput = false
            private set
        var evalValue = false
            private set
        private var defer: ((() -> Unit) -> Unit)? = null
        var evaluated = false
            private set

        lateinit var getter: () -> W
            private set
        val value by lazy { evaluated = true; getter() }

        operator fun provideDelegate(library: Library<I, V>, property: KProperty<*>): LibraryEntry<J, W> {
            this.property = property
            this.index = library._entries.size
            library._entries[name] = this
            return this
        }

        operator fun getValue(library: Library<I, V>, property: KProperty<*>): W {
            return value
        }

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

        fun evaluateInput(): Library<I, V>.LibraryEntry<J, W> {
            evalInput = true
            return this
        }

        fun evaluateValue(): Library<I, V>.LibraryEntry<J, W> {
            evalValue = true
            return this
        }

        fun <T> tag(list: LibraryTagList<V, T>, value: T): Library<I, V>.LibraryEntry<J, W> {
            list.add(this, value)
            return this
        }

        fun deferBuild(consumer: (() -> Unit) -> Unit): Library<I, V>.LibraryEntry<J, W> {
            defer = consumer
            return this
        }

    }

}