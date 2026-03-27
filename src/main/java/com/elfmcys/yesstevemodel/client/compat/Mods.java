package com.elfmcys.yesstevemodel.client.compat;

import cpw.mods.fml.common.Loader;

public final class Mods {
    public static final String BACKHAND = "backhand";
    static boolean BACKHAND_INSTALLED = false;
    static final String ET_FUTURUM = "etfuturum";
    static boolean ET_FUTURUM_INSTALLED = false;
    static final String AQUA_ACROBATICS = "aquaacrobatics";
    static boolean AA_INSTALLED = false;
    static final String BACKLYTRA = "backlytra";
    static boolean BACKLYTRA_INSTALLED = false;
    static final String MKB = "mkb";
    static boolean MKB_INSTALLED = false;
    static final String CONTROLLING = "controlling";
    static boolean CONTROLLING_INSTALLED = false;

    public static void init() {
        BACKHAND_INSTALLED = Loader.isModLoaded(BACKHAND);
        ET_FUTURUM_INSTALLED = Loader.isModLoaded(ET_FUTURUM);
        AA_INSTALLED = Loader.isModLoaded(AQUA_ACROBATICS);
        BACKLYTRA_INSTALLED = Loader.isModLoaded(BACKLYTRA);
        MKB_INSTALLED = Loader.isModLoaded(MKB);
        CONTROLLING_INSTALLED = Loader.isModLoaded(CONTROLLING);
    }
}
