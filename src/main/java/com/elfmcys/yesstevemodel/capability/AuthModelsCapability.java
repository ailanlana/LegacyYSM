package com.elfmcys.yesstevemodel.capability;

import com.elfmcys.yesstevemodel.YesSteveModel;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.Set;

public class AuthModelsCapability implements IExtendedEntityProperties {
    public static final String IDENTIFIER = new ResourceLocation(YesSteveModel.MOD_ID, "own_models").toString();
    private Set<ResourceLocation> authModels = Sets.newHashSet();

    public void addModel(ResourceLocation modelId) {
        this.authModels.add(modelId);
    }

    public void copyFrom(AuthModelsCapability source) {
        this.authModels = source.authModels;
    }

    public void removeModel(ResourceLocation modelId) {
        this.authModels.remove(modelId);
    }

    public boolean containModel(ResourceLocation modelId) {
        return this.authModels.contains(modelId);
    }

    public Set<ResourceLocation> getAuthModels() {
        return this.authModels;
    }

    public void setAuthModels(Set<ResourceLocation> authModels) {
        this.authModels = authModels;
    }

    public void clear() {
        this.authModels.clear();
    }

    public NBTTagList serializeNBT() {
        NBTTagList listTag = new NBTTagList();
        for (ResourceLocation modelId : this.authModels) {
            listTag.appendTag(new NBTTagString(modelId.toString()));
        }
        return listTag;
    }

    public void deserializeNBT(NBTTagList nbt) {
        this.authModels.clear();
        for (int i = 0; i < nbt.tagCount(); i++) {
            this.authModels.add(new ResourceLocation(nbt.getStringTagAt(i)));
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        compound.setTag(IDENTIFIER, this.serializeNBT());
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (compound.hasKey(IDENTIFIER)) {
            this.deserializeNBT(compound.getTagList(IDENTIFIER, 8));
        }
    }

    @Override
    public void init(Entity entity, World world) {
    }
}
