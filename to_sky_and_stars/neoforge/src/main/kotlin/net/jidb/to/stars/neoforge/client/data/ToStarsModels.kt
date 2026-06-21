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
import java.util.*

object ToStarsModels {

    val TEXTURES_ATOMIC_BOMB = { identifier: Identifier ->
        TextureMapping()
            .put(ToDataClientHelper.NUMERIC_TEXTURES[0], Material(identifier.withSuffix("_tail_side")))
            .put(ToDataClientHelper.NUMERIC_TEXTURES[1], Material(identifier.withSuffix("_tail")))
            .put(ToDataClientHelper.NUMERIC_TEXTURES[2], Material(identifier.withSuffix("_fin")))
            .put(ToDataClientHelper.NUMERIC_TEXTURES[3], Material(identifier.withSuffix("_core")))
            .put(ToDataClientHelper.NUMERIC_TEXTURES[4], Material(identifier.withSuffix("_head")))
            .copySlot(ToDataClientHelper.NUMERIC_TEXTURES[2], TextureSlot.PARTICLE)
    }

    val TEMPLATE_ATOMIC_BOMB_HEAD = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToStarsMod.modid, "template_atomic_bomb_head")),
        Optional.of("_head"),
        TextureSlot.PARTICLE,
        ToDataClientHelper.NUMERIC_TEXTURES[1],
        ToDataClientHelper.NUMERIC_TEXTURES[4],
    )
    val ATOMIC_BOMB_HEAD = TexturedModel.createDefault({ TEXTURES_ATOMIC_BOMB(it.identifier.withPrefix("block/")) }, TEMPLATE_ATOMIC_BOMB_HEAD)

    val TEMPLATE_ATOMIC_BOMB_MIDDLE = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToStarsMod.modid, "template_atomic_bomb_middle")),
        Optional.of("_middle"),
        TextureSlot.PARTICLE,
        ToDataClientHelper.NUMERIC_TEXTURES[1],
        ToDataClientHelper.NUMERIC_TEXTURES[4],
    )
    val ATOMIC_BOMB_MIDDLE = TexturedModel.createDefault({ TEXTURES_ATOMIC_BOMB(it.identifier.withPrefix("block/")) }, TEMPLATE_ATOMIC_BOMB_MIDDLE)

    val TEMPLATE_ATOMIC_BOMB_TAIL = ModelTemplate(
        Optional.of(IdentifierHelper.blockPrefix(ToStarsMod.modid, "template_atomic_bomb_tail")),
        Optional.of("_tail"),
        TextureSlot.PARTICLE,
        *ToDataClientHelper.NUMERIC_TEXTURES.take(4).toTypedArray()
    )
    val ATOMIC_BOMB_TAIL = TexturedModel.createDefault({ TEXTURES_ATOMIC_BOMB(it.identifier.withPrefix("block/")) }, TEMPLATE_ATOMIC_BOMB_TAIL)

    val TEMPLATE_ATOMIC_BOMB_ITEM = ModelTemplate(
        Optional.of(IdentifierHelper.itemPrefix(ToStarsMod.modid, "template_atomic_bomb")),
        Optional.empty(),
        TextureSlot.PARTICLE,
        *ToDataClientHelper.NUMERIC_TEXTURES.take(5).toTypedArray()
    )

    val TEMPLATE_HEAT_PIPE = ModelTemplate(
        Optional.of(IdentifierHelper.itemPrefix(ToStarsMod.modid, "template_heat_pipe")),
        Optional.empty(),
        TextureSlot.PARTICLE,
        TextureSlot.SIDE,
        TextureSlot.INSIDE,
        TextureSlot.END,
    )

    val TEXTURES_POWER_BANK = { identifier: Identifier ->
        TextureMapping()
            .put(TextureSlot.TOP, Material(identifier.withSuffix("_top")))
            .put(TextureSlot.INNER_TOP, Material(identifier.withPath { it.split("_").first() + "_machine_enclosure_top" }))
            .put(TextureSlot.FRONT, Material(identifier.withSuffix("_front")))
            .put(TextureSlot.SIDE, Material(identifier.withSuffix("_side")))
            .put(TextureSlot.INSIDE, Material(identifier.withPath { it.split("_").first() + "_machine_enclosure_side" }))
            .put(TextureSlot.END, Material(identifier.withSuffix("_bottom")))
            .copySlot(TextureSlot.INSIDE, TextureSlot.PARTICLE)
    }

    val TEMPLATE_POWER_BANK = ModelTemplate(
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
    val POWER_BANK = TexturedModel.createDefault({ TEXTURES_POWER_BANK(it.identifier.withPrefix("block/")) }, TEMPLATE_POWER_BANK)

}