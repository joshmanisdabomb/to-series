package net.jidb.to.base.api.wiki

import net.minecraft.network.chat.ClickEvent

/**
 * Represents a link to a specific [WikiArticle], designed to handle click events.
 *
 * @param article The associated [WikiArticle] for this link.
 *
 * This class implements the [ClickEvent] interface and is used to define a custom action when the link is clicked.
 *
 * @since 0.1.0
 */
data class WikiArticleLink(val article: WikiArticle) : ClickEvent {

    override fun action() = ClickEvent.Action.CUSTOM

}
