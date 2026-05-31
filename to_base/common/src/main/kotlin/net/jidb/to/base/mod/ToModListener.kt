package net.jidb.to.base.mod

interface ToModListener<M : ToModListener<M>> {

    fun onInitialised(listener: (mod: M) -> Unit)
    fun onInitialised(listener: () -> Unit) = onInitialised { it -> listener() }
    fun onSetup(listener: (mod: M) -> Unit)
    fun onSetup(listener: () -> Unit) = onSetup { it -> listener() }

}
