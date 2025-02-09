package svenhjol.charm.module.extra_recipes;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.helper.RecipeHelper;
import svenhjol.charm.loader.CharmModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CommonModule(mod = Charm.MOD_ID, description = "Adds custom recipes.")
public class ExtraRecipes extends CharmModule {
    @Config(name = "Ore block from raw ore block", description = "If true, adds a blast furnace recipe for smelting raw ore blocks into ore blocks.")
    public static boolean rawOreBlocks = true;

    @Config(name = "Gilded Blackstone", description = "If true, adds a recipe for Gilded Blackstone using gold nuggets and blackstone.")
    public static boolean gildedBlackstone = true;

    @Config(name = "Trident", description = "If true, adds a recipe for the Trident using a heart of the sea, prismarine shards and crystals.")
    public static boolean trident = true;

    @Config(name = "Cyan Dye from warped roots", description = "If true, adds a recipe for Cyan Dye using warped roots.")
    public static boolean cyanDye = true;

    @Config(name = "Green Dye from yellow and blue", description = "If true, adds a recipe for Green Dye using yellow and blue dyes.")
    public static boolean greenDye = true;

    @Config(name = "Snowballs from snow blocks", description = "If true, adds a recipe for turning snow blocks back into snowballs.")
    public static boolean snowballs = true;

    @Config(name = "Quartz from quartz blocks", description = "If true, adds a recipe for turning quartz blocks back into quartz.")
    public static boolean quartz = true;

    @Config(name = "Clay balls from clay blocks", description = "If true, adds a recipe for turning clay blocks back into clay balls.")
    public static boolean clay = true;

    @Config(name = "Simpler Soul Torch", description = "If true, adds a recipe for Soul Torches using soul sand/soul soil and sticks.")
    public static boolean soulTorch = true;

    @Config(name = "Shapeless bread", description = "If true, adds a shapeless recipe for bread.")
    public static boolean bread = true;

    @Config(name = "Shapeless paper", description = "If true, adds a shapeless recipe for paper.")
    public static boolean paper = true;

    @Config(name = "Bundle from leather", description = "If true, adds a recipe for crafting bundles from leather.")
    public static boolean bundle = true;

    @Override
    public void register() {
        // remove recipes that are not valid according to the config
        List<String> invalid = new ArrayList<>();
        if (!rawOreBlocks) invalid.addAll(Arrays.asList(
            "copper_block_from_blasting_raw_copper_block.json",
            "gold_block_from_blasting_raw_gold_block",
            "iron_block_from_blasting_raw_iron_block"
        ));
        if (!gildedBlackstone) invalid.add("gilded_blackstone");
        if (!snowballs) invalid.add("snowballs_from_snow_block");
        if (!quartz) invalid.add("quartz_from_quartz_block");
        if (!clay) invalid.add("clay_balls_from_clay_block");
        if (!trident) invalid.add("trident");
        if (!cyanDye) invalid.add("cyan_dye");
        if (!greenDye) invalid.add("green_dye");
        if (!soulTorch) invalid.add("soul_torch");
        if (!bread) invalid.add("bread");
        if (!paper) invalid.add("paper");
        if (!bundle) invalid.add("bundle");

        invalid.forEach(recipe -> RecipeHelper.removeRecipe(new ResourceLocation(Charm.MOD_ID, "extra_recipes/" + recipe)));
    }
}

