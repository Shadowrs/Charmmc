package svenhjol.charm.event;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.structure.pool.LegacySinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.BuiltinRegistries;
import svenhjol.charm.mixin.accessor.JigsawPatternAccessor;
import svenhjol.charm.base.enums.ICharmEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface StructureSetupCallback {
    Map<ResourceLocation, StructurePool> vanillaPools = new HashMap<>();

    Event<StructureSetupCallback> EVENT = EventFactory.createArrayBacked(StructureSetupCallback.class, (listeners) -> () -> {
        for (StructureSetupCallback listener : listeners) {
            listener.interact();
        }
    });

    static StructurePool getVanillaPool(ResourceLocation id) {
        if (!vanillaPools.containsKey(id)) {
            StructurePool pool = BuiltinRegistries.STRUCTURE_POOL.get(id);

            // convert elementCounts to mutable list
            List<Pair<StructurePoolElement, Integer>> elementCounts = ((JigsawPatternAccessor) pool).getRawTemplates();
            ((JigsawPatternAccessor)pool).setRawTemplates(new ArrayList<>(elementCounts));

            if (false) { // DELETES ALL IN POOL, DO NOT USE!
                ((JigsawPatternAccessor) pool).setRawTemplates(new ArrayList<>());
            }

            vanillaPools.put(id, pool);
        }

        return vanillaPools.get(id);
    }

    static void addStructurePoolElement(ResourceLocation poolId, ResourceLocation pieceId, StructureProcessorList processor, StructurePool.Projection projection, int count) {
        Pair<Function<StructurePool.Projection, LegacySinglePoolElement>, Integer> pair =
            Pair.of(StructurePoolElement.method_30426(pieceId.toString(), processor), count);

        StructurePoolElement element = pair.getFirst().apply(projection);
        StructurePool pool = getVanillaPool(poolId);
        
        // add custom piece to the element counts
        ((JigsawPatternAccessor)pool).getRawTemplates().add(Pair.of(element, count));
        
        // add custom piece to the elements
        for (int i = 0; i < count; i++) {
            ((JigsawPatternAccessor)pool).getJigsawPieces().add(element);
        }
    }

    static void addVillageHouse(VillageType type, ResourceLocation pieceId, int count) {
        ResourceLocation houses = new ResourceLocation("village/" + type.asString() + "/houses");
        StructureProcessorList processor = StructureProcessorLists.MOSSIFY_10_PERCENT;
        StructurePool.Projection projection = StructurePool.Projection.RIGID;
        addStructurePoolElement(houses, pieceId, processor, projection, count);
    }

    void interact();

    enum VillageType implements ICharmEnum {
        DESERT,
        PLAINS,
        SAVANNA,
        SNOWY,
        TAIGA
    }
}
