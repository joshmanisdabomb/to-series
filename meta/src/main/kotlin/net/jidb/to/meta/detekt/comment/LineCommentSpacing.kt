package net.jidb.to.meta.detekt.comment

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiWhiteSpace
import dev.detekt.api.Config
import dev.detekt.api.Configuration
import dev.detekt.api.Entity
import dev.detekt.api.Finding
import dev.detekt.api.Rule
import dev.detekt.api.config
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.psiUtil.prevLeaf

/**
 * Requires an end-of-line comment to start immediately after its slashes, with no space between them and the text.
 *
 * This is the Kotlin counterpart of the LineCommentSpacing rule in checkstyle.xml, and it is the reverse of ktlint's
 * CommentSpacing, which is off for that reason. Block and KDoc comments are a different convention and are not visited
 * at all; only the end-of-line form is checked.
 */
class LineCommentSpacing(config: Config) : Rule(config, "An end-of-line comment must start immediately after its slashes.") {

    @Configuration("a comment that trails code must still be separated from it by a space")
    private val requireSpaceBeforeComment: Boolean by config(true)

    override fun visitComment(comment: PsiComment) {
        super.visitComment(comment)

        if (comment.tokenType != KtTokens.EOL_COMMENT) return

        //The leading run of slashes may be longer than two, as in a '////' divider, and the text starts after all of them.
        val text = comment.text
        val body = text.dropWhile { it == '/' }
        if (body.isNotBlank() && body.first().isWhitespace()) {
            report(
                Finding(
                    Entity.from(comment),
                    "Comment text must follow // immediately, with no space between them.",
                ),
            )
        }

        //The other half of the rule ktlint's CommentSpacing was covering: 'val x = 1//note' runs the comment into the code.
        if (requireSpaceBeforeComment) {
            val previous = comment.prevLeaf(skipEmptyElements = true)
            if (previous != null && previous !is PsiWhiteSpace) {
                report(
                    Finding(
                        Entity.from(comment),
                        "A comment that follows code on the same line must be preceded by a space.",
                    ),
                )
            }
        }
    }
}