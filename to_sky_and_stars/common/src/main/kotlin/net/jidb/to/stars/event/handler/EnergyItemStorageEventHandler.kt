package net.jidb.to.stars.event.handler

import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.event.EventHandler
import net.jidb.to.base.pub.event.item.ModifyItemComponentEventContext
import net.jidb.to.base.pub.item.component.ToEnergyItemData
import net.jidb.to.stars.ToStarsMod

class EnergyItemStorageEventHandler : EventHandler<ModifyItemComponentEventContext, Unit>() {

    override fun invoke(context: ModifyItemComponentEventContext) {
        context.modify(ToStarsMod.blocks.copper_power_bank) { components, item -> components.set(ToBaseMod.itemComponents.energy_data, ToEnergyItemData(0, ToStarsMod.blocks.copper_power_bank.machine.bankStorage, ToStarsMod.blocks.copper_power_bank.machine.maxInput, ToStarsMod.blocks.copper_power_bank.machine.maxOutput)) }
        context.modify(ToStarsMod.blocks.gold_power_bank) { components, item -> components.set(ToBaseMod.itemComponents.energy_data, ToEnergyItemData(0, ToStarsMod.blocks.gold_power_bank.machine.bankStorage, ToStarsMod.blocks.gold_power_bank.machine.maxInput, ToStarsMod.blocks.gold_power_bank.machine.maxOutput)) }
        context.modify(ToStarsMod.items.copper_battery) { components, item -> components.set(ToBaseMod.itemComponents.energy_data, ToEnergyItemData(0, ToStarsMod.items.copper_battery.machine.batteryStorage, ToStarsMod.items.copper_battery.machine.maxInput, ToStarsMod.items.copper_battery.machine.maxOutput)) }
        context.modify(ToStarsMod.items.gold_battery) { components, item -> components.set(ToBaseMod.itemComponents.energy_data, ToEnergyItemData(0, ToStarsMod.items.gold_battery.machine.batteryStorage, ToStarsMod.items.gold_battery.machine.maxInput, ToStarsMod.items.gold_battery.machine.maxOutput)) }
    }

}
