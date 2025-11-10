package com.eve.eng1.screen;

import java.util.function.Consumer;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.eve.eng1.Main;
import com.eve.eng1.asset.AssetService;
import com.eve.eng1.asset.MapAsset;
import com.eve.eng1.audio.AudioService;
import com.eve.eng1.input.GameControllerState;
import com.eve.eng1.input.KeyboardController;
import com.eve.eng1.system.AnimationSystem;
import com.eve.eng1.system.ControllerSystem;
import com.eve.eng1.system.FacingSystem;
import com.eve.eng1.system.FsmSystem;
import com.eve.eng1.system.PhysicDebugRenderSystem;
import com.eve.eng1.system.PhysicMoveSystem;
import com.eve.eng1.system.PhysicSystem;
import com.eve.eng1.system.RenderSystem;
import com.eve.eng1.input.GameControllerState;
import com.eve.eng1.input.KeyboardController;
import com.eve.eng1.system.*;
import com.eve.eng1.tiled.TiledAshleyConfigurator;
import com.eve.eng1.tiled.TiledService;
import com.eve.eng1.ui.Hud;
import com.eve.eng1.util.ExitValidation;


public class GameScreen extends ScreenAdapter {
    private final Main game;
    private final Batch batch;
    private final AssetService assetService;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final AudioService audioService;
    private Hud hud;
    private SpriteBatch spriteBatch;



    private final TiledService tiledService;
    private final Engine engine;
    private final TiledAshleyConfigurator tiledAshleyConfigurator;
    private final World physicWorld;
    private final KeyboardController keyboardController;
    private final Stage stage;
    private final Viewport uiViewport;

    public GameScreen(Main game) {
        this.game = game;
        this.assetService = game.getAssetService();
        this.viewport = game.getViewport();
        this.camera = game.getCamera();
        this.batch = game.getBatch();
        this.audioService = game.getAudioService();

        this.physicWorld = new World(Vector2.Zero, true);
        this.tiledService = new TiledService(game.getAssetService());
        this.engine = new Engine();
        this.physicWorld.setAutoClearForces(false);
        this.tiledAshleyConfigurator = new TiledAshleyConfigurator(this.engine, game.getAssetService(), this.physicWorld);
        this.keyboardController = new KeyboardController(GameControllerState.class, engine);
        this.uiViewport = new FitViewport(320f, 180f);
        this.stage = new Stage(uiViewport, game.getBatch());

        this.engine.addSystem(new PhysicMoveSystem());
        this.engine.addSystem(new FsmSystem());
        this.engine.addSystem(new FacingSystem());
        this.engine.addSystem(new PhysicSystem(physicWorld, 1 / 60f));
        this.engine.addSystem(new ControllerSystem(game.getAudioService()));
        this.engine.addSystem(new AnimationSystem(game.getAssetService()));
        this.engine.addSystem(new TriggerSystem(game.getAudioService()));
        this.engine.addSystem(new RenderSystem(game.getBatch(), game.getViewport(), game.getCamera()));
        this.engine.addSystem(new PhysicDebugRenderSystem(physicWorld, game.getCamera()));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.uiViewport.update(width, height, true);
    }

    @Override
    public void show() {
        game.setInputProcessors(stage, keyboardController);
        keyboardController.setActiveState(GameControllerState.class);

        Consumer<TiledMap> renderConsumer = this.engine.getSystem(RenderSystem.class)::setMap;
        Consumer<TiledMap> audioConsumer = audioService::setMap;

        this.tiledService.setMapChangeConsumer(renderConsumer.andThen(audioConsumer));
        this.tiledService.setLoadObjectConsumer(this.tiledAshleyConfigurator::onLoadObject);
        this.tiledService.setLoadTileConsumer(tiledAshleyConfigurator::onLoadTile);
        this.tiledService.setLoadTriggerConsumer(this.tiledAshleyConfigurator::onLoadTrigger);
        TiledMap tiledMap = this.tiledService.loadMap(MapAsset.BEDROOM);
        this.tiledService.setMap(tiledMap);

        spriteBatch = new SpriteBatch();
        hud = new Hud(spriteBatch);
        hud.startTimer();

    }

    @Override
    public void hide() {
        // Removes all entities in order to clean up
        this.engine.removeAllEntities();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        hud.update(delta);
        delta = Math.min(delta, 1 / 30f);
        this.engine.update(delta);

        uiViewport.apply();
        stage.getBatch().setColor(Color.WHITE);
        stage.act(delta);
        stage.draw();
        hud.draw();

        if (hud.isTimeUp()){
            goToMainMenu();
        }

        if(ExitValidation.getBackpack_value() && ExitValidation.getDoor_validation()){
            Gdx.app.exit();
        }
    }


    @Override
    public void dispose() {
        for (EntitySystem system : this.engine.getSystems()) {
            if (system instanceof Disposable disposableSystem) {
                disposableSystem.dispose();
            }
        }
        physicWorld.dispose();
        stage.dispose();
    }

    private void goToMainMenu(){
        game.setScreen(MainMenuScreen.class);

    }
}
