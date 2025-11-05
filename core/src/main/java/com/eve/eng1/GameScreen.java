package com.eve.eng1;

import java.util.Map;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.eve.eng1.asset.AssetService;
import com.eve.eng1.asset.MapAsset;
import com.eve.eng1.system.RenderSystem;

public class GameScreen extends ScreenAdapter {
    private final Main game;
    private final Batch batch;
    private final AssetService assetService;
    private final Viewport viewport;
    private final OrthographicCamera camera;




    private final Engine engine;


    public GameScreen(Main game) {
        this.game = game;
        this.assetService = game.getAssetService();
        this.viewport = game.getViewport();
        this.camera = game.getCamera();
        this.batch = game.getBatch();



        this.engine = new Engine();
        this.engine.addSystem(new RenderSystem(this.batch, this.viewport));
    }

    @Override
    public void show() {
        this.assetService.load(MapAsset.BEDROOM);
        this.engine.getSystem(RenderSystem.class).setMap(this.assetService.get(MapAsset.BEDROOM));
    }

    @Override
    public void hide() {
        // Removes all entities in order to clean up
        this.engine.removeAllEntities();
    }

    @Override
    public void render(float delta) {
        delta = Math.min(delta, 1 / 30f);
        this.engine.update(delta);
        /*
        Good practice to apply the viewport before any rendering calls are done, so that the
        rendering process in the background knows the dimensions and where to put everything.
        */

    }

    @Override
    public void dispose() {
        for (EntitySystem system : this.engine.getSystems()) {
            if (system instanceof Disposable disposableSystem) {
                disposableSystem.dispose();
            }
        }

    }
}
