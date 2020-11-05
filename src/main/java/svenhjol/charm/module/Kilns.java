package svenhjol.charm.module;

import net.minecraft.item.crafting.CookingRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.TileEntity.KilnTileEntity;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ClientRegistryHandler;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.helper.DecorationHelper;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.KilnBlock;
import svenhjol.charm.gui.KilnScreen;
import svenhjol.charm.recipe.FiringRecipe;
import svenhjol.charm.screenhandler.KilnScreenHandler;

@Module(mod = Charm.MOD_ID, description = "A functional block that speeds up cooking of clay, bricks and terracotta.")
public class Kilns extends CharmModule {
    public static ResourceLocation RECIPE_ID = new ResourceLocation("firing");
    public static ResourceLocation BLOCK_ID = new ResourceLocation(Charm.MOD_ID, "kiln");
    public static KilnBlock KILN;
    public static TileEntityType<KilnTileEntity> BLOCK_ENTITY;
    public static IRecipeType<FiringRecipe> RECIPE_TYPE;
    public static CookingRecipeSerializer<FiringRecipe> RECIPE_SERIALIZER;
    public static ScreenHandlerType<KilnScreenHandler> SCREEN_HANDLER;

    @Override
    public void register() {
        KILN = new KilnBlock(this);
        RECIPE_TYPE = RegistryHandler.recipeType(RECIPE_ID.toString());
        RECIPE_SERIALIZER = RegistryHandler.recipeSerializer(RECIPE_ID.toString(), new CookingRecipeSerializer<>(FiringRecipe::new, 100));
        BLOCK_ENTITY = RegistryHandler.TileEntity(BLOCK_ID, KilnTileEntity::new, KILN);
        SCREEN_HANDLER = RegistryHandler.screenHandler(BLOCK_ID, KilnScreenHandler::new);
    }

    @Override
    public void clientInit() {
        ClientRegistryHandler.screenHandler(SCREEN_HANDLER, KilnScreen::new);
    }

    @Override
    public void init() {
        DecorationHelper.DECORATION_BLOCKS.add(KILN);
        DecorationHelper.STATE_CALLBACK.put(KILN, facing -> KILN.getDefaultState().with(KilnBlock.FACING, facing));
    }
}
