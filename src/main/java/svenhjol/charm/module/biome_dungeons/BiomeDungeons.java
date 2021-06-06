package svenhjol.charm.module.biome_dungeons;

import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.module.biome_dungeons.DungeonBuilds;
import svenhjol.charm.module.biome_dungeons.DungeonFeature;
import svenhjol.charm.module.biome_dungeons.DungeonGenerator;

import static svenhjol.charm.helper.RegistryHelper.configuredStructureFeature;
import static svenhjol.charm.helper.BiomeHelper.addStructureToBiomeCategories;

@Module(mod = Charm.MOD_ID, description = "Dungeon variations with different themes according to the biome.")
public class BiomeDungeons extends CharmModule {
    public static final ResourceLocation DUNGEON_ID = new ResourceLocation(Charm.MOD_ID, "dungeon");
    public static StructureFeature<JigsawConfiguration> DUNGEON_FEATURE;

    public static ConfiguredStructureFeature<?, ?> BADLANDS;
    public static ConfiguredStructureFeature<?, ?> DESERT;
    public static ConfiguredStructureFeature<?, ?> FOREST;
    public static ConfiguredStructureFeature<?, ?> JUNGLE;
    public static ConfiguredStructureFeature<?, ?> MOUNTAINS;
    public static ConfiguredStructureFeature<?, ?> NETHER;
    public static ConfiguredStructureFeature<?, ?> PLAINS;
    public static ConfiguredStructureFeature<?, ?> SAVANNA;
    public static ConfiguredStructureFeature<?, ?> SNOWY;
    public static ConfiguredStructureFeature<?, ?> TAIGA;

    @Override
    public void register() {
        DUNGEON_FEATURE = new DungeonFeature(JigsawConfiguration.CODEC);

        FabricStructureBuilder.create(DUNGEON_ID, DUNGEON_FEATURE)
            .step(GenerationStep.Decoration.UNDERGROUND_STRUCTURES)
            .defaultConfig(6, 2, 1225502)
            .register();

        int dungeonSize = 1;

        BADLANDS    = DUNGEON_FEATURE.configured(new JigsawConfiguration(() -> svenhjol.charm.module.biome_dungeons.DungeonGenerator.BADLANDS_POOL, dungeonSize));
        DESERT      = DUNGEON_FEATURE.configured(new JigsawConfiguration(() -> svenhjol.charm.module.biome_dungeons.DungeonGenerator.DESERT_POOL, dungeonSize));
        FOREST      = DUNGEON_FEATURE.configured(new JigsawConfiguration(() -> svenhjol.charm.module.biome_dungeons.DungeonGenerator.FOREST_POOL, dungeonSize));
        JUNGLE      = DUNGEON_FEATURE.configured(new JigsawConfiguration(() -> svenhjol.charm.module.biome_dungeons.DungeonGenerator.JUNGLE_POOL, dungeonSize));
        MOUNTAINS   = DUNGEON_FEATURE.configured(new JigsawConfiguration(() -> svenhjol.charm.module.biome_dungeons.DungeonGenerator.MOUNTAINS_POOL, dungeonSize));
        NETHER      = DUNGEON_FEATURE.configured(new JigsawConfiguration(() -> svenhjol.charm.module.biome_dungeons.DungeonGenerator.NETHER_POOL, dungeonSize));
        PLAINS      = DUNGEON_FEATURE.configured(new JigsawConfiguration(() -> svenhjol.charm.module.biome_dungeons.DungeonGenerator.PLAINS_POOL, dungeonSize));
        SAVANNA     = DUNGEON_FEATURE.configured(new JigsawConfiguration(() -> svenhjol.charm.module.biome_dungeons.DungeonGenerator.SAVANNA_POOL, dungeonSize));
        SNOWY       = DUNGEON_FEATURE.configured(new JigsawConfiguration(() -> svenhjol.charm.module.biome_dungeons.DungeonGenerator.SNOWY_POOL, dungeonSize));
        TAIGA       = DUNGEON_FEATURE.configured(new JigsawConfiguration(() -> svenhjol.charm.module.biome_dungeons.DungeonGenerator.TAIGA_POOL, dungeonSize));

        configuredStructureFeature(new ResourceLocation(Charm.MOD_ID, "dungeon_badlands"), BADLANDS);
        configuredStructureFeature(new ResourceLocation(Charm.MOD_ID, "dungeon_desert"), DESERT);
        configuredStructureFeature(new ResourceLocation(Charm.MOD_ID, "dungeon_forest"), FOREST);
        configuredStructureFeature(new ResourceLocation(Charm.MOD_ID, "dungeon_jungle"), JUNGLE);
        configuredStructureFeature(new ResourceLocation(Charm.MOD_ID, "dungeon_mountains"), MOUNTAINS);
        configuredStructureFeature(new ResourceLocation(Charm.MOD_ID, "dungeon_nether"), NETHER);
        configuredStructureFeature(new ResourceLocation(Charm.MOD_ID, "dungeon_plains"), PLAINS);
        configuredStructureFeature(new ResourceLocation(Charm.MOD_ID, "dungeon_savanna"), SAVANNA);
        configuredStructureFeature(new ResourceLocation(Charm.MOD_ID, "dungeon_snowy"), SNOWY);
        configuredStructureFeature(new ResourceLocation(Charm.MOD_ID, "dungeon_taiga"), TAIGA);

        DungeonBuilds.init();
        svenhjol.charm.module.biome_dungeons.DungeonGenerator.init();
    }

