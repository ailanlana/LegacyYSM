package com.elfmcys.yesstevemodel.client.animation.condition;

import com.elfmcys.yesstevemodel.client.compat.Hand;
import net.minecraft.entity.player.EntityPlayer;

public class ConditionalHold extends AbstractConditionItem {
    private static final String EMPTY_MAINHAND = "hold_mainhand:empty";
    private static final String EMPTY_OFFHAND = "hold_offhand:empty";

    public ConditionalHold(Hand hand) {
        super("hold_" + (hand == Hand.MAIN_HAND ? "mainhand" : "offhand"));
    }

    @Override
    public String doTest(EntityPlayer player, Hand hand) {
        if (hand.getHeldItem(player) == null) {
            return hand == Hand.MAIN_HAND ? EMPTY_MAINHAND : EMPTY_OFFHAND;
        }
        return super.doTest(player, hand);
    }
}
