package svenhjol.charm.building.module;

import net.minecraft.item.DyeColor;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.building.block.MoonstoneBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.List;

@Module(mod = Charm.MOD_ID, category = CharmCategories.BUILDING,
    description = "A decorative block made from 4 moonstones of the same color.")
public class BlockOfMoonstone extends MesonModule
{
    public static List<MoonstoneBlock> blocks = new ArrayList<>();

    @Config(name = "Moon glow", description = "If true, Moonstone blocks change their light levels according to the moon phase.")
    public static boolean moonglow = true;

    @Override
    public void init()
    {
        for (DyeColor value : DyeColor.values()) {
            blocks.add(new MoonstoneBlock(this, value));
        }
    }
}
