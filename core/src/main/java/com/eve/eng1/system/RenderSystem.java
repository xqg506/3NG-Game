package com.eve.eng1.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.eve.eng1.Main;
import com.eve.eng1.asset.AssetService;
import com.eve.eng1.asset.MapAsset;
import com.eve.eng1.component.Graphic;
import com.eve.eng1.component.Transform;

import java.util.Comparator;

public class RenderSystem extends SortedIteratingSystem implements Disposable {
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Batch batch;
    private final Viewport viewport;
    private final OrthographicCamera camera;

    /*
    We are using a sorted iterating system because we need to render some objects in front of other based on their positon
    e.g. a player would stand in front of a tree.
     */

    public RenderSystem(Batch batch, Viewport viewport) {
        super(
            // A family is a combination of components.
            Family.all(Transform.class, Graphic.class).get(), Comparator.comparing(Transform.MAPPER::get));
        this.batch = batch;
        this.viewport = viewport;
        this.camera = (OrthographicCamera) viewport.getCamera();
        this.mapRenderer = new OrthogonalTiledMapRenderer(null, Main.UNIT_SCALE, this.batch);
    }

    @Override
    public void update(float deltaTime) {
        this.viewport.apply();
        this.batch.setColor(Color.WHITE);
        this.mapRenderer.setView(this.camera);
        this.mapRenderer.render();

        // We do force sort because it needs to update dynamically and calculate the entities position
        forceSort();
        super.update(deltaTime);
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Transform transform = Transform.MAPPER.get(entity);
        Graphic graphic = Graphic.MAPPER.get(entity);

        // If there is no region specified to render for the entity, do nothing.
        if (graphic.getRegion() == null) {
            return;
        }

        // IF there is a region, we render the entity.
        Vector2 position = transform.getPosition();
        Vector2 scaling = transform.getScaling();
        Vector2 size = transform.getSize();
        this.batch.setColor(graphic.getColor());
        // When we draw something in openGL with the spritebatch, imagine it as an image.
        this.batch.draw(
            graphic.getRegion(),
            position.x - (1f - scaling.x) * size.x * 0.5f,
            position.y - (1f - scaling.y) * size.y * 0.5f,
            size.x * 0.5f, size.y * 0.5f,
            size.x, size.y,
            scaling.x, scaling.y,
            transform.getRotationDeg()
        );
    }

    public void setMap(TiledMap tiledMap) {
        this.mapRenderer.setMap(tiledMap);
    }


    @Override
    public void dispose() {
        this.mapRenderer.dispose();
    }

}
