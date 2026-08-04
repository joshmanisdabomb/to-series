package net.jidb.to.meta.detekt.doc

import dev.detekt.api.Config
import dev.detekt.api.Configuration
import dev.detekt.api.Entity
import dev.detekt.api.Finding
import dev.detekt.api.Rule
import dev.detekt.api.config
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.KtEnumEntry
import org.jetbrains.kotlin.psi.KtNamedDeclaration
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtSecondaryConstructor
import org.jetbrains.kotlin.psi.KtTypeAlias
import org.jetbrains.kotlin.psi.psiUtil.containingClassOrObject

/**
 * Requires a `@since` tag on every documented declaration, naming the version it first appeared in.
 *
 * This is a library convention rather than a language one, so unlike the other KDoc rules it is not applied everywhere: the `includes` filter in detekt.yml narrows it to the to_base sources, whose declarations are the published API that dependent mods compile against.
 * Detekt has no built-in rule for it - comments>OutdatedDocumentation only checks the tags a declaration already has.
 *
 * Only a declaration that already carries KDoc is checked, matching [MissingKDocTags]. A declaration with no KDoc at all is reported once by [UndocumentedType], [UndocumentedFunction] or [UndocumentedProperty], rather than a second time here.
 */
class MissingSinceTag(config: Config) : Rule(config, "KDoc must name the version a declaration first appeared in with @since.") {

    @Configuration("the form a version has to take. Matched against the whole tag text, so '.*' accepts anything and checks only that the tag has one.")
    private val versionPattern: Regex by config("\\d+\\.\\d+\\.\\d+") { it.toRegex() }

    @Configuration("a declaration that overrides a supertype declaration appeared with the supertype's")
    private val ignoreOverridden: Boolean by config(true)

    @Configuration("an anonymous object expression has no name to document and is not checked")
    private val ignoreObjectLiterals: Boolean by config(true)

    @Configuration("an unnamed 'companion object' is a language construct rather than a type")
    private val ignoreDefaultCompanionObject: Boolean by config(true)

    @Configuration("a declaration inside a function body is not part of any API")
    private val ignoreLocalDeclarations: Boolean by config(true)

    @Configuration("enum entries are versioned like any other constant")
    private val ignoreEnumEntries: Boolean by config(false)

    override fun visitClassOrObject(classOrObject: KtClassOrObject) {
        super.visitClassOrObject(classOrObject)

        //An enum entry is a KtClass, and is left to visitEnumEntry below so that ignoreEnumEntries covers it.
        if (classOrObject is KtEnumEntry) return
        if (ignoreObjectLiterals && classOrObject is KtObjectDeclaration && classOrObject.isObjectLiteral()) return
        if (ignoreDefaultCompanionObject && KDocSupport.isDefaultCompanionObject(classOrObject)) return
        checkSince(classOrObject)
    }

    override fun visitTypeAlias(typeAlias: KtTypeAlias) {
        super.visitTypeAlias(typeAlias)

        checkSince(typeAlias)
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)

        if (ignoreLocalDeclarations && function.isLocal) return
        checkSince(function)
    }

    override fun visitSecondaryConstructor(constructor: KtSecondaryConstructor) {
        super.visitSecondaryConstructor(constructor)

        //A constructor has no name of its own, so it is named after the type it builds and located by its signature.
        checkSince(constructor, Entity.from(constructor), "the secondary constructor of ${constructor.containingClassOrObject?.name}")
    }

    override fun visitProperty(property: KtProperty) {
        super.visitProperty(property)

        if (ignoreLocalDeclarations && property.isLocal) return
        checkSince(property)
    }

    override fun visitEnumEntry(enumEntry: KtEnumEntry) {
        super.visitEnumEntry(enumEntry)

        if (ignoreEnumEntries) return
        checkSince(enumEntry)
    }

    private fun checkSince(declaration: KtNamedDeclaration) {
        checkSince(declaration, Entity.atName(declaration), declaration.name ?: return)
    }

    /**
     * Reports [declaration] unless its KDoc carries a `@since` naming a version.
     * A declaration with no KDoc at all is left to the rule that requires the KDoc in the first place.
     */
    private fun checkSince(declaration: KtDeclaration, entity: Entity, subject: String) {
        if (KDocSupport.isExempt(declaration, ignoreOverridden)) return
        val kDoc = declaration.docComment ?: return

        val tag = KDocSupport.sinceTag(kDoc)
        if (tag == null) {
            report(Finding(entity, "KDoc of $subject is missing @since."))
            return
        }

        val version = tag.getContent().trim()
        if (!versionPattern.matches(version)) {
            val found = if (version.isEmpty()) "no version" else "'$version'"
            report(Finding(entity, "@since of $subject names $found, which is not a version of the form ${versionPattern.pattern}."))
        }
    }
}