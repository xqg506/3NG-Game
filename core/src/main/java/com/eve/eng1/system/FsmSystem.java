package com.eve.eng1.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.eve.eng1.component.Fsm;

public class FsmSystem extends IteratingSystem {
    public FsmSystem() {
        super(Family.all(Fsm.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Fsm.MAPPER.get(entity).getAnimationFsm().update();
    }
}
