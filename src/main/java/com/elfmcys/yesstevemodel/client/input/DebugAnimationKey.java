package com.elfmcys.yesstevemodel.client.input;

import com.elfmcys.yesstevemodel.client.compat.*;
import com.elfmcys.yesstevemodel.client.event.ReloadResourceEvent;
import com.elfmcys.yesstevemodel.geckolib3.util.MolangUtils;
import com.elfmcys.yesstevemodel.mclib.utils.Interpolations;
import com.elfmcys.yesstevemodel.util.EntityUtil;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.Locale;
import java.util.function.DoubleSupplier;

public class DebugAnimationKey {
    public static boolean DEBUG = false;

    public static final KeyBinding DEBUG_ANIMATION_KEY = KeyCompat.createKeyBinding(
            "key.yes_steve_model.debug_animation.desc",
            KeyCompat.ConflictContext.IN_GAME,
            KeyCompat.Modifier.ALT,
            Keyboard.KEY_B,
            "key.category.yes_steve_model"
    );

    @SubscribeEvent
    public void onKeyboardInput(InputEvent.KeyInputEvent event) {
        if (DEBUG_ANIMATION_KEY.isPressed()) {
            DEBUG = !DEBUG;
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            if (player == null) {
                return;
            }
            if (DEBUG) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("message.yes_steve_model.model.debug_animation.true"));
            } else {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("message.yes_steve_model.model.debug_animation.false"));
            }
        }
    }

    @SubscribeEvent
    public void render(RenderGameOverlayEvent.Text event) {
        if (!DEBUG) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.gameSettings.showDebugInfo) {
            return;
        }
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        if (mc.ingameGUI == null) {
            return;
        }
        final GuiIngame gui = mc.ingameGUI;
        GL11.glPushMatrix();
        float partialTick = event.partialTicks;

        EntityPlayerSP player = mc.thePlayer;
        if (mc.theWorld == null || player == null) {
            return;
        }

        /*
        这一段计算模拟的是 GeoReplacedEntityRenderer，需保持对等，以便于调试
         */
        float lerpBodyRot = Interpolations.lerpYaw(player.prevRenderYawOffset, player.renderYawOffset, partialTick);
        float lerpHeadRot = Interpolations.lerpYaw(player.prevRotationYawHead, player.rotationYawHead, partialTick);
        float netHeadYaw = lerpHeadRot - lerpBodyRot;
        boolean shouldSit = player.isRiding() && (player.ridingEntity != null && player.ridingEntity.shouldRiderSit());

        if (shouldSit && player.ridingEntity instanceof EntityLivingBase vehicle) {
            lerpBodyRot = Interpolations.lerpYaw(vehicle.prevRenderYawOffset, vehicle.renderYawOffset, partialTick);
            netHeadYaw = lerpHeadRot - lerpBodyRot;
            float clampedHeadYaw = MathHelper.clamp_float(MathHelper.wrapAngleTo180_float(netHeadYaw), -85, 85);
            lerpBodyRot = lerpHeadRot - clampedHeadYaw;
            if (clampedHeadYaw * clampedHeadYaw > 2500f) {
                lerpBodyRot += clampedHeadYaw * 0.2f;
            }
            netHeadYaw = lerpHeadRot - lerpBodyRot;
        }
        float headPitch = Interpolations.lerp(player.prevRotationPitch, player.rotationPitch, partialTick);
        final float outputHeadPitch = -headPitch;
        final float outputNetHeadYaw = -MathHelper.clamp_float(MathHelper.wrapAngleTo180_float(netHeadYaw), -85, 85);

        int[] y = {5};

        renderText(gui, y, "PI", String.format("§7%.4f", Math.PI));
        renderText(gui, y, "E", String.format("§7%.4f", Math.E));

        renderText(gui, y, "query.actor_count", mc.theWorld.getLoadedEntityList().size());
        renderText(gui, y, "query.anim_time", () -> 0.0);

        renderText(gui, y, "query.body_x_rotation", () -> player.rotationPitch);
        renderText(gui, y, "query.body_y_rotation", () -> MathHelper.wrapAngleTo180_float(player.rotationYaw));
        renderText(gui, y, "query.cardinal_facing_2d", EntityUtil.getHorizontalIndex(player));
        renderText(gui, y, "query.distance_from_camera", () -> EntityUtil.getCameraPosition(mc, partialTick).distanceTo(EntityUtil.getPositionVector(player)));
        renderText(gui, y, "query.equipment_count", getEquipmentCount(player));
        renderText(gui, y, "query.eye_target_x_rotation", () -> EntityUtil.getViewXRot(player, partialTick));
        renderText(gui, y, "query.eye_target_y_rotation", () -> EntityUtil.getViewYRot(player, partialTick));
        renderText(gui, y, "query.ground_speed", () -> EntityUtil.getGroundSpeed(player));

        renderText(gui, y, "query.has_cape", hasCape(player));
        renderText(gui, y, "query.has_rider", player.riddenByEntity != null);
        renderText(gui, y, "query.head_x_rotation", () -> outputNetHeadYaw);
        renderText(gui, y, "query.head_y_rotation", () -> outputHeadPitch);
        renderText(gui, y, "query.health", player::getHealth);
        renderText(gui, y, "query.hurt_time", player.hurtTime);

        renderText(gui, y, "query.is_eating", player.getItemInUse() != null && player.getItemInUse().getItemUseAction() == EnumAction.eat);
        renderText(gui, y, "query.is_first_person", mc.gameSettings.thirdPersonView == 0);
        renderText(gui, y, "query.is_in_water", player.isInWater());
        renderText(gui, y, "query.is_in_water_or_rain", player.isWet());
        renderText(gui, y, "query.is_jumping", !player.capabilities.isFlying && !player.isRiding() && !player.onGround && !player.isInWater());
        renderText(gui, y, "query.is_on_fire", player.isBurning());
        renderText(gui, y, "query.is_on_ground", player.onGround);
        renderText(gui, y, "query.is_playing_dead", !player.isEntityAlive());
        renderText(gui, y, "query.is_riding", player.isRiding());
        renderText(gui, y, "query.is_sleeping", player.isPlayerSleeping());
        renderText(gui, y, "query.is_sneaking", player.onGround && player.isSneaking());
        renderText(gui, y, "query.is_spectator", SpectatorCompat.isSpectator(player));
        renderText(gui, y, "query.is_sprinting", player.isSprinting());
        if (SwimmingCompat.hasSwimming()) {
            renderText(gui, y, "query.is_swimming", SwimmingCompat.isSwimming(player));
        }
        renderText(gui, y, "query.is_using_item", Hand.getUsingHand(player) != null);
        renderText(gui, y, "query.item_in_use_duration", () -> EntityUtil.getItemInUseMaxCount(player) / 20.0);
        renderText(gui, y, "query.item_max_use_duration", () -> getMaxUseDuration(player) / 20.0);
        renderText(gui, y, "query.item_remaining_use_duration", () -> player.getItemInUseCount() / 20.0);

        renderText(gui, y, "query.life_time", () -> (player.ticksExisted + partialTick) / 20.0);
        renderText(gui, y, "query.max_health", player::getMaxHealth);
        renderText(gui, y, "query.modified_distance_moved", () -> player.distanceWalkedModified);
        renderText(gui, y, "query.moon_phase", mc.theWorld.getMoonPhase());

        renderText(gui, y, "query.player_level", player.experienceLevel);
        renderText(gui, y, "query.time_of_day", () -> MolangUtils.normalizeTime(mc.theWorld.getWorldTime()));
        renderText(gui, y, "query.time_stamp", mc.theWorld.getWorldTime());
        renderText(gui, y, "query.vertical_speed", () -> EntityUtil.getVerticalSpeed(player));
        renderText(gui, y, "query.walk_distance", () -> player.distanceWalkedOnStepModified);
        renderText(gui, y, "query.yaw_speed", () -> getYawSpeed(partialTick, player));

        renderText(gui, y, "ysm.armor_value", player.getTotalArmorValue());

        renderText(gui, y, "ysm.has_helmet", getSlotValue(player, EntityEquipmentSlot.HEAD));
        renderText(gui, y, "ysm.has_chest_plate", getSlotValue(player, EntityEquipmentSlot.CHEST));
        renderText(gui, y, "ysm.has_leggings", getSlotValue(player, EntityEquipmentSlot.LEGS));
        renderText(gui, y, "ysm.has_boots", getSlotValue(player, EntityEquipmentSlot.FEET));
        renderText(gui, y, "ysm.has_mainhand", getSlotValue(player, EntityEquipmentSlot.MAINHAND));
        renderText(gui, y, "ysm.has_offhand", getSlotValue(player, EntityEquipmentSlot.OFFHAND));

        renderText(gui, y, "ysm.has_elytra", ElytraCompat.isWearingElytra(player));
        renderText(gui, y, "ysm.elytra_rot_x", () -> Math.toDegrees(ElytraCompat.getRotateElytraX(player)));
        renderText(gui, y, "ysm.elytra_rot_y", () -> Math.toDegrees(ElytraCompat.getRotateElytraY(player)));
        renderText(gui, y, "ysm.elytra_rot_z", () -> Math.toDegrees(ElytraCompat.getRotateElytraZ(player)));

        renderText(gui, y, "ysm.is_close_eyes", getEyeCloseState(partialTick, player));
