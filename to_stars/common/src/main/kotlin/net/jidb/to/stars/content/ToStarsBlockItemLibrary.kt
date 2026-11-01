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
     * An exclusion entry for the nuclear fire block.
     * @see ToStarsBlockLibrary.nuclear_fire
     * @since 0.1.0
     */
    val nuclear_fire by this()

    /**
     * The item of the tier 1 machine enclosure. Contains machine tier tooltip text.
     * @see ToStarsBlockLibrary.copper_machine_enclosure
     * @since 0.1.0
     */
    val copper_machine_enclosure by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE)))) }

    /**
     * The item of the tier 1.5 machine enclosure. Contains machine tier tooltip text.
     * @see ToStarsBlockLibrary.gold_machine_enclosure
     * @since 0.1.0
     */
    val gold_machine_enclosure by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE_5)))) }

    /**
     * The item of the tier 2 machine enclosure. Contains machine tier tooltip text.
     * @see ToStarsBlockLibrary.steel_machine_enclosure
     * @since 0.2.0
     */
    val steel_machine_enclosure by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.TWO)))) }

    /**
     * The item of the tier 2.5 machine enclosure. Contains machine tier tooltip text.
     * @see ToStarsBlockLibrary.netherite_machine_enclosure
     * @since 0.2.0
     */
    val netherite_machine_enclosure by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.TWO_5)))) }

    /**
     * The item of the tier 1 power bank. Displays information about energy contents in the durability bar render.
     * Contains machine tier and info tooltip text.
     * @see ToStarsBlockLibrary.copper_power_bank
     * @see BatteryBlockItem
     * @since 0.1.0
     */
    val copper_power_bank by this { block, initial -> BatteryBlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE)))) }

    /**
     * The item of the tier 1.5 power bank. Displays information about energy contents in the durability bar render.
     * Contains machine tier and info tooltip text.
     * @see ToStarsBlockLibrary.gold_power_bank
     * @see BatteryBlockItem
     * @since 0.1.0
     */
    val gold_power_bank by this { block, initial -> BatteryBlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE_5)))) }

    /**
     * The item of the tier 2 power bank. Displays information about energy contents in the durability bar render.
     * Contains machine tier and info tooltip text.
     * @see ToStarsBlockLibrary.steel_power_bank
     * @see BatteryBlockItem
     * @since 0.2.0
     */
    val steel_power_bank by this { block, initial -> BatteryBlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.TWO)))) }

    /**
     * The item of the tier 2.5 power bank. Displays information about energy contents in the durability bar render.
     * Contains machine tier and info tooltip text.
     * @see ToStarsBlockLibrary.netherite_power_bank
     * @see BatteryBlockItem
     * @since 0.2.0
     */
    val netherite_power_bank by this { block, initial -> BatteryBlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.TWO_5)))) }

    /**
     * The item of the tier 1 solid generator. Contains machine tier and info tooltip text.
     * @see ToStarsBlockLibrary.copper_solid_generator
     * @since 0.1.0
     */
    val copper_solid_generator by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE) + ToStarsTooltipEngine.getGeneratorInfo(MachineTier.ONE)))) }

    /**
     * The item of the tier 1.5 solid generator. Contains machine tier and info tooltip text.
     * @see ToStarsBlockLibrary.gold_solid_generator
     * @since 0.1.0
     */
    val gold_solid_generator by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE_5) + ToStarsTooltipEngine.getGeneratorInfo(MachineTier.ONE_5)))) }

    /**
     * The item of the tier 2 solid generator. Contains machine tier and info tooltip text.
     * @see ToStarsBlockLibrary.steel_solid_generator
     * @since 0.2.0
     */
    val steel_solid_generator by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.TWO) + ToStarsTooltipEngine.getGeneratorInfo(MachineTier.TWO)))) }

    /**
     * The item of the tier 2.5 solid generator. Contains machine tier and info tooltip text.
     * @see ToStarsBlockLibrary.netherite_solid_generator
     * @since 0.2.0
     */
    val netherite_solid_generator by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.TWO_5) + ToStarsTooltipEngine.getGeneratorInfo(MachineTier.TWO_5)))) }

    /**
     * An exclusion entry for the boiler.
     * @see ToStarsBlockLibrary.boiler
     * @since 0.1.0
     */
    val boiler by this()

    /**
     * The item of the tier 1 turbine. Contains machine tier and info tooltip text.
     * @see ToStarsBlockLibrary.copper_turbine
     * @since 0.1.0
     */
    val copper_turbine by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE) + ToStarsTooltipEngine.getTurbineInfo(MachineTier.ONE)))) }

    /**
     * The item of the tier 1.5 turbine. Contains machine tier and info tooltip text.
     * @see ToStarsBlockLibrary.gold_turbine
     * @since 0.1.0
     */
    val gold_turbine by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE_5) + ToStarsTooltipEngine.getTurbineInfo(MachineTier.ONE_5)))) }

    /**
     * The item of the tier 2 turbine. Contains machine tier and info tooltip text.
     * @see ToStarsBlockLibrary.steel_turbine
     * @since 0.2.0
     */
    val steel_turbine by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.TWO) + ToStarsTooltipEngine.getTurbineInfo(MachineTier.TWO)))) }

    /**
     * The item of the tier 2.5 turbine. Contains machine tier and info tooltip text.
     * @see ToStarsBlockLibrary.netherite_turbine
     * @since 0.2.0
     */
    val netherite_turbine by this { block, initial -> BlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.TWO_5) + ToStarsTooltipEngine.getTurbineInfo(MachineTier.TWO_5)))) }

    /**
     * The item of the tier 1 centrifuge. Displays information about energy contents in the durability bar render.
     * Contains machine tier and info tooltip text.
     * @see ToStarsBlockLibrary.copper_centrifuge
     * @see BatteryBlockItem
     * @since 0.1.0
     */
    val copper_centrifuge by this { block, initial -> BatteryBlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE) + ToStarsTooltipEngine.getProcessorInfo(MachineTier.ONE)))) }

    /**
     * The item of the tier 1.5 centrifuge. Displays information about energy contents in the durability bar render.
     * Contains machine tier and info tooltip text.
     * @see ToStarsBlockLibrary.gold_centrifuge
     * @see BatteryBlockItem
     * @since 0.1.0
     */
    val gold_centrifuge by this { block, initial -> BatteryBlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.ONE_5) + ToStarsTooltipEngine.getProcessorInfo(MachineTier.ONE_5)))) }

    /**
     * The item of the tier 2 centrifuge. Displays information about energy contents in the durability bar render.
     * Contains machine tier and info tooltip text.
     * @see ToStarsBlockLibrary.steel_centrifuge
     * @see BatteryBlockItem
     * @since 0.2.0
     */
    val steel_centrifuge by this { block, initial -> BatteryBlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.TWO) + ToStarsTooltipEngine.getProcessorInfo(MachineTier.TWO)))) }

    /**
     * The item of the tier 2.5 centrifuge. Displays information about energy contents in the durability bar render.
     * Contains machine tier and info tooltip text.
     * @see ToStarsBlockLibrary.netherite_centrifuge
     * @see BatteryBlockItem
     * @since 0.2.0
     */
    val netherite_centrifuge by this { block, initial -> BatteryBlockItem(block!!(), Item.Properties()
        .setId(ResourceKey.create(Registries.ITEM, block().identifier))
        .useBlockDescriptionPrefix()
        .component(DataComponents.LORE, TooltipEngine.asItemLore(ToStarsTooltipEngine.getMachineInfo(MachineTier.TWO_5) + ToStarsTooltipEngine.getProcessorInfo(MachineTier.TWO_5)))) }

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
