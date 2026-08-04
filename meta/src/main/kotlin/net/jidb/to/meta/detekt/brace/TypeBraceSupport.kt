package net.jidb.to.meta.detekt.brace

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtClassBody
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtEnumEntry
import org.jetbrains.kotlin.psi.KtObjectDeclaration

/**
 * Shared code for [TypeOpenBraceBlankLine] and [TypeCloseBraceBlankLine].
 */
internal object TypeBraceSupport {

    /**
     * A body written entirely on one line, and a body with nothing in it, are both left alone.
     * This matches the Java rules, whose regexes only fire once a newline follows the opening brace.
     *
     * @param body The type declaration.
     * @return Whether the body is written on multiple lines and has content.
     */
    fun isPaddable(body: KtClassBody): Boolean {
        if (body.declarations.isEmpty() && body.danglingAnnotations.isEmpty()) return false
        val lBrace = body.lBrace ?: return false
        val rBrace = body.rBrace ?: return false
        return body.text.substring(
            lBrace.startOffsetInParent,
            rBrace.startOffsetInParent,
        ).contains('\n')
    }

    /**
     * Whether the declaration is one of the kinds that carries padding.
     * Companion objects, enum classes and anonymous object expressions were written without it throughout the sources, so they can be optionally enabled.
     *
     * @param declaration The type declaration.
     * @param ignoreCompanionObjects Whether to ignore companion objects.
     * @param ignoreEnumClasses Whether to ignore enum classes.
     * @param ignoreObjectLiterals Whether to ignore object literals.
     * @return Whether the declaration should be padded.
     */
    fun isPaddedKind(
        declaration: KtClassOrObject,
        ignoreCompanionObjects: Boolean,
        ignoreEnumClasses: Boolean,
        ignoreObjectLiterals: Boolean,
    ): Boolean {
        if (declaration is KtObjectDeclaration) {
            if (ignoreCompanionObjects && declaration.isCompanion()) return false
            if (ignoreObjectLiterals && declaration.isObjectLiteral()) return false
        }
        //An enum entry with a body is a KtClass too, and is covered by the same option: the entry bodies in the sources are written without padding, just as the enums holding them are.
        if (ignoreEnumClasses && declaration is KtEnumEntry) return false
        if (ignoreEnumClasses && declaration is KtClass && declaration.isEnum()) return false
        return true
    }

    /**
     * Whether [element] is whitespace spanning a blank line, i.e. holding two or more newlines.
     *
     * @param element The PsiElement to check.
     * @return Whether the PsiElement is a PsiWhiteSpace spanning a blank line.
     */
    fun isBlankLine(element: PsiElement?): Boolean = element is PsiWhiteSpace && element.text.count { it == '\n' } >= 2
}
