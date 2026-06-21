package net.jidb.to.base.pub.library

import net.jidb.to.base.api.library.Library
import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.base.service.Services
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvent

open class SoundEventLibrary(modId: String) : SimpleRegistryLibrary<SoundEvent>(modId) {

    override val registry = BuiltInRegistries.SOUND_EVENT

    override fun <U : SoundEvent> i(entry: Library<SoundEvent, SoundEvent>.LibraryEntry<out SoundEvent, out SoundEvent>, input: () -> U): () -> U {
        val event = input()
        return Services.register(registry, event.location) { event }
    }

    override fun getEntryIdentifier(entry: Library<SoundEvent, SoundEvent>.LibraryEntry<out SoundEvent, out SoundEvent>) = entry.value.location

}
