package svenhjol.charm.module.azalea_wood;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.block.*;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.item.CharmBoatItem;
import svenhjol.charm.item.CharmSignItem;
import svenhjol.charm.enums.CharmWoodMaterial;
import svenhjol.charm.mixin.accessor.ConfiguredFeaturesAccessor;
import svenhjol.charm.mixin.accessor.TreeFeatureConfigAccessor;
import svenhjol.charm.module.bookcases.BookcaseBlock;
import svenhjol.charm.module.bookcases.Bookcases;
import svenhjol.charm.module.variant_barrels.VariantBarrelBlock;
import svenhjol.charm.module.variant_barrels.VariantBarrels;
import svenhjol.charm.module.variant_bookshelves.VariantBookshelfBlock;
import svenhjol.charm.module.variant_bookshelves.VariantBookshelves;
import svenhjol.charm.module.variant_chests.VariantChestBlock;
import svenhjol.charm.module.variant_chests.VariantChests;
import svenhjol.charm.module.variant_chests.VariantTrappedChestBlock;
import svenhjol.charm.module.variant_ladders.VariantLadderBlock;
import svenhjol.charm.module.variant_ladders.VariantLadders;

@Module(mod = Charm.MOD_ID, client = AzaleaWoodClient.class, description = "Azalea wood is obtainable from naturally occurring azalea trees or by growing azalea saplings.")
public class AzaleaWood extends CharmModule {
    public static Identifier ID = new Identifier(Charm.MOD_ID, "azalea");

    public static SignType SIGN_TYPE;

    public static CharmWoodenButtonBlock BUTTON;
    public static CharmDoorBlock DOOR;
    public static CharmFenceBlock FENCE;
    public static CharmFenceGateBlock FENCE_GATE;
    public static CharmLogBlock LOG;
    public static CharmPlanksBlock PLANKS;
    public static CharmPressurePlate PRESSURE_PLATE;
    public static CharmSignBlock SIGN_BLOCK;
    public static CharmSlabBlock SLAB;
    public static CharmStairsBlock STAIRS;
    public static CharmLogBlock STRIPPED_LOG;
    public static CharmLogBlock STRIPPED_WOOD;
    public static CharmTrapdoorBlock TRAPDOOR;
    public static CharmWallSignBlock WALL_SIGN_BLOCK;
    public static CharmLogBlock WOOD;

    public static VariantBarrelBlock BARREL;
    public static BookcaseBlock BOOKCASE;
    public static VariantBookshelfBlock BOOKSHELF;
    public static VariantChestBlock CHEST;
    public static VariantLadderBlock LADDER;
    public static VariantTrappedChestBlock TRAPPED_CHEST;

    public static CharmBoatItem BOAT;
    public static CharmSignItem SIGN_ITEM;

    @Override
    public void register() {
        // must init these first, other blocks depend on them being registered
        SIGN_TYPE = RegistryHelper.signType(ID);
        PLANKS = new AzaleaBlocks.AzaleaPlanksBlock(this);

        BUTTON = new AzaleaBlocks.AzaleaButtonBlock(this);
        DOOR = new AzaleaBlocks.AzaleaDoorBlock(this);
        FENCE = new AzaleaBlocks.AzaleaFenceBlock(this);
        FENCE_GATE = new AzaleaBlocks.AzaleaFenceGateBlock(this);
        LOG = new AzaleaBlocks.AzaleaLogBlock(this);
        STRIPPED_LOG = new AzaleaBlocks.StrippedAzaleaLogBlock(this);
        PRESSURE_PLATE = new AzaleaBlocks.AzaleaPressurePlateBlock(this);
        SIGN_BLOCK = new AzaleaBlocks.AzaleaSignBlock(this);
        SLAB = new AzaleaBlocks.AzaleaSlabBlock(this);
        STAIRS = new AzaleaBlocks.AzaleaStairsBlock(this);
        TRAPDOOR = new AzaleaBlocks.AzaleaTrapdoorBlock(this);
        WALL_SIGN_BLOCK = new AzaleaBlocks.AzaleaWallSignBlock(this);
        WOOD = new AzaleaBlocks.AzaleaWoodBlock(this);
        STRIPPED_WOOD = new AzaleaBlocks.StrippedAzaleaWoodBlock(this);

        BOAT = new AzaleaItems.AzaleaBoatItem(this);
        SIGN_ITEM = new AzaleaItems.AzaleaSignItem(this);

        BARREL = VariantBarrels.registerBarrel(this, CharmWoodMaterial.AZALEA);
        BOOKCASE = Bookcases.registerBookcase(this, CharmWoodMaterial.AZALEA);
        BOOKSHELF = VariantBookshelves.registerBookshelf(this, CharmWoodMaterial.AZALEA);
        CHEST = VariantChests.registerChest(this, CharmWoodMaterial.AZALEA);
        LADDER = VariantLadders.registerLadder(this, CharmWoodMaterial.AZALEA);
        TRAPPED_CHEST = VariantChests.registerTrappedChest(this, CharmWoodMaterial.AZALEA);
    }

    @Override
    public void init() {
        ConfiguredFeature<TreeFeatureConfig, ?> feature = ConfiguredFeaturesAccessor.getAzaleaTree();
        TreeFeatureConfig config = feature.getConfig();
        ((TreeFeatureConfigAccessor)config).setTrunkProvider(new SimpleBlockStateProvider(AzaleaWood.LOG.getDefaultState()));
        RegistryHelper.addBlocksToBlockEntity(BlockEntityType.SIGN, SIGN_BLOCK, WALL_SIGN_BLOCK);
    }
}