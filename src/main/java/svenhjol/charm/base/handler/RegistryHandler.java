package svenhjol.charm.base.handler;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import svenhjol.charm.Charm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings({"UnusedReturnValue", "unchecked", "rawtypes"})
public class RegistryHandler {
    private static final Map<IForgeRegistry<?>, List<Supplier<IForgeRegistryEntry<?>>>> REGISTRY = new HashMap<>();

    @SubscribeEvent
    public static void onRegister(RegistryEvent.Register<?> event) {
        IForgeRegistry registry = event.getRegistry();

        if (REGISTRY.containsKey(registry)) {
            REGISTRY.get(registry).forEach(supplier -> {
                IForgeRegistryEntry<?> entry = supplier.get();
                if (entry != null) {
                    Charm.LOG.debug("Registering to " + registry.getRegistryName() + " - " + entry.getRegistryName());
                    registry.register(entry);
                }
            });
        }
    }

    public static Block block(ResourceLocation id, Block block) {
        return register(ForgeRegistries.BLOCKS, id, block);
    }

    public static <T extends TileEntity> TileEntityType<T> tileEntity(ResourceLocation id, Supplier<T> supplier, Block... blocks) {
        TileEntityType<T> build = TileEntityType.Builder.create(supplier, blocks).build(null);
        register(ForgeRegistries.TILE_ENTITIES, id, build);
        return build;
    }

    public static StructureFeature<?, ?> configuredFeature(ResourceLocation id, StructureFeature<?, ?> configuredFeature) {
        WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, id, configuredFeature);
        return configuredFeature;
    }

    public static <T extends Entity> EntityType<T> entity(ResourceLocation id, EntityType<T> entityType) {
        register(ForgeRegistries.ENTITIES, id, entityType);
        return entityType;
    }

    public static Enchantment enchantment(ResourceLocation id, Enchantment enchantment) {
        return register(ForgeRegistries.ENCHANTMENTS, id, enchantment);
    }

    public static Item item(ResourceLocation id, Item item) {
        return register(ForgeRegistries.ITEMS, id, item);
    }

    public static LootFunctionType lootFunctionType(ResourceLocation id, LootFunctionType lootFunctionType) {
        return Registry.register(Registry.LOOT_FUNCTION_TYPE, id, lootFunctionType);
    }

    public static PointOfInterestType pointOfInterestType(ResourceLocation id, PointOfInterestType poit) {
        return register(ForgeRegistries.POI_TYPES, id, poit);
    }

    public static <T extends IRecipe<?>> IRecipeType<T> recipeType(String recipeId) {
        return IRecipeType.register(recipeId);
    }

    public static <S extends IRecipeSerializer<T>, T extends IRecipe<?>> S recipeSerializer(String recipeId, S serializer) {
        register(ForgeRegistries.RECIPE_SERIALIZERS, new ResourceLocation(recipeId), serializer);
        return serializer;
    }

    public static <T extends Container> ContainerType<T> container(ResourceLocation id, ContainerType.IFactory<T> factory) {
        ContainerType<T> container = new ContainerType<>(factory);
        register(ForgeRegistries.CONTAINERS, id, container);
        return container;
    }

    public static SoundEvent sound(ResourceLocation id, SoundEvent sound) {
        return register(ForgeRegistries.SOUND_EVENTS, id, sound);
    }

    public static IStructurePieceType structurePiece(ResourceLocation id, IStructurePieceType structurePieceType) {
        return Registry.register(Registry.STRUCTURE_PIECE, id, structurePieceType);
    }

    public static VillagerProfession villagerProfession(ResourceLocation id, VillagerProfession profession) {
        return register(ForgeRegistries.PROFESSIONS, id, profession);
    }

    public static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistry<T> type, Supplier<IForgeRegistryEntry<?>> supplier) {
        REGISTRY.putIfAbsent(type, new ArrayList<>());
        REGISTRY.get(type).add(supplier);
    }

    public static <T extends IForgeRegistryEntry<T>> T register(IForgeRegistry<T> type, ResourceLocation id, T entry) {
        entry.setRegistryName(id);
        REGISTRY.putIfAbsent(type, new ArrayList<>());
        REGISTRY.get(type).add(() -> entry);
        return entry;
    }
}
