package com.eve.eng1;

import java.util.Map;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.eve.eng1.asset.AssetService;
import com.eve.eng1.asset.MapAsset;

public class GameScreen extends ScreenAdapter {
    private final Main game;
    private final Batch batch;
    private final AssetService assetService;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    

    private final OrthogonalTiledMapRenderer mapRenderer;

    public GameScreen(Main game) {
        this.game = game;
        this.assetService = game.getAssetService();
        this.viewport = game.getViewport();
        this.camera = game.getCamera();
        this.batch = game.getBatch();
        this.mapRenderer = new OrthogonalTiledMapRenderer(null, Main.UNIT_SCALE, this.batch);
    }

    @Override
    public void show() {
        this.assetService.load(MapAsset.BEDROOM);
        this.mapRenderer.setMap(this.assetService.get(MapAsset.BEDROOM));
    }

    @Override
    public void render(float delta) {
        /*
        Good practice to apply the viewport before any rendering calls are done, so that the 
        rendering process in the background knows the dimensions and where to put everything.
        */ 
        this.viewport.apply();
        this.batch.setColor(Color.WHITE);
        this.mapRenderer.setView(this.camera);
        this.mapRenderer.render();
    }

    @Override
    public void dispose() {
        this.mapRenderer.dispose();
        super.dispose();
    }
}
