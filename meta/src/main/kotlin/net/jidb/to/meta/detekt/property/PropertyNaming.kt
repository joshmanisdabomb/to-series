package net.jidb.to.meta.detekt.property

import dev.detekt.api.Config
import dev.detekt.api.Configuration
import dev.detekt.api.Entity
import dev.detekt.api.Finding
import dev.detekt.api.Rule
import dev.detekt.api.config
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtNamedDeclaration
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtUserType
import org.jetbrains.kotlin.psi.psiUtil.containingClassOrObject
import org.jetbrains.kotlin.psi.psiUtil.parents

/**
 * Requires every property to be named in camelCase.
 *
 * This owns all property naming, which is why the built-in naming>VariableNaming, naming>ObjectPropertyNaming and naming>TopLevelPropertyNaming are disabled in Detekt config.
 * Their patterns can be tightened to camelCase, but none of them can make an exception for the library types.
 * VariableNaming has an excludeClassPattern but never sees an object's properties, and ObjectPropertyNaming has no equivalent option.
 */
class PropertyNaming(config: Config) : Rule(config, "Property names must be camelCase.") {

    @Configuration("pattern a property name must match")
    private val propertyPattern: Regex by config("[a-z][A-Za-z0-9]*") { it.toRegex() }

    @Configuration("pattern a private property name must match")
    private val privatePropertyPattern: Regex by config("(_)?[a-z][A-Za-z0-9]*") { it.toRegex() }

    @Configuration("pattern a constant name must match")
    private val constantPattern: Regex by config("[a-z][A-Za-z0-9]*|[A-Z][_A-Z0-9]*") { it.toRegex() }

    @Configuration("types whose own name or supertype name matches are not checked")
    private val excludeTypePattern: Regex by config(".*Library.*") { it.toRegex() }

    @Configuration("properties that override a supertype declaration are not checked")
    private val ignoreOverridden: Boolean by config(true)

    @Configuration("a val at the top level or in an object counts as a constant without the const keyword")
    private val treatObjectValsAsConstants: Boolean by config(false)

    override fun visitProperty(property: KtProperty) {
        super.visitProperty(property)

        check(property, isConstantLike(property))
    }

    override fun visitParameter(parameter: KtParameter) {
        super.visitParameter(parameter)

        //Only a primary constructor val/var declares a property; a plain parameter is named by naming>ConstructorParameterNaming and naming>FunctionParameterNaming instead.
        if (!parameter.hasValOrVar()) return
        check(parameter, false)
    }

    private fun check(declaration: KtNamedDeclaration, isConstant: Boolean) {
        if (ignoreOverridden && declaration.hasModifier(KtTokens.OVERRIDE_KEYWORD)) return
        if (isInExcludedType(declaration)) return

        //A name that collides with a keyword is written in backticks, e.g. `val \`object\``, and it is the identifier inside them that has to be camelCase.
        val name = declaration.nameIdentifier?.text?.removeSurrounding("`") ?: return
        //A constant may be spelled either camelCase or capitalised snake_case.
        val patterns = buildList {
            if (declaration.hasModifier(KtTokens.PRIVATE_KEYWORD)) add(privatePropertyPattern) else add(propertyPattern)
            if (isConstant) add(constantPattern)
        }
        if (patterns.any { it.matches(name) }) return

        report(
            Finding(
                Entity.from(declaration),
                "Property $name should match the pattern: ${patterns.joinToString(" or ") { it.pattern }}",
            ),
        )
    }

    /**
     * Whether [property] may take the [constantPattern] spelling.
     *
     * [treatObjectValsAsConstants] restores the looser reading, under which a val declared at the top level or directly in an object or companion object, with no custom getter, is treated as a constant regardless of whether it could be one.
     * That is what Kotlin's style guide permits and what vanilla's own fields look like, so it stays available, but it is off by default.
     */
    private fun isConstantLike(property: KtProperty): Boolean {
        if (property.hasModifier(KtTokens.CONST_KEYWORD)) return true
        if (!treatObjectValsAsConstants) return false
        if (property.isVar || property.isLocal) return false
        if (property.getter?.hasBody() == true) return false
        val container = property.containingClassOrObject
        return container == null || container is KtObjectDeclaration
    }

    private fun isInExcludedType(declaration: KtNamedDeclaration): Boolean =
        declaration.parents.filterIsInstance<KtClassOrObject>().any(::isExcludedType)

    private fun isExcludedType(type: KtClassOrObject): Boolean {
        val name = type.name
        if (name != null && excludeTypePattern.matches(name)) return true
        return type.superTypeListEntries.any {
            val supertype = (it.typeReference?.typeElement as? KtUserType)?.referencedName
            supertype != null && excludeTypePattern.matches(supertype)
        }
    }
}
