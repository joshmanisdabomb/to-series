package net.jidb.to.stars.mixin;

import net.jidb.to.stars.block.entity.KilnBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin extends BaseContainerBlockEntity {

    @Shadow
    @Final
    @Mutable
    private RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> quickCheck;

    protected AbstractFurnaceBlockEntityMixin(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void construct(BlockEntityType type, BlockPos worldPosition, BlockState blockState, RecipeType recipeType, CallbackInfo ci) {
        RecipeManager.CachedCheck<SingleRecipeInput, SmeltingRecipe> smeltCheck = RecipeManager.createCheck(RecipeType.SMELTING);
        List<RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe>> otherCheck = new ArrayList<>();
        otherCheck.add(RecipeManager.createCheck(RecipeType.SMOKING));
        otherCheck.add(RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING));
        otherCheck.add(RecipeManager.createCheck(RecipeType.BLASTING));
        if ((Object)this instanceof KilnBlockEntity) {
            quickCheck = (RecipeManager.CachedCheck<SingleRecipeInput, AbstractCookingRecipe>) (input, level) -> {
                Optional<RecipeHolder<SmeltingRecipe>> recipe = smeltCheck.getRecipeFor(input, level);
                if (recipe.isPresent()) {
                    if (otherCheck.stream().anyMatch(check -> check.getRecipeFor(input, level).isPresent())) {
                        return Optional.empty();
                    }
                    RecipeHolder<SmeltingRecipe> holder = recipe.get();

                    SmeltingRecipe original = holder.value();
                    SmeltingRecipe modified = new SmeltingRecipe(
                        new Recipe.CommonInfo(original.showNotification()),
                        new AbstractCookingRecipe.CookingBookInfo(original.category(), original.group()),
                        original.input(),
                        ItemStackTemplate.fromStack(original.assemble(input)),
                        original.experience(),
                        original.cookingTime() / 2
                    );

                    return Optional.of(new RecipeHolder(holder.id(), modified));
                }
                return Optional.empty();
            };
        }
    }

}
