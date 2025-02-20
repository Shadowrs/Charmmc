package svenhjol.charm.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import svenhjol.charm.loader.CharmModule;

public class CharmPressurePlate extends PressurePlateBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmPressurePlate(CharmModule module, String name, Sensitivity activationRule, Properties settings) {
        super(activationRule, settings);

        this.register(module, name);
        this.module = module;
        this.setBurnTime(300);
    }

    public CharmPressurePlate(CharmModule module, String name, Block block) {
        this(module, name, Sensitivity.EVERYTHING, Properties.of(Material.WOOD, block.defaultMaterialColor())
            .noCollission()
            .strength(0.5F)
            .sound(SoundType.WOOD));
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (enabled())
            super.fillItemCategory(group, items);
    }

    @Override
    public boolean enabled() {
        return module.isEnabled();
    }
}
