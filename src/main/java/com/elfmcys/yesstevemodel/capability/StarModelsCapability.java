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

public class StarModelsCapability implements IExtendedEntityProperties {
    public static final String IDENTIFIER = new ResourceLocation(YesSteveModel.MOD_ID, "star_models").toString();
    private Set<ResourceLocation> starModels = Sets.newHashSet();

    public void addModel(ResourceLocation modelId) {
        this.starModels.add(modelId);
    }

    public void copyFrom(StarModelsCapability source) {
        this.starModels = source.starModels;
    }

    public void removeModel(ResourceLocation modelId) {
        this.starModels.remove(modelId);
    }

    public boolean containModel(ResourceLocation modelId) {
        return this.starModels.contains(modelId);
    }

    public Set<ResourceLocation> getStarModels() {
        return this.starModels;
    }

    public void setStarModels(Set<ResourceLocation> starModels) {
        this.starModels = starModels;
    }

    public void clear() {
        this.starModels.clear();
    }

    public NBTTagList serializeNBT() {
        NBTTagList listTag = new NBTTagList();
        for (ResourceLocation modelId : this.starModels) {
            listTag.appendTag(new NBTTagString(modelId.toString()));
        }
        return listTag;
    }

    public void deserializeNBT(NBTTagList nbt) {
        this.starModels.clear();
        for (int i = 0; i < nbt.tagCount(); i++) {
            this.starModels.add(new ResourceLocation(nbt.getStringTagAt(i)));
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
