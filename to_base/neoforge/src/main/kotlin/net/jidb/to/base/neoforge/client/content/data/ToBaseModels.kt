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
import java.util.*

object ToBaseModels {

    val TEXTURE_SLOT_CORE = TextureSlot.create("core")

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

    val TEXTURES_BARE_CABLE = { identifier: Identifier ->
        TextureMapping()
            .put(TextureSlot.SIDE, Material(identifier))
            .put(TextureSlot.INSIDE, Material(identifier.withSuffix("_inside")))
            .put(TextureSlot.END, Material(identifier.withSuffix("_end")))
            .copySlot(TextureSlot.INSIDE, TEXTURE_SLOT_CORE)
            .copySlot(TextureSlot.SIDE, TextureSlot.PARTICLE)
    }
    val TEXTURES_CABLE = { identifier: Identifier ->
        TextureMapping()
            .put(TextureSlot.SIDE, Material(identifier))
            .put(TextureSlot.INSIDE, Material(identifier.withSuffix("_inside")))
            .put(TextureSlot.END, Material(identifier.withSuffix("_end")))
            .copySlot(TextureSlot.SIDE, TEXTURE_SLOT_CORE)
            .copySlot(TextureSlot.SIDE, TextureSlot.PARTICLE)
    }

    val TEMPLATE_CABLE4_CENTER = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_cable4_center")),
        Optional.of("_center"),
        TextureSlot.PARTICLE,
        TEXTURE_SLOT_CORE
    )
    val BARE_CABLE4_CENTER = TexturedModel.createDefault({ TEXTURES_BARE_CABLE(it.identifier.withPrefix("block/")) }, TEMPLATE_CABLE4_CENTER)
    val CABLE4_CENTER = TexturedModel.createDefault({ TEXTURES_CABLE(it.identifier.withPrefix("block/")) }, TEMPLATE_CABLE4_CENTER)

    val TEMPLATE_CABLE4_CONNECTION = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_cable4_connection")),
        Optional.of("_connection"),
        TextureSlot.PARTICLE,
        TextureSlot.SIDE,
        TextureSlot.INSIDE
    )
    val BARE_CABLE4_CONNECTION = TexturedModel.createDefault({ TEXTURES_BARE_CABLE(it.identifier.withPrefix("block/")) }, TEMPLATE_CABLE4_CONNECTION)
    val CABLE4_CONNECTION = TexturedModel.createDefault({ TEXTURES_CABLE(it.identifier.withPrefix("block/")) }, TEMPLATE_CABLE4_CONNECTION)

    val TEMPLATE_CABLE4_END = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_cable4_end")),
        Optional.of("_end"),
        TextureSlot.PARTICLE,
        TextureSlot.END,
        TextureSlot.INSIDE
    )
    val CABLE4_END = TexturedModel.createDefault({ TEXTURES_CABLE(it.identifier.withPrefix("block/")) }, TEMPLATE_CABLE4_END)

    val TEMPLATE_CABLE4_ITEM = ModelTemplate(
        Optional.of(IdentifierHelper.itemPrefix(ToBaseMod.modid, "template_cable4")),
        Optional.empty(),
        TextureSlot.PARTICLE,
        TextureSlot.SIDE,
        TextureSlot.INSIDE
    )

    val TEMPLATE_CABLE10_CENTER = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_cable10_center")),
        Optional.of("_center"),
        TextureSlot.PARTICLE,
        TEXTURE_SLOT_CORE
    )
    val BARE_CABLE10_CENTER = TexturedModel.createDefault({ TEXTURES_BARE_CABLE(it.identifier.withPrefix("block/")) }, TEMPLATE_CABLE10_CENTER)
    val CABLE10_CENTER = TexturedModel.createDefault({ TEXTURES_CABLE(it.identifier.withPrefix("block/")) }, TEMPLATE_CABLE10_CENTER)

    val TEMPLATE_CABLE10_CONNECTION = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_cable10_connection")),
        Optional.of("_connection"),
        TextureSlot.PARTICLE,
        TextureSlot.SIDE,
        TextureSlot.INSIDE
    )
    val BARE_CABLE10_CONNECTION = TexturedModel.createDefault({ TEXTURES_BARE_CABLE(it.identifier.withPrefix("block/")) }, TEMPLATE_CABLE10_CONNECTION)
    val CABLE10_CONNECTION = TexturedModel.createDefault({ TEXTURES_CABLE(it.identifier.withPrefix("block/")) }, TEMPLATE_CABLE10_CONNECTION)

    val TEMPLATE_CABLE10_END = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_cable10_end")),
        Optional.of("_end"),
        TextureSlot.PARTICLE,
        TextureSlot.END,
        TextureSlot.INSIDE
    )
    val CABLE10_END = TexturedModel.createDefault({ TEXTURES_CABLE(it.identifier.withPrefix("block/")) }, TEMPLATE_CABLE10_END)

}