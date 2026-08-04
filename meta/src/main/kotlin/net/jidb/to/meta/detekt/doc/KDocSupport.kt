package net.jidb.to.meta.detekt.doc

import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.kdoc.parser.KDocKnownTag
import org.jetbrains.kotlin.kdoc.psi.api.KDoc
import org.jetbrains.kotlin.kdoc.psi.impl.KDocTag
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.KtObjectDeclaration

/**
 * Shared code for each KDoc rule.
 */
internal object KDocSupport {

    /**
     * Whether [declaration] is one the KDoc rules should leave alone: an override inherits the documentation of the member it overrides.
     * This is the same as Checkstyle `allowedAnnotations` exempts an `@Override` method on the Java side.
     * @param declaration The declaration that may not need KDoc.
     * @param ignoreOverridden Whether to ignore overridden declarations.
     * @return Whether the declaration should be left alone.
     */
    fun isExempt(declaration: KtDeclaration, ignoreOverridden: Boolean): Boolean = ignoreOverridden && declaration.hasModifier(KtTokens.OVERRIDE_KEYWORD)

    /**
     * Whether [declaration] is a companion object written without a name, i.e. `companion object`.
     * @param declaration The declaration to check.
     * @return Whether the declaration is a default companion object.
     */
    fun isDefaultCompanionObject(declaration: KtDeclaration): Boolean = declaration is KtObjectDeclaration && declaration.isCompanion() && declaration.nameIdentifier == null

    /**
     * The subjects of every `@param` and `@property` tag in [kDoc]. Both are collected because KDoc documents a constructor property with either one.
     * @param kDoc The KDoc to find subjects in.
     * @return The subjects of the KDoc.
     */
    fun documentedSubjects(kDoc: KDoc): Set<String> = tags(kDoc)
        .filter { it.knownTag == KDocKnownTag.PARAM || it.knownTag == KDocKnownTag.PROPERTY }
        .mapNotNull { it.getSubjectName() }
        .toSet()

    /**
     * Whether [kDoc] carries a `@return` tag.
     * @param kDoc The KDoc to check.
     * @return Whether the KDoc returns something.
     */
    fun hasReturnTag(kDoc: KDoc): Boolean = tags(kDoc).any { it.knownTag == KDocKnownTag.RETURN }

    /**
     * The `@since` tag of [kDoc], or null where it carries none. The first is taken if there is somehow more than one, as KDoc has no meaning for a repeated `@since`.
     * @param kDoc The KDoc to find the tag in.
     * @return The `@since` tag, or null.
     */
    fun sinceTag(kDoc: KDoc): KDocTag? = tags(kDoc).firstOrNull { it.knownTag == KDocKnownTag.SINCE }

    /**
     * Every tag in [kDoc]. The KDoc body itself is a tag with a null [KDocTag.knownTag], so callers filter on the tag they want rather than assuming the list only holds real tags.
     * @param kDoc The KDoc to get tags of.
     * @return A list of tags.
     */
    private fun tags(kDoc: KDoc): List<KDocTag> = PsiTreeUtil.findChildrenOfType(kDoc, KDocTag::class.java).toList()
}
