package com.elfmcys.yesstevemodel.geckolib3.geo;

import com.elfmcys.yesstevemodel.geckolib3.core.IAnimatable;
import com.elfmcys.yesstevemodel.geckolib3.core.controller.AnimationController;
import com.elfmcys.yesstevemodel.geckolib3.core.event.predicate.AnimationEvent;
import com.elfmcys.yesstevemodel.geckolib3.core.util.Color;
import com.elfmcys.yesstevemodel.geckolib3.geo.render.built.GeoModel;
import com.elfmcys.yesstevemodel.geckolib3.model.AnimatedGeoModel;
import com.elfmcys.yesstevemodel.geckolib3.model.provider.data.EntityModelData;
import com.elfmcys.yesstevemodel.geckolib3.util.EModelRenderCycle;
import com.elfmcys.yesstevemodel.geckolib3.util.IRenderCycle;
import com.elfmcys.yesstevemodel.mclib.utils.Interpolations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"rawtypes", "unchecked"})
public class GeoProjectilesRenderer<T extends Entity, E extends IAnimatable> extends Render implements IGeoRenderer<T> {
    protected static final Map<Class<? extends IAnimatable>, GeoProjectilesRenderer> renderers = new ConcurrentHashMap<>();

    static {
        AnimationController.addModelFetcher((IAnimatable object) -> {
            GeoProjectilesRenderer renderer = renderers.get(object.getClass());
            return renderer == null ? null : renderer.getGeoModelProvider();
        });
    }

    protected final AnimatedGeoModel<IAnimatable> modelProvider;
    protected E animatable;
    private IRenderCycle currentModelRenderCycle = EModelRenderCycle.INITIAL;

    public GeoProjectilesRenderer(AnimatedGeoModel<IAnimatable> modelProvider, E animatable) {
        super();
        this.setRenderManager(RenderManager.instance);
        this.modelProvider = modelProvider;
        this.animatable = animatable;
        renderers.putIfAbsent(animatable.getClass(), this);
    }

    @Override
    public void doRender(@Nonnull Entity entity, double x, double y, double z, float yaw, float partialTick) {
        this.render((T) entity, x, y, z, yaw, partialTick);
    }

    public void render(@Nonnull T entity, double x, double y, double z, float yaw, float partialTick) {
        GeoModel model = this.modelProvider.getModel(this.modelProvider.getModelLocation(this.animatable));
        this.setCurrentModelRenderCycle(EModelRenderCycle.INITIAL);
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(Interpolations.lerp(entity.prevRotationYaw, entity.rotationYaw, partialTick) - 90, 0, 1, 0);
        GL11.glRotatef(Interpolations.lerp(entity.prevRotationPitch, entity.rotationPitch, partialTick), 0, 0, 1);

        /*
        参考 RenderArrow
         */
        // 箭矢不摇晃似乎是预期行为，先注释掉
        /*
        if (entity instanceof EntityArrow arrow) {
            float deltaShake = arrow.arrowShake - partialTick;
            if (deltaShake > 0.0F) {
                GL11.glRotatef(-MathHelper.sin(deltaShake * 3.0F) * deltaShake, 0.0F, 0.0F, 1.0F);
            }
        }
         */

        AnimationEvent<E> predicate = new AnimationEvent<>(this.animatable, 0, 0, partialTick, false, Collections.singletonList(new EntityModelData()));
        this.modelProvider.setCustomAnimations(this.animatable, this.getInstanceId(entity), predicate);

        Minecraft mc = Minecraft.getMinecraft();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glTranslatef(0, 0.01f, 0);
        boolean scoreTeamColor = false;

        Color renderColor = this.getRenderColor(entity, partialTick);
        boolean isVisible = !entity.isInvisible();
        boolean isGhost = !isVisible && !entity.isInvisibleToPlayer(mc.thePlayer);
        if (isVisible || isGhost) {
            this.bindEntityTexture(entity);
            if (isGhost) {
                // GlStateManager.Profile.TRANSPARENT_MODEL
                GL11.glPushMatrix();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.15F);
                GL11.glDepthMask(false);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glAlphaFunc(GL11.GL_GREATER, 1.0F / 255.0F);
            }
            this.render(model, entity, partialTick,
                    (float) renderColor.getRed() / 255f, (float) renderColor.getGreen() / 255f,
                    (float) renderColor.getBlue() / 255f, (float) renderColor.getAlpha() / 255);
            if (isGhost) {
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                GL11.glPopMatrix();
                GL11.glDepthMask(true);
            }
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    @Override
    public AnimatedGeoModel getGeoModelProvider() {
        return this.modelProvider;
    }

    @Override
    @Nonnull
    public IRenderCycle getCurrentModelRenderCycle() {
        return this.currentModelRenderCycle;
    }

    @Override
    public void setCurrentModelRenderCycle(IRenderCycle currentModelRenderCycle) {
        this.currentModelRenderCycle = currentModelRenderCycle;
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(@Nonnull Entity instance) {
        return this.getTextureLocation((T) instance);
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(@Nonnull T instance) {
        return this.modelProvider.getTextureLocation(this.animatable);
    }
}
