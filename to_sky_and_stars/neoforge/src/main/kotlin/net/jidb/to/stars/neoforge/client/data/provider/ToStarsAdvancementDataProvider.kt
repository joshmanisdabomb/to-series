package net.jidb.to.stars.neoforge.client.data.provider

import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.advancements.AtomicBombAdvancementTrigger
import net.jidb.to.stars.advancements.RaceAdvancementTrigger
import net.jidb.to.stars.content.ToStarsItemTagLibrary
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.criterion.InventoryChangeTrigger.TriggerInstance
import net.minecraft.advancements.criterion.ItemPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.advancements.AdvancementSubProvider
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStackTemplate
import java.util.*
import java.util.function.Consumer

class ToStarsAdvancementDataProvider : AdvancementSubProvider {

    override fun generate(registries: HolderLookup.Provider, output: Consumer<AdvancementHolder>) {
        val getter = registries.lookupOrThrow(Registries.ITEM)

        val root = Advancement.Builder.advancement()
            .display(
                ItemStackTemplate(ToStarsMod.items.test_item),
                Component.translatable("advancements.${ToStarsMod.modid}.root.title"),
                Component.translatable("advancements.${ToStarsMod.modid}.root.description"),
                Identifier.fromNamespaceAndPath(ToStarsMod.modid, "gui/advancements/background"),
                AdvancementType.TASK,
                false,
                false,
                false
            )
            .addCriterion("has_relevant", TriggerInstance.hasItems(
                ItemPredicate.Builder.item().of(getter, ToStarsItemTagLibrary.root_advancement_unlock))
            )
            .save(output, Identifier.fromNamespaceAndPath(ToStarsMod.modid, "root"))

        val uranium = Advancement.Builder.advancement()
            .parent(root)
            .display(
                ItemStackTemplate(ToStarsMod.items.uranium),
                Component.translatable("advancements.${ToStarsMod.modid}.uranium.title"),
                Component.translatable("advancements.${ToStarsMod.modid}.uranium.description"),
                null,
                AdvancementType.TASK,
                true,
                true,
                false
            )
            .addCriterion("has_uranium", TriggerInstance.hasItems(ToStarsMod.items.uranium))
            .save(output, Identifier.fromNamespaceAndPath(ToStarsMod.modid, "uranium"))

        val enrichment = Advancement.Builder.advancement()
            .parent(uranium)
            .display(
                ItemStackTemplate(ToStarsMod.items.enriched_uranium),
                Component.translatable("advancements.${ToStarsMod.modid}.enrichment.title"),
                Component.translatable("advancements.${ToStarsMod.modid}.enrichment.description"),
                null,
                AdvancementType.TASK,
                true,
                true,
                false
            )
            .addCriterion("has_enriched_uranium", TriggerInstance.hasItems(ToStarsMod.items.enriched_uranium))
            .save(output, Identifier.fromNamespaceAndPath(ToStarsMod.modid, "enrichment"))

        val nuke = Advancement.Builder.advancement()
            .parent(enrichment)
            .display(
                ItemStackTemplate(ToStarsMod.blocks.atomic_bomb.asItem()),
                Component.translatable("advancements.${ToStarsMod.modid}.nuke.title"),
                Component.translatable("advancements.${ToStarsMod.modid}.nuke.description"),
                null,
                AdvancementType.GOAL,
                true,
                true,
                false
            )
            .addCriterion("detonate", ToStarsMod.advancementTriggers.atomic_bomb.createCriterion(
                AtomicBombAdvancementTrigger.TriggerInstance(Optional.empty(), Optional.empty())
            ))
            .save(output, Identifier.fromNamespaceAndPath(ToStarsMod.modid, "nuke"))

        val nuke_race = Advancement.Builder.advancement()
            .parent(nuke)
            .display(
                ItemStackTemplate(ToStarsMod.blocks.atomic_bomb.asItem()),
                Component.translatable("advancements.${ToStarsMod.modid}.nuke_race.title"),
                Component.translatable("advancements.${ToStarsMod.modid}.nuke_race.description"),
                null,
                AdvancementType.CHALLENGE,
                true,
                true,
                true
            )
            .addCriterion("race", ToStarsMod.advancementTriggers.race.createCriterion(
                RaceAdvancementTrigger.TriggerInstance(Optional.empty(), Optional.of(nuke.id())))
            )
            .rewards(AdvancementRewards.Builder().addExperience(500))
            .save(output, Identifier.fromNamespaceAndPath(ToStarsMod.modid, "nuke_race"))
    }

}