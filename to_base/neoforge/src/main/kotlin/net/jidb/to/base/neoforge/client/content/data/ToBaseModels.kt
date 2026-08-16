package net.jidb.to.base.neoforge.client.content.data

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.helper.IdentifierHelper
import net.jidb.to.base.api.helper.IdentifierHelper.identifier
import net.jidb.to.base.client.data.api.ToDataClientHelper
import net.minecraft.client.data.models.model.ModelTemplate
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.resources.Identifier
import java.util.Optional

object ToBaseModels {

    val textureSlotCore = TextureSlot.create("core")

    val researchDeskTexture = { identifier: Identifier ->
        TextureMapping()
            .put(ToDataClientHelper.numericTextures[0], Material(identifier.withSuffix("_writing")))
            .put(ToDataClientHelper.numericTextures[1], Material(identifier.withSuffix("_ink")))
            .put(ToDataClientHelper.numericTextures[2], Material(identifier))
            .put(ToDataClientHelper.numericTextures[3], Material(identifier.withSuffix("_side")))
            .copySlot(ToDataClientHelper.numericTextures[2], TextureSlot.PARTICLE)
    }

    val researchDeskLeftTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_research_desk_left")),
        Optional.of("_left"),
        TextureSlot.PARTICLE,
        ToDataClientHelper.numericTextures[0],
        ToDataClientHelper.numericTextures[2],
    )

    val researchDeskLeft = TexturedModel.createDefault({ researchDeskTexture(it.identifier.withPrefix("block/")) }, researchDeskLeftTemplate)

    val researchDeskRightTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_research_desk_right")),
        Optional.of("_right"),
        TextureSlot.PARTICLE,
        *ToDataClientHelper.numericTextures.take(4).toTypedArray()
    )

    val researchDeskRight = TexturedModel.createDefault({ researchDeskTexture(it.identifier.withPrefix("block/")) }, researchDeskRightTemplate)

    val researchDeskItemTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.itemPrefix(ToBaseMod.modid, "template_research_desk")),
        Optional.empty(),
        TextureSlot.PARTICLE,
        *ToDataClientHelper.numericTextures.take(4).toTypedArray()
    )

    val bareCableTexture = { identifier: Identifier ->
        TextureMapping()
            .put(TextureSlot.SIDE, Material(identifier))
            .put(TextureSlot.INSIDE, Material(identifier.withSuffix("_inside")))
            .put(TextureSlot.END, Material(identifier.withSuffix("_end")))
            .copySlot(TextureSlot.INSIDE, textureSlotCore)
            .copySlot(TextureSlot.SIDE, TextureSlot.PARTICLE)
    }

    val cableTexture = { identifier: Identifier ->
        TextureMapping()
            .put(TextureSlot.SIDE, Material(identifier))
            .put(TextureSlot.INSIDE, Material(identifier.withSuffix("_inside")))
            .put(TextureSlot.END, Material(identifier.withSuffix("_end")))
            .copySlot(TextureSlot.SIDE, textureSlotCore)
            .copySlot(TextureSlot.SIDE, TextureSlot.PARTICLE)
    }

    val cable4CenterTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_cable4_center")),
        Optional.of("_center"),
        TextureSlot.PARTICLE,
        textureSlotCore
    )

    val cable4CenterBare = TexturedModel.createDefault({ bareCableTexture(it.identifier.withPrefix("block/")) }, cable4CenterTemplate)

    val cable4Center = TexturedModel.createDefault({ cableTexture(it.identifier.withPrefix("block/")) }, cable4CenterTemplate)

    val cable4ConnectionTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_cable4_connection")),
        Optional.of("_connection"),
        TextureSlot.PARTICLE,
        TextureSlot.SIDE,
        TextureSlot.INSIDE
    )

    val cable4ConnectionBare = TexturedModel.createDefault({ bareCableTexture(it.identifier.withPrefix("block/")) }, cable4ConnectionTemplate)

    val cable4Connection = TexturedModel.createDefault({ cableTexture(it.identifier.withPrefix("block/")) }, cable4ConnectionTemplate)

    val cable4EndTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_cable4_end")),
        Optional.of("_end"),
        TextureSlot.PARTICLE,
        TextureSlot.END,
        TextureSlot.INSIDE
    )

    val cable4End = TexturedModel.createDefault({ cableTexture(it.identifier.withPrefix("block/")) }, cable4EndTemplate)

    val cable4ItemTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.itemPrefix(ToBaseMod.modid, "template_cable4")),
        Optional.empty(),
        TextureSlot.PARTICLE,
        TextureSlot.SIDE,
        TextureSlot.INSIDE
    )

    val cable10CenterTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_cable10_center")),
        Optional.of("_center"),
        TextureSlot.PARTICLE,
        textureSlotCore
    )

    val cable10CenterBare = TexturedModel.createDefault({ bareCableTexture(it.identifier.withPrefix("block/")) }, cable10CenterTemplate)

    val cable10Center = TexturedModel.createDefault({ cableTexture(it.identifier.withPrefix("block/")) }, cable10CenterTemplate)

    val cable10ConnectionTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_cable10_connection")),
        Optional.of("_connection"),
        TextureSlot.PARTICLE,
        TextureSlot.SIDE,
        TextureSlot.INSIDE
    )

    val cable10ConnectionBare = TexturedModel.createDefault({ bareCableTexture(it.identifier.withPrefix("block/")) }, cable10ConnectionTemplate)

    val cable10Connection = TexturedModel.createDefault({ cableTexture(it.identifier.withPrefix("block/")) }, cable10ConnectionTemplate)

    val cable10EndTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_cable10_end")),
        Optional.of("_end"),
        TextureSlot.PARTICLE,
        TextureSlot.END,
        TextureSlot.INSIDE
    )

    val cable10End = TexturedModel.createDefault({ cableTexture(it.identifier.withPrefix("block/")) }, cable10EndTemplate)

}
