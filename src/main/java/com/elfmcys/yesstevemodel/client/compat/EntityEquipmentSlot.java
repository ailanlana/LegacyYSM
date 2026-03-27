package com.elfmcys.yesstevemodel.client.compat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public enum EntityEquipmentSlot {
    MAINHAND(EntityEquipmentSlot.Type.HAND, 0, 0, "mainhand"),
    OFFHAND(EntityEquipmentSlot.Type.HAND, 1, 5, "offhand"),
    FEET(EntityEquipmentSlot.Type.ARMOR, 0, 1, "feet"),
    LEGS(EntityEquipmentSlot.Type.ARMOR, 1, 2, "legs"),
    CHEST(EntityEquipmentSlot.Type.ARMOR, 2, 3, "chest"),
    HEAD(EntityEquipmentSlot.Type.ARMOR, 3, 4, "head");

    private final EntityEquipmentSlot.Type slotType;
    private final int index;
    private final int slotIndex;
    private final String name;

    EntityEquipmentSlot(EntityEquipmentSlot.Type slotTypeIn, int indexIn, int slotIndexIn, String nameIn) {
        this.slotType = slotTypeIn;
        this.index = indexIn;
        this.slotIndex = slotIndexIn;
        this.name = nameIn;
    }

    public EntityEquipmentSlot.Type getSlotType() {
        return this.slotType;
    }

    public int getIndex() {
        return this.index;
    }

    /**
     * Gets the actual slot index.
     */
    public int getSlotIndex() {
        return this.slotIndex;
    }

    public String getName() {
        return this.name;
    }

    public ItemStack getItemStackFromSlot(EntityLivingBase living) {
        return switch (this) {
            case MAINHAND -> Hand.MAIN_HAND.getHeldItem(living);
            case OFFHAND -> Hand.OFF_HAND.getHeldItem(living);
            default -> living.getEquipmentInSlot(this.slotIndex);
        };
    }

    @Nullable
    public static EntityEquipmentSlot getType(String type) {
        for (EntityEquipmentSlot equipmentslot : EntityEquipmentSlot.values()) {
            if (equipmentslot.getName().equals(type)) {
                return equipmentslot;
            }
        }
        return null;
    }

    public enum Type {
        HAND,
        ARMOR
    }
}
