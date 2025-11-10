package com.eve.eng1.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.eve.eng1.util.CountdownTimer;

public class Hud {
    private Stage stage;
    private Label countdownLabel;
    private CountdownTimer timer;

    public Hud(SpriteBatch batch) {
        stage = new Stage(new ScreenViewport(), batch);
        timer = new CountdownTimer(300);

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();
        style.fontColor = Color.WHITE;
        style.font.getData().setScale(2f);

        countdownLabel = new Label("300", style);
        countdownLabel.setPosition(40, Gdx.graphics.getHeight()-30);

        stage.addActor(countdownLabel);

    }

    public void update(float delta){
        timer.update(delta);
        countdownLabel.setText(String.valueOf(timer.getSeconds()));
    }

    public void startTimer() {
        timer.reset(300);
        timer.start();
    }

    public void draw(){
        stage.draw();

    }

    public boolean isTimeUp(){
        return timer.isFinished();
    }
}
