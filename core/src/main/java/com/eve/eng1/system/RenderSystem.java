package com.eve.eng1.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.eve.eng1.Main;
import com.eve.eng1.asset.AssetService;
import com.eve.eng1.asset.MapAsset;
import com.eve.eng1.component.Graphic;
import com.eve.eng1.component.Transform;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RenderSystem extends SortedIteratingSystem implements Disposable {
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Batch batch;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final List<MapLayer> fgdLayers;
    private final List<MapLayer> bgdLayers;

    /*
    We are using a sorted iterating system because we need to render some objects in front of other based on their positon
    e.g. a player would stand in front of a tree.
     */

    public RenderSystem(Batch batch, Viewport viewport, OrthographicCamera camera) {
        super(
            // A family is a combination of components.
            Family.all(Transform.class, Graphic.class).get(), Comparator.comparing(Transform.MAPPER::get));
        this.batch = batch;
        this.viewport = viewport;
        this.camera = camera;
        this.mapRenderer = new OrthogonalTiledMapRenderer(null, Main.UNIT_SCALE, this.batch);
        this.fgdLayers = new ArrayList<>();
        this.bgdLayers = new ArrayList<>();
    }

    @Override
    public void update(float deltaTime) {
        /*
        Good practice to apply the viewport before any rendering calls are done, so that the
        rendering process in the background knows the dimensions and where to put everything.
        */
        AnimatedTiledMapTile.updateAnimationBaseTime();
        this.viewport.apply();

        batch.begin();
        this.batch.setColor(Color.WHITE);
        this.mapRenderer.setView(this.camera);
        bgdLayers.forEach(mapRenderer::renderMapLayer);

        // We do force sort because it needs to update dynamically and calculate the entities position
        forceSort();
        super.update(deltaTime);

        this.batch.setColor(Color.WHITE);
        fgdLayers.forEach(mapRenderer::renderMapLayer);
        batch.end();
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
        /*
        Start with the background layer as the current layer. Then we iterate through the layers. When we get to the
        objects layer, we switch the current layer to the foreground layers. This is because every layer on top of the
        objects will be in the foreground.
         */
        this.mapRenderer.setMap(tiledMap);
        this.fgdLayers.clear();
        this.bgdLayers.clear();
        List<MapLayer> currentLayers = bgdLayers;

        for (MapLayer layer : tiledMap.getLayers()) {
            if("objects".equals(layer.getName())) {
                currentLayers = fgdLayers;
                continue;
            }
            if (layer.getClass().equals(MapLayer.class)) {
                continue;
            }

            currentLayers.add(layer);
        }
    }


    @Override
    public void dispose() {
        this.mapRenderer.dispose();
    }

}
