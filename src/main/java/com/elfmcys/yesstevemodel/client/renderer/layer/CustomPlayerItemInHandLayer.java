package com.elfmcys.yesstevemodel.client.renderer.layer;

import com.elfmcys.yesstevemodel.client.compat.Hand;
import com.elfmcys.yesstevemodel.geckolib3.core.util.Color;
import com.elfmcys.yesstevemodel.geckolib3.geo.GeoLayerRenderer;
import com.elfmcys.yesstevemodel.geckolib3.geo.IGeoRenderer;
import com.elfmcys.yesstevemodel.geckolib3.geo.render.built.GeoBone;
import com.elfmcys.yesstevemodel.geckolib3.geo.render.built.GeoModel;
import com.elfmcys.yesstevemodel.geckolib3.util.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.List;

public class CustomPlayerItemInHandLayer<T extends EntityLivingBase, R extends IGeoRenderer<T>> extends GeoLayerRenderer<T, R> {
    public CustomPlayerItemInHandLayer(R entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(@Nonnull T entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, Color renderColor) {
        GeoModel geoModel = this.entityRenderer.getGeoModel();
        if (geoModel == null) return;
        renderArmWithItem(entityLivingBaseIn, geoModel.rightHandBones, Hand.MAIN_HAND);
        renderArmWithItem(entityLivingBaseIn, geoModel.leftHandBones, Hand.OFF_HAND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected static void renderArmWithItem(EntityLivingBase livingEntity, List<GeoBone> bones, Hand arm) {
        if (bones.isEmpty() || !(livingEntity instanceof EntityPlayer player)) return;
        ItemStack stack = arm.getHeldItem(livingEntity);
        if (stack == null) return;
        Item item = stack.getItem();
        if (item == null) return;

        GL11.glPushMatrix();
        // 缩放不为 0 才会渲染
        boolean scaleResult = RenderUtils.prepMatrixForLocator(bones);
        if (!scaleResult) {
            GL11.glTranslated(0, 0.0625, 0.1);
            /// {@link xonin.backhand.client.utils.BackhandRenderHelper#renderOffhandItemIn3rdPerson(EntityPlayer, ModelBiped, float)}
            boolean isLeftHand = arm == Hand.OFF_HAND;

            GL11.glScalef(-1, -1, 1);
            if (isLeftHand) {
                GL11.glScalef(-1, 1, 1);
                GL11.glFrontFace(GL11.GL_CW);
            }

            if (player.fishEntity != null && item == Items.fishing_rod) {
                stack = new ItemStack(Items.stick);
            }

            IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(stack, IItemRenderer.ItemRenderType.EQUIPPED);
            if (isLeftHand && Hand.isSpecialOffhandRenderer(customRenderer)) {
                GL11.glFrontFace(GL11.GL_CCW);
                GL11.glPopMatrix();
                return;
            }
            boolean is3DBlock = customRenderer != null && customRenderer.shouldUseRenderHelper(IItemRenderer.ItemRenderType.EQUIPPED, stack, IItemRenderer.ItemRendererHelper.BLOCK_3D);
            if (is3DBlock || (item instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(item).getRenderType()))) {
                float scale = 0.5F;
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                scale *= 0.75F;
                GL11.glRotatef(20.0F, 1, 0, 0);
                GL11.glRotatef(45.0F, 0, 1, 0);
                GL11.glScalef(-scale, -scale, scale);
            } else if (item == Items.bow) {
                float scale = 0.625F;
                GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
                GL11.glRotatef(-20.0F, 0, 1, 0);
                GL11.glScalef(scale, -scale, scale);
                GL11.glRotatef(-100.0F, 1, 0, 0);
                GL11.glRotatef(45.0F, 0, 1, 0);
            } else if (item.isFull3D()) {
                float scale = 0.625F;
                if (item.shouldRotateAroundWhenRendering()) {
                    GL11.glRotatef(180.0F, 0, 0, 1);
                    GL11.glTranslatef(0.0F, -0.125F, 0.0F);
                }
                if (player.getItemInUseCount() > 0 && stack.getItemUseAction() == EnumAction.block) {
                    GL11.glTranslatef(0.05F, 0.0F, -0.1F);
                    GL11.glRotatef(-50.0F, 0, 1, 0);
                    GL11.glRotatef(-10.0F, 1, 0, 0);
                    GL11.glRotatef(-60.0F, 0, 0, 1);
                }
                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                GL11.glScalef(scale, -scale, scale);
                GL11.glRotatef(-100.0F, 1, 0, 0);
                GL11.glRotatef(45.0F, 0, 1, 0);
            } else {
                float scale = 0.375F;
                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                GL11.glScalef(scale, scale, scale);
                GL11.glRotatef(60.0F, 0, 0, 1);
                GL11.glRotatef(-90.0F, 1, 0, 0);
                GL11.glRotatef(20.0F, 0, 0, 1);
            }

            for (int i = 0; i < item.getRenderPasses(stack.getItemDamage()); i++) {
                int color = item.getColorFromItemStack(stack, i);
                GL11.glColor4f(
                        (float) (color >> 16 & 255) / 255.0F,
                        (float) (color >> 8 & 255) / 255.0F,
                        (float) (color & 255) / 255.0F,
                        1.0F
                );
                RenderManager.instance.itemRenderer.renderItem(player, stack, i);
            }

            if (isLeftHand) GL11.glFrontFace(GL11.GL_CCW);
        }
        GL11.glPopMatrix();
    }
}
