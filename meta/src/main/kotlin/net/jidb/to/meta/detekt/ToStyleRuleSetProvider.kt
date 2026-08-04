package net.jidb.to.meta.detekt

import dev.detekt.api.RuleSet
import dev.detekt.api.RuleSetId
import dev.detekt.api.RuleSetProvider
import net.jidb.to.meta.detekt.brace.TypeCloseBraceBlankLine
import net.jidb.to.meta.detekt.brace.TypeOpenBraceBlankLine
import net.jidb.to.meta.detekt.comment.LineCommentSpacing
import net.jidb.to.meta.detekt.doc.MissingKDocTags
import net.jidb.to.meta.detekt.doc.MissingSinceTag
import net.jidb.to.meta.detekt.doc.UndocumentedFunction
import net.jidb.to.meta.detekt.doc.UndocumentedProperty
import net.jidb.to.meta.detekt.doc.UndocumentedType
import net.jidb.to.meta.detekt.property.PropertyNaming

/**
 * Registers the project's own Detekt rules under the 'to-style' rule set, which is the section name they are configured under.
 * Discovered by Detekt through the ServiceLoader entry in META-INF/services.
 */
class ToStyleRuleSetProvider : RuleSetProvider {

    override val ruleSetId = RuleSetId("to-style")

    override fun instance() = RuleSet(
        ruleSetId,
        listOf(
            ::TypeOpenBraceBlankLine,
            ::TypeCloseBraceBlankLine,
            ::LineCommentSpacing,
            ::PropertyNaming,
            ::UndocumentedType,
            ::UndocumentedFunction,
            ::UndocumentedProperty,
            ::MissingKDocTags,
            ::MissingSinceTag,
        ),
    )
}
