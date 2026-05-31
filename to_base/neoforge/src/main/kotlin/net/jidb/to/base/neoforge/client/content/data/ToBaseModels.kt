package net.jidb.to.base.neoforge.client.content.data

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.client.data.ToDataClientHelper
import net.jidb.to.base.helper.IdentifierHelper
import net.jidb.to.base.helper.IdentifierHelper.identifier
import net.minecraft.client.data.models.model.ModelTemplate
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.resources.Identifier
import java.util.*

object ToBaseModels {

    val TEXTURES_RESEARCH_DESK = { identifier: Identifier ->
        TextureMapping()
            .put(ToDataClientHelper.NUMERIC_TEXTURES[0], Material(identifier.withSuffix("_writing")))
            .put(ToDataClientHelper.NUMERIC_TEXTURES[1],  Material(identifier.withSuffix("_ink")))
            .put(ToDataClientHelper.NUMERIC_TEXTURES[2],  Material(identifier))
            .put(ToDataClientHelper.NUMERIC_TEXTURES[3],  Material(identifier.withSuffix("_side")))
            .copySlot(ToDataClientHelper.NUMERIC_TEXTURES[2], TextureSlot.PARTICLE)
    }

    val TEMPLATE_RESEARCH_DESK_LEFT = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_research_desk_left")),
        Optional.of("_left"),
        TextureSlot.PARTICLE,
        ToDataClientHelper.NUMERIC_TEXTURES[0],
        ToDataClientHelper.NUMERIC_TEXTURES[2],
    )
    val RESEARCH_DESK_LEFT = TexturedModel.createDefault({ TEXTURES_RESEARCH_DESK(it.identifier.withPrefix("block/")) }, TEMPLATE_RESEARCH_DESK_LEFT)

    val TEMPLATE_RESEARCH_DESK_RIGHT = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_research_desk_right")),
        Optional.of("_right"),
        TextureSlot.PARTICLE,
        *ToDataClientHelper.NUMERIC_TEXTURES.take(4).toTypedArray()
    )
    val RESEARCH_DESK_RIGHT = TexturedModel.createDefault({ TEXTURES_RESEARCH_DESK(it.identifier.withPrefix("block/")) }, TEMPLATE_RESEARCH_DESK_RIGHT)

    val TEMPLATE_RESEARCH_DESK_ITEM = ModelTemplate(
        Optional.of(IdentifierHelper.itemPrefix(ToBaseMod.modid, "template_research_desk")),
        Optional.empty(),
        TextureSlot.PARTICLE,
        *ToDataClientHelper.NUMERIC_TEXTURES.take(4).toTypedArray()
    )

}