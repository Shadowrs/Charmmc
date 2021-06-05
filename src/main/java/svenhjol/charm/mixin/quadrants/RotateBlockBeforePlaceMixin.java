package svenhjol.charm.mixin.quadrants;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.quadrants.Quadrants;

@Mixin(BlockItem.class)
public abstract class RotateBlockBeforePlaceMixin {
    @Shadow @Nullable protected abstract BlockState getPlacementState(ItemPlacementContext context);

    /**
     * Delegate the block placement to the Quadrants module
     * so it can change the rotation before it is placed.
     */
    @Redirect(
        method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/BlockItem;getPlacementState(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/block/BlockState;"
        )
    )
    private BlockState hookPlace(BlockItem blockItem, ItemPlacementContext context) {
        BlockState state = this.getPlacementState(context); // vanilla behavior, pass this to Quadrants
        return Quadrants.getRotatedBlockState(state, context);
    }
}