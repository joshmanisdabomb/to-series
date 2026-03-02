package net.jidb.to.base.library

import net.minecraft.resources.Identifier
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

sealed class Library<I, V>(val modid: String) {

    internal val _entries = mutableMapOf<String, LibraryEntry<out I, out V>>()
    val entries by lazy { _entries.toMap() }
    val values by lazy { entries.mapValues { it.value.value } }

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

    open fun getEntryIdentifier(entry: LibraryEntry<out I, out V>) = Identifier.fromNamespaceAndPath(modid, entry.name)

    override fun toString() = "$modid ${this.javaClass}"

    protected operator fun <J : I, W : V> invoke(builder: LibraryEntry<out I, out V>.(() -> J) -> () -> W, initial: (LibraryEntry<J, W>) -> J): LibraryEntry<J, W> {
        return LibraryEntry(builder, initial)
    }

    inner class LibraryEntry<J : I, W : V>(val builder: LibraryEntry<out I, out V>.(() -> J) -> () -> W, val initial: (LibraryEntry<J, W>) -> J) {

        lateinit var property: KProperty<*>
            private set
        val name get() = property.name
        var index by Delegates.notNull<Int>()
            private set

        var initialised = false
            private set
        var evalInput = false
            private set
        var evalValue = false
            private set
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
            val input: () -> J
            if (evalInput) {
                val resolved = initial(this)
                input = { resolved }
            } else {
                input = { initial(this) }
            }
            getter = builder(this, input)
            if (evalValue) value
            initialised = true
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

        fun <T> tag(list: LibraryTagList<T>, value: T): Library<I, V>.LibraryEntry<J, W> {
            list.add(this, value)
            return this
        }

    }

}