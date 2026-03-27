package com.elfmcys.yesstevemodel.client.compat;

import com.unascribed.backlytra.FieldImitations;
import com.unascribed.backlytra.ItemElytra;
import com.unascribed.backlytra.MethodImitations;
import cpw.mods.fml.common.Optional;
import ganymedes01.etfuturum.api.elytra.IElytraPlayer;
import ganymedes01.etfuturum.elytra.IClientElytraPlayer;
import ganymedes01.etfuturum.items.equipment.ItemArmorElytra;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ElytraCompat {
    public static boolean isWearingElytra(EntityLivingBase living) {
        return (Mods.ET_FUTURUM_INSTALLED && isEFElytra(living)) ||
                (Mods.BACKLYTRA_INSTALLED && isBElytra(living));
    }

    @Optional.Method(modid = Mods.ET_FUTURUM)
    private static boolean isEFElytra(EntityLivingBase living) {
        return ItemArmorElytra.getElytra(living) != null;
    }

    @Optional.Method(modid = Mods.BACKLYTRA)
    private static boolean isBElytra(EntityLivingBase living) {
        ItemStack stack = EntityEquipmentSlot.CHEST.getItemStackFromSlot(living);
        return stack != null && stack.getItem() instanceof ItemElytra;
    }

    public static boolean isElytraFlying(EntityPlayer player) {
        return (Mods.ET_FUTURUM_INSTALLED && isEFFlying(player)) ||
                (Mods.BACKLYTRA_INSTALLED && isBFlying(player));
    }

    @Optional.Method(modid = Mods.ET_FUTURUM)
    private static boolean isEFFlying(EntityPlayer player) {
        return ((IElytraPlayer) player).etfu$isElytraFlying();
    }

    @Optional.Method(modid = Mods.BACKLYTRA)
    private static boolean isBFlying(EntityPlayer player) {
        return MethodImitations.isElytraFlying(player);
    }

    public static float getRotateElytraX(AbstractClientPlayer player) {
        return Mods.ET_FUTURUM_INSTALLED && isEFFlying(player) ? getEFRotX(player) :
                (Mods.BACKLYTRA_INSTALLED && isBFlying(player) ? getBRotX(player) : 0F);
    }

    @Optional.Method(modid = Mods.ET_FUTURUM)
    private static float getEFRotX(AbstractClientPlayer player) {
        return ((IClientElytraPlayer) player).getRotateElytraX();
    }

    @Optional.Method(modid = Mods.BACKLYTRA)
    private static float getBRotX(AbstractClientPlayer player) {
        return FieldImitations.get(player, "rotateElytraX", 0F);
    }

    public static float getRotateElytraY(AbstractClientPlayer player) {
        return Mods.ET_FUTURUM_INSTALLED && isEFFlying(player) ? getEFRotY(player) :
                (Mods.BACKLYTRA_INSTALLED && isBFlying(player) ? getBRotY(player) : 0F);
    }

    @Optional.Method(modid = Mods.ET_FUTURUM)
    private static float getEFRotY(AbstractClientPlayer player) {
        return ((IClientElytraPlayer) player).getRotateElytraY();
    }

    @Optional.Method(modid = Mods.BACKLYTRA)
    private static float getBRotY(AbstractClientPlayer player) {
        return FieldImitations.get(player, "rotateElytraY", 0F);
    }

    public static float getRotateElytraZ(AbstractClientPlayer player) {
        return Mods.ET_FUTURUM_INSTALLED && isEFFlying(player) ? getEFRotZ(player) :
                (Mods.BACKLYTRA_INSTALLED && isBFlying(player) ? getBRotZ(player) : 0F);
    }

    @Optional.Method(modid = Mods.ET_FUTURUM)
    private static float getEFRotZ(AbstractClientPlayer player) {
        return ((IClientElytraPlayer) player).getRotateElytraZ();
    }

    @Optional.Method(modid = Mods.BACKLYTRA)
    private static float getBRotZ(AbstractClientPlayer player) {
        return FieldImitations.get(player, "rotateElytraZ", 0F);
    }
}
