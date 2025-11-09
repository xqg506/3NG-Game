package com.eve.eng1;


import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.eve.eng1.asset.AssetService;
import com.eve.eng1.audio.AudioService;
import com.eve.eng1.screen.GameScreen;
import com.eve.eng1.input.ControllerState;
import com.eve.eng1.input.GameControllerState;
import com.eve.eng1.input.KeyboardController;
import com.eve.eng1.screen.LoadingScreen;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public static final float WORLD_WIDTH = 14f;
    public static final float WORLD_HEIGHT = 14f;
    public static final float UNIT_SCALE = 1f / 16f;

    private Batch batch;
    private OrthographicCamera camera;
    private AssetService assetService;
    private AudioService audioService;
    private Engine engine;
    private InputMultiplexer inputMultiplexer;

    // The specific part of the map that the player sees
    private Viewport viewport;

    private final Map<Class<? extends Screen>, Screen> screenCache = new HashMap<>();

    @Override
    public void create() {
        this.inputMultiplexer = new InputMultiplexer();

        Gdx.input.setInputProcessor(inputMultiplexer);

        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        this.assetService = new AssetService(new InternalFileHandleResolver());
        this.audioService = new AudioService(assetService);
        this.engine = new Engine();
        KeyboardController keyboardController = new KeyboardController((Class<? extends ControllerState>) GameControllerState.class,engine);

    inputMultiplexer.addProcessor(keyboardController);
        addScreen(new LoadingScreen(this, assetService));
        setScreen(LoadingScreen.class);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        super.resize(width, height);
    }


    public void addScreen(Screen screen) {
        screenCache.put(screen.getClass(), screen);
    }

    public void removeScreen(Screen screen) {
        screenCache.remove(screen.getClass());
    }

    public void setScreen(Class<? extends Screen> screenClass) {
        Screen screen = screenCache.get(screenClass);

        if(screen == null) {
            throw new GdxRuntimeException("Screen class " + screenClass + " not found!");
        }
        super.setScreen(screen);
    }

    @Override
    public void dispose() {
        screenCache.values().forEach(Screen::dispose);
        screenCache.clear();

        this.batch.dispose();
        this.assetService.debugDiagnostics();
        this.assetService.dispose();
    }
    //Game updates
    public void render(float delta) {
        delta = Math.min(delta, 1 / 30f);
        this.engine.update(delta);
    }

    public void setInputProcessors(InputProcessor... processors) {
        //Resetting anything currently in + setting up inputs
        if (inputMultiplexer == null) {
            return;
        }
        inputMultiplexer.clear();

        if (processors == null) return;

        for (InputProcessor processor : processors) {
            inputMultiplexer.addProcessor(processor);
        }

    }
    public Batch getBatch() {
        return batch;
    }

    public AssetService getAssetService() {
        return assetService;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public AudioService getAudioService(){
        return audioService;
    }





}
