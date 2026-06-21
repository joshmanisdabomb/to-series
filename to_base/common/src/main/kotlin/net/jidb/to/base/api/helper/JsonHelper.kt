package net.jidb.to.base.api.helper

import com.google.gson.*
import net.jidb.to.base.api.util.JsonSerializable

val DATA_GSON = GsonBuilder().setLenient().setPrettyPrinting().create()

fun <T> JsonArray.add(obj: T, serialiser: (T) -> JsonElement) = this.add(serialiser(obj))
fun JsonArray.add(obj: JsonSerializable) = this.add(obj, JsonSerializable::toJson)

fun <T> JsonObject.add(key: String, obj: T, serialiser: (T) -> JsonElement) = this.add(key, serialiser(obj))
fun JsonObject.add(key: String, obj: JsonSerializable) = this.add(key, obj, JsonSerializable::toJson)

fun JsonObject.merge(obj: JsonObject) = obj.keySet().forEach { if (!this.has(it)) this.add(it, obj.get(it)) }
fun JsonObject.replace(obj: JsonObject) = obj.keySet().forEach { this.add(it, obj.get(it)) }

fun JsonObject.getBool(key: String) = getAsJsonPrimitive(key)?.asBoolean
fun JsonObject.getNumber(key: String) = getAsJsonPrimitive(key)?.asNumber
fun JsonObject.getString(key: String) = getAsJsonPrimitive(key)?.asString
fun JsonObject.getChar(key: String) = getAsJsonPrimitive(key)?.asCharacter
fun JsonObject.addBool(key: String, value: Boolean) = addProperty(key, value)
fun JsonObject.addNumber(key: String, value: Number) = addProperty(key, value)
fun JsonObject.addString(key: String, value: String) = addProperty(key, value)
fun JsonObject.addChar(key: String, value: Char) = addProperty(key, value)

fun <I : Any, O: Any> JsonObject.modify(key: String, getter: JsonObject.(key: String) -> I?, adder: JsonObject.(key: String, value: O) -> Unit, default: I, m: (I) -> O?) = m(getter(this, key) ?: default)?.also { adder(key, it) }
fun JsonObject.modifyBool(key: String, default: Boolean = false, m: (Boolean) -> Boolean?) = modify(key, JsonObject::getBool, JsonObject::addBool, default, m)
fun JsonObject.modifyNumber(key: String, default: Number, m: (Number) -> Number?) = modify(key, JsonObject::getNumber, JsonObject::addNumber, default, m)
fun JsonObject.modifyString(key: String, default: String = "", m: (String) -> String?) = modify(key, JsonObject::getString, JsonObject::addString, default, m)
fun JsonObject.modifyChar(key: String, default: Char, m: (Char) -> Char?) = modify(key, JsonObject::getChar, JsonObject::addChar, default, m)
fun JsonObject.modifyElement(key: String, default: JsonElement, m: (JsonElement) -> JsonElement?) = modify(key, JsonObject::get, JsonObject::add, default, m)
fun JsonObject.modifyObject(key: String, default: JsonObject = JsonObject(), m: (JsonObject) -> JsonObject?) = modify(key, JsonObject::getAsJsonObject, JsonObject::add, default, m)
fun JsonObject.modifyArray(key: String, default: JsonArray = JsonArray(), m: (JsonArray) -> JsonArray?) = modify(key, JsonObject::getAsJsonArray, JsonObject::add, default, m)
fun JsonObject.modifyPrimitive(key: String, default: JsonPrimitive, m: (JsonPrimitive) -> JsonPrimitive?) = modify(key, JsonObject::getAsJsonPrimitive, JsonObject::add, default, m)

fun JsonObject.getOrCreateObject(key: String, obj: JsonObject = JsonObject(), modify: (JsonObject) -> Unit = {}): JsonObject {
    val get = this.get(key)
    if (get is JsonObject) return get.also(modify)
    val new = obj.also(modify)
    this.add(key, new)
    return new
}

fun <T> JsonObject.getOrPut(key: String, default: T, getter: JsonObject.(key: String) -> T?, adder: JsonObject.(key: String, value: T) -> Unit): T {
    if (has(key)) {
        return getter(key)!!
    }
    adder(key, default)
    return default
}
fun JsonObject.getOrPut(key: String, default: Boolean) = getOrPut(key, default, JsonObject::getBool, JsonObject::addProperty)
fun JsonObject.getOrPut(key: String, default: Number) = getOrPut(key, default, JsonObject::getNumber, JsonObject::addProperty)
fun JsonObject.getOrPut(key: String, default: String) = getOrPut(key, default, JsonObject::getString, JsonObject::addProperty)
fun JsonObject.getOrPut(key: String, default: Char) = getOrPut(key, default, JsonObject::getChar, JsonObject::addProperty)
fun JsonObject.getOrPut(key: String, default: JsonElement) = getOrPut(key, default, JsonObject::get, JsonObject::add)
fun JsonObject.getOrPut(key: String, default: JsonObject) = getOrPut(key, default, JsonObject::getAsJsonObject, JsonObject::add)
fun JsonObject.getOrPut(key: String, default: JsonArray) = getOrPut(key, default, JsonObject::getAsJsonArray, JsonObject::add)
fun JsonObject.getOrPut(key: String, default: JsonPrimitive) = getOrPut(key, default, JsonObject::getAsJsonPrimitive, JsonObject::add)

