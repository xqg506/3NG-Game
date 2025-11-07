package com.eve.eng1.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

public class Move implements Component {
    public static final ComponentMapper<Move> MAPPER = ComponentMapper.getFor(Move.class);

    private final float maxSpeed = 6;
    private final Vector2 direction;
    private boolean isRooted;

    public Move(float maxSpeed) {
        this.direction = new Vector2();
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void setRooted(boolean rooted) {
        this.isRooted = rooted;
    }

    public boolean isRooted() {
        return isRooted;
    }

}
