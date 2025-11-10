package com.eve.eng1.ui.model;

import com.badlogic.gdx.Gdx;
import com.eve.eng1.Main;
import com.eve.eng1.audio.AudioService;
import com.eve.eng1.screen.GameScreen;
import com.eve.eng1.ui.model.ViewModel;


public class MenuViewModel extends ViewModel {
    private final AudioService audioService;

    public MenuViewModel(Main game) {
        super(game);
        this.audioService = game.getAudioService();

    }

    public float getMusicVolume() {
        return audioService.getMusicVolume();
    }

    public float getSoundVolume() {
        return audioService.getSoundVolume();
    }

    public void setMusicVolume(float volume) {
        audioService.setMusicVolume(volume);
    }

    public void setSoundVolume(float volume) {
        audioService.setSoundVolume(volume);
    }

    public void startGame() {
        game.setScreen(GameScreen.class);
    }

    public void quitGame() {
        Gdx.app.exit();
    }
}
