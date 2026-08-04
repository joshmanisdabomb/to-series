package net.jidb.to.base.api.util

import com.google.gson.JsonElement

/**
 * An interface that was designed to allow an object to define how it converts to and from JSON, to then be used in [net.jidb.to.base.api.helper.JsonHelper].
 * Now deprecated in favour of Mojang's [com.mojang.serialization.Codec] system, will be removed later.
 *
 * @since 0.1.0
 */
@Deprecated("Prefer using Mojang's codec system over building JSON directly.", ReplaceWith("com.mojang.serialization"))
interface JsonSerializable {

    /**
     * Converts the object to a [JsonElement] suitable for serialization.
     *
     * @return a [JsonElement] representation of the object.
     * @since 0.1.0
     */
    fun toJson(): JsonElement

}
