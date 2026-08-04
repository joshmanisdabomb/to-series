package net.jidb.to.meta.detekt.doc

import dev.detekt.api.Config
import dev.detekt.api.Configuration
import dev.detekt.api.Entity
import dev.detekt.api.Finding
import dev.detekt.api.Rule
import dev.detekt.api.config
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtEnumEntry
import org.jetbrains.kotlin.psi.KtNamedDeclaration
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.KtTypeAlias

/**
 * Requires KDoc on every class, interface, object, and type alias, for any visibility.
 *
 * This is the Kotlin counterpart of the JavadocType rule in Checkstyle.
 * Detekt's comments>UndocumentedPublicClass only ever looks at public and protected declarations.
 */
class UndocumentedType(config: Config) : Rule(config, "Types must be documented with KDoc.") {

    @Configuration("an anonymous object expression has no name to document and is not checked")
    private val ignoreObjectLiterals: Boolean by config(true)

    @Configuration("an unnamed 'companion object' is a language construct rather than a type")
    private val ignoreDefaultCompanionObject: Boolean by config(true)

    override fun visitClassOrObject(classOrObject: KtClassOrObject) {
        super.visitClassOrObject(classOrObject)

        //An enum entry is a KtClass, but Checkstyle counts an enum constant as a field, so here we leave it to UndocumentedProperty to report.
        if (classOrObject is KtEnumEntry) return
        if (ignoreObjectLiterals && classOrObject is KtObjectDeclaration && classOrObject.isObjectLiteral()) return
        if (ignoreDefaultCompanionObject && KDocSupport.isDefaultCompanionObject(classOrObject)) return
        checkDocumented(classOrObject)
    }

    override fun visitTypeAlias(typeAlias: KtTypeAlias) {
        super.visitTypeAlias(typeAlias)

        checkDocumented(typeAlias)
    }

    private fun checkDocumented(declaration: KtNamedDeclaration) {
        if (declaration.docComment != null) return

        report(
            Finding(
                Entity.atName(declaration),
                "${declaration.name ?: declaration.text} is missing KDoc.",
            ),
        )
    }
}
