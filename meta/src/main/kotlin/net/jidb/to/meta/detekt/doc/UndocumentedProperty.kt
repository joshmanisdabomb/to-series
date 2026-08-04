package net.jidb.to.meta.detekt.doc

import dev.detekt.api.Config
import dev.detekt.api.Configuration
import dev.detekt.api.Entity
import dev.detekt.api.Finding
import dev.detekt.api.Rule
import dev.detekt.api.config
import org.jetbrains.kotlin.psi.KtEnumEntry
import org.jetbrains.kotlin.psi.KtNamedDeclaration
import org.jetbrains.kotlin.psi.KtProperty

/**
 * Requires KDoc on every property and enum entry, for any visibility.
 *
 * This is the Kotlin counterpart of the JavadocVariable rule in Checkstyle which covers fields and enum constants.
 * Detekt's comments>UndocumentedPublicProperty only ever looks at public and protected properties.
 * A primary constructor property is documented by a `@param` or `@property` tag on the class rather than by KDoc of its own, left to MissingKDocTags.
 */
class UndocumentedProperty(config: Config) : Rule(config, "Properties must be documented with KDoc.") {

    @Configuration("a property that overrides a supertype declaration inherits its documentation")
    private val ignoreOverridden: Boolean by config(true)

    @Configuration("a local variable is not a field and has no checkstyle counterpart")
    private val ignoreLocalProperties: Boolean by config(true)

    @Configuration("enum entries are documented like any other constant")
    private val ignoreEnumEntries: Boolean by config(false)

    override fun visitProperty(property: KtProperty) {
        super.visitProperty(property)

        if (ignoreLocalProperties && property.isLocal) return
        if (KDocSupport.isExempt(property, ignoreOverridden)) return
        checkDocumented(property)
    }

    override fun visitEnumEntry(enumEntry: KtEnumEntry) {
        super.visitEnumEntry(enumEntry)

        if (ignoreEnumEntries) return
        checkDocumented(enumEntry)
    }

    private fun checkDocumented(declaration: KtNamedDeclaration) {
        if (declaration.docComment != null) return

        report(
            Finding(
                Entity.atName(declaration),
                "${declaration.name} is missing KDoc.",
            ),
        )
    }
}
