package svenhjol.charm.mixin.variant_chests;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import svenhjol.charm.module.variant_chests.IVariantChestBoat;

@Mixin(ChestBoat.class)
public class VariantChestBoatMixin extends Boat implements IVariantChestBoat {
    private static final String VARIANT_CHEST_TAG = "variant_chest";
    private static final EntityDataAccessor<String> DATA_ID_VARIANT_CHEST = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.STRING);

    public VariantChestBoatMixin(EntityType<? extends Boat> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_VARIANT_CHEST, "");
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        setVariantChest(compoundTag.getString(VARIANT_CHEST_TAG));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putString(VARIANT_CHEST_TAG, getVariantChest());
    }

    public void setVariantChest(String type) {
        this.entityData.set(DATA_ID_VARIANT_CHEST, type);
    }

    public String getVariantChest() {
        return this.entityData.get(DATA_ID_VARIANT_CHEST);
    }
}
