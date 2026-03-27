package com.elfmcys.yesstevemodel.client.animation.condition;

import com.elfmcys.yesstevemodel.client.compat.Hand;

public class ConditionalSwing extends AbstractConditionItem {
    public ConditionalSwing(Hand hand) {
        super(hand == Hand.MAIN_HAND ? "swing" : "swing_offhand");
    }
}
