package com.eve.eng1.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Graphic implements Component {
    /*
    A component mapper is the fast way to access the component of an entity.
    Imagine the ECS is just an array of components (e.g. array of graphic components, array of transformation components).
    The entity is simply an ID in the array, so we can just pass this in, and we find which component it has.
     */
    public static final ComponentMapper<Graphic> MAPPER = ComponentMapper.getFor(Graphic.class);


    private TextureRegion region;
    private final Color color;

    public Graphic (TextureRegion region, Color color) {
        this.color = color;
        this.region = region;
    }

    public void setRegion(TextureRegion region) {
        this.region = region;
    }

    public TextureRegion getRegion() {
        return region;
    }

    public Color getColor() {
        return color;
    }
}
