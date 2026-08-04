package net.jidb.to.stars.neoforge.client.data

import net.jidb.to.base.api.helper.IdentifierHelper
import net.jidb.to.base.api.helper.IdentifierHelper.identifier
import net.jidb.to.base.client.data.api.ToDataClientHelper
import net.jidb.to.stars.ToStarsMod
import net.minecraft.client.data.models.model.ModelTemplate
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.resources.Identifier
import java.util.Optional

/**
 * Holds the model templates and texture mappings the content mod's blocks are generated from, which are named here rather than inline so that a shape used by several blocks is written once.
 */
object ToStarsModels {

    /**
     * The textures of the atomic bomb, given the name of its block texture.
     */
    val atomicBombTexture = { identifier: Identifier ->
        TextureMapping()
            .put(ToDataClientHelper.numericTextures[0], Material(identifier.withSuffix("_tail_side")))
            .put(ToDataClientHelper.numericTextures[1], Material(identifier.withSuffix("_tail")))
            .put(ToDataClientHelper.numericTextures[2], Material(identifier.withSuffix("_fin")))
            .put(ToDataClientHelper.numericTextures[3], Material(identifier.withSuffix("_core")))
            .put(ToDataClientHelper.numericTextures[4], Material(identifier.withSuffix("_head")))
            .copySlot(ToDataClientHelper.numericTextures[2], TextureSlot.PARTICLE)
    }

