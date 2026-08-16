package net.jidb.to.base.data.api.library

import net.jidb.to.base.api.library.IResourceKeyLibrary
import net.jidb.to.base.api.library.Library
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.JukeboxSong

open class JukeboxSongLibrary(modid: String) : DatapackLibrary<JukeboxSong>(modid), IResourceKeyLibrary<JukeboxSong, JukeboxSong, JukeboxSong> {

    override val registryKey = Registries.JUKEBOX_SONG

    operator fun invoke(sound: SoundEvent, length: Float, comparator: Int, description: Component? = null): Library<JukeboxSong, JukeboxSong>.LibraryEntry<JukeboxSong, JukeboxSong> = invoke(::i) { JukeboxSong(Holder.direct(sound), description ?: Component.translatable(it.id.toLanguageKey("jukebox_song")), length, comparator) }

}
