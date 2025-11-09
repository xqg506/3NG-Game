package com.eve.eng1.system;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.eve.eng1.asset.AssetService;
import com.eve.eng1.asset.AtlasAsset;
import com.eve.eng1.component.Animation2D;
import com.eve.eng1.component.Facing;
import com.eve.eng1.component.Facing.FacingDirection;
import com.eve.eng1.component.Graphic;

public class AnimationSystem extends IteratingSystem {
    private static final float FRAME_DURATION = 1/4f;

    private final AssetService assetService;
    private final Map<CacheKey, Animation<TextureRegion>> animationCache;

    public AnimationSystem(AssetService assetService) {
        super(Family.all(Animation2D.class, Graphic.class, Facing.class).get());
        this.assetService = assetService;
        this.animationCache = new HashMap<>();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Animation2D animation2D = Animation2D.MAPPER.get(entity);
        FacingDirection facingDirection = Facing.MAPPER.get(entity).getDirection();
        final float stateTime;
        if(animation2D.isDirty() || facingDirection != animation2D.getDirection()) {
            updateAnimation(animation2D, facingDirection);
            stateTime = 0f;
        } else {
            stateTime = animation2D.incAndGetStateTime(deltaTime);
        }

        Animation<TextureRegion> animation = animation2D.getAnimation();
        animation.setPlayMode(animation2D.getPlayMode());
        TextureRegion keyFrame = animation.getKeyFrame(stateTime);
        Graphic.MAPPER.get(entity).setRegion(keyFrame);
    }

    private void updateAnimation(Animation2D animation2D, FacingDirection facingDirection) {
        AtlasAsset atlasAsset = animation2D.getAtlasAsset();
        String atlasKey = animation2D.getAtlasKey();
        Animation2D.AnimationType type = animation2D.getType();
        CacheKey cacheKey = new CacheKey(atlasAsset, atlasKey, type, facingDirection);
        Animation<TextureRegion> animation = animationCache.computeIfAbsent(cacheKey, key -> {
            TextureAtlas textureAtlas = this.assetService.get(atlasAsset);
            String combinedKey = "player/animframes/" + type.getAtlasKey() +  facingDirection.getAtlasKey();
            Array<TextureAtlas.AtlasRegion> regions = textureAtlas.findRegions(combinedKey);
            if(regions.isEmpty()){
                throw new GdxRuntimeException("No regions found for key: " + combinedKey);
            }
            return new Animation<>(FRAME_DURATION, regions);
        });
        animation2D.setAnimation(animation, facingDirection);
    }

    public record CacheKey(
        AtlasAsset atlasAsset, 
        String atlasKey,
        Animation2D.AnimationType type,
        Facing.FacingDirection direction
    ) {

    }
}
