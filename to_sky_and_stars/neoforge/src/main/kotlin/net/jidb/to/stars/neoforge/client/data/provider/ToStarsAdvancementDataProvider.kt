package net.jidb.to.stars.neoforge.client.data.provider

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.helper.IdentifierHelper.identifier
import net.jidb.to.base.pub.item.component.ToEnergyItemComponentData
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.advancements.AtomicBombAdvancementTrigger
import net.jidb.to.stars.advancements.RaceAdvancementTrigger
import net.jidb.to.stars.block.EnergyStorageBlock
import net.jidb.to.stars.block.HeatGeneratorBlock
import net.jidb.to.stars.block.ProcessorBlock
import net.jidb.to.stars.block.TurbineBlock
import net.jidb.to.stars.content.ToStarsItemTagLibrary
import net.jidb.to.stars.info.MachineTier
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.predicates.ItemPredicate
import net.minecraft.advancements.triggers.InventoryChangeTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentPatch
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
            .addCriterion("has_relevant", InventoryChangeTrigger.TriggerInstance.hasItems(
                ItemPredicate.Builder.item().of(getter, ToStarsItemTagLibrary.root_advancement_unlock))
            )
            .save(output, Identifier.fromNamespaceAndPath(ToStarsMod.modid, "root"))

        val copper_machine = Advancement.Builder.advancement()
            .parent(root)
            .display(
                ItemStackTemplate(ToStarsMod.blocks.copper_machine_enclosure.asItem()),
                Component.translatable("advancements.${ToStarsMod.modid}.copper_machine.title"),
                Component.translatable("advancements.${ToStarsMod.modid}.copper_machine.description"),
                null,
                AdvancementType.TASK,
                true,
                true,
                false
            )
            .addCriterion("has_copper_enclosure", InventoryChangeTrigger.TriggerInstance.hasItems(ToStarsMod.blocks.copper_machine_enclosure))
            .save(output, Identifier.fromNamespaceAndPath(ToStarsMod.modid, "copper_machine"))

        val generator = Advancement.Builder.advancement()
            .parent(copper_machine)
            .display(
                ItemStackTemplate(ToStarsMod.blocks.copper_solid_generator.asItem()),
                Component.translatable("advancements.${ToStarsMod.modid}.generator.title"),
                Component.translatable("advancements.${ToStarsMod.modid}.generator.description"),
                null,
                AdvancementType.TASK,
                true,
                true,
                false
            )
            .addCriterion("has_generator", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(getter, ToStarsMod.itemTags.heat_generators)))
            .save(output, Identifier.fromNamespaceAndPath(ToStarsMod.modid, "generator"))

        val turbine = Advancement.Builder.advancement()
            .parent(generator)
            .display(
                ItemStackTemplate(ToStarsMod.blocks.copper_turbine.asItem()),
                Component.translatable("advancements.${ToStarsMod.modid}.turbine.title"),
                Component.translatable("advancements.${ToStarsMod.modid}.turbine.description"),
                null,
                AdvancementType.TASK,
                true,
                true,
                false
            )
            .addCriterion("has_turbine", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(getter, ToStarsMod.itemTags.turbines)))
            .save(output, Identifier.fromNamespaceAndPath(ToStarsMod.modid, "turbine"))

        val power_bank = Advancement.Builder.advancement()
            .parent(turbine)
            .display(
                ItemStackTemplate(ToStarsMod.blocks.copper_power_bank.asItem(), DataComponentPatch.builder().set(ToBaseMod.itemComponents.energy_data, ToEnergyItemComponentData(1L, 2L, 0L, 0L)).build()),
                Component.translatable("advancements.${ToStarsMod.modid}.power_bank.title"),
                Component.translatable("advancements.${ToStarsMod.modid}.power_bank.description"),
                null,
                AdvancementType.TASK,
                true,
                true,
                false
            )
            .addCriterion("has_power_bank", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(getter, ToStarsMod.itemTags.power_banks)))
            .save(output, Identifier.fromNamespaceAndPath(ToStarsMod.modid, "power_bank"))

        val gold_machine = Advancement.Builder.advancement()
            .parent(copper_machine)
            .display(
                ItemStackTemplate(ToStarsMod.blocks.gold_solid_generator.asItem()),
                Component.translatable("advancements.${ToStarsMod.modid}.gold_machine.title"),
                Component.translatable("advancements.${ToStarsMod.modid}.gold_machine.description"),
                null,
                AdvancementType.TASK,
                true,
                true,
                false
            )
            .addCriterion("has_gold_machine", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(getter, ToStarsMod.itemTags.gold_machines)))
            .save(output, Identifier.fromNamespaceAndPath(ToStarsMod.modid, "gold_machine"))

        val gold_machine_all = Advancement.Builder.advancement()
            .parent(gold_machine)
            .display(
                ItemStackTemplate(ToStarsMod.blocks.gold_power_bank.asItem(), DataComponentPatch.builder().set(ToBaseMod.itemComponents.energy_data, ToEnergyItemComponentData(1L, 1L, 0L, 0L)).build()),
                Component.translatable("advancements.${ToStarsMod.modid}.gold_machine_all.title"),
                Component.translatable("advancements.${ToStarsMod.modid}.gold_machine_all.description"),
                null,
                AdvancementType.GOAL,
                true,
                true,
                false
            )
            .apply { ToStarsMod.blocks.values.filter { (it is HeatGeneratorBlock && it.machine == MachineTier.ONE_5) || (it is TurbineBlock && it.machine == MachineTier.ONE_5) || (it is EnergyStorageBlock && it.machine == MachineTier.ONE_5) || (it is ProcessorBlock && it.machine == MachineTier.ONE_5) }.forEach {
                addCriterion("has_gold_${it.identifier.path}", InventoryChangeTrigger.TriggerInstance.hasItems(it))
            } }
            .save(output, Identifier.fromNamespaceAndPath(ToStarsMod.modid, "gold_machine_all"))

        val centrifuge = Advancement.Builder.advancement()
            .parent(copper_machine)
            .display(
                ItemStackTemplate(ToStarsMod.blocks.copper_centrifuge.asItem()),
                Component.translatable("advancements.${ToStarsMod.modid}.centrifuge.title"),
                Component.translatable("advancements.${ToStarsMod.modid}.centrifuge.description"),
                null,
                AdvancementType.TASK,
                true,
                true,
                false
            )
            .addCriterion("has_centrifuge", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(getter, ToStarsMod.itemTags.centrifuges)))
            .save(output, Identifier.fromNamespaceAndPath(ToStarsMod.modid, "centrifuge"))

        val enrichment = Advancement.Builder.advancement()
            .parent(centrifuge)
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
            .addCriterion("has_enriched_uranium", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(getter, ToStarsMod.itemTags.enriched_uranium)))
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
            .rewards(AdvancementRewards.Builder().addExperience(500).addLootTable(ToStarsMod.lootTables.advancement_nuke_race))
            .save(output, Identifier.fromNamespaceAndPath(ToStarsMod.modid, "nuke_race"))
    }

}