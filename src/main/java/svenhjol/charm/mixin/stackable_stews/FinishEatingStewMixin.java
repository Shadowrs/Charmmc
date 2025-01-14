package svenhjol.charm.mixin.stackable_stews;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.stackable_stews.StackableStews;

@Mixin({BowlFoodItem.class, SuspiciousStewItem.class})
public class FinishEatingStewMixin {
    /**
     * Defer to tryEatStewStack when mushroom stew is eaten.
     * If the check passes, return the decremented stack.
     */
    @Inject(
        method = "finishUsingItem",
        at = @At("RETURN"),
        cancellable = true
    )
    private void hookFinishUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
        if (StackableStews.changeReturnedItem(itemStack, cir.getReturnValue())) {
            cir.setReturnValue(itemStack);
        }
    }
}
