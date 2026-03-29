package com.elfmcys.yesstevemodel.util;

import com.elfmcys.yesstevemodel.client.ClientProxy;
import com.elfmcys.yesstevemodel.client.compat.EntityEquipmentSlot;
import com.elfmcys.yesstevemodel.client.compat.Hand;
import com.elfmcys.yesstevemodel.client.entity.CustomPlayerEntity;
import com.elfmcys.yesstevemodel.client.renderer.CustomPlayerRenderer;
import com.elfmcys.yesstevemodel.geckolib3.core.IAnimatable;
import com.elfmcys.yesstevemodel.geckolib3.geo.GeoReplacedEntityRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/*
1.16.5 - 1.12.2
player.yBodyRot - player.renderYawOffset	身体转向角度
player.yRot - player.rotationYaw	视口偏航角
player.xRot	- player.rotationPitch	视口俯仰角
player.yHeadRot	- player.rotationYawHead	头部偏航角
player.yHeadRotO - player.prevRotationYawHead	上一刻头部偏航角
 */
public final class RenderUtil {
    public static void renderTextureScreenEntity(float pPosX, float pPosY, float pScale, float pitch, float yaw, EntityPlayer player, ResourceLocation modelId, ResourceLocation textureId, boolean showGround, Consumer<CustomPlayerEntity> consumer) {
        if (player == null) {
            return;
        }
        try {
            CustomPlayerRenderer renderer = ClientProxy.getInstance();
            IAnimatable animatable = AnimatableCacheUtil.TEXTURE_GUI_CACHE.get(modelId, CustomPlayerEntity::new);
            if (animatable instanceof CustomPlayerEntity entity) {
                consumer.accept(entity);

                entity.setMainModel(ModelIdUtil.getMainId(modelId));
                entity.setTexture(textureId);

                GL11.glPushMatrix();
                GL11.glTranslatef(pPosX, pPosY, 1050.0F);
                GL11.glScalef(1.0F, 1.0F, -1.0F);

                /*
                动画位移矩阵开始
                 */
                GL11.glPushMatrix();
                GL11.glTranslated(0.0D, 0.0D, 1000.0D);
                GL11.glScalef(pScale, pScale, pScale);
                GL11.glTranslated(0, 0.8, 0);
                GL11.glRotatef(180.0F, 0, 0, 1);
                rotateAndEnableLighting();
                float xp = -10 + pitch;
                GL11.glRotatef(xp, 1, 0, 0);

                float yBodyRot = player.renderYawOffset;
                float yBodyRotO = player.prevRenderYawOffset;
                float yRot = player.rotationYaw;
                float yRotO = player.prevRotationYaw;
                float xRot = player.rotationPitch;
                float xRotO = player.prevRotationPitch;
                float yHeadRot = player.rotationYawHead;
                float yHeadRotO = player.prevRotationYawHead;
                Entity vehicle = player.ridingEntity;

                boolean sleeping = player.sleeping;
                ChunkCoordinates bedLocation = player.playerLocation; // bedLocation，MCP 标错了
                float renderOffsetX = player.field_71079_bU; // renderOffsetX
                float renderOffsetY = player.field_71082_cx; // renderOffsetY
                float renderOffsetZ = player.field_71089_bV; // renderOffsetZ

                player.renderYawOffset = -yaw;
                player.prevRenderYawOffset = player.renderYawOffset;
                player.rotationYaw = 180;
                player.prevRotationYaw = player.rotationYaw;
                player.rotationPitch = 0;
                player.prevRotationPitch = player.rotationPitch;
                player.rotationYawHead = player.renderYawOffset;
                player.prevRotationYawHead = player.rotationYawHead;
                player.ridingEntity = null;

                RenderManager dispatcher = RenderManager.instance;
                xp = 180.0F - xp;
                dispatcher.playerViewY = xp;
                //dispatcher.setRenderShadow(false);
                if (entity.hasPreviewAnimation("sleep")) {
                    GL11.glRotatef(yaw - 90, 0, 1, 0);
                    GL11.glTranslated(0.5, 0.5625, 0);
                    player.sleeping = true;
                    // 原版的床睡完后不会重置这几个状态
                    player.playerLocation = null;
                    player.field_71079_bU = 0;
                    player.field_71082_cx = 0;
                    player.field_71089_bV = 0;
                }
                if (entity.hasPreviewAnimation("swim") || entity.hasPreviewAnimation("swim_stand")) {
                    //player.setPose(Pose.SWIMMING);
                }
                if (entity.hasPreviewAnimation("sneak") || entity.hasPreviewAnimation("sneaking")) {
                    //player.setPose(Pose.CROUCHING);
                }
                if (entity.hasPreviewAnimation("sit")) {
                    GL11.glTranslated(0, -0.5, 0);
                }
                if (entity.hasPreviewAnimation("ride")) {
                    GL11.glTranslated(0, 0.70, 0);
                }
                if (entity.hasPreviewAnimation("ride_pig")) {
                    GL11.glTranslated(0, 0.1625, 0);
                }
                if (entity.hasPreviewAnimation("boat")) {
                    GL11.glTranslated(0, -0.45, 0);
                }
                GL11.glPushMatrix();
                GL11.glTranslatef(0.0F, player.yOffset, 0.0F);
                renderer.render(player, entity, 0, 0, 0, 0.0F, 1.0F);
                // 清理实体渲染
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glEnable(GL11.GL_COLOR_MATERIAL);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
                try {
                    renderExtraEntity(yaw, player, entity, dispatcher);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
                GL11.glPopMatrix();
                GL11.glPopMatrix();
                /*
                动画位移矩阵结束
                 */
                if (showGround) {
                    if (entity.hasPreviewAnimation("sleep")) {
                        renderBed(pScale, pitch, yaw);
                    }
                    renderGround(pScale, pitch, yaw);
                }
                //dispatcher.setRenderShadow(true);

                player.renderYawOffset = yBodyRot;
                player.prevRenderYawOffset = yBodyRotO;
                player.rotationYaw = yRot;
                player.prevRotationYaw = yRotO;
                player.rotationPitch = xRot;
                player.prevRotationPitch = xRotO;
                player.rotationYawHead = yHeadRot;
                player.prevRotationYawHead = yHeadRotO;
                player.ridingEntity = vehicle;

                player.sleeping = sleeping;
                player.playerLocation = bedLocation;
                player.field_71079_bU = renderOffsetX;
                player.field_71082_cx = renderOffsetY;
                player.field_71089_bV = renderOffsetZ;

                GL11.glPopMatrix();

                RenderHelper.disableStandardItemLighting();
                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
                OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void renderBed(float scale, float pitch, float yaw) {
        GL11.glPushMatrix();
        GL11.glTranslated(0.0D, 0.0D, 1000.0D);
        GL11.glScalef(scale, scale, scale);
        GL11.glTranslated(0, 0.8, 0);
        GL11.glRotatef(180.0F, 0, 0, 1);
        GL11.glRotatef(-10 + pitch, 1, 0, 0);

        GL11.glRotatef(yaw + 180, 0, 1, 0);
        GL11.glTranslated(-0.5, 0, 0.5);

        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        RenderBlocks renderBlocks = RenderBlocks.getInstance();

        GL11.glPushMatrix();
        GL11.glTranslatef(0, 0, 1);
        renderBlocks.renderBlockAsItem(Blocks.bed, 0, 1.0F);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        renderBlocks.renderBlockAsItem(Blocks.bed, 8, 1.0F);
        GL11.glPopMatrix();

        GL11.glPopMatrix();
    }

    private static void renderGround(float scale, float pitch, float yaw) {
        GL11.glPushMatrix();
        GL11.glTranslated(0.0D, 0.0D, 1000.0D);
        GL11.glScalef(scale, scale, scale);
        GL11.glTranslated(0, 0.8, 0);
        GL11.glRotatef(180.0F, 0, 0, 1);
        GL11.glRotatef(-10 + pitch, 1, 0, 0);

        GL11.glRotatef(yaw, 0, 1, 0);
        GL11.glTranslated(-1, -0.5, -2);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                GL11.glTranslatef(0, 0, 1);
                renderSingleBlock(Blocks.grass, 0);
            }
            GL11.glTranslatef(1, 0, -3);
        }
        GL11.glTranslatef(-1, 1, 1);
        renderSingleBlock(Blocks.tallgrass, 1);
        GL11.glTranslatef(0, 0, 1);
        renderSingleBlock(Blocks.red_flower, 4);
        GL11.glPopMatrix();
    }

    private static void renderSingleBlock(Block block, int meta) {
        GL11.glPushMatrix(); // 一定要 push，原版方块渲染不干净
        final Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        RenderBlocks.getInstance().renderBlockAsItem(block, meta, 1.0F);
        GL11.glPopMatrix();
    }

    @SuppressWarnings("UnnecessaryReturnStatement")
    private static void renderExtraEntity(float yaw, EntityPlayer player, CustomPlayerEntity playerEntity, RenderManager dispatcher) throws ExecutionException {
        if (playerEntity.hasPreviewAnimation("ride")) {
            Entity entity = AnimatableCacheUtil.ENTITIES_CACHE.get(EntityList.classToStringMapping.get(EntityHorse.class), () -> new EntityHorse(player.worldObj));
            renderExtraEntity(yaw, player, dispatcher, entity);
            return;
        }
        if (playerEntity.hasPreviewAnimation("ride_pig")) {
            Entity entity = AnimatableCacheUtil.ENTITIES_CACHE.get(EntityList.classToStringMapping.get(EntityPig.class), () -> new EntityPig(player.worldObj));
            renderExtraEntity(yaw, player, dispatcher, entity);
            return;
        }
        if (playerEntity.hasPreviewAnimation("boat")) {
            Entity entity = AnimatableCacheUtil.ENTITIES_CACHE.get(EntityList.classToStringMapping.get(EntityBoat.class), () -> new EntityBoat(player.worldObj));
            renderExtraEntity(yaw, player, dispatcher, entity);
            return;
        }
    }

    private static void renderExtraEntity(float yaw, EntityPlayer player, RenderManager dispatcher, Entity entity) {
        GL11.glRotatef(yaw, 0, 1, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // Args: entity, x, y, z, yaw, partialTicks
        dispatcher.renderEntityWithPosYaw(entity, 0, -entity.getMountedYOffset() - player.getYOffset(), 0, 0.0F, 1.0F);
        // 清理实体渲染
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
    }

    public static void renderEntityInInventory(int pPosX, int pPosY, int pScale, EntityPlayer player, ResourceLocation modelId, ResourceLocation textureId, Consumer<CustomPlayerEntity> consumer, boolean disableRot) {
        if (player == null) {
            return;
        }
        try {
            CustomPlayerRenderer renderer = ClientProxy.getInstance();
            IAnimatable animatable = AnimatableCacheUtil.ANIMATABLE_CACHE.get(modelId, CustomPlayerEntity::new);
            if (animatable instanceof CustomPlayerEntity entity) {
                consumer.accept(entity);
                renderModel(pPosX, pPosY, (float) pScale, player, modelId, textureId, renderer, entity, disableRot);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@link com.elfmcys.yesstevemodel.client.gui.button.TextureButton#renderWidget(Minecraft, int, int)}
     */
    @SuppressWarnings("JavadocReference")
    public static void renderTextureButtonEntity(int pPosX, int pPosY, int pScale, EntityPlayer player, ResourceLocation modelId, ResourceLocation textureId) {
        renderEntityInInventory(pPosX, pPosY, pScale, player, modelId, textureId, entity -> {
            if (entity.hasPreviewAnimation()) {
                entity.clearPreviewAnimation();
            }
        }, false);
    }

    private static void renderModel(
            double pPosX, double pPosY, float pScale, EntityPlayer player,
            ResourceLocation modelId, ResourceLocation textureId,
            GeoReplacedEntityRenderer<EntityPlayer, CustomPlayerEntity> renderer, CustomPlayerEntity entity,
            boolean disableRot
    ) {
        entity.setMainModel(ModelIdUtil.getMainId(modelId));
        entity.setTexture(textureId);

        GL11.glPushMatrix();
        GL11.glTranslatef((float) pPosX, (float) pPosY, 1050.0F);
        GL11.glScalef(1.0F, 1.0F, -1.0F);

        GL11.glTranslated(0.0D, disableRot ? 5.5D : 0.0D, 1000.0D);
        GL11.glScalef(pScale, pScale, pScale);
        GL11.glRotatef(180.0F, 0, 0, 1);
        rotateAndEnableLighting();
        float xp = disableRot ? 0.0F : -10.0F;
        GL11.glRotatef(xp, 1, 0, 0);

        float yBodyRot = player.renderYawOffset;
        float yBodyRotO = player.prevRenderYawOffset;
        float yRot = player.rotationYaw;
        float yRotO = player.prevRotationYaw;
        float xRot = player.rotationPitch;
        float xRotO = player.prevRotationPitch;
        float yHeadRot = player.rotationYawHead;
        float yHeadRotO = player.prevRotationYawHead;
        Entity vehicle = player.ridingEntity;

        ItemStack[] itemStacks = new ItemStack[EntityEquipmentSlot.values().length];
        int i = 0;
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            itemStacks[i] = slot.getItemStackFromSlot(player);
            if (slot == EntityEquipmentSlot.MAINHAND) {
                Hand.MAIN_HAND.setHeldItem(player, null);
            } else if (slot == EntityEquipmentSlot.OFFHAND) {
                Hand.OFF_HAND.setHeldItem(player, null);
            } else {
                player.inventory.armorInventory[slot.getIndex()] = null;
            }
            i++;
        }

        float renderYRot = disableRot ? 180.0F : 200.0F;
        player.renderYawOffset = renderYRot;
        player.prevRenderYawOffset = player.renderYawOffset;
        player.rotationYaw = renderYRot;
        player.prevRotationYaw = player.rotationYaw;
        player.rotationPitch = 0.0F;
        player.prevRotationPitch = player.rotationPitch;
        player.rotationYawHead = player.renderYawOffset;
        player.prevRotationYawHead = player.rotationYawHead;
        player.ridingEntity = null;

        RenderManager dispatcher = RenderManager.instance;
        xp = 180.0F - xp;
        dispatcher.playerViewY = xp;
        //dispatcher.setRenderShadow(false);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, player.yOffset, 0.0F);
        renderer.render(player, entity, 0, 0, 0, 0.0F, 1.0F);
        GL11.glPopMatrix();
        //dispatcher.setRenderShadow(true);

        player.renderYawOffset = yBodyRot;
        player.prevRenderYawOffset = yBodyRotO;
        player.rotationYaw = yRot;
        player.prevRotationYaw = yRotO;
        player.rotationPitch = xRot;
        player.prevRotationPitch = xRotO;
        player.rotationYawHead = yHeadRot;
        player.prevRotationYawHead = yHeadRotO;
        player.ridingEntity = vehicle;

        i = 0;
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            ItemStack itemStack = itemStacks[i];
            if (slot == EntityEquipmentSlot.MAINHAND) {
                Hand.MAIN_HAND.setHeldItem(player, itemStack);
            } else if (slot == EntityEquipmentSlot.OFFHAND) {
                Hand.OFF_HAND.setHeldItem(player, itemStack);
            } else {
                player.inventory.armorInventory[slot.getIndex()] = itemStack;
            }
            i++;
        }

        GL11.glPopMatrix();

        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static void renderPlayerEntity(EntityPlayer player, double posX, double posY, float scale, float yawOffset, int z) {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) posX + scale * 0.5f, (float) posY + scale * 2, z);
        GL11.glScalef(1, 1, -1);
        GL11.glScalef(scale, scale, scale);
        GL11.glRotatef(180.0F, 0, 0, 1);
        rotateAndEnableLighting();
        float yRot = player.renderYawOffset + yawOffset - 180;
        GL11.glRotatef(yRot, 0, 1, 0);
        RenderManager renderDispatcher = RenderManager.instance;
        yRot = 180.0F - yRot;
        renderDispatcher.playerViewY = yRot;
        //renderDispatcher.setRenderShadow(false);
        GL11.glTranslatef(0.0F, player.yOffset, 0.0F);
        // Args: entity, x, y, z, yaw, partialTicks
        renderDispatcher.renderEntityWithPosYaw(player, 0, 0, 0, 0.0F, 1.0F);
        //renderDispatcher.setRenderShadow(true);
        GL11.glPopMatrix();

        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void rotateAndEnableLighting() {
        GL11.glRotatef(135.0F, 0, 1, 0);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0, 1, 0);
    }

    public static void scissor(int screenX, int screenY, int boxWidth, int boxHeight) {
        final Minecraft mc = Minecraft.getMinecraft();
        int scale = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight).getScaleFactor();

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(
                screenX * scale,
                mc.displayHeight - (screenY * scale + boxHeight * scale),
                Math.max(0, boxWidth * scale),
                Math.max(0, boxHeight * scale)
        );
    }

    /**
     * Draws a textured rectangle at z = 0.
     */
    public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
        Gui.func_146110_a(x, y, u, v, width, height, textureWidth, textureHeight);
    }

    /**
     * Draws a scaled, textured, tiled modal rect at z = 0.
     *
     * @param u          Texture U (or x) coordinate, in pixels
     * @param v          Texture V (or y) coordinate, in pixels
     * @param uWidth     Width of the rendered part of the texture, in pixels.
     *                   Parts of the texture outside of it will wrap around
     * @param vHeight    Height of the rendered part of the texture, in pixels.
     *                   Parts of the texture outside of it will wrap around
     * @param tileWidth  total width of the texture
     * @param tileHeight total height of the texture
     */
    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        Gui.func_152125_a(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
    }
}
