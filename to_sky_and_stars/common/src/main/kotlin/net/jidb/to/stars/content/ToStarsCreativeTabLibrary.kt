package net.jidb.to.stars.content

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.library.SimpleRegistryLibrary
import net.jidb.to.base.pub.item.component.ToEnergyItemData
import net.jidb.to.base.service.Services
import net.jidb.to.stars.ToStarsMod
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack

object ToStarsCreativeTabLibrary : SimpleRegistryLibrary<CreativeModeTab>(ToStarsMod.MOD_ID) {

    override val registry = BuiltInRegistries.CREATIVE_MODE_TAB

    val tab by this(::i) { entry ->
        Services.platform.creativeTabs.builder { parameters, output ->
            output(ItemStack(ToStarsMod.blocks.uranium_ore))
            output(ItemStack(ToStarsMod.blocks.deepslate_uranium_ore))
            output(ItemStack(ToStarsMod.items.uranium_nugget))
            output(ItemStack(ToStarsMod.items.uranium))
            output(ItemStack(ToStarsMod.blocks.uranium_block))
            output(ItemStack(ToStarsMod.items.enriched_uranium_nugget))
            output(ItemStack(ToStarsMod.items.enriched_uranium))
            output(ItemStack(ToStarsMod.blocks.enriched_uranium_block))
            output(ItemStack(ToStarsMod.items.heavy_uranium_nugget))
            output(ItemStack(ToStarsMod.items.heavy_uranium))
            output(ItemStack(ToStarsMod.blocks.heavy_uranium_block))
            output(ItemStack(ToStarsMod.blocks.heavy_uranium_shielding))
            output(ItemStack(ToStarsMod.items.magnetic_iron))
            output(ItemStack(ToStarsMod.blocks.copper_machine_enclosure))
            output(ItemStack(ToStarsMod.blocks.gold_machine_enclosure))
            output(ItemStack(ToStarsMod.blocks.copper_solid_generator))
            output(ItemStack(ToStarsMod.blocks.gold_solid_generator))
            output(ItemStack(ToStarsMod.blocks.rotor_blades))
            output(ItemStack(ToStarsMod.blocks.copper_turbine))
            output(ItemStack(ToStarsMod.blocks.gold_turbine))
            output(ItemStack(ToStarsMod.blocks.copper_power_bank))
            output(ItemStack(ToStarsMod.blocks.copper_power_bank).also {
                it.set(ToBaseMod.itemComponents.energy_data, ToEnergyItemData(ToStarsMod.blocks.copper_power_bank.machine.bankStorage, ToStarsMod.blocks.copper_power_bank.machine.bankStorage, ToStarsMod.blocks.copper_power_bank.machine.maxInput, ToStarsMod.blocks.copper_power_bank.machine.maxOutput))
            })
            output(ItemStack(ToStarsMod.blocks.gold_power_bank))
            output(ItemStack(ToStarsMod.blocks.gold_power_bank).also {
                it.set(ToBaseMod.itemComponents.energy_data, ToEnergyItemData(ToStarsMod.blocks.gold_power_bank.machine.bankStorage, ToStarsMod.blocks.gold_power_bank.machine.bankStorage, ToStarsMod.blocks.gold_power_bank.machine.maxInput, ToStarsMod.blocks.gold_power_bank.machine.maxOutput))
            })
            output(ItemStack(ToStarsMod.items.copper_battery))
            output(ItemStack(ToStarsMod.items.copper_battery).also {
                it.set(ToBaseMod.itemComponents.energy_data, ToEnergyItemData(ToStarsMod.items.copper_battery.machine.batteryStorage, ToStarsMod.items.copper_battery.machine.batteryStorage, ToStarsMod.items.copper_battery.machine.maxInput, ToStarsMod.items.copper_battery.machine.maxOutput))
            })
            output(ItemStack(ToStarsMod.items.gold_battery))
            output(ItemStack(ToStarsMod.items.gold_battery).also {
                it.set(ToBaseMod.itemComponents.energy_data, ToEnergyItemData(ToStarsMod.items.gold_battery.machine.batteryStorage, ToStarsMod.items.gold_battery.machine.batteryStorage, ToStarsMod.items.gold_battery.machine.maxInput, ToStarsMod.items.gold_battery.machine.maxOutput))
            })
            output(ItemStack(ToStarsMod.blocks.power_cable))
            output(ItemStack(ToStarsMod.blocks.heat_pipe))
            output(ItemStack(ToStarsMod.blocks.creative_power_source))
            output(ItemStack(ToStarsMod.blocks.atomic_bomb))
            output(ItemStack(ToStarsMod.blocks.nuclear_waste))
            output(ItemStack(ToStarsMod.items.test_item))
        }
            .title(Component.translatable("itemgroup.${modid}.${entry.name}"))
            .icon { ItemStack(ToStarsMod.items.test_item) }
            .build()
    }

}