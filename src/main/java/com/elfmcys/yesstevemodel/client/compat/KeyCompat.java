package com.elfmcys.yesstevemodel.client.compat;

import com.elfmcys.yesstevemodel.YesSteveModel;
import committee.nova.mkb.api.IKeyBinding;
import committee.nova.mkb.keybinding.KeyConflictContext;
import committee.nova.mkb.keybinding.KeyModifier;
import cpw.mods.fml.common.Optional;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

import java.lang.reflect.Method;

public class KeyCompat {
    public static KeyBinding createKeyBinding(
            String description,
            ConflictContext conflictContext,
            Modifier modifier,
            int keyCode,
            String category
    ) {
        KeyBinding key = new KeyBinding(description, keyCode, category);
        modernizeKey(key, conflictContext, modifier);
        return key;
    }

    public static void modernizeKey(KeyBinding key, ConflictContext conflictContext, Modifier modifier) {
        if (Mods.MKB_INSTALLED) mkbKey(key, conflictContext, modifier);
        else if (Mods.CONTROLLING_INSTALLED) controllingKey(key, conflictContext, modifier);
    }

    @Optional.Method(modid = Mods.MKB)
    private static void mkbKey(KeyBinding key, ConflictContext conflictContext, Modifier modifier) {
        final IKeyBinding keyBinding = (IKeyBinding) key;
        keyBinding.setKeyConflictContext(KeyConflictContext.valueOf(conflictContext.name()));
        keyBinding.setInitialKeyModifierAndCode(KeyModifier.valueFromString(modifier.name()), key.getKeyCodeDefault());
    }

    @SuppressWarnings({"rawtypes", "JavaReflectionMemberAccess", "unchecked"})
    @Optional.Method(modid = Mods.CONTROLLING)
    private static void controllingKey(KeyBinding key, ConflictContext conflictContext, Modifier modifier) {
        // 使用反射实现，来兼容旧版 Controlling
        try {
            Class<?> apiC = Class.forName("com.blamejared.controlling.api.ControllingApi");
            Class<? extends Enum> modifierC = (Class<? extends Enum>) Class.forName("com.blamejared.controlling.api.ComboModifier");
            Enum<?> comboModifier = Enum.valueOf(modifierC, modifier.name());
            Method setDefaultComboKeyBinding = apiC.getMethod("setDefaultComboKeyBinding", KeyBinding.class, modifierC);
            setDefaultComboKeyBinding.invoke(null, key, comboModifier);
        } catch (ClassNotFoundException e) {
            Mods.CONTROLLING_INSTALLED = false;
        } catch (Exception e) {
            YesSteveModel.LOGGER.error("Error when modernizing KeyBinding through Controlling.", e);
        }
    }

    public static String getDisplayName(KeyBinding key) {
        if (Mods.MKB_INSTALLED) return getMKBName(key);
        else if (Mods.CONTROLLING_INSTALLED) return getControllingName(key);
        else return GameSettings.getKeyDisplayString(key.getKeyCode());
    }

    @Optional.Method(modid = Mods.MKB)
    private static String getMKBName(KeyBinding key) {
        return ((IKeyBinding) key).getDisplayName();
    }

    @Optional.Method(modid = Mods.CONTROLLING)
    private static String getControllingName(KeyBinding key) {
        try {
            Class<?> comboBindingC = Class.forName("com.blamejared.controlling.keybinding.ComboKeyBinding");
            if (comboBindingC.isInstance(key)) {
                Method getDisplayNameM = comboBindingC.getMethod("controlling$getDisplayName");
                return (String) getDisplayNameM.invoke(key);
            }
        } catch (ClassNotFoundException e) {
            Mods.CONTROLLING_INSTALLED = false;
        } catch (Exception e) {
            YesSteveModel.LOGGER.error("Error when getting KeyBinding name through Controlling.", e);
        }
        return GameSettings.getKeyDisplayString(key.getKeyCode());
    }

    public static Modifier getModifier(KeyBinding key) {
        if (Mods.MKB_INSTALLED) return getMKBModifier(key);
        else if (Mods.CONTROLLING_INSTALLED) return getControllingModifier(key);
        else return Modifier.NONE;
    }

    @Optional.Method(modid = Mods.MKB)
    private static Modifier getMKBModifier(KeyBinding key) {
        return Modifier.valueOf(((IKeyBinding) key).getKeyModifier().name());
    }

    @Optional.Method(modid = Mods.CONTROLLING)
    private static Modifier getControllingModifier(KeyBinding key) {
        try {
            Class<?> apiC = Class.forName("com.blamejared.controlling.api.ControllingApi");
            Method getComboModifierM = apiC.getMethod("getComboModifier", KeyBinding.class);
            Enum<?> comboModifier = (Enum<?>) getComboModifierM.invoke(null, key);
            return Modifier.valueOf(comboModifier.name());
        } catch (ClassNotFoundException e) {
            Mods.CONTROLLING_INSTALLED = false;
        } catch (Exception e) {
            YesSteveModel.LOGGER.error("Error when getting KeyModifier through Controlling.", e);
        }
        return Modifier.NONE;
    }

    public static Modifier getActiveModifier() {
        if (Mods.MKB_INSTALLED) return getMKBActiveModifier();
        else if (Mods.CONTROLLING_INSTALLED) return getControllingActiveModifier();
        return Modifier.NONE;
    }

    @Optional.Method(modid = Mods.MKB)
    private static Modifier getMKBActiveModifier() {
        return Modifier.valueOf(KeyModifier.getActiveModifier().name());
    }

    @Optional.Method(modid = Mods.CONTROLLING)
    private static Modifier getControllingActiveModifier() {
        try {
            Class<?> keyModifierC = Class.forName("com.blamejared.controlling.keybinding.KeyModifier");
            Method getActiveModifierM = keyModifierC.getMethod("getActiveModifier");
            Enum<?> activeModifier = (Enum<?>) getActiveModifierM.invoke(null);
            return Modifier.valueOf(activeModifier.name());
        } catch (ClassNotFoundException e) {
            Mods.CONTROLLING_INSTALLED = false;
        } catch (Exception e) {
            YesSteveModel.LOGGER.error("Error when getting active KeyModifier through Controlling.", e);
        }
        return Modifier.NONE;
    }

    public enum ConflictContext {
        UNIVERSAL, GUI, IN_GAME
    }

    public enum Modifier {
        NONE, CONTROL, SHIFT, ALT
    }
}
