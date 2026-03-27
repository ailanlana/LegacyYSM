package com.elfmcys.yesstevemodel.geckolib3.geo;

import com.elfmcys.yesstevemodel.geckolib3.core.util.Color;
import com.elfmcys.yesstevemodel.geckolib3.model.provider.GeoModelProvider;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

public abstract class GeoLayerRenderer<T extends Entity, R extends IGeoRenderer<T>> {
    protected final R entityRenderer;

    public GeoLayerRenderer(R entityRendererIn) {
        this.entityRenderer = entityRendererIn;
    }

    protected static <T extends EntityLivingBase> void renderCopyCutoutModel(
            ModelBase modelParentIn, ModelBase modelIn,
            ResourceLocation textureLocationIn, T entityIn,
            float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch, float partialTicks,
            float red, float green, float blue
    ) {
        if (!entityIn.isInvisible()) {
            modelParentIn.onGround = modelIn.onGround; // 应为 swingProgress，MCP 标错了
            modelParentIn.isRiding = modelIn.isRiding;
            modelParentIn.isChild = modelIn.isChild;
            modelIn.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTicks);
            modelIn.setRotationAngles(
                    limbSwing, limbSwingAmount, ageInTicks,
                    netHeadYaw, headPitch, 1 / 16F, entityIn
            );
            renderCutoutModel(
                    modelIn, textureLocationIn, entityIn,
                    limbSwing, limbSwingAmount, ageInTicks,
                    netHeadYaw, headPitch, 1 / 16F, red, green, blue
            );
        }
    }

    protected static <T extends EntityLivingBase> void renderCutoutModel(
            ModelBase modelIn, ResourceLocation textureLocationIn, T entityIn,
            float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch, float scale,
            float red, float green, float blue
    ) {
        GL11.glColor4f(red, green, blue, 1f);
        modelIn.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    public IGeoRenderer<T> getRenderer() {
        return this.entityRenderer;
    }

    @SuppressWarnings("unchecked")
    public GeoModelProvider<T> getEntityModel() {
        return this.entityRenderer.getGeoModelProvider();
    }

    protected ResourceLocation getEntityTexture(T entityIn) {
        return this.entityRenderer.getTextureLocation(entityIn);
    }

    /**
     * 核心渲染方法，会在主模型渲染后调用。
     */
    public abstract void render(
            @Nonnull T entityLivingBaseIn, float limbSwing, float limbSwingAmount,
            float partialTicks, float ageInTicks,
            float netHeadYaw, float headPitch, Color renderColor
    );

    /**
     * 由外部调用，可影响是否要加上额外效果，如受击红光。
     */
    public boolean shouldCombineTextures() {
        return false;
    }
}
