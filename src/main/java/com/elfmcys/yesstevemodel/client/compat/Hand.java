package com.elfmcys.yesstevemodel.client.compat;

import cpw.mods.fml.common.Optional;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import xonin.backhand.api.core.BackhandUtils;
import xonin.backhand.api.core.EnumHand;
import xonin.backhand.api.core.IBackhandPlayer;
import xonin.backhand.client.utils.BackhandRenderHelper;

import javax.annotation.Nullable;

public enum Hand {
    /// 右手
    MAIN_HAND {
        @Override
        public ItemStack getHeldItem(EntityLivingBase living) {
            return Mods.BACKHAND_INSTALLED && living instanceof EntityPlayer player ? getMainHandItem(player) : living.getHeldItem();
        }

        @Optional.Method(modid = Mods.BACKHAND)
        private static ItemStack getMainHandItem(EntityPlayer player) {
            return EnumHand.MAIN_HAND.getItem(player);
        }

        @Override
        public void setHeldItem(EntityPlayer player, ItemStack stack) {
            player.inventory.mainInventory[player.inventory.currentItem] = stack;
        }

        @Override
        public boolean isUsing(EntityPlayer player) {
            return player.isUsingItem();
        }

        @Override
        public boolean isSwinging(EntityPlayer player) {
            return player.isSwingInProgress;
        }
    },
    /// 左手
    OFF_HAND {
        @Override
        public ItemStack getHeldItem(EntityLivingBase living) {
            return Mods.BACKHAND_INSTALLED && living instanceof EntityPlayer player ? getOffHandItem(player) : null;
        }

        @Optional.Method(modid = Mods.BACKHAND)
        private static ItemStack getOffHandItem(EntityPlayer player) {
            return EnumHand.OFF_HAND.getItem(player);
        }

        @Override
        public void setHeldItem(EntityPlayer player, ItemStack stack) {
            if (Mods.BACKHAND_INSTALLED) setOffHandItem(player, stack);
        }

        @Optional.Method(modid = Mods.BACKHAND)
        private static void setOffHandItem(EntityPlayer player, ItemStack stack) {
            BackhandUtils.setPlayerOffhandItem(player, stack);
        }

        @Override
        public boolean isUsing(EntityPlayer player) {
            return isUsingOffhand(player);
        }

        @Optional.Method(modid = Mods.BACKHAND)
        private static boolean isUsingOffhand(EntityPlayer player) {
            return BackhandUtils.isUsingOffhand(player);
        }

        @Override
        public boolean isSwinging(EntityPlayer player) {
            return isSwingingOffhand(player);
        }

        @Optional.Method(modid = Mods.BACKHAND)
        private static boolean isSwingingOffhand(EntityPlayer player) {
            return ((IBackhandPlayer) player).getOffSwingProgress(1.0F) > 0;
        }
    };

    public abstract ItemStack getHeldItem(EntityLivingBase living);

    public abstract void setHeldItem(EntityPlayer player, ItemStack stack);

    public abstract boolean isUsing(EntityPlayer player);

    public abstract boolean isSwinging(EntityPlayer player);

    @Nullable
    public static Hand getUsingHand(EntityPlayer player) {
        return OFF_HAND.isUsing(player) ? OFF_HAND : (MAIN_HAND.isUsing(player) ? MAIN_HAND : null);
    }

    @Nullable
    public static Hand getSwingingHand(EntityPlayer player) {
        return OFF_HAND.isSwinging(player) ? OFF_HAND : (MAIN_HAND.isSwinging(player) ? MAIN_HAND : null);
    }

    private static final ModelBiped DUMMY_BIPED = new ModelBiped();

    static {
        DUMMY_BIPED.bipedLeftArm.rotationPointX = 0.0F;
        DUMMY_BIPED.bipedLeftArm.rotationPointY = 0.0F;
        DUMMY_BIPED.bipedLeftArm.rotationPointZ = 0.0F;
        DUMMY_BIPED.bipedLeftArm.rotateAngleX = 0.0F;
        DUMMY_BIPED.bipedLeftArm.rotateAngleY = 0.0F;
        DUMMY_BIPED.bipedLeftArm.rotateAngleZ = 0.0F;
    }

    public static void renderOffhandItemIn3rdPerson(EntityLivingBase living) {
        if (Mods.BACKHAND_INSTALLED && living instanceof EntityPlayer player) renderBackhandItem(player);
    }

    @Optional.Method(modid = Mods.BACKHAND)
    private static void renderBackhandItem(EntityPlayer player) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0625F, -0.4375F, -0.0625F);
        BackhandRenderHelper.renderOffhandItemIn3rdPerson(player, DUMMY_BIPED, 0F);
        GL11.glPopMatrix();
    }
}
