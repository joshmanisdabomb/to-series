package net.jidb.to.stars.neoforge.client.data

import net.jidb.to.base.data.api.collection.DataCollection
import net.jidb.to.base.data.api.collection.event.RecipeDataCollectionEvent
import net.jidb.to.stars.ToStarsMod
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks

object ToStarsDataHelper {

    fun isMachine(id: Identifier, suffix: String, tier: Int? = null): Boolean {
        if (!id.path.endsWith("_$suffix")) {
            return false
        }
        return when (tier) {
            null -> true
            1 -> id.path.startsWith("copper_") || id.path.startsWith("gold_")
            2 -> id.path.startsWith("steel_") || id.path.startsWith("netherite_")
            else -> false
        }
    }

    fun createGoldMachineRecipe(upgradee: ItemLike): ShapedRecipeBuilder.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> ShapedRecipeBuilder = { collection, event ->
        pattern("ggg")
        pattern("rMr")
        pattern("iGi")
        define('r', Items.REDSTONE)
        define('M', upgradee)
        define('G', Blocks.GOLD_BLOCK)
        define('g', Items.GOLD_INGOT)
        define('i', Items.IRON_INGOT)
        event.helper.createHas(this, upgradee)
        this
    }

    fun createGoldMachineRecipe(upgradee: TagKey<Item>): ShapedRecipeBuilder.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> ShapedRecipeBuilder = { collection, event ->
        pattern("ggg")
        pattern("rMr")
        pattern("iGi")
        define('r', Items.REDSTONE)
        define('M', upgradee)
        define('G', Blocks.GOLD_BLOCK)
        define('g', Items.GOLD_INGOT)
        define('i', Items.IRON_INGOT)
        event.helper.createHas(this, upgradee)
        this
    }

    fun createSteelMachineRecipe(upgradee: ItemLike): ShapedRecipeBuilder.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> ShapedRecipeBuilder = { collection, event ->
        pattern("sss")
        pattern("sMs")
        pattern("ddd")
        define('M', upgradee)
        define('s', ToStarsMod.items.steel_ingot)
        define('d', Items.DIAMOND)
        event.helper.createHas(this, ToStarsMod.items.steel_ingot)
        this
    }

    fun createSteelMachineRecipe(upgradee: TagKey<Item>): ShapedRecipeBuilder.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> ShapedRecipeBuilder = { collection, event ->
        pattern("sss")
        pattern("sMs")
        pattern("ddd")
        define('M', upgradee)
        define('s', ToStarsMod.items.steel_ingot)
        define('d', Items.DIAMOND)
        event.helper.createHas(this, ToStarsMod.items.steel_ingot)
        this
    }

    fun createPowerBankRecipe(upgradee: ItemLike): ShapedRecipeBuilder.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> ShapedRecipeBuilder = { collection, event ->
        pattern("bpb")
        pattern("bpb")
        pattern("RMR")
        define('b', ToStarsMod.itemTags.batteries)
        define('R', Blocks.REDSTONE_BLOCK)
        define('M', upgradee)
        define('p', ToStarsMod.blocks.power_cable)
        this
    }

    fun createSolidGeneratorRecipe(upgradee: ItemLike): ShapedRecipeBuilder.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> ShapedRecipeBuilder = { collection, event ->
        pattern("e")
        pattern("m")
        pattern("f")
        define('e', Blocks.IRON_BARS)
        define('m', upgradee)
        define('f', Blocks.FURNACE)
        event.helper.createHas(this, upgradee)
        this
    }

    fun createTurbineRecipe(upgradee: ItemLike): ShapedRecipeBuilder.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> ShapedRecipeBuilder = { collection, event ->
        pattern("ccc")
        pattern("sss")
        pattern("rmr")
        define('c', ToStarsMod.blocks.power_cable)
        define('r', Items.REDSTONE)
        define('s', ToStarsMod.items.magnetic_iron)
        define('m', upgradee)
        event.helper.createHas(this, upgradee)
        this
    }

    fun createCentrifugeRecipe(upgradee: ItemLike): ShapedRecipeBuilder.(collection: DataCollection<out ItemLike>, event: RecipeDataCollectionEvent) -> ShapedRecipeBuilder = { collection, event ->
        pattern("brb")
        pattern("bmb")
        define('b', Items.GLASS_BOTTLE)
        define('r', ToStarsMod.blocks.rotor_blades)
        define('m', upgradee)
        event.helper.createHas(this, upgradee)
        this
    }

}
