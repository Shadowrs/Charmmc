package svenhjol.charm.module.storage_crates;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.enums.IVariantMaterial;
import svenhjol.charm.enums.VanillaVariantMaterial;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.helper.PlayerHelper;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.enums.CharmWoodMaterial;
import svenhjol.charm.module.storage_crates.StorageCrateBlock;
import svenhjol.charm.module.storage_crates.StorageCrateBlockEntity;
import svenhjol.charm.module.storage_crates.StorageCratesClient;

import java.util.HashMap;
import java.util.Map;

@Module(mod = Charm.MOD_ID, priority = 10, client = StorageCratesClient.class, description = "A **Storage crate** has the equivalent capacity of a double-chest for a single item or block type.")
public class StorageCrates extends CharmModule {
    public static final ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "storage_crate");
    public static final ResourceLocation MSG_CLIENT_UPDATED_CRATE = new ResourceLocation(Charm.MOD_ID, "client_interacted_with_crate");
    public static Map<IVariantMaterial, svenhjol.charm.module.storage_crates.StorageCrateBlock> STORAGE_CRATE_BLOCKS = new HashMap<>();
    public static BlockEntityType<svenhjol.charm.module.storage_crates.StorageCrateBlockEntity> BLOCK_ENTITY;

    @Config(name = "Maximum stacks", description = "Number of stacks of a single item or block that a storage crate will hold.")
    public static int maximumStacks = 54;

    @Config(name = "Show label", description = "If true, storage crates show their type and capacity as a hovering label. Requires the 'Storage Labels' feature to be enabled.")
    public static boolean showLabel = true;

    @Override
    public void register() {
        UseBlockCallback.EVENT.register(this::handleUseBlock);
        BLOCK_ENTITY = RegistryHelper.blockEntity(ID, svenhjol.charm.module.storage_crates.StorageCrateBlockEntity::new);

        VanillaVariantMaterial.getTypes().forEach(material -> {
            registerStorageCrate(this, material);
        });

        for (CharmWoodMaterial material : CharmWoodMaterial.values()) {
            registerStorageCrate(this, material);
        }
    }

    public static svenhjol.charm.module.storage_crates.StorageCrateBlock registerStorageCrate(CharmModule module, IVariantMaterial material) {
        svenhjol.charm.module.storage_crates.StorageCrateBlock crate = new svenhjol.charm.module.storage_crates.StorageCrateBlock(module, material);
        STORAGE_CRATE_BLOCKS.put(material, crate);
        RegistryHelper.addBlocksToBlockEntity(BLOCK_ENTITY, crate);
        return crate;
    }

    private InteractionResult handleUseBlock(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        if (!(world.getBlockState(pos).getBlock() instanceof StorageCrateBlock))
            return InteractionResult.PASS;

        ItemStack held = player.getItemInHand(hand);
        boolean isSneaking = player.isShiftKeyDown();

        BlockEntity blockEntity = world.getBlockEntity(hitResult.getBlockPos());
        if (blockEntity instanceof svenhjol.charm.module.storage_crates.StorageCrateBlockEntity) {
            svenhjol.charm.module.storage_crates.StorageCrateBlockEntity crate = (StorageCrateBlockEntity) blockEntity;

            if (!world.isClientSide) {
                if (!crate.isEmpty() && (isSneaking || held.isEmpty())) {
                    ItemStack stack = crate.takeStack();
                    PlayerHelper.addOrDropStack(player, stack);

                } else if (crate.isEmpty() && isSneaking) {
                    return InteractionResult.PASS;

                } else if (!held.isEmpty()) {
                    // slot doesn't matter here, we're just checking the item type
                    if (crate.isEmpty() || crate.canPlaceItemThroughFace(0, held, Direction.UP)) {
                        if (crate.isFull()) {
                            sendClientEffects(world, pos, ActionType.FILLED);
                        } else {
                            ItemStack added = crate.addStack(held);
                            player.setItemInHand(hand, added);
                        }
                    }
                }

                crate.sync();
            }

            return InteractionResult.sidedSuccess(world.isClientSide);
        }

        return InteractionResult.PASS;
    }

    public static void sendClientEffects(Level world, BlockPos pos, ActionType actionType) {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
        data.writeEnum(actionType);
        data.writeLong(pos.asLong());

        world.getEntitiesOfClass(Player.class, (new AABB(pos)).inflate(8.0D)).forEach(p
            -> ServerPlayNetworking.send((ServerPlayer) p, MSG_CLIENT_UPDATED_CRATE, data));
    }

    public enum ActionType {
        ADDED,
        REMOVED,
        FILLED
    }
}
