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

/**
 * [BlockItemLibrary] implementation holding the item of each block of this mod.
 *
 * Most of them carry a tooltip describing what the machine can do, which is written into the item's lore as it is registered rather than worked out each time the tooltip is drawn.
 */
object ToStarsBlockItemLibrary : BlockItemLibrary(ToStarsMod.MOD_ID, ToStarsMod.blocks) {

    /**
     * The item of the nuclear fire block, which no player can obtain.
     */
    val nuclear_fire by this()

    /**
     * The item of the tier one machine enclosure.
     */
    val copper_machine_enclosure by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE)))) }

    /**
     * The item of the tier one and a half machine enclosure.
     */
    val gold_machine_enclosure by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE_5)))) }

    /**
     * The item of the tier one power bank, which keeps whatever energy the block held.
     */
    val copper_power_bank by this { block, initial -> BatteryBlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE)))) }

    /**
     * The item of the tier one and a half power bank, which keeps whatever energy the block held.
     */
    val gold_power_bank by this { block, initial -> BatteryBlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE_5)))) }

    /**
     * The item of the tier one solid generator.
     */
    val copper_solid_generator by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE) + ToStarsTooltipEngine.getGeneratorInfo(MachineTier.ONE)))) }

    /**
     * The item of the tier one and a half solid generator.
     */
    val gold_solid_generator by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE_5) + ToStarsTooltipEngine.getGeneratorInfo(MachineTier.ONE_5)))) }

    /**
     * The item of the boiler.
     */
    val boiler by this()

    /**
     * The item of the tier one turbine.
     */
    val copper_turbine by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE) + ToStarsTooltipEngine.getTurbineInfo(MachineTier.ONE)))) }

    /**
     * The item of the tier one and a half turbine.
     */
    val gold_turbine by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE_5) + ToStarsTooltipEngine.getTurbineInfo(MachineTier.ONE_5)))) }

    /**
     * The item of the tier one centrifuge, which keeps whatever energy the block held.
     */
    val copper_centrifuge by this { block, initial -> BatteryBlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE) + ToStarsTooltipEngine.getProcessorInfo(MachineTier.ONE)))) }

    /**
     * The item of the tier one and a half centrifuge, which keeps whatever energy the block held.
     */
    val gold_centrifuge by this { block, initial -> BatteryBlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE_5) + ToStarsTooltipEngine.getProcessorInfo(MachineTier.ONE_5)))) }

    /**
     * The item of the power cable, whose tooltip names how much energy is lost across it.
     */
    val power_cable by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getPowerCableInfo(block() as ToEnergyCableBlock)))) }

    /**
     * The item of the creative power source, which gives out endless energy wherever it is held.
     */
    val creative_power_source by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(ToStarsMod.itemComponents.infinite_energy, InfiniteEnergyItemComponent)) }

}
