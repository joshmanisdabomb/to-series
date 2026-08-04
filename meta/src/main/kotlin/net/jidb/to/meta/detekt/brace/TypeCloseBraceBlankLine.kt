package net.jidb.to.meta.detekt.brace

import dev.detekt.api.Config
import dev.detekt.api.Configuration
import dev.detekt.api.Entity
import dev.detekt.api.Finding
import dev.detekt.api.Rule
import dev.detekt.api.config
import org.jetbrains.kotlin.psi.KtClassOrObject

/**
 * Requires a blank line between the last member of a type body and its closing brace.
 *
 * This is the Kotlin counterpart of the TypeCloseBraceBlankLine rule in Checkstyle. Detekt has no built-in equivalent or regex support.
 */
class TypeCloseBraceBlankLine(config: Config) : Rule(config, "The closing brace of a type body must be preceded by a blank line.") {

    @Configuration("companion objects are written without padding and are not checked")
    private val ignoreCompanionObjects: Boolean by config(true)

    @Configuration("enum classes and their entry bodies are written without padding and are not checked")
    private val ignoreEnumClasses: Boolean by config(true)

    @Configuration("anonymous object expressions are written without padding and are not checked")
    private val ignoreObjectLiterals: Boolean by config(true)

    override fun visitClassOrObject(classOrObject: KtClassOrObject) {
        super.visitClassOrObject(classOrObject)

        if (!TypeBraceSupport.isPaddedKind(
                classOrObject,
                ignoreCompanionObjects,
                ignoreEnumClasses,
                ignoreObjectLiterals,
            )
        ) {
            return
        }

        val body = classOrObject.body ?: return
        if (!TypeBraceSupport.isPaddable(body)) return

        val rBrace = body.rBrace ?: return
        if (TypeBraceSupport.isBlankLine(rBrace.prevSibling)) return

        report(
            Finding(
                Entity.from(rBrace),
                "Closing brace of a type must be preceded by a blank line.",
            ),
        )
    }
}
