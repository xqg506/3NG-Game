package com.eve.eng1.ui.view;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.eve.eng1.asset.AtlasAsset;
import com.eve.eng1.ui.model.ViewModel;

public abstract class View<T extends ViewModel> extends Table {

    protected final Stage stage;
    protected final Skin skin;
    protected final T viewModel;

    public View(Stage stage, Skin skin, T viewModel) {
        super(skin);
        this.stage = stage;
        this.skin = skin;
        this.viewModel = viewModel;
        setupUI();
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (stage == null) {
            viewModel.clearPropertyChanges();
        } else {
            setPropertyChanges();
        }
    }
    protected abstract void setupUI();

    protected void setPropertyChanges() {
        
    }
}
