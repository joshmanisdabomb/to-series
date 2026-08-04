package net.jidb.to.meta.detekt.doc

import dev.detekt.api.Config
import dev.detekt.api.Configuration
import dev.detekt.api.Entity
import dev.detekt.api.Finding
import dev.detekt.api.Rule
import dev.detekt.api.config
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtNamedDeclaration
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtSecondaryConstructor
import org.jetbrains.kotlin.psi.KtTypeAlias
import org.jetbrains.kotlin.psi.KtTypeParameterListOwner
import org.jetbrains.kotlin.psi.KtUserType

/**
 * Requires a `@param` tag for every parameter and a `@return` tag on every function that returns a value.
 *
 * This is the half of checkstyle's JavadocMethod and JavadocType that Detekt has no rule for.
 * Detekt's comments>OutdatedDocumentation only checks the other direction - a `@param` for a parameter that no longer exists, or tags in the wrong order - and stays silent on KDoc with no tags at all, so the two rules are complementary and both are on.
 *
 * Only a declaration that already carries KDoc is checked. A declaration with none at all is reported once by [UndocumentedType], [UndocumentedFunction] or [UndocumentedProperty], rather than once per missing tag.
 */
class MissingKDocTags(config: Config) : Rule(config, "KDoc must document every parameter, and the return value of every function that has one.") {

    @Configuration("a `@param` is required for each value parameter")
    private val checkParameters: Boolean by config(true)

    @Configuration("a `@param` is required for each type parameter")
    private val checkTypeParameters: Boolean by config(true)

    @Configuration("a `@return` is required on a function that returns a value")
    private val checkReturn: Boolean by config(true)

    @Configuration("a `@return` is required on an expression-bodied function with no declared return type. Detekt runs without type resolution, so such a function is assumed to return a value unless its body is literally Unit.")
    private val checkInferredReturn: Boolean by config(true)

    @Configuration("a function that overrides a supertype declaration inherits its documentation")
    private val ignoreOverridden: Boolean by config(true)

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)

        if (function.isLocal) return
        if (KDocSupport.isExempt(function, ignoreOverridden)) return
        val kDoc = function.docComment ?: return

        val documented = KDocSupport.documentedSubjects(kDoc)
        if (checkParameters) {
            reportMissing(function, function.valueParameters.mapNotNull { it.name } - documented, "parameter")
        }
        checkTypeParameters(function, documented)

        if (checkReturn && requiresReturnTag(function) && !KDocSupport.hasReturnTag(kDoc)) {
            report(Finding(Entity.atName(function), "KDoc of ${function.name} is missing @return."))
        }
    }

    override fun visitSecondaryConstructor(constructor: KtSecondaryConstructor) {
        super.visitSecondaryConstructor(constructor)

        val kDoc = constructor.docComment ?: return
        if (!checkParameters) return

        val documented = KDocSupport.documentedSubjects(kDoc)
        reportMissing(constructor, constructor.valueParameters.mapNotNull { it.name } - documented, "parameter")
    }

    override fun visitClassOrObject(classOrObject: KtClassOrObject) {
        super.visitClassOrObject(classOrObject)

        val kDoc = classOrObject.docComment ?: return
        val documented = KDocSupport.documentedSubjects(kDoc)

        //The primary constructor has no KDoc of its own, its parameters are documented on the class regardless of whether they also declare a property.
        if (checkParameters) {
            val parameters = classOrObject.primaryConstructorParameters.mapNotNull { it.name }
            reportMissing(classOrObject, parameters - documented, "constructor parameter")
        }
        checkTypeParameters(classOrObject, documented)
    }

    override fun visitTypeAlias(typeAlias: KtTypeAlias) {
        super.visitTypeAlias(typeAlias)

        val kDoc = typeAlias.docComment ?: return
        checkTypeParameters(typeAlias, KDocSupport.documentedSubjects(kDoc))
    }

    private fun checkTypeParameters(owner: KtTypeParameterListOwner, documented: Set<String>) {
        if (!checkTypeParameters) return
        reportMissing(owner, owner.typeParameters.mapNotNull { it.name } - documented, "type parameter")
    }

    private fun reportMissing(declaration: KtNamedDeclaration, missing: List<String>, kind: String) {
        if (missing.isEmpty()) return

        val subject = declaration.name ?: return
        val plural = if (missing.size == 1) kind else "${kind}s"
        report(
            Finding(
                Entity.atName(declaration),
                "KDoc of $subject is missing @param for $plural ${missing.joinToString(", ")}.",
            ),
        )
    }

    /**
     * Whether [function] returns something worth documenting. A declared type answers it outright.
     * Without one, a block body returns Unit, and an expression body has a type only the compiler knows - see [checkInferredReturn].
     */
    private fun requiresReturnTag(function: KtNamedFunction): Boolean {
        val declared = function.typeReference
        if (declared != null) {
            val name = (declared.typeElement as? KtUserType)?.referencedName
            return name != "Unit" && name != "Nothing"
        }
        if (function.hasBlockBody() || !function.hasBody()) return false
        return checkInferredReturn && function.bodyExpression?.text != "Unit"
    }
}
