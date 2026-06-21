package net.jidb.to.base.mixin.client;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.BiConsumer;

@Mixin(ItemModelGenerators.class)
public interface ItemModelGeneratorsAccessor {
    @Accessor("modelOutput")
    BiConsumer<Identifier, ModelInstance> to_base$getModelOutput();

    @Accessor("itemModelOutput")
    ItemModelOutput to_base$getItemModelOutput();

    @Invoker("generateFlatItem")
    void to_base$generateFlatItem(Item item, ModelTemplate modelTemplate);
}