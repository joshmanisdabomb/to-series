package net.jidb.to.stars.mixin;

import net.jidb.to.stars.block.entity.KilnBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
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

/**
 * Mixin class for injecting modifications into the {@link net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity} class.
 * This class specifically modifies the behavior of certain furnace-like block entities, such as injecting a custom recipe check for the kiln block entity.
 *
 * This mixin relies on the capabilities provided in the {@link net.jidb.to.stars.block.entity.KilnBlockEntity} class and {@link net.jidb.to.stars.inventory.menu.KilnMenu} class.
 *
 * @since 0.2.0
 */
@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin extends BaseContainerBlockEntity {

    /**
     * This mixin field points to {@link net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity#quickCheck}.
     * It is replaced by {@link net.jidb.to.stars.mixin.AbstractFurnaceBlockEntityMixin#construct} with a kiln recipe checker built by {@code KilnMenu.createCustomCheck}.
     *
     * @see RecipeManager.CachedCheck
     * @since 0.2.0
     **/
    @Shadow
    @Final
    @Mutable
    private RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> quickCheck;

    /**
     * Fake constructor for the {@link net.jidb.to.stars.mixin.AbstractFurnaceBlockEntityMixin} mixin class.
     *
     * @param type The type of the block entity being initialized.
     * @param worldPosition The position of the block entity in the world.
     * @param blockState The block state associated with the block entity.
     * @since 0.2.0
     */
    protected AbstractFurnaceBlockEntityMixin(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
    }

    /**
     * Injects custom initialization logic for modifying the behavior of specific block entities, such as the kiln block entity.
     * Replaces the default recipe check functionality with a custom implementation tailored to the kiln's requirements.
     *
     * @param type The type of the block entity being initialized.
     * @param worldPosition The world position of the block entity.
     * @param blockState The block state associated with the block entity.
     * @param recipeType The recipe type used by the block entity for processing items.
     * @param ci The callback information for the mixin injection point.
     * @since 0.2.0
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    public void construct(BlockEntityType type, BlockPos worldPosition, BlockState blockState, RecipeType recipeType, CallbackInfo ci) {
        if ((Object)this instanceof KilnBlockEntity) {
            quickCheck = KilnBlockEntity.Companion.createCustomCheck();
        }
    }

}