    /**
     * The model template of the bomb's nose.
     */
    val atomicBombHeadTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToStarsMod.modid, "template_atomic_bomb_head")),
        Optional.of("_head"),
        TextureSlot.PARTICLE,
        ToDataClientHelper.numericTextures[1],
        ToDataClientHelper.numericTextures[4],
    )

    /**
     * The bomb's nose, textured from its own name.
     */
    val atomicBombHead = TexturedModel.createDefault({ atomicBombTexture(it.identifier.withPrefix("block/")) }, atomicBombHeadTemplate)

    /**
     * The model template of the bomb's body.
     */
    val atomicBombMiddleTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToStarsMod.modid, "template_atomic_bomb_middle")),
        Optional.of("_middle"),
        TextureSlot.PARTICLE,
        ToDataClientHelper.numericTextures[1],
        ToDataClientHelper.numericTextures[4],
    )

    /**
     * The bomb's body, textured from its own name.
     */
    val atomicBombMiddle = TexturedModel.createDefault({ atomicBombTexture(it.identifier.withPrefix("block/")) }, atomicBombMiddleTemplate)

    /**
     * The model template of the bomb's tail.
     */
    val atomicBombTailTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToStarsMod.modid, "template_atomic_bomb_tail")),
        Optional.of("_tail"),
        TextureSlot.PARTICLE,
        *ToDataClientHelper.numericTextures.take(4).toTypedArray()
    )

    /**
     * The bomb's tail, textured from its own name.
     */
    val atomicBombTail = TexturedModel.createDefault({ atomicBombTexture(it.identifier.withPrefix("block/")) }, atomicBombTailTemplate)

    /**
     * The model template of the bomb's own item, which shows all three segments at once.
     */
    val atomicBombItemTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.itemPrefix(ToStarsMod.modid, "template_atomic_bomb")),
        Optional.empty(),
        TextureSlot.PARTICLE,
        *ToDataClientHelper.numericTextures.take(5).toTypedArray()
    )

    /**
     * The model template of the heat pipe's own item.
     */
    val heatPipeTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.itemPrefix(ToStarsMod.modid, "template_heat_pipe")),
        Optional.empty(),
        TextureSlot.PARTICLE,
        TextureSlot.SIDE,
        TextureSlot.INSIDE,
        TextureSlot.END,
    )

    /**
     * The textures of a power bank, given the name of its block texture.
     */
    val powerBankTexture = { identifier: Identifier ->
        TextureMapping()
            .put(TextureSlot.TOP, Material(identifier.withSuffix("_top")))
            .put(TextureSlot.INNER_TOP, Material(identifier.withPath { it.split("_").first() + "_machine_enclosure_top" }))
            .put(TextureSlot.FRONT, Material(identifier.withSuffix("_front")))
            .put(TextureSlot.SIDE, Material(identifier.withSuffix("_side")))
            .put(TextureSlot.INSIDE, Material(identifier.withPath { it.split("_").first() + "_machine_enclosure_side" }))
            .put(TextureSlot.END, Material(identifier.withSuffix("_bottom")))
            .copySlot(TextureSlot.INSIDE, TextureSlot.PARTICLE)
    }

    /**
     * The model template of a power bank, whose charge is drawn on every side but the one it faces.
     */
    val powerBankTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToStarsMod.modid, "template_power_bank")),
        Optional.empty(),
        TextureSlot.PARTICLE,
        TextureSlot.TOP,
        TextureSlot.INNER_TOP,
        TextureSlot.FRONT,
        TextureSlot.SIDE,
        TextureSlot.INSIDE,
        TextureSlot.END,
        TextureSlot.BOTTOM,
    )

    /**
     * A power bank, textured from its own name.
     */
    val powerBank = TexturedModel.createDefault({ powerBankTexture(it.identifier.withPrefix("block/")) }, powerBankTemplate)

    /**
     * The textures of the rotor blades, given the name of their block texture.
     */
    val rotorBladesTexture = { identifier: Identifier ->
        TextureMapping()
            .put(TextureSlot.STEM, Material(identifier.withSuffix("_stem")))
            .put(TextureSlot.FAN, Material(identifier))
            .copySlot(TextureSlot.FAN, TextureSlot.PARTICLE)
    }

    /**
     * The model template of the still rotor blades.
     */
    val rotorBladesTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToStarsMod.modid, "template_rotor_blades")),
        Optional.empty(),
        TextureSlot.PARTICLE,
        TextureSlot.STEM,
        TextureSlot.FAN,
    )

    /**
     * The still rotor blades, textured from their own name.
     */
    val rotorBlades = TexturedModel.createDefault({ rotorBladesTexture(it.identifier.withPrefix("block/")) }, rotorBladesTemplate)

    /**
     * The model template of the still rotor blades a half turn out, so that a stack of them does not all line up.
     */
    val rotorBladesAltTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToStarsMod.modid, "template_rotor_blades_alt")),
        Optional.empty(),
        TextureSlot.PARTICLE,
        TextureSlot.STEM,
        TextureSlot.FAN,
    )

    /**
     * The alternate still rotor blades, textured from their own name.
     */
    val rotorBladesAlt = TexturedModel.createDefault({ rotorBladesTexture(it.identifier.withPrefix("block/")) }, rotorBladesAltTemplate)

    /**
     * The textures of a centrifuge, given the name of its block texture.
     */
    val centrifugeTexture = { identifier: Identifier ->
        TextureMapping()
            .put(TextureSlot.SIDE, Material(identifier.withPath { it.split("_").first() + "_machine_enclosure_side" }))
            .put(TextureSlot.BOTTOM, Material(identifier.withPath { it.split("_").first() + "_machine_enclosure_bottom" }))
            .put(TextureSlot.TOP, Material(identifier.withPath { it.split("_").first() + "_machine_enclosure_top" }))
            .put(TextureSlot.INSIDE, Material(identifier.withPath { "block/" + it.split("_").last() }))
            .copySlot(TextureSlot.SIDE, TextureSlot.PARTICLE)
    }

    /**
     * The model template of an idle centrifuge.
     */
    val centrifugeTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToStarsMod.modid, "template_centrifuge")),
        Optional.empty(),
        TextureSlot.PARTICLE,
        TextureSlot.SIDE,
        TextureSlot.BOTTOM,
        TextureSlot.TOP,
        TextureSlot.INSIDE,
    )

    /**
     * An idle centrifuge, textured from its own name.
     */
    val centrifuge = TexturedModel.createDefault({ centrifugeTexture(it.identifier.withPrefix("block/")) }, centrifugeTemplate)

    /**
     * The model template of a working centrifuge.
     */
    val centrifugeLitTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToStarsMod.modid, "template_centrifuge_lit")),
        Optional.empty(),
        TextureSlot.PARTICLE,
        TextureSlot.SIDE,
        TextureSlot.BOTTOM,
        TextureSlot.TOP,
        TextureSlot.INSIDE,
    )

    /**
     * A working centrifuge, textured from its own name.
     */
    val centrifugeLit = TexturedModel.createDefault({ centrifugeTexture(it.identifier.withPrefix("block/")) }, centrifugeLitTemplate)

}
