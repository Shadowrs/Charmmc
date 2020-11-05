package svenhjol.charm.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.AbstractFurnaceTileEntity;
import net.minecraft.client.gui.screen.recipebook.AbstractFurnaceRecipeBookScreen;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class KilnRecipeBookScreen extends AbstractFurnaceRecipeBookScreen {
    private static final Text text = new TranslatableText("gui.charm.recipebook.toggleRecipes.fireable");

    protected Text getToggleCraftableButtonText() {
        return text;
    }

    protected Set<Item> getAllowedFuels() {
        return AbstractFurnaceTileEntity.createFuelTimeMap().keySet();
    }
}
