package svenhjol.charm.module.atlases;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import svenhjol.charm.Charm;
import svenhjol.charm.module.atlases.helper.ClientMapHelper;

public class AtlasRenderer {
    public final RenderType ATLAS_BACKGROUND = RenderType.text(new ResourceLocation(Charm.MOD_ID, "textures/map/atlas_background.png"));
    private final MapRenderer mapItemRenderer;
    private final EntityRenderDispatcher renderManager;
    private final TextureManager textureManager;

    public AtlasRenderer() {
        Minecraft minecraft = Minecraft.getInstance();
        mapItemRenderer = minecraft.gameRenderer.getMapRenderer();
        renderManager = minecraft.getEntityRenderDispatcher();
        textureManager = minecraft.getTextureManager();
    }

    public void renderAtlas(PoseStack poseStack, MultiBufferSource buffers, int light, InteractionHand hand, float equip, float swing, ItemStack stack) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        LocalPlayer player = minecraft.player;
        if (player == null) return;
        AtlasInventory inventory = Atlases.getInventory(level, stack);

        poseStack.pushPose(); // needed so that parent renderer isn't affect by what we do here

        HumanoidArm handSide = hand == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();

        // copypasta from renderMapFirstPersonSide
        float e = handSide == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        poseStack.translate(e * 0.125F, -0.125D, 0.0D);

        // render player arm
        if (!player.isInvisible()) {
            poseStack.pushPose();
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(e * 10.0F));
            renderArm(player, poseStack, buffers, light, swing, equip, handSide);
            poseStack.popPose();
        }

        // transform page based on the hand it is held and render it
        poseStack.pushPose();
        transformPageForHand(poseStack, buffers, light, swing, equip, handSide);

        int mapId = inventory.getActiveMapId(level);
        MapItemSavedData mapState = inventory.getActiveMap(level);
        renderAtlasMap(mapId, mapState, poseStack, buffers, light);
        poseStack.popPose();

        poseStack.popPose(); // close
    }

    public void renderArm(LocalPlayer player, PoseStack poseStack, MultiBufferSource buffers, int light, float swing, float equip, HumanoidArm side) {
        // copypasta from remderArmHoldingItem
        boolean flag = side != HumanoidArm.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = Mth.sqrt(swing);
        float f2 = -0.3F * Mth.sin(f1 * (float)Math.PI);
        float f3 = 0.4F * Mth.sin(f1 * ((float)Math.PI * 2F));
        float f4 = -0.4F * Mth.sin(swing * (float)Math.PI);
        poseStack.translate(f * (f2 + 0.64000005F), f3 + -0.6F + equip * -0.6F, f4 + -0.71999997F);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(f * 45.0F));
        float f5 = Mth.sin(swing * swing * (float)Math.PI);
        float f6 = Mth.sin(f1 * (float)Math.PI);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(f * f6 * 70.0F));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(f * f5 * -20.0F));
        RenderSystem.setShaderTexture(0, player.getSkinTextureLocation());
        poseStack.translate(f * -1.0F, 3.6F, 3.5D);
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(f * 120.0F));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(200.0F));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(f * -135.0F));
        poseStack.translate(f * 5.6F, 0.0D, 0.0D);
        PlayerRenderer playerrenderer = (PlayerRenderer) renderManager.getRenderer(player);
        if (flag) {
            playerrenderer.renderRightHand(poseStack, buffers, light, player);
        } else {
            playerrenderer.renderLeftHand(poseStack, buffers, light, player);
        }
    }

    public void transformPageForHand(PoseStack poseStack, MultiBufferSource buffers, int light, float swing, float equip, HumanoidArm handSide) {
        float e = handSide == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        poseStack.translate((e * 0.51F), (-0.08F + equip * -1.2F), -0.75D);
        float f1 = Mth.sqrt(swing);
        float f2 = Mth.sin(f1 * (float) Math.PI);
        float f3 = -0.5F * f2;
        float f4 = 0.4F * Mth.sin(f1 * ((float) Math.PI * 2F));
        float f5 = -0.3F * Mth.sin(swing * (float) Math.PI);
        poseStack.translate((e * f3), (f4 - 0.3F * f2), f5);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(f2 * -45.0F));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(e * f2 * -30.0F));
    }

    public void renderAtlasMap(int mapId, MapItemSavedData mapData, PoseStack poseStack, MultiBufferSource buffers, int light) {
        this.renderBackground(ATLAS_BACKGROUND, poseStack, buffers, light);

        if (mapData != null) {
            poseStack.pushPose();
            mapItemRenderer.render(poseStack, buffers, mapId, mapData, false, light);
            poseStack.popPose();
        }
    }

    private void renderBackground(RenderType background, PoseStack poseStack, MultiBufferSource buffers, int light) {
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        poseStack.scale(0.38F, 0.38F, 0.38F);
        poseStack.translate(-0.5D, -0.5D, 0.0D);
        poseStack.scale(0.0078125F, 0.0078125F, 0.0078125F);
        ClientMapHelper.drawBackgroundVertex(poseStack, light, buffers.getBuffer(background));
    }
}