//        if (TridentCompat.hasRiptide()) {
//            renderText(gui, y, "ysm.is_riptide", TridentCompat.isAutoSpinAttack(player));
//        }
        renderText(gui, y, "ysm.food_level", player.getFoodStats().getFoodLevel());

//        if (FirstPersonCompat.isInstalled()) {
//            renderText(gui, y, "ysm.first_person_mod_hide", FirstPersonCompat.isHeadHide());
//        }
        GL11.glPopMatrix();
    }

    private static boolean hasCape(EntityPlayerSP player) {
        return player.func_152122_n() && !player.isInvisible() && !player.getHideCape();
    }

    private static int getEquipmentCount(EntityPlayerSP player) {
        int count = 0;
        for (int i = 1; i <= 4; i++) {
            if (player.getEquipmentInSlot(i) != null) {
                count += 1;
            }
        }
        return count;
    }

    private static double getMaxUseDuration(EntityPlayerSP player) {
        ItemStack useItem = player.getItemInUse();
        if (useItem == null) {
            return 0.0;
        } else {
            return useItem.getMaxItemUseDuration();
        }
    }

    private static void renderText(GuiIngame gui, int[] y, String name, String data) {
        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        if ((y[0] - 5) % 20 == 0) {
            Gui.drawRect(2, y[0] - 1, ReloadResourceEvent.DEBUG_BG_WIDTH, y[0] + 9, 0xc0505050);
        } else {
            Gui.drawRect(2, y[0] - 1, ReloadResourceEvent.DEBUG_BG_WIDTH, y[0] + 9, 0xc0506050);
        }
        gui.drawString(font, name, 5, y[0], 0xffffff);
        gui.drawString(font, data, 200, y[0], 0xffffff);
        gui.drawString(font, EnumChatFormatting.GRAY + I18n.format(String.format("molang.yes_steve_model.%s.desc", name.toLowerCase(Locale.US))), 260, y[0], 0xFFFFFFFF);
        y[0] = y[0] + 10;
    }

    private static void renderText(GuiIngame gui, int[] y, String name, DoubleSupplier supplier) {
        renderText(gui, y, name, String.format("§b%.4f", supplier.getAsDouble()));
    }

    private static void renderText(GuiIngame gui, int[] y, String name, int number) {
        renderText(gui, y, name, String.format("§6%d", number));
    }

    private static void renderText(GuiIngame gui, int[] y, String name, long number) {
        renderText(gui, y, name, String.format("§1%d", number));
    }

    private static void renderText(GuiIngame gui, int[] y, String name, boolean data) {
        String str = data ? String.format("§c%b", data) : String.format("§a%b", data);
        renderText(gui, y, name, str);
    }

    private static float getYawSpeed(float partialTick, EntityPlayerSP player) {
        double seekTime = player.ticksExisted + partialTick;
        return EntityUtil.getViewYRot(player, (float) seekTime) - EntityUtil.getViewYRot(player, (float) seekTime - 0.1f);
    }

    private static boolean getEyeCloseState(float partialTick, EntityPlayerSP player) {
        double remainder = (player.ticksExisted + partialTick + Math.abs(player.getUniqueID().getLeastSignificantBits()) % 10) % 90;
        boolean isBlinkTime = 85 < remainder && remainder < 90;
        return player.isPlayerSleeping() || isBlinkTime;
    }

    private static boolean getSlotValue(EntityPlayerSP player, EntityEquipmentSlot slot) {
        return slot.getItemStackFromSlot(player) != null;
    }
}
