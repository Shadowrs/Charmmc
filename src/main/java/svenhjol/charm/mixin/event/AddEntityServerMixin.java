package svenhjol.charm.mixin.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.api.event.AddEntityCallback;

/**
 * Be aware that this event is also fired on the server side
 * by the {@link AddEntityManagerMixin}.
 */
@Mixin(ServerLevel.class)
public class AddEntityServerMixin {
    /**
     * Fires the {@link AddEntityCallback} event when any entity
     * is added to the server world.
     */
    @Inject(
        method = "addEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/entity/PersistentEntitySectionManager;addNewEntity(Lnet/minecraft/world/level/entity/EntityAccess;)Z"
        ),
        cancellable = true
    )
    private void hookAddEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        InteractionResult result = AddEntityCallback.EVENT.invoker().interact(entity);
        if (result == InteractionResult.FAIL) {
            cir.setReturnValue(false);
        }
    }

    /**
     * Fires the {@link AddEntityCallback} event when a player
     * joins the game on the server side.
     */
    @Inject(
        method = "addPlayer",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookAddPlayer(ServerPlayer player, CallbackInfo ci) {
        InteractionResult result = AddEntityCallback.EVENT.invoker().interact(player);
        if (result == InteractionResult.FAIL) {
            ci.cancel();
        }
    }
}
