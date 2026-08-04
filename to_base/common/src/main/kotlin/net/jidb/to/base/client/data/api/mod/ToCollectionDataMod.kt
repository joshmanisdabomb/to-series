package net.jidb.to.base.client.data.api.mod

import net.jidb.to.base.data.api.library.DataCollectionLibrary

/**
 * Implemented by a mod that generates its data from collections, i.e. by describing each piece of content once and letting the modules attached to it write out every file it needs.
 *
 * See [ToContentDataMod] for the providers a mod hands to the generator itself.
 *
 * @since 0.3.0
 */
interface ToCollectionDataMod {

    /**
     * The library holding the collections of this mod, or `null` where it generates nothing this way. Defaults to `null`.
     *
     * @since 0.3.0
     */
    val collections: DataCollectionLibrary? get() = null

}
