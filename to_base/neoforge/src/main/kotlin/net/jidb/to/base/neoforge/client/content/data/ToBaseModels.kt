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

/**
 * Holds the model templates and texture mappings the base mod's own content is generated from, which are named here rather than inline so that another mod can build the same shapes from its own textures.
 *
 * @since 0.1.0
 */
object ToBaseModels {

    /**
     * The texture slot of the conductor a cable is drawn around, which vanilla has no equivalent of.
     *
     * @since 0.6.0
     */
    val textureSlotCore = TextureSlot.create("core")

    /**
     * The textures of the research desk, given the name of its block texture: the writing, the ink, the top and the sides.
     *
     * @since 0.1.0
     */
    val researchDeskTexture = { identifier: Identifier ->
        TextureMapping()
            .put(ToDataClientHelper.numericTextures[0], Material(identifier.withSuffix("_writing")))
            .put(ToDataClientHelper.numericTextures[1], Material(identifier.withSuffix("_ink")))
            .put(ToDataClientHelper.numericTextures[2], Material(identifier))
            .put(ToDataClientHelper.numericTextures[3], Material(identifier.withSuffix("_side")))
            .copySlot(ToDataClientHelper.numericTextures[2], TextureSlot.PARTICLE)
    }

    /**
     * The model template of the left half of the research desk.
     *
     * @since 0.1.0
     */
    val researchDeskLeftTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_research_desk_left")),
        Optional.of("_left"),
        TextureSlot.PARTICLE,
        ToDataClientHelper.numericTextures[0],
        ToDataClientHelper.numericTextures[2],
    )

    /**
     * The left half of the research desk, textured from its own name.
     *
     * @since 0.1.0
     */
    val researchDeskLeft = TexturedModel.createDefault({ researchDeskTexture(it.identifier.withPrefix("block/")) }, researchDeskLeftTemplate)

    /**
     * The model template of the right half of the research desk.
     *
     * @since 0.1.0
     */
    val researchDeskRightTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_research_desk_right")),
        Optional.of("_right"),
        TextureSlot.PARTICLE,
        *ToDataClientHelper.numericTextures.take(4).toTypedArray()
    )

    /**
     * The right half of the research desk, textured from its own name.
     *
     * @since 0.1.0
     */
    val researchDeskRight = TexturedModel.createDefault({ researchDeskTexture(it.identifier.withPrefix("block/")) }, researchDeskRightTemplate)

    /**
     * The model template of the research desk's own item, which shows both halves at once.
     *
     * @since 0.1.0
     */
    val researchDeskItemTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.itemPrefix(ToBaseMod.modid, "template_research_desk")),
        Optional.empty(),
        TextureSlot.PARTICLE,
        *ToDataClientHelper.numericTextures.take(4).toTypedArray()
    )

    /**
     * The textures of a cable drawn without its outer casing, where the conductor is the inside texture.
     *
     * @since 0.6.0
     */
    val bareCableTexture = { identifier: Identifier ->
        TextureMapping()
            .put(TextureSlot.SIDE, Material(identifier))
            .put(TextureSlot.INSIDE, Material(identifier.withSuffix("_inside")))
            .put(TextureSlot.END, Material(identifier.withSuffix("_end")))
            .copySlot(TextureSlot.INSIDE, textureSlotCore)
            .copySlot(TextureSlot.SIDE, TextureSlot.PARTICLE)
    }

    /**
     * The textures of an ordinary cable, where the conductor is the side texture.
     *
     * @since 0.6.0
     */
    val cableTexture = { identifier: Identifier ->
        TextureMapping()
            .put(TextureSlot.SIDE, Material(identifier))
            .put(TextureSlot.INSIDE, Material(identifier.withSuffix("_inside")))
            .put(TextureSlot.END, Material(identifier.withSuffix("_end")))
            .copySlot(TextureSlot.SIDE, textureSlotCore)
            .copySlot(TextureSlot.SIDE, TextureSlot.PARTICLE)
    }

    /**
     * The model template of the middle of a cable four pixels across, drawn where it reaches out in no direction.
     *
     * @since 0.6.0
     */
    val cable4CenterTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_cable4_center")),
        Optional.of("_center"),
        TextureSlot.PARTICLE,
        textureSlotCore
    )

    /**
     * The middle of a bare cable four pixels across.
     *
     * @since 0.6.0
     */
    val cable4CenterBare = TexturedModel.createDefault({ bareCableTexture(it.identifier.withPrefix("block/")) }, cable4CenterTemplate)

    /**
     * The middle of a cable four pixels across.
     *
     * @since 0.6.0
     */
    val cable4Center = TexturedModel.createDefault({ cableTexture(it.identifier.withPrefix("block/")) }, cable4CenterTemplate)

    /**
     * The model template of one arm of a cable four pixels across, drawn where it reaches out.
     *
     * @since 0.6.0
     */
    val cable4ConnectionTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_cable4_connection")),
        Optional.of("_connection"),
        TextureSlot.PARTICLE,
        TextureSlot.SIDE,
        TextureSlot.INSIDE
    )

    /**
     * One arm of a bare cable four pixels across.
     *
     * @since 0.6.0
     */
    val cable4ConnectionBare = TexturedModel.createDefault({ bareCableTexture(it.identifier.withPrefix("block/")) }, cable4ConnectionTemplate)

    /**
     * One arm of a cable four pixels across.
     *
     * @since 0.6.0
     */
    val cable4Connection = TexturedModel.createDefault({ cableTexture(it.identifier.withPrefix("block/")) }, cable4ConnectionTemplate)

    /**
     * The model template of the rim of a cable four pixels across, drawn where it meets a machine.
     *
     * @since 0.6.0
     */
    val cable4EndTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_cable4_end")),
        Optional.of("_end"),
        TextureSlot.PARTICLE,
        TextureSlot.END,
        TextureSlot.INSIDE
    )

    /**
     * The rim of a cable four pixels across.
     *
     * @since 0.6.0
     */
    val cable4End = TexturedModel.createDefault({ cableTexture(it.identifier.withPrefix("block/")) }, cable4EndTemplate)

    /**
     * The model template of the item of a cable four pixels across.
     *
     * @since 0.6.0
     */
    val cable4ItemTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.itemPrefix(ToBaseMod.modid, "template_cable4")),
        Optional.empty(),
        TextureSlot.PARTICLE,
        TextureSlot.SIDE,
        TextureSlot.INSIDE
    )

    /**
     * The model template of the middle of a cable ten pixels across, drawn where it reaches out in no direction.
     *
     * @since 0.6.0
     */
    val cable10CenterTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_cable10_center")),
        Optional.of("_center"),
        TextureSlot.PARTICLE,
        textureSlotCore
    )

    /**
     * The middle of a bare cable ten pixels across.
     *
     * @since 0.6.0
     */
    val cable10CenterBare = TexturedModel.createDefault({ bareCableTexture(it.identifier.withPrefix("block/")) }, cable10CenterTemplate)

    /**
     * The middle of a cable ten pixels across.
     *
     * @since 0.6.0
     */
    val cable10Center = TexturedModel.createDefault({ cableTexture(it.identifier.withPrefix("block/")) }, cable10CenterTemplate)

    /**
     * The model template of one arm of a cable ten pixels across, drawn where it reaches out.
     *
     * @since 0.6.0
     */
    val cable10ConnectionTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_cable10_connection")),
        Optional.of("_connection"),
        TextureSlot.PARTICLE,
        TextureSlot.SIDE,
        TextureSlot.INSIDE
    )

    /**
     * One arm of a bare cable ten pixels across.
     *
     * @since 0.6.0
     */
    val cable10ConnectionBare = TexturedModel.createDefault({ bareCableTexture(it.identifier.withPrefix("block/")) }, cable10ConnectionTemplate)

    /**
     * One arm of a cable ten pixels across.
     *
     * @since 0.6.0
     */
    val cable10Connection = TexturedModel.createDefault({ cableTexture(it.identifier.withPrefix("block/")) }, cable10ConnectionTemplate)

    /**
     * The model template of the rim of a cable ten pixels across, drawn where it meets a machine.
     *
     * @since 0.6.0
     */
    val cable10EndTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToBaseMod.modid, "template_cable10_end")),
        Optional.of("_end"),
        TextureSlot.PARTICLE,
        TextureSlot.END,
        TextureSlot.INSIDE
    )

    /**
     * The rim of a cable ten pixels across.
     *
     * @since 0.6.0
     */
    val cable10End = TexturedModel.createDefault({ cableTexture(it.identifier.withPrefix("block/")) }, cable10EndTemplate)

}
