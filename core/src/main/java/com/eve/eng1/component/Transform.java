package com.eve.eng1.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

public class Transform implements Component, Comparable<Transform> {
    public static final ComponentMapper<Transform> MAPPER = ComponentMapper.getFor(Transform.class);

    // Position of entity
    private final Vector2 position;
    // Additional index for sorting z-index
    private final int z;
    // Entity Size
    private final Vector2 size;
    // Entity Scale
    private final Vector2 scaling;
    // Entity rotation
    private float rotationDeg;

    public Transform(Vector2 position, int z, Vector2 size, Vector2 scaling, float rotationDeg) {
        this.position = position;
        this.z = z;
        this.size = size;
        this.scaling = scaling;
        this.rotationDeg = rotationDeg;
    }

    @Override
    public int compareTo(Transform other) {
        if (this.z != other.z) {
            return Float.compare(this.z, other.z);
        }
        if (this.position.y != other.position.y) {
            return Float.compare(other.position.y, this.position.y);
        }
        return Float.compare(this.position.x, other.position.x);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSize() {
        return size;
    }

    public Vector2 getScaling() {
        return scaling;
    }

    public float getRotationDeg() {
        return rotationDeg;
    }
}
