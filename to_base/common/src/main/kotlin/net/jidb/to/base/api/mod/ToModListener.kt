package net.jidb.to.base.api.mod

/**
 * Provides an interface for other mods to listen to when this mod is initialized and setup.
 * This is a generic interface where the type parameter [M] is intended to represent the specific module
 * implementing this listener.
 *
 * @param M The self type that this interface is being attached to, e.g. your [net.jidb.to.base.pub.mod.ToMod].
 * @since 0.5.0
 */
interface ToModListener<M : ToModListener<M>> {

    /**
     * Registers a listener that will be invoked when this mod is initialized.
     *
     * Example usage would be to defer the build of your [LibraryEntry] until this mod is initialized: `LibraryEntry.deferBuild(ToMod::onInitialised)`
     *
     * @param listener A function to be executed when this mod is initialized. The function receives this mod as a parameter.
     * @since 0.5.0
     */
    fun onInitialised(listener: (mod: M) -> Unit)

    /**
     * Registers a listener that will be invoked when this mod is initialized.
     *
     * Example usage would be to defer the build of your [LibraryEntry] until this mod is initialized: `LibraryEntry.deferBuild(ToMod::onInitialised)`
     *
     * @param listener A function to be executed when this mod is initialized. The function does not receive this mod as a parameter.
     * @return [Unit]
     * @since 0.5.0
     */
    fun onInitialised(listener: () -> Unit) = onInitialised { _ -> listener() }

    /**
     * Registers a listener that will be invoked when this mod has run setup.
     *
     * Example usage would be to defer the build of your [LibraryEntry] until this mod is set up: `LibraryEntry.deferBuild(ToMod::onSetup)`
     *
     * @param listener A function to be executed when this mod is set up. The function receives this mod as a parameter.
     * @since 0.5.0
     */
    fun onSetup(listener: (mod: M) -> Unit)

    /**
     * Registers a listener that will be invoked when this mod has run setup.
     *
     * Example usage would be to defer the build of your [LibraryEntry] until this mod is set up: `LibraryEntry.deferBuild(ToMod::onSetup)`
     *
     * @param listener A function to be executed when this mod is set up. The function does not receive this mod as a parameter.
     * @return [Unit]
     * @since 0.5.0
     */
    fun onSetup(listener: () -> Unit) = onSetup { _ -> listener() }

}
