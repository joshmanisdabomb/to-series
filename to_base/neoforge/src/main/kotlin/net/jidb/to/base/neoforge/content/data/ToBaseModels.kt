package net.jidb.to.base.neoforge.content.data

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.helper.IdentifierHelper.identifier
import net.jidb.to.base.neoforge.content.data.ToBaseModelDataProvider.Companion.NUMERIC_TEXTURES
import net.minecraft.client.data.models.model.ModelTemplate
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.resources.Identifier
import java.util.*

object ToBaseModels {

    private fun loc(name: String, prefix: String) = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "$prefix/$name")
    private fun blockLoc(name: String) = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "block/$name")
    private fun itemLoc(name: String) = Identifier.fromNamespaceAndPath(ToBaseMod.modid, "item/$name")

    val TEXTURES_RESEARCH_DESK = { identifier: Identifier ->
        TextureMapping()
            .put(NUMERIC_TEXTURES[0], identifier.withSuffix("_writing"))
            .put(NUMERIC_TEXTURES[1], identifier.withSuffix("_ink"))
            .put(NUMERIC_TEXTURES[2], identifier)
            .put(NUMERIC_TEXTURES[3], identifier.withSuffix("_side"))
            .copySlot(NUMERIC_TEXTURES[2], TextureSlot.PARTICLE)
    }

    val TEMPLATE_RESEARCH_DESK_LEFT = ModelTemplate(
        Optional.of(blockLoc("template_research_desk_left")),
        Optional.of("_left"),
        TextureSlot.PARTICLE,
        NUMERIC_TEXTURES[0],
        NUMERIC_TEXTURES[2],
    )
    val RESEARCH_DESK_LEFT = TexturedModel.createDefault({ TEXTURES_RESEARCH_DESK(it.identifier.withPrefix("block/")) }, TEMPLATE_RESEARCH_DESK_LEFT)

    val TEMPLATE_RESEARCH_DESK_RIGHT = ModelTemplate(
        Optional.of(blockLoc("template_research_desk_right")),
        Optional.of("_right"),
        TextureSlot.PARTICLE,
        *NUMERIC_TEXTURES.take(4).toTypedArray()
    )
    val RESEARCH_DESK_RIGHT = TexturedModel.createDefault({ TEXTURES_RESEARCH_DESK(it.identifier.withPrefix("block/")) }, TEMPLATE_RESEARCH_DESK_RIGHT)

    val TEMPLATE_RESEARCH_DESK_ITEM = ModelTemplate(
        Optional.of(itemLoc("template_research_desk")),
        Optional.empty(),
        *NUMERIC_TEXTURES.take(4).toTypedArray()
    )

}