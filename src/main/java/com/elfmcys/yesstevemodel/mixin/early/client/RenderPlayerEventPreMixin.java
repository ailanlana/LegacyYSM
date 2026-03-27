package com.elfmcys.yesstevemodel.mixin.early.client;

import com.elfmcys.yesstevemodel.geckolib3.extended.RenderPlayerEventPreAccessor;
import net.minecraftforge.client.event.RenderPlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("UnusedMixin")
@Mixin(RenderPlayerEvent.Pre.class)
public class RenderPlayerEventPreMixin implements RenderPlayerEventPreAccessor {
    @Unique
    private double ysm$x = 0;
    @Unique
    private double ysm$y = 0;
    @Unique
    private double ysm$z = 0;

    @Unique
    @Override
    public void ysm$setX(double x) {
        this.ysm$x = x;
    }

    @Unique
    @Override
    public void ysm$setY(double y) {
        this.ysm$y = y;
    }

    @Unique
    @Override
    public void ysm$setZ(double z) {
        this.ysm$z = z;
    }

    @Unique
    @Override
    public double ysm$getX() {
        return this.ysm$x;
    }

    @Unique
    @Override
    public double ysm$getY() {
        return this.ysm$y;
    }

    @Unique
    @Override
    public double ysm$getZ() {
        return this.ysm$z;
    }
}
