package com.elfmcys.yesstevemodel.client.compat;

import cpw.mods.fml.common.Optional;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import xonin.backhand.api.core.BackhandUtils;
import xonin.backhand.api.core.EnumHand;
import xonin.backhand.api.core.IBackhandPlayer;
import xonin.backhand.compat.IOffhandRenderOptOut;

import javax.annotation.Nullable;

public enum Hand {
    // Backhand 没有反转主副手功能，这里也就直接硬写左右了
    // 实际上，最新版 YSM 也不支持反转
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
            return player.isUsingItem() && !OFF_HAND.isUsing(player);
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
            // 当安装 Backhand 时，左右手使用都会让 isUsingItem 返回 true
            return Mods.BACKHAND_INSTALLED && player.isUsingItem() && isUsingOffhand(player);
        }

        @Optional.Method(modid = Mods.BACKHAND)
        private static boolean isUsingOffhand(EntityPlayer player) {
            return ((IBackhandPlayer) player).isOffhandItemInUse();
        }

        @Override
        public boolean isSwinging(EntityPlayer player) {
            return Mods.BACKHAND_INSTALLED && isSwingingOffhand(player);
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

    public static boolean isSpecialOffhandRenderer(IItemRenderer customRenderer) {
        return Mods.BACKHAND_INSTALLED && isIOffhandRenderOptOut(customRenderer);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Optional.Method(modid = Mods.BACKHAND)
    private static boolean isIOffhandRenderOptOut(IItemRenderer customRenderer) {
        return customRenderer instanceof IOffhandRenderOptOut;
    }
}
