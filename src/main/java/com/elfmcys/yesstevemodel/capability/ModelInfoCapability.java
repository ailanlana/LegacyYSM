package com.elfmcys.yesstevemodel.capability;

import com.elfmcys.yesstevemodel.YesSteveModel;
import com.elfmcys.yesstevemodel.config.ServerConfig;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ModelInfoCapability implements IExtendedEntityProperties {
    public static final String IDENTIFIER = new ResourceLocation(YesSteveModel.MOD_ID, "model_id").toString();
    private ResourceLocation modelId = new ResourceLocation(YesSteveModel.MOD_ID, ServerConfig.DEFAULT_MODEL_ID);
    private ResourceLocation selectTexture = new ResourceLocation(YesSteveModel.MOD_ID, ServerConfig.DEFAULT_MODEL_ID + "/" + ServerConfig.DEFAULT_MODEL_TEXTURE);
    private String animation = "idle";
    private boolean playAnimation = false;
    private boolean dirty;

    public void setModelAndTexture(ResourceLocation modelId, ResourceLocation selectTexture) {
        this.modelId = modelId;
        this.selectTexture = selectTexture;
        this.markDirty();
    }

    public void copyFrom(ModelInfoCapability source) {
        this.modelId = source.modelId;
        this.selectTexture = source.selectTexture;
        this.animation = source.animation;
        this.playAnimation = source.playAnimation;
        this.markDirty();
    }

    public ResourceLocation getModelId() {
        return this.modelId;
    }

    public ResourceLocation getSelectTexture() {
        return this.selectTexture;
    }

    public void setSelectTexture(ResourceLocation selectTexture) {
        this.selectTexture = selectTexture;
        this.markDirty();
    }

    public void playAnimation(String animation) {
        this.animation = animation;
        this.playAnimation = true;
        this.markDirty();
    }

    public void stopAnimation() {
        this.playAnimation = false;
        this.markDirty();
    }

    public String getAnimation() {
        return this.animation;
    }

    public boolean isPlayAnimation() {
        return this.playAnimation;
    }

    public void markDirty() {
        this.dirty = true;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("model_id", this.modelId.toString());
        tag.setString("select_texture", this.selectTexture.toString());
        tag.setString("animation", this.animation);
        tag.setBoolean("play_animation", this.playAnimation);
        return tag;
    }

    public void deserializeNBT(NBTTagCompound nbt) {
        this.modelId = new ResourceLocation(nbt.getString("model_id"));
        this.selectTexture = new ResourceLocation(nbt.getString("select_texture"));
        this.animation = nbt.getString("animation");
        this.playAnimation = nbt.getBoolean("play_animation");
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        compound.setTag(IDENTIFIER, this.serializeNBT());
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (compound.hasKey(IDENTIFIER)) {
            this.deserializeNBT(compound.getCompoundTag(IDENTIFIER));
        }
    }

    @Override
    public void init(Entity entity, World world) {
    }
}
