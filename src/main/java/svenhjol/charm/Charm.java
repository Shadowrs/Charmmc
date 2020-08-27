package svenhjol.charm;

import net.fabricmc.api.ModInitializer;
import svenhjol.charm.module.*;
import svenhjol.meson.MesonMod;
import svenhjol.meson.MesonModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Charm extends MesonMod implements ModInitializer {
    public static final String MOD_ID = "charm";

    @Override
    public void onInitialize() {
        super.init(MOD_ID);
    }

    @Override
    public List<Class<? extends MesonModule>> getModules() {
        return new ArrayList<>(Arrays.asList(
            AnvilImprovements.class,
            ArmorInvisibility.class,
            AutomaticRecipeUnlock.class,
            BatBucket.class,
            BeaconsHealMobs.class,
            BlockOfGunpowder.class,
            BlockOfSugar.class,
            CampfiresNoDamage.class,
            Candles.class,
            CaveSpidersDropCobwebs.class,
            ChickensDropFeathers.class,
            Core.class,
            DecreaseRepairCost.class,
            DirtToPath.class,
            ExtractEnchantments.class,
            FeatherFallingCrops.class,
            GoldChains.class,
            GoldLanterns.class,
            HoeHarvesting.class,
            HuskImprovements.class,
            InventoryTidying.class,
            LanternImprovements.class,
            MineshaftImprovements.class,
            MoreVillageBiomes.class,
            MoreVillagerTrades.class,
            MusicImprovements.class,
            NetheriteNuggets.class,
            PathToDirt.class,
            ParrotsStayOnShoulder.class,
            PortableCrafting.class,
            PortableEnderChest.class,
            RedstoneLanterns.class,
            RemoveNitwits.class,
            RemovePotionGlint.class,
            SleepImprovements.class,
            StackableEnchantedBooks.class,
            StackablePotions.class,
            StrayImprovements.class,
            TamedAnimalsNoDamage.class,
            UseTotemFromInventory.class,
            VariantAnimalTextures.class,
            VillagersFollowEmeraldBlocks.class,
            WanderingTraderImprovements.class,
            WitchesDropLuck.class
        ));
    }
}
