package net.jidb.to.base.data.api.library

import net.jidb.to.base.api.library.IResourceKeyLibrary
import net.jidb.to.base.api.library.Library
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.JukeboxSong

/**
 * [DatapackLibrary] implementation that generates the jukebox songs of a mod, i.e. what a music disc plays and how it behaves in a jukebox.
 *
 * @param modid The mod ID associated with the library.
 * @since 0.8.0
 */
open class JukeboxSongLibrary(modid: String) : DatapackLibrary<JukeboxSong>(modid), IResourceKeyLibrary<JukeboxSong, JukeboxSong, JukeboxSong> {

    override val registryKey = Registries.JUKEBOX_SONG

    /**
     * Declares a jukebox song named after the property it is assigned to, in the namespace of this library.
     *
     * @param sound The sound event the song plays.
     * @param length How long the song lasts, in seconds.
     * @param comparator The signal strength a comparator reads from a jukebox playing this song.
     * @param description The name shown while the song plays, or `null` to use the translation key of the entry. Defaults to `null`.
     * @return The [Library.LibraryEntry] for the declared property.
     * @since 0.8.0
     */
    operator fun invoke(sound: SoundEvent, length: Float, comparator: Int, description: Component? = null): Library<JukeboxSong, JukeboxSong>.LibraryEntry<JukeboxSong, JukeboxSong> = invoke(::i) { JukeboxSong(Holder.direct(sound), description ?: Component.translatable(it.id.toLanguageKey("jukebox_song")), length, comparator) }

}
