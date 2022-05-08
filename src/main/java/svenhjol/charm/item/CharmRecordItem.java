package svenhjol.charm.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.RecordItem;
import svenhjol.charm.loader.CharmModule;

public class CharmRecordItem extends RecordItem implements ICharmItem {
    protected CharmModule module;

    protected CharmRecordItem(CharmModule module, int redstoneStrength, SoundEvent soundEvent, Properties properties) {
        super(redstoneStrength, soundEvent, properties);
        this.module = module;
    }

    @Override
    public boolean enabled() {
        return module.isEnabled();
    }
}
