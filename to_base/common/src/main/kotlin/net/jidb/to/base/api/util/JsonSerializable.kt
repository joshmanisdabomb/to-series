package net.jidb.to.base.api.util

import com.google.gson.JsonElement

interface JsonSerializable {

    fun toJson(): JsonElement

}
