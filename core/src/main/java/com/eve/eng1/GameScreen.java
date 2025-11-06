package com.eve.eng1;

import java.util.function.Consumer;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.eve.eng1.asset.AssetService;
import com.eve.eng1.asset.MapAsset;
import com.eve.eng1.input.GameControllerState;
import com.eve.eng1.input.KeyboardController;
import com.eve.eng1.system.ControllerSystem;
import com.eve.eng1.system.MoveSystem;
import com.eve.eng1.system.RenderSystem;
import com.eve.eng1.tiled.TiledAshleyConfigurator;
import com.eve.eng1.tiled.TiledService;


public class GameScreen extends ScreenAdapter {
    private final Main game;
    private final Batch batch;
    private final AssetService assetService;
    private final Viewport viewport;
    private final OrthographicCamera camera;

    private final Engine engine;

    private final TiledService tiledService;
    private final TiledAshleyConfigurator tiledAshleyConfigurator;
    private final KeyboardController keyboardController;

    public GameScreen(Main game) {
        this.game = game;
        this.assetService = game.getAssetService();
        this.viewport = game.getViewport();
        this.camera = game.getCamera();
        this.batch = game.getBatch();
        this.tiledService = new TiledService(this.assetService);


        this.engine = new Engine();
        this.engine.addSystem(new ControllerSystem());
        this.engine.addSystem(new MoveSystem());
        this.engine.addSystem(new RenderSystem(this.batch, this.viewport, this.camera));

        this.tiledAshleyConfigurator = new TiledAshleyConfigurator(this.engine, this.assetService);

        this.keyboardController = new KeyboardController(GameControllerState.class, engine);
    }

    @Override
    public void show() {

        game.setInputProcessors(keyboardController);
        keyboardController.setActiveState(GameControllerState.class);

        Consumer<TiledMap> renderConsumer = this.engine.getSystem(RenderSystem.class)::setMap;
        this.tiledService.setMapChangeConsumer(renderConsumer);
        this.tiledService.setLoadObjectConsumer(this.tiledAshleyConfigurator::onLoadObject);

        TiledMap tiledMap = this.tiledService.loadMap(MapAsset.BEDROOM);
        this.tiledService.setMap(tiledMap);
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
