package svenhjol.charm.block;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;

public class CharmBlockItem extends BlockItem {
    private final BiConsumer<ItemStack, Boolean> inventoryTickConsumer;

    public CharmBlockItem(ICharmBlock block, Properties settings) {
        super((Block) block, settings);

        // callable inventory tick consumer from the block
        this.inventoryTickConsumer = block.getInventoryTickConsumer();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entityIn, int itemSlot, boolean isSelected) {
        if (inventoryTickConsumer != null) {
            inventoryTickConsumer.accept(stack, isSelected);
        }
    }
}
