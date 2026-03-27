package com.elfmcys.yesstevemodel.capability;

import com.elfmcys.yesstevemodel.YesSteveModel;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ArrowModelCapability implements IExtendedEntityProperties {
    public static final String IDENTIFIER = new ResourceLocation(YesSteveModel.MOD_ID, "arrow_model").toString();
    private String modelId = "";

    public String getModelId() {
        return this.modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public void copyFrom(ArrowModelCapability source) {
        this.modelId = source.modelId;
    }

    public NBTTagString serializeNBT() {
        return new NBTTagString(this.modelId);
    }

    public void deserializeNBT(NBTTagString nbt) {
        this.modelId = nbt.func_150285_a_();
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        compound.setTag(IDENTIFIER, this.serializeNBT());
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (compound.hasKey(IDENTIFIER)) {
            this.deserializeNBT((NBTTagString) compound.getTag(IDENTIFIER));
        }
    }

    @Override
    public void init(Entity entity, World world) {
    }
}
