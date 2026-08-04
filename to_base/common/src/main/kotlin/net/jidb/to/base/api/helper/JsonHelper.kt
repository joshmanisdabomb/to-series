package net.jidb.to.base.api.helper

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import net.jidb.to.base.api.util.JsonSerializable

@Deprecated("Prefer using Mojang's codec system over building JSON directly.", ReplaceWith("com.mojang.serialization"))
@Suppress("detekt.all")
object JsonHelper {

    val gsonBuilderData = GsonBuilder().setLenient().setPrettyPrinting().create()

    fun <T> add(array: JsonArray, obj: T, serialiser: (T) -> JsonElement) = array.add(serialiser(obj))
    @JvmName("addExt")
    fun <T> JsonArray.add(obj: T, serialiser: (T) -> JsonElement) = add(this, obj, serialiser)
    fun add(array: JsonArray, obj: JsonSerializable) = add(array, obj, JsonSerializable::toJson)
    @JvmName("addExt")
    fun JsonArray.add(obj: JsonSerializable) = add(this, obj)

    fun <T> add(json: JsonObject, key: String, obj: T, serialiser: (T) -> JsonElement) = json.add(key, serialiser(obj))
    @JvmName("addExt")
    fun <T> JsonObject.add(key: String, obj: T, serialiser: (T) -> JsonElement) = add(this, key, obj, serialiser)
    fun add(json: JsonObject, key: String, obj: JsonSerializable) = add(json, key, obj, JsonSerializable::toJson)
    @JvmName("addExt")
    fun JsonObject.add(key: String, obj: JsonSerializable) = add(this, key, obj)

    fun replace(json: JsonObject, obj: JsonObject) = obj.keySet().forEach { json.add(it, obj.get(it)) }
    @JvmName("replaceExt")
    fun JsonObject.replace(obj: JsonObject) = replace(this, obj)

    fun getBool(json: JsonObject, key: String) = json.getAsJsonPrimitive(key)?.asBoolean
    @JvmName("getBoolExt")
    fun JsonObject.getBool(key: String) = getBool(this, key)
    fun getNumber(json: JsonObject, key: String) = json.getAsJsonPrimitive(key)?.asNumber
    @JvmName("getNumberExt")
    fun JsonObject.getNumber(key: String) = getNumber(this, key)
    fun getString(json: JsonObject, key: String) = json.getAsJsonPrimitive(key)?.asString
    @JvmName("getStringExt")
    fun JsonObject.getString(key: String) = getString(this, key)
    fun getChar(json: JsonObject, key: String) = json.getAsJsonPrimitive(key)?.asCharacter
    @JvmName("getCharExt")
    fun JsonObject.getChar(key: String) = getChar(this, key)
    fun addBool(json: JsonObject, key: String, value: Boolean) = json.addProperty(key, value)
    @JvmName("addBoolExt")
    fun JsonObject.addBool(key: String, value: Boolean) = addBool(this, key, value)
    fun addNumber(json: JsonObject, key: String, value: Number) = json.addProperty(key, value)
    @JvmName("addNumberExt")
    fun JsonObject.addNumber(key: String, value: Number) = addNumber(this, key, value)
    fun addString(json: JsonObject, key: String, value: String) = json.addProperty(key, value)
    @JvmName("addStringExt")
    fun JsonObject.addString(key: String, value: String) = addString(this, key, value)
    fun addChar(json: JsonObject, key: String, value: Char) = json.addProperty(key, value)
    @JvmName("addCharExt")
    fun JsonObject.addChar(key: String, value: Char) = addChar(this, key, value)

    fun <I : Any, O : Any> modify(json: JsonObject, key: String, getter: JsonObject.(key: String) -> I?, adder: JsonObject.(key: String, value: O) -> Unit, default: I, m: (I) -> O?) = m(getter(json, key) ?: default)?.also { adder(json, key, it) }
    @JvmName("modifyExt")
    fun <I : Any, O : Any> JsonObject.modify(key: String, getter: JsonObject.(key: String) -> I?, adder: JsonObject.(key: String, value: O) -> Unit, default: I, m: (I) -> O?) = modify(this, key, getter, adder, default, m)

