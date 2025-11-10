package com.eve.eng1.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

//For determining which direction the character should be facing at a given time

public class Facing implements Component {
    public static final ComponentMapper<Facing> MAPPER = ComponentMapper.getFor(Facing.class);

    private FacingDirection direction;

    public Facing(FacingDirection direction) {
        this.direction = direction;
    }

    public FacingDirection getDirection() {
        return direction;
    }

    public void setDirection(FacingDirection direction) {
        this.direction = direction;
    }

    public enum FacingDirection {
        U, D, L, R;

        private final String AtlasKey;

        FacingDirection() {
            this.AtlasKey = name().toLowerCase(); //formatting to look the same as the files
        }

        public String getAtlasKey() {
            return AtlasKey;
        }

        public String AtlasKey() {
            return AtlasKey;
        }
    }
}