    @Override
    public void init() {
        if (!svenhjol.charm.module.biome_dungeons.DungeonGenerator.BADLANDS_DUNGEONS.isEmpty()) addStructureToBiomeCategories(BADLANDS, Biome.BiomeCategory.MESA);
        if (!svenhjol.charm.module.biome_dungeons.DungeonGenerator.DESERT_DUNGEONS.isEmpty()) addStructureToBiomeCategories(DESERT, Biome.BiomeCategory.DESERT);
        if (!svenhjol.charm.module.biome_dungeons.DungeonGenerator.FOREST_DUNGEONS.isEmpty()) addStructureToBiomeCategories(FOREST, Biome.BiomeCategory.FOREST);
        if (!svenhjol.charm.module.biome_dungeons.DungeonGenerator.JUNGLE_DUNGEONS.isEmpty()) addStructureToBiomeCategories(JUNGLE, Biome.BiomeCategory.JUNGLE);
        if (!svenhjol.charm.module.biome_dungeons.DungeonGenerator.MOUNTAINS_DUNGEONS.isEmpty()) addStructureToBiomeCategories(MOUNTAINS, Biome.BiomeCategory.EXTREME_HILLS);
        if (!svenhjol.charm.module.biome_dungeons.DungeonGenerator.NETHER_DUNGEONS.isEmpty()) addStructureToBiomeCategories(MOUNTAINS, Biome.BiomeCategory.NETHER);
        if (!svenhjol.charm.module.biome_dungeons.DungeonGenerator.PLAINS_DUNGEONS.isEmpty()) addStructureToBiomeCategories(PLAINS, Biome.BiomeCategory.PLAINS);
        if (!svenhjol.charm.module.biome_dungeons.DungeonGenerator.SAVANNA_DUNGEONS.isEmpty()) addStructureToBiomeCategories(SAVANNA, Biome.BiomeCategory.SAVANNA);
        if (!svenhjol.charm.module.biome_dungeons.DungeonGenerator.SNOWY_DUNGEONS.isEmpty()) addStructureToBiomeCategories(SNOWY, Biome.BiomeCategory.ICY);
        if (!DungeonGenerator.TAIGA_DUNGEONS.isEmpty()) addStructureToBiomeCategories(TAIGA, Biome.BiomeCategory.TAIGA);
    }
}