fun JsonArray.addBools(elements: Array<out Boolean>) = elements.forEach(this::add)
fun JsonArray.addNumbers(elements: Array<out Number>) = elements.forEach(this::add)
fun JsonArray.addStrings(elements: Array<out String>) = elements.forEach(this::add)
fun JsonArray.addChars(elements: Array<out Char>) = elements.forEach(this::add)
fun JsonArray.addElements(elements: Array<out JsonElement>) = elements.forEach(this::add)
fun <T> JsonArray.addSerialisable(elements: Array<out T>, serialiser: (T) -> JsonElement) = elements.forEach { this.add(it, serialiser) }
fun JsonArray.addSerialisable(elements: Array<out JsonSerializable>) = elements.forEach { this.add(it, JsonSerializable::toJson) }

fun JsonObject.addBools(elements: Map<String, Boolean>) = elements.forEach { (k, v) -> this.addProperty(k, v) }
fun JsonObject.addNumbers(elements: Map<String, Number>) = elements.forEach { (k, v) -> this.addProperty(k, v) }
fun JsonObject.addStrings(elements: Map<String, String>) = elements.forEach { (k, v) -> this.addProperty(k, v) }
fun JsonObject.addChars(elements: Map<String, Char>) = elements.forEach { (k, v) -> this.addProperty(k, v) }
fun JsonObject.addElements(elements: Map<String, JsonElement>) = elements.forEach { (k, v) -> this.add(k, v) }
fun <T> JsonObject.addSerialisable(elements: Map<String, T>, serialiser: (T) -> JsonElement) = elements.forEach { (k, v) -> this.add(k, v, serialiser) }
fun JsonObject.addSerialisable(elements: Map<String, JsonSerializable>) = elements.forEach { (k, v) -> this.add(k, v, JsonSerializable::toJson) }

private fun <J : JsonElement, T> J.addObjectTemplate(obj: Map<String, T>, add: J.(JsonObject) -> Unit, addAll: JsonObject.(Map<String, T>) -> Unit) = add(this, JsonObject().also { addAll(it, obj) })

fun JsonArray.addBoolObject(obj: Map<String, Boolean>) = this.addObjectTemplate(obj, JsonArray::add, JsonObject::addBools)
fun JsonArray.addNumberObject(obj: Map<String, Number>) = this.addObjectTemplate(obj, JsonArray::add, JsonObject::addNumbers)
fun JsonArray.addStringObject(obj: Map<String, String>) = this.addObjectTemplate(obj, JsonArray::add, JsonObject::addStrings)
fun JsonArray.addCharObject(obj: Map<String, Char>) = this.addObjectTemplate(obj, JsonArray::add, JsonObject::addChars)
fun JsonArray.addElementObject(obj: Map<String, JsonElement>) = this.addObjectTemplate(obj, JsonArray::add, JsonObject::addElements)
fun <T> JsonArray.addSerialisableObject(obj: Map<String, T>, serialiser: (T) -> JsonElement) = this.addObjectTemplate(obj, JsonArray::add) { this.addSerialisable(it, serialiser) }
fun JsonArray.addSerialisableObject(obj: Map<String, JsonSerializable>) = this.addObjectTemplate(obj, JsonArray::add, JsonObject::addSerialisable)

fun JsonObject.addBoolObject(key: String, obj: Map<String, Boolean>) = this.addObjectTemplate(obj, { this.add(key, it) }, JsonObject::addBools)
fun JsonObject.addNumberObject(key: String, obj: Map<String, Number>) = this.addObjectTemplate(obj, { this.add(key, it) }, JsonObject::addNumbers)
fun JsonObject.addStringObject(key: String, obj: Map<String, String>) = this.addObjectTemplate(obj, { this.add(key, it) }, JsonObject::addStrings)
fun JsonObject.addCharObject(key: String, obj: Map<String, Char>) = this.addObjectTemplate(obj, { this.add(key, it) }, JsonObject::addChars)
fun JsonObject.addElementObject(key: String, obj: Map<String, JsonElement>) = this.addObjectTemplate(obj, { this.add(key, it) }, JsonObject::addElements)
fun <T> JsonObject.addSerialisableObject(key: String, obj: Map<String, T>, serialiser: (T) -> JsonElement) = this.addObjectTemplate(obj, { this.add(key, it) }) { this.addSerialisable(it, serialiser) }
fun JsonObject.addSerialisableObject(key: String, obj: Map<String, JsonSerializable>) = this.addObjectTemplate(obj, { this.add(key, it) }, JsonObject::addSerialisable)

