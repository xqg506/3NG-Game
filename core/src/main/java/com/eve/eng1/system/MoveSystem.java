package com.eve.eng1.system;

//import javax.xml.crypto.dsig.Transform;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.eve.eng1.component.Move;
import com.eve.eng1.component.Transform;


public class MoveSystem extends IteratingSystem {
    
    public MoveSystem() {
        super(Family.all(Move.class, Transform.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Move move = Move.MAPPER.get(entity);
        if (move.isRooted() || move.getDirection().isZero()) {
            return;
        }

        Transform transform = Transform.MAPPER.get(entity);
        Vector2 position = transform.getPosition();
        position.set(
            position.x + move.getMaxSpeed() * move.getDirection().x * deltaTime, //multiplied by deltatime ensures higherfps != faster movement 
            position.x + move.getMaxSpeed() * move.getDirection().y *deltaTime
        );
    }
}
