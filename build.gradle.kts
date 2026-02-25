plugins {
    // see https://fabricmc.net/develop/ for new versions
    alias(libs.plugins.loom) apply false
    // see https://projects.neoforged.net/neoforged/moddevgradle for new versions
    alias(libs.plugins.moddev) apply false
}

data class NamespaceProps(
    val group: String,
    val version: String,
    val modId: String,
    val modName: String,
    val description: String?
)

fun parseSimpleToml(file: File): Map<String, String> {
    val result = mutableMapOf<String, String>()
    file.readLines().forEach { line ->
        val trimmed = line.substringBefore('#').trim()
        if (trimmed.isEmpty()) return@forEach
        val idx = trimmed.indexOf('=')
        if (idx <= 0) return@forEach
        val key = trimmed.substring(0, idx).trim()
        var raw = trimmed.substring(idx + 1).trim()
        if ((raw.startsWith('"') && raw.endsWith('"')) || (raw.startsWith('\'') && raw.endsWith('\''))) {
            raw = raw.substring(1, raw.length - 1)
        }
        result[key] = raw
    }
    return result
}

val namespacePropsCache = mutableMapOf<String, NamespaceProps?>()

fun loadNamespaceProps(namespace: String): NamespaceProps? {
    return namespacePropsCache.getOrPut(namespace) {
        val file = rootProject.file("$namespace/properties.toml")
        if (!file.exists()) return@getOrPut null
        val map = parseSimpleToml(file)
        val group = map["group"] ?: return@getOrPut null
        val version = map["version"] ?: return@getOrPut null
        val modId = map["modId"] ?: return@getOrPut null
        val modName = map["modName"] ?: return@getOrPut null
        val description = map["description"]
        NamespaceProps(group, version, modId, modName, description)
    }
}

subprojects {
    val namespace = path.substring(1).substringBefore(':')
    val props = loadNamespaceProps(namespace) ?: return@subprojects
    group = props.group
    version = props.version
    extra["modId"] = props.modId
    extra["modName"] = props.modName
    props.description?.let { description = it }
}

allprojects {
    val props = loadNamespaceProps("to_base") ?: return@allprojects
    extra["toBaseVersion"] = props.version
}