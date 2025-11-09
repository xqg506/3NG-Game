package com.eve.eng1.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.eve.eng1.asset.AtlasAsset;
import com.eve.eng1.component.Facing.FacingDirection;

public class Animation2D implements Component {
    public static final ComponentMapper<Animation2D> MAPPER = ComponentMapper.getFor(Animation2D.class);

    private final AtlasAsset atlasAsset;
    private final String atlasKey;
    private AnimationType type;
    private FacingDirection direction;
    private Animation.PlayMode playMode;
    private float speed;
    private float stateTime;
    private Animation<TextureRegion> animation;
    private boolean dirty;

    public Animation2D(AtlasAsset atlasAsset, String atlasKey, AnimationType type, Animation.PlayMode playMode, float speed) {
        this.atlasAsset = atlasAsset;
        this.atlasKey = atlasKey;
        this.type = type;
        this.direction = null;
        this.playMode = playMode;
        this.speed = speed;
        this.stateTime = 0f;
        this.animation = null;
        
        this.speed = 4f;
    }

    public void setAnimation(Animation<TextureRegion> animation, FacingDirection direction) {
        this.animation = animation;
        this.direction = direction;
        this.stateTime = 0f;
        this.dirty = false;
    }

    public FacingDirection getDirection() {
        return direction;
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public AtlasAsset getAtlasAsset() {
        return atlasAsset;
    }

    public String getAtlasKey() {
        return atlasKey;
    }

    public void setType(AnimationType type) {
        this.type = type;
        this.dirty = true;
    }

    public AnimationType getType() {
        return type;
    }

    public Animation.PlayMode getPlayMode() {
        return playMode;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setPlayMode(Animation.PlayMode playMode) {
        this.playMode = playMode;
    }

    public boolean isDirty() {
        return dirty;
    }

    public boolean isFinished() {
        return animation.isAnimationFinished(stateTime);
    }

    public float incAndGetStateTime(float deltaTime) {
        this.stateTime += deltaTime * speed;
        return this.stateTime;
    }

    public enum AnimationType {
        IDLE, WALK;

        private final String atlasKey;

        AnimationType() {
            this.atlasKey = name().toLowerCase();
        }

        public String getAtlasKey() {
            return atlasKey;
        }
    }
}
