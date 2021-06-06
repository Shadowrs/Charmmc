package svenhjol.charm.module.mooblooms;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.EntityHelper;
import svenhjol.charm.module.mooblooms.MoobloomEntityRenderer;
import svenhjol.charm.module.mooblooms.Mooblooms;

public class MoobloomsClient extends CharmClientModule {
    public static ModelLayerLocation LAYER;

    public MoobloomsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        LAYER = EntityHelper.registerEntityModelLayer(svenhjol.charm.module.mooblooms.Mooblooms.ID, CowModel.createBodyLayer().bakeRoot());
        EntityRendererRegistry.INSTANCE.register(Mooblooms.MOOBLOOM, MoobloomEntityRenderer::new);
    }
}