private fun <J : JsonElement, T> J.addArrayTemplate(elements: Array<T>, add: J.(JsonArray) -> Unit, addAll: JsonArray.(Array<T>) -> Unit) = add(this, JsonArray().also { addAll(it, elements) })

fun JsonArray.addBoolArray(elements: Array<out Boolean>) = this.addArrayTemplate(elements, JsonArray::add) { addBools(it) }
fun JsonArray.addNumberArray(elements: Array<out Number>) = this.addArrayTemplate(elements, JsonArray::add) { addNumbers(it) }
fun JsonArray.addStringArray(elements: Array<out String>) = this.addArrayTemplate(elements, JsonArray::add) { addStrings(it) }
fun JsonArray.addCharArray(elements: Array<out Char>) = this.addArrayTemplate(elements, JsonArray::add) { addChars(it) }
fun JsonArray.addElementArray(elements: Array<out JsonElement>) = this.addArrayTemplate(elements, JsonArray::add) { addElements(it) }
fun <T> JsonArray.addSerialisableArray(elements: Array<out T>, serialiser: (T) -> JsonElement) = this.addArrayTemplate(elements, JsonArray::add) { addSerialisable(it, serialiser = serialiser) }
fun JsonArray.addSerialisableArray(elements: Array<out JsonSerializable>) = this.addArrayTemplate(elements, JsonArray::add) { addSerialisable(it) }

fun JsonObject.addBoolArray(key: String, elements: Array<out Boolean>) = this.addArrayTemplate(elements, { this.add(key, it) }) { addBools(it) }
fun JsonObject.addNumberArray(key: String, elements: Array<out Number>) = this.addArrayTemplate(elements, { this.add(key, it) }) { addNumbers(it) }
fun JsonObject.addStringArray(key: String, elements: Array<out String>) = this.addArrayTemplate(elements, { this.add(key, it) }) { addStrings(it) }
fun JsonObject.addCharArray(key: String, elements: Array<out Char>) = this.addArrayTemplate(elements, { this.add(key, it) }) { addChars(it) }
fun JsonObject.addElementArray(key: String, elements: Array<out JsonElement>) = this.addArrayTemplate(elements, { this.add(key, it) }) { addElements(it) }
fun <T> JsonObject.addSerialisableArray(key: String, elements: Array<out T>, serialiser: (T) -> JsonElement) = this.addArrayTemplate(elements, { this.add(key, it) }) { addSerialisable(it, serialiser = serialiser) }
fun JsonObject.addSerialisableArray(key: String, elements: Array<out JsonSerializable>) = this.addArrayTemplate(elements, { this.add(key, it) }) { addSerialisable(it) }

private fun <J : JsonElement, T : Any> J.addOrNullTemplate(obj: T?, add: (T) -> Unit) = obj?.also(add)

fun JsonArray.addOrNull(obj: Boolean?) = this.addOrNullTemplate(obj, this::add)
fun JsonArray.addOrNull(obj: Number?) = this.addOrNullTemplate(obj, this::add)
fun JsonArray.addOrNull(obj: String?) = this.addOrNullTemplate(obj, this::add)
fun JsonArray.addOrNull(obj: Char?) = this.addOrNullTemplate(obj, this::add)
fun JsonArray.addOrNull(obj: JsonElement?) = this.addOrNullTemplate(obj, this::add)
fun <T : Any> JsonArray.addOrNull(obj: T?, serialiser: (T) -> JsonElement) = this.addOrNullTemplate(obj) { this.add(it, serialiser) }
fun JsonArray.addOrNull(obj: JsonSerializable?) = this.addOrNullTemplate(obj, this::add)

fun JsonObject.addOrNull(key: String, obj: Boolean?) = this.addOrNullTemplate(obj) { this.addProperty(key, it) }
fun JsonObject.addOrNull(key: String, obj: Number?) = this.addOrNullTemplate(obj) { this.addProperty(key, it) }
fun JsonObject.addOrNull(key: String, obj: String?) = this.addOrNullTemplate(obj) { this.addProperty(key, it) }
fun JsonObject.addOrNull(key: String, obj: Char?) = this.addOrNullTemplate(obj) { this.addProperty(key, it) }
fun JsonObject.addOrNull(key: String, obj: JsonElement?) = this.addOrNullTemplate(obj) { this.add(key, it) }
fun <T : Any> JsonObject.addOrNull(key: String, obj: T?, serialiser: (T) -> JsonElement) = this.addOrNullTemplate(obj) { this.add(key, it, serialiser) }
fun JsonObject.addOrNull(key: String, obj: JsonSerializable?) = this.addOrNullTemplate(obj) { this.add(key, it) }