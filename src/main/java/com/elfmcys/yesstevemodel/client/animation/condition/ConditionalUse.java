package com.elfmcys.yesstevemodel.client.animation.condition;

import com.elfmcys.yesstevemodel.client.compat.Hand;

public class ConditionalUse extends AbstractConditionItem {
    public ConditionalUse(Hand hand) {
        super("use_" + (hand == Hand.MAIN_HAND ? "mainhand" : "offhand"));
    }
}
