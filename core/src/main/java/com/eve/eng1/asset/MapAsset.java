package com.eve.eng1.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public enum MapAsset implements Asset<TiledMap> {
    BEDROOM("bedroom.tmx");

    private final AssetDescriptor<TiledMap> descriptor;

    MapAsset(String mapName) {
        TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters();
        parameters.projectFilePath = "maps/3NG-Game.tiled-project";
        this.descriptor = new AssetDescriptor<>("maps/" + mapName, TiledMap.class);
        
    }
    public AssetDescriptor<TiledMap> getDescriptor() {
        return this.descriptor;
    }
}
