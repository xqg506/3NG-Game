package com.eve.eng1.screen;

import java.util.function.Consumer;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.eve.eng1.Main;
import com.eve.eng1.asset.AssetService;
import com.eve.eng1.asset.MapAsset;
import com.eve.eng1.input.GameControllerState;
import com.eve.eng1.input.KeyboardController;
import com.eve.eng1.system.*;
import com.eve.eng1.tiled.TiledAshleyConfigurator;
import com.eve.eng1.tiled.TiledService;


public class GameScreen extends ScreenAdapter {
    private final Main game;

    private final Engine engine;

    private final TiledService tiledService;
    private final TiledAshleyConfigurator tiledAshleyConfigurator;
    private final KeyboardController keyboardController;

    private final World physicWorld;


    public GameScreen(Main game) {
        this.game = game;
        this.tiledService = new TiledService(game.getAssetService());
        this.engine = new Engine();
        this.physicWorld = new World(Vector2.Zero, true);
        this.physicWorld.setAutoClearForces(false);
        this.tiledAshleyConfigurator = new TiledAshleyConfigurator(this.engine, game.getAssetService(), this.physicWorld);
        this.keyboardController = new KeyboardController(GameControllerState.class, engine);


        this.engine.addSystem(new ControllerSystem());
        this.engine.addSystem(new PhysicMoveSystem());
        //Fsm system goes here
        //Facing system goes here
        this.engine.addSystem(new PhysicSystem(physicWorld, 1 / 60f));
        //Animation system goes here
        this.engine.addSystem(new RenderSystem(game.getBatch(), game.getViewport(), game.getCamera()));
        this.engine.addSystem(new PhysicDebugRenderSystem(physicWorld, game.getCamera()));
    }

    @Override
    public void show() {

        game.setInputProcessors(keyboardController);
        keyboardController.setActiveState(GameControllerState.class);

        Consumer<TiledMap> renderConsumer = this.engine.getSystem(RenderSystem.class)::setMap;
        this.tiledService.setMapChangeConsumer(renderConsumer);
        this.tiledService.setLoadObjectConsumer(this.tiledAshleyConfigurator::onLoadObject);
        this.tiledService.setLoadTileConsumer(tiledAshleyConfigurator::onLoadTile);
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
        this.physicWorld.dispose();
    }
}
