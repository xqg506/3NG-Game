package com.eve.eng1.ui.view;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.eve.eng1.ui.model.MenuViewModel;

public class MenuView extends View<MenuViewModel> {

    public MenuView(Stage stage, Skin skin, MenuViewModel viewModel) {
        super(stage, skin, viewModel);

    }

    @Override
    public void setupUI() {

        setFillParent(true);
        setBackground(skin.getDrawable("Background"));

        Label label = new Label("3NG GAME", skin, "Title");
        label.setTouchable(Touchable.disabled);
        label.setAlignment(Align.center);
        label.setColor(skin.getColor("RGBA_255_255_255_255"));
        add(label).expand().minWidth(300.0f).minHeight(75.0f).row();

        setupMenuContent();

        label = new Label("By Group 3", skin);
        label.setName("creditLabel");
        label.setAlignment(Align.bottomRight);
        add(label).grow();

    }

    private void setupMenuContent() {
        Label label;
        Table contentTable = new Table();

        TextButton textButton = new TextButton("Start Game", skin);
        contentTable.add(textButton);
        onClick(textButton, viewModel::startGame);

        contentTable.row();

        Slider musicSlider = setupVolumeSlider(contentTable, "Music Volume");
        musicSlider.setValue(viewModel.getMusicVolume());
        onChange(musicSlider, (slider) -> viewModel.setMusicVolume(slider.getValue()));

        Slider soundSlider = setupVolumeSlider(contentTable, "Sound Volume");
        musicSlider.setValue(viewModel.getSoundVolume());
        onChange(soundSlider,  (slider) -> viewModel.setSoundVolume(slider.getValue()));


        textButton = new TextButton("Quit Game", skin);
        contentTable.add(textButton);
        onClick(textButton, viewModel::quitGame);
        add(contentTable).fill().row();
    }

    private Slider setupVolumeSlider(Table contentTable, String title) {
        Label label;
        Table table = new Table();

        label = new Label(title, skin);
//        label.setName("musicVolumeLabel");
        label.setTouchable(Touchable.disabled);
        table.add(label).row();

        Slider slider = new Slider(0.0f, 1f, 0.05f, false, skin, "default-horizontal");
        table.add(slider);
        contentTable.add(table).row();

        return slider;
    }
}
