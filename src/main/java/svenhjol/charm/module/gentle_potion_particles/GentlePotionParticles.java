package svenhjol.charm.module.gentle_potion_particles;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Potion effect particles are much less obtrusive by default and can optionally be entirely hidden.")
public class GentlePotionParticles extends CharmModule {
    @Config(name = "Translucent particles", description = "If true, translucent particles will be rendered.  If false, no particles will be rendered.")
    public static boolean translucent = true;

    public static boolean tryRenderParticles(Level level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        if (!Charm.LOADER.isEnabled(GentlePotionParticles.class)) return false;

        if (translucent) {
            level.addParticle(ParticleTypes.AMBIENT_ENTITY_EFFECT, x, y, z, velocityX, velocityY, velocityZ);
        }

        return true;
    }
}
