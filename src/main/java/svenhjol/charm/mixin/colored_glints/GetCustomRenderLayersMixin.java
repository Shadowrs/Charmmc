package svenhjol.charm.mixin.colored_glints;

import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.colored_glints.ColoredGlintHandler;
import svenhjol.charm.module.colored_glints.ColoredGlints;

/**
 * In Forge: RenderTypeMixin
 */
@Mixin(RenderLayer.class)
public class GetCustomRenderLayersMixin {
    @Inject(
        method = "getArmorGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetArmorGlint(CallbackInfoReturnable<RenderLayer> cir) {
        if (ColoredGlints.enabled)
            cir.setReturnValue(ColoredGlintHandler.getArmorGlintRenderLayer());
    }

    @Inject(
        method = "getArmorEntityGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetArmorEntityGlint(CallbackInfoReturnable<RenderLayer> cir) {
        if (ColoredGlints.enabled)
            cir.setReturnValue(ColoredGlintHandler.getArmorEntityGlintRenderLayer());
    }

    @Inject(
        method = "getEntityGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetEntityGlint(CallbackInfoReturnable<RenderLayer> cir) {
        if (ColoredGlints.enabled)
            cir.setReturnValue(ColoredGlintHandler.getEntityGlintRenderLayer());
    }

    @Inject(
        method = "getDirectEntityGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetEntityGlintDirect(CallbackInfoReturnable<RenderLayer> cir) {
        if (ColoredGlints.enabled)
            cir.setReturnValue(ColoredGlintHandler.getDirectEntityGlintRenderLayer());
    }

    @Inject(
        method = "getGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetGlint(CallbackInfoReturnable<RenderLayer> cir) {
        if (ColoredGlints.enabled)
            cir.setReturnValue(ColoredGlintHandler.getGlintRenderLayer());
    }

    @Inject(
        method = "getDirectGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetGlintDirect(CallbackInfoReturnable<RenderLayer> cir) {
        if (ColoredGlints.enabled)
            cir.setReturnValue(ColoredGlintHandler.getDirectGlintRenderLayer());
    }
}