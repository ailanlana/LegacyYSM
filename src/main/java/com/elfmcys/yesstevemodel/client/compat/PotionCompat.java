package com.elfmcys.yesstevemodel.client.compat;

import cpw.mods.fml.common.Optional;
import ganymedes01.etfuturum.items.ItemLingeringPotion;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

public class PotionCompat {
    public static boolean isThrowablePotion(ItemStack stack) {
        return ItemPotion.isSplash(stack.getItemDamage()) || (Mods.ET_FUTURUM_INSTALLED && isLingering(stack.getItem()));
    }

    @Optional.Method(modid = Mods.ET_FUTURUM)
    private static boolean isLingering(Item item) {
        return item instanceof ItemLingeringPotion;
    }
}
