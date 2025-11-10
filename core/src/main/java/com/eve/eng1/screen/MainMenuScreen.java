package com.eve.eng1.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.eve.eng1.Main;
import com.eve.eng1.asset.MusicAsset;
import com.eve.eng1.asset.SkinAsset;
import com.eve.eng1.ui.model.MenuViewModel;
import com.eve.eng1.ui.view.MenuView;

public class MainMenuScreen extends ScreenAdapter {
    private final Main game;
    private final Skin skin;
    private final Stage stage;
    private final Viewport uiViewport;

    public MainMenuScreen(Main game) {
        this.game = game;
        this.uiViewport = new FitViewport(800f, 600f);
        this.stage = new Stage(uiViewport, game.getBatch());
        this.skin = game.getAssetService().get(SkinAsset.DEFAULT);
    }

    @Override
    public void resize(int width, int height) {
        uiViewport.update(width, height, true);
    }

    @Override
    public void show() {
        this.game.setInputProcessors(stage);

        stage.addActor(new MenuView(stage, skin, new MenuViewModel(game)));

        this.game.getAudioService().playMusic(MusicAsset.MUSIC_RELAX);
    }

    @Override
    public void hide() {
        this.stage.clear();
    }

    @Override
    public void render(float delta) {
        uiViewport.apply();
        stage.getBatch().setColor(Color.WHITE);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
