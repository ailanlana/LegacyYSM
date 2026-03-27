package com.elfmcys.yesstevemodel.client.renderer.layer;

import com.elfmcys.yesstevemodel.client.compat.Hand;
import com.elfmcys.yesstevemodel.geckolib3.core.util.Color;
import com.elfmcys.yesstevemodel.geckolib3.geo.GeoLayerRenderer;
import com.elfmcys.yesstevemodel.geckolib3.geo.IGeoRenderer;
import com.elfmcys.yesstevemodel.geckolib3.geo.render.built.GeoModel;
import com.elfmcys.yesstevemodel.geckolib3.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

public class CustomPlayerItemInHandLayer<T extends EntityLivingBase, R extends IGeoRenderer<T>> extends GeoLayerRenderer<T, R> {
    public CustomPlayerItemInHandLayer(R entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(@Nonnull T entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, Color renderColor) {
        if (this.entityRenderer.getGeoModel() == null) {
            return;
        }
        ItemStack offhandItem = Hand.OFF_HAND.getHeldItem(entityLivingBaseIn);
        ItemStack mainHandItem = Hand.MAIN_HAND.getHeldItem(entityLivingBaseIn);
        GeoModel geoModel = this.entityRenderer.getGeoModel();
        if (offhandItem != null || mainHandItem != null) {
            if (!geoModel.rightHandBones.isEmpty()) {
                GL11.glPushMatrix();
                this.renderArmWithItem(entityLivingBaseIn, mainHandItem, Hand.MAIN_HAND);
                GL11.glPopMatrix();
            }
            if (!geoModel.leftHandBones.isEmpty()) {
                GL11.glPushMatrix();
                this.renderArmWithItem(entityLivingBaseIn, offhandItem, Hand.OFF_HAND);
                GL11.glPopMatrix();
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }


    protected void renderArmWithItem(EntityLivingBase livingEntity, ItemStack itemStack, Hand arm) {
        if (itemStack != null) {
            boolean isLeftHand = arm == Hand.OFF_HAND;
            // 缩放不为 0 才会渲染
            boolean scaleResult = translateToHand(arm, this.entityRenderer.getGeoModel());
            if (!scaleResult) {
                GL11.glTranslatef(0, -0.0625F, -0.1F);
                GL11.glRotatef(-90.0F, 1, 0, 0);
                if (isLeftHand) {
                    Hand.renderOffhandItemIn3rdPerson(livingEntity);
                } else {
                    Minecraft.getMinecraft().entityRenderer.itemRenderer.renderItem(livingEntity, itemStack, 0);
                }
            }
        }
    }

    protected static boolean translateToHand(Hand arm, GeoModel geoModel) {
        return RenderUtils.prepMatrixForLocator(arm == Hand.OFF_HAND ? geoModel.leftHandBones : geoModel.rightHandBones);
    }
}
