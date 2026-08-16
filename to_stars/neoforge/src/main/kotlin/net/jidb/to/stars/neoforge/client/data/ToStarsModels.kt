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

object ToStarsModels {

    val atomicBombTexture = { identifier: Identifier ->
        TextureMapping()
            .put(ToDataClientHelper.numericTextures[0], Material(identifier.withSuffix("_tail_side")))
            .put(ToDataClientHelper.numericTextures[1], Material(identifier.withSuffix("_tail")))
            .put(ToDataClientHelper.numericTextures[2], Material(identifier.withSuffix("_fin")))
            .put(ToDataClientHelper.numericTextures[3], Material(identifier.withSuffix("_core")))
            .put(ToDataClientHelper.numericTextures[4], Material(identifier.withSuffix("_head")))
            .copySlot(ToDataClientHelper.numericTextures[2], TextureSlot.PARTICLE)
    }

    val atomicBombHeadTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToStarsMod.modid, "template_atomic_bomb_head")),
        Optional.of("_head"),
        TextureSlot.PARTICLE,
        ToDataClientHelper.numericTextures[1],
        ToDataClientHelper.numericTextures[4],
    )

    val atomicBombHead = TexturedModel.createDefault({ atomicBombTexture(it.identifier.withPrefix("block/")) }, atomicBombHeadTemplate)

    val atomicBombMiddleTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToStarsMod.modid, "template_atomic_bomb_middle")),
        Optional.of("_middle"),
        TextureSlot.PARTICLE,
        ToDataClientHelper.numericTextures[1],
        ToDataClientHelper.numericTextures[4],
    )

    val atomicBombMiddle = TexturedModel.createDefault({ atomicBombTexture(it.identifier.withPrefix("block/")) }, atomicBombMiddleTemplate)

    val atomicBombTailTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToStarsMod.modid, "template_atomic_bomb_tail")),
        Optional.of("_tail"),
        TextureSlot.PARTICLE,
        *ToDataClientHelper.numericTextures.take(4).toTypedArray()
    )

    val atomicBombTail = TexturedModel.createDefault({ atomicBombTexture(it.identifier.withPrefix("block/")) }, atomicBombTailTemplate)

    val atomicBombItemTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.itemPrefix(ToStarsMod.modid, "template_atomic_bomb")),
        Optional.empty(),
        TextureSlot.PARTICLE,
        *ToDataClientHelper.numericTextures.take(5).toTypedArray()
    )

    val heatPipeTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.itemPrefix(ToStarsMod.modid, "template_heat_pipe")),
        Optional.empty(),
        TextureSlot.PARTICLE,
        TextureSlot.SIDE,
        TextureSlot.INSIDE,
        TextureSlot.END,
    )

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

    val powerBank = TexturedModel.createDefault({ powerBankTexture(it.identifier.withPrefix("block/")) }, powerBankTemplate)

    val rotorBladesTexture = { identifier: Identifier ->
        TextureMapping()
            .put(TextureSlot.STEM, Material(identifier.withSuffix("_stem")))
            .put(TextureSlot.FAN, Material(identifier))
            .copySlot(TextureSlot.FAN, TextureSlot.PARTICLE)
    }

    val rotorBladesTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToStarsMod.modid, "template_rotor_blades")),
        Optional.empty(),
        TextureSlot.PARTICLE,
        TextureSlot.STEM,
        TextureSlot.FAN,
    )

    val rotorBlades = TexturedModel.createDefault({ rotorBladesTexture(it.identifier.withPrefix("block/")) }, rotorBladesTemplate)

    val rotorBladesAltTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToStarsMod.modid, "template_rotor_blades_alt")),
        Optional.empty(),
        TextureSlot.PARTICLE,
        TextureSlot.STEM,
        TextureSlot.FAN,
    )

    val rotorBladesAlt = TexturedModel.createDefault({ rotorBladesTexture(it.identifier.withPrefix("block/")) }, rotorBladesAltTemplate)

    val centrifugeTexture = { identifier: Identifier ->
        TextureMapping()
            .put(TextureSlot.SIDE, Material(identifier.withPath { it.split("_").first() + "_machine_enclosure_side" }))
            .put(TextureSlot.BOTTOM, Material(identifier.withPath { it.split("_").first() + "_machine_enclosure_bottom" }))
            .put(TextureSlot.TOP, Material(identifier.withPath { it.split("_").first() + "_machine_enclosure_top" }))
            .put(TextureSlot.INSIDE, Material(identifier.withPath { "block/" + it.split("_").last() }))
            .copySlot(TextureSlot.SIDE, TextureSlot.PARTICLE)
    }

    val centrifugeTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToStarsMod.modid, "template_centrifuge")),
        Optional.empty(),
        TextureSlot.PARTICLE,
        TextureSlot.SIDE,
        TextureSlot.BOTTOM,
        TextureSlot.TOP,
        TextureSlot.INSIDE,
    )

    val centrifuge = TexturedModel.createDefault({ centrifugeTexture(it.identifier.withPrefix("block/")) }, centrifugeTemplate)

    val centrifugeLitTemplate = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToStarsMod.modid, "template_centrifuge_lit")),
        Optional.empty(),
        TextureSlot.PARTICLE,
        TextureSlot.SIDE,
        TextureSlot.BOTTOM,
        TextureSlot.TOP,
        TextureSlot.INSIDE,
    )

    val centrifugeLit = TexturedModel.createDefault({ centrifugeTexture(it.identifier.withPrefix("block/")) }, centrifugeLitTemplate)

}
