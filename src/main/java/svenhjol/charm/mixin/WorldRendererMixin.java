package svenhjol.charm.mixin;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.client.SnowStormsClient;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow private ClientWorld world;

    private float gradient;

    @Inject(
        method = "renderWeather",
        at = @At("HEAD")
    )
    private void hookRenderWeather(LightmapTextureManager manager, float f, double d, double e, double g, CallbackInfo ci) {
        gradient = f;
    }

    @Inject(
        method = "renderWeather",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/BufferBuilder;begin(Lnet/minecraft/client/render/VertexFormat$DrawMode;Lnet/minecraft/client/render/VertexFormat;)V",
            shift = At.Shift.BEFORE
        )
    )
    private void hookRenderWeatherTexture(LightmapTextureManager manager, float f, double d, double e, double g, CallbackInfo ci) {
        SnowStormsClient.tryHeavySnowTexture(world, gradient);
    }
}
