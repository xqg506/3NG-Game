package com.eve.eng1.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.eve.eng1.Main;
import com.eve.eng1.asset.AssetService;
import com.eve.eng1.asset.AtlasAsset;

public class LoadingScreen extends ScreenAdapter {

    private final Main game;
    private final AssetService assetService;

    public LoadingScreen(Main game,  AssetService assetService) {
        this.game = game;
        this.assetService = assetService;
    }

    @Override
    public void show() {
        /*
        When the loading screen gets shown, for all of our atlas assets we have we iterate over them,
        and queue the asset for loading (asynchronous loading).
         */
        for (AtlasAsset atlas : AtlasAsset.values()) {
            assetService.queue(atlas);
        }
    }

    @Override
    public void render(float delta) {
        /*
        If we decide to include a loading screen it should be implemented here.
        This is possible by implementing this.assetManager.getProgress() in the update function in the
        AssetManager - it will return a value between 0 and 1.
         */
        if(this.assetService.update()) {
            Gdx.app.debug("LoadingScreen", "Finished asset loading");
            createScreens();
            this.game.removeScreen(this);
            this.dispose();
            this.game.setScreen(GameScreen.class);
        }
    }

    private void createScreens() {
        this.game.addScreen(new GameScreen(this.game));
    }

}
