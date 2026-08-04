package net.jidb.to.meta.detekt.brace

import dev.detekt.api.Config
import dev.detekt.api.Configuration
import dev.detekt.api.Entity
import dev.detekt.api.Finding
import dev.detekt.api.Rule
import dev.detekt.api.config
import org.jetbrains.kotlin.psi.KtClassOrObject

/**
 * Requires a blank line between the opening brace of a type body and its first member.
 *
 * This is the Kotlin counterpart of the TypeOpenBraceBlankLine rule in Checkstyle. Detekt has no built-in equivalent or regex support.
 */
class TypeOpenBraceBlankLine(config: Config) : Rule(config, "The opening brace of a type body must be followed by a blank line.") {

    @Configuration("companion objects are written without padding and are not checked")
    private val ignoreCompanionObjects: Boolean by config(true)

    @Configuration("enum classes and their entry bodies are written without padding and are not checked")
    private val ignoreEnumClasses: Boolean by config(true)

    @Configuration("anonymous object expressions are written without padding and are not checked")
    private val ignoreObjectLiterals: Boolean by config(true)

    override fun visitClassOrObject(classOrObject: KtClassOrObject) {
        super.visitClassOrObject(classOrObject)

        if (!TypeBraceSupport.isPaddedKind(classOrObject, ignoreCompanionObjects, ignoreEnumClasses, ignoreObjectLiterals)) {
            return
        }

        val body = classOrObject.body ?: return
        if (!TypeBraceSupport.isPaddable(body)) return

        val lBrace = body.lBrace ?: return
        if (TypeBraceSupport.isBlankLine(lBrace.nextSibling)) return

        report(
            Finding(
                Entity.from(lBrace),
                "Opening brace of a type must be followed by a blank line.",
            ),
        )
    }
}
