package net.jidb.to.stars.content

import net.jidb.to.base.api.helper.IdentifierHelper.identifier
import net.jidb.to.base.api.info.TooltipEngine
import net.jidb.to.base.pub.block.ToEnergyCableBlock
import net.jidb.to.base.pub.library.BlockItemLibrary
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.info.MachineTier
import net.jidb.to.stars.info.ToStarsTooltipEngine
import net.jidb.to.stars.item.BatteryBlockItem
import net.jidb.to.stars.item.component.InfiniteEnergyItemComponent
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item

object ToStarsBlockItemLibrary : BlockItemLibrary(ToStarsMod.MOD_ID, ToStarsMod.blocks) {

    val nuclear_fire by this()

    val copper_machine_enclosure by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE)))) }

    val gold_machine_enclosure by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE_5)))) }

    val copper_power_bank by this { block, initial -> BatteryBlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE)))) }

    val gold_power_bank by this { block, initial -> BatteryBlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE_5)))) }

    val copper_solid_generator by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE) + ToStarsTooltipEngine.getGeneratorInfo(MachineTier.ONE)))) }

    val gold_solid_generator by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE_5) + ToStarsTooltipEngine.getGeneratorInfo(MachineTier.ONE_5)))) }

    val boiler by this()

    val copper_turbine by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE) + ToStarsTooltipEngine.getTurbineInfo(MachineTier.ONE)))) }

    val gold_turbine by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE_5) + ToStarsTooltipEngine.getTurbineInfo(MachineTier.ONE_5)))) }

    val copper_centrifuge by this { block, initial -> BatteryBlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE) + ToStarsTooltipEngine.getProcessorInfo(MachineTier.ONE)))) }

    val gold_centrifuge by this { block, initial -> BatteryBlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE_5) + ToStarsTooltipEngine.getProcessorInfo(MachineTier.ONE_5)))) }

    val power_cable by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getPowerCableInfo(block() as ToEnergyCableBlock)))) }

    val creative_power_source by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(ToStarsMod.itemComponents.infinite_energy, InfiniteEnergyItemComponent)) }

}
