package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;

public interface EntityTickCallback {
    Event<EntityTickCallback> EVENT = EventFactory.createArrayBacked(EntityTickCallback.class, (listeners) -> (entity) -> {
        for (EntityTickCallback listener : listeners) {
            listener.interact(entity);
        }
    });

    void interact(LivingEntity entity);
}