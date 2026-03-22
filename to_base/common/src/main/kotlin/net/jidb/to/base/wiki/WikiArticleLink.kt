package net.jidb.to.base.wiki

import net.minecraft.network.chat.ClickEvent

data class WikiArticleLink(val article: WikiArticle) : ClickEvent {

    override fun action() = ClickEvent.Action.CUSTOM

}