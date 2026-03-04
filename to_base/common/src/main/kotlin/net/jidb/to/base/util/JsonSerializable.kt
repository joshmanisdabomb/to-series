package net.jidb.to.base.util

import com.google.gson.JsonElement

interface JsonSerializable {

    fun toJson(): JsonElement

}
