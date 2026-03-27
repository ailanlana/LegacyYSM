package com.elfmcys.yesstevemodel.geckolib3.util;

import net.minecraft.util.MathHelper;

import javax.annotation.concurrent.Immutable;

@SuppressWarnings("unused")
@Immutable
public class Vec3i implements Comparable<Vec3i> {
    public static final Vec3i ZERO = new Vec3i(0, 0, 0);
    private final int x;
    private final int y;
    private final int z;

    public Vec3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3i(int[] pos) {
        this(pos[0], pos[1], pos[2]);
    }

    public Vec3i(float x, float y, float z) {
        this(MathHelper.floor_float(x), MathHelper.floor_float(y), MathHelper.floor_float(z));
    }

    public Vec3i(double x, double y, double z) {
        this(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof Vec3i vec && this.getX() == vec.getX() && this.getY() == vec.getY() && this.getZ() == vec.getZ());
    }

    @Override
    public int hashCode() {
        return (this.getY() + this.getZ() * 31) * 31 + this.getX();
    }

    @Override
    public int compareTo(Vec3i vec) {
        if (this.getY() == vec.getY()) {
            return this.getZ() == vec.getZ() ? this.getX() - vec.getX() : this.getZ() - vec.getZ();
        } else {
            return this.getY() - vec.getY();
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public Vec3i crossProduct(Vec3i vec) {
        return new Vec3i(
                this.getY() * vec.getZ() - this.getZ() * vec.getY(),
                this.getZ() * vec.getX() - this.getX() * vec.getZ(),
                this.getX() * vec.getY() - this.getY() * vec.getX()
        );
    }

    public double getDistance(int xIn, int yIn, int zIn) {
        double d0 = this.getX() - xIn;
        double d1 = this.getY() - yIn;
        double d2 = this.getZ() - zIn;
        return Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    /**
     * Calculate squared distance to the given coordinates
     */
    public double distanceSq(double toX, double toY, double toZ) {
        double d0 = (double) this.getX() - toX;
        double d1 = (double) this.getY() - toY;
        double d2 = (double) this.getZ() - toZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    /**
     * Compute square of distance from point x, y, z to center of this Block
     */
    public double distanceSqToCenter(double xIn, double yIn, double zIn) {
        double d0 = (double) this.getX() + 0.5D - xIn;
        double d1 = (double) this.getY() + 0.5D - yIn;
        double d2 = (double) this.getZ() + 0.5D - zIn;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    /**
     * Calculate squared distance to the given Vector
     */
    public double distanceSq(Vec3i to) {
        return this.distanceSq(to.getX(), to.getY(), to.getZ());
    }

    @Override
    public String toString() {
        return String.format("x = %s, y = %s, z = %s", this.getX(), this.getY(), this.getZ());
    }
}
