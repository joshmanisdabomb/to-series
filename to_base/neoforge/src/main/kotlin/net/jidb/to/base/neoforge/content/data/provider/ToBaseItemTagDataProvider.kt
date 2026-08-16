package net.jidb.to.base.neoforge.content.data.provider

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.content.ToBaseItemTagLibrary
import net.jidb.to.base.service.Services
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.references.ItemIds
import net.neoforged.neoforge.common.data.ItemTagsProvider
import java.util.concurrent.CompletableFuture

class ToBaseItemTagDataProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>) : ItemTagsProvider(output, provider, ToBaseMod.modid) {

    override fun addTags(provider: HolderLookup.Provider) {
        this.tag(ToBaseItemTagLibrary.research_desk_unlock)
            .add(ItemIds.BOOK)
            .addTag(Services.platform.tags.getCommonItem("bookshelves")!!)
    }

}