    fun getOrCreateObject(json: JsonObject, key: String, obj: JsonObject = JsonObject(), modify: (JsonObject) -> Unit = {}): JsonObject {
        val get = json.get(key)
        if (get is JsonObject) return get.also(modify)
        val new = obj.also(modify)
        json.add(key, new)
        return new
    }
    @JvmName("getOrCreateObjectExt")
    fun JsonObject.getOrCreateObject(key: String, obj: JsonObject = JsonObject(), modify: (JsonObject) -> Unit = {}) = getOrCreateObject(this, key, obj, modify)

    fun <T> getOrPut(json: JsonObject, key: String, default: T, getter: JsonObject.(key: String) -> T?, adder: JsonObject.(key: String, value: T) -> Unit): T {
        if (json.has(key)) {
            return getter(json, key)!!
        }
        adder(json, key, default)
        return default
    }
    @JvmName("getOrPutExt")
    fun <T> JsonObject.getOrPut(key: String, default: T, getter: JsonObject.(key: String) -> T?, adder: JsonObject.(key: String, value: T) -> Unit) = getOrPut(this, key, default, getter, adder)
    fun getOrPut(json: JsonObject, key: String, default: Boolean) = getOrPut(json, key, default, ::getBool, JsonObject::addProperty)
    @JvmName("getOrPutExt")
    fun JsonObject.getOrPut(key: String, default: Boolean) = getOrPut(this, key, default)
    fun getOrPut(json: JsonObject, key: String, default: Number) = getOrPut(json, key, default, ::getNumber, JsonObject::addProperty)
    @JvmName("getOrPutExt")
    fun JsonObject.getOrPut(key: String, default: Number) = getOrPut(this, key, default)
    fun getOrPut(json: JsonObject, key: String, default: String) = getOrPut(json, key, default, ::getString, JsonObject::addProperty)
    @JvmName("getOrPutExt")
    fun JsonObject.getOrPut(key: String, default: String) = getOrPut(this, key, default)
    fun getOrPut(json: JsonObject, key: String, default: Char) = getOrPut(json, key, default, ::getChar, JsonObject::addProperty)
    @JvmName("getOrPutExt")
    fun JsonObject.getOrPut(key: String, default: Char) = getOrPut(this, key, default)
    fun getOrPut(json: JsonObject, key: String, default: JsonElement) = getOrPut(json, key, default, JsonObject::get, JsonObject::add)
    @JvmName("getOrPutExt")
    fun JsonObject.getOrPut(key: String, default: JsonElement) = getOrPut(this, key, default)
    fun getOrPut(json: JsonObject, key: String, default: JsonObject) = getOrPut(json, key, default, JsonObject::getAsJsonObject, JsonObject::add)
    @JvmName("getOrPutExt")
    fun JsonObject.getOrPut(key: String, default: JsonObject) = getOrPut(this, key, default)
    fun getOrPut(json: JsonObject, key: String, default: JsonArray) = getOrPut(json, key, default, JsonObject::getAsJsonArray, JsonObject::add)
    @JvmName("getOrPutExt")
    fun JsonObject.getOrPut(key: String, default: JsonArray) = getOrPut(this, key, default)
    fun getOrPut(json: JsonObject, key: String, default: JsonPrimitive) = getOrPut(json, key, default, JsonObject::getAsJsonPrimitive, JsonObject::add)
    @JvmName("getOrPutExt")
    fun JsonObject.getOrPut(key: String, default: JsonPrimitive) = getOrPut(this, key, default)

    fun addStrings(array: JsonArray, elements: Array<out String>) = elements.forEach(array::add)
    @JvmName("addStringsExt")
    fun JsonArray.addStrings(elements: Array<out String>) = addStrings(this, elements)
    fun <T> addSerialisable(array: JsonArray, elements: Array<out T>, serialiser: (T) -> JsonElement) = elements.forEach { add(array, it, serialiser) }
    @JvmName("addSerialisableExt")
    fun <T> JsonArray.addSerialisable(elements: Array<out T>, serialiser: (T) -> JsonElement) = addSerialisable(this, elements, serialiser)
    fun addSerialisable(array: JsonArray, elements: Array<out JsonSerializable>) = elements.forEach { add(array, it, JsonSerializable::toJson) }
    @JvmName("addSerialisableExt")
    fun JsonArray.addSerialisable(elements: Array<out JsonSerializable>) = addSerialisable(this, elements)

    fun addStrings(json: JsonObject, elements: Map<String, String>) = elements.forEach { (k, v) -> json.addProperty(k, v) }
    @JvmName("addStringsExt")
    fun JsonObject.addStrings(elements: Map<String, String>) = addStrings(this, elements)

    private fun <J : JsonElement, T> addObjectTemplate(element: J, obj: Map<String, T>, add: J.(JsonObject) -> Unit, addAll: JsonObject.(Map<String, T>) -> Unit) = add(element, JsonObject().also { addAll(it, obj) })

    fun addStringObject(json: JsonObject, key: String, obj: Map<String, String>) = addObjectTemplate(json, obj, { this.add(key, it) }, ::addStrings)
    @JvmName("addStringObjectExt")
    fun JsonObject.addStringObject(key: String, obj: Map<String, String>) = addStringObject(this, key, obj)

    private fun <J : JsonElement, T> addArrayTemplate(element: J, elements: Array<T>, add: J.(JsonArray) -> Unit, addAll: JsonArray.(Array<T>) -> Unit) = add(element, JsonArray().also { addAll(it, elements) })

    fun <T> addSerialisableArray(array: JsonArray, elements: Array<out T>, serialiser: (T) -> JsonElement) = addArrayTemplate(array, elements, JsonArray::add) { addSerialisable(this, it, serialiser) }
    @JvmName("addSerialisableArrayExt")
    fun <T> JsonArray.addSerialisableArray(elements: Array<out T>, serialiser: (T) -> JsonElement) = addSerialisableArray(this, elements, serialiser)
    fun addSerialisableArray(array: JsonArray, elements: Array<out JsonSerializable>) = addArrayTemplate(array, elements, JsonArray::add) { addSerialisable(this, it) }
    @JvmName("addSerialisableArrayExt")
    fun JsonArray.addSerialisableArray(elements: Array<out JsonSerializable>) = addSerialisableArray(this, elements)

    fun <T> addSerialisableArray(json: JsonObject, key: String, elements: Array<out T>, serialiser: (T) -> JsonElement) = addArrayTemplate(json, elements, { this.add(key, it) }) { addSerialisable(this, it, serialiser) }
    @JvmName("addSerialisableArrayExt")
    fun <T> JsonObject.addSerialisableArray(key: String, elements: Array<out T>, serialiser: (T) -> JsonElement) = addSerialisableArray(this, key, elements, serialiser)
    fun addSerialisableArray(json: JsonObject, key: String, elements: Array<out JsonSerializable>) = addArrayTemplate(json, elements, { this.add(key, it) }) { addSerialisable(this, it) }
    @JvmName("addSerialisableArrayExt")
    fun JsonObject.addSerialisableArray(key: String, elements: Array<out JsonSerializable>) = addSerialisableArray(this, key, elements)

    private fun <T : Any> addOrNullTemplate(obj: T?, add: (T) -> Unit) = obj?.also(add)

    fun <T : Any> addOrNull(array: JsonArray, obj: T?, serialiser: (T) -> JsonElement) = addOrNullTemplate(obj) { add(array, it, serialiser) }
    @JvmName("addOrNullExt")
    fun <T : Any> JsonArray.addOrNull(obj: T?, serialiser: (T) -> JsonElement) = addOrNull(this, obj, serialiser)

    fun addOrNull(json: JsonObject, key: String, obj: Number?) = addOrNullTemplate(obj) { json.addProperty(key, it) }
    @JvmName("addOrNullExt")
    fun JsonObject.addOrNull(key: String, obj: Number?) = addOrNull(this, key, obj)

}
