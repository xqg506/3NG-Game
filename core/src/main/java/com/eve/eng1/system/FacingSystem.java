package com.eve.eng1.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.eve.eng1.component.Facing;
import com.eve.eng1.component.Move;

public class FacingSystem extends IteratingSystem {
    public FacingSystem() {
        super(Family.all(Facing.class, Move.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Move move = Move.MAPPER.get(entity);
        Vector2 moveDirection = move.getDirection();
        if (move.getDirection().isZero()) {
            return;
        }

        Facing facing = Facing.MAPPER.get(entity);
        if (moveDirection.y > 0f ) {
            facing.setDirection(Facing.FacingDirection.U);
        } else if (moveDirection.y < 0f) {
            facing.setDirection(Facing.FacingDirection.D);
        } else if (moveDirection.x < 0f) {
            facing.setDirection(Facing.FacingDirection.L);
        } else {
            facing.setDirection(Facing.FacingDirection.R);
        }
    }

}
