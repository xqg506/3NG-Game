package com.eve.eng1.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.maps.tiled.TiledMap;

public enum MapAsset implements Asset<TiledMap> {
    BEDROOM("bedroom.tmx");

    private final AssetDescriptor<TiledMap> descriptor;

    public MapAsset(String mapName) {
        this.descriptor = new AssetDescriptor<>("maps/" + mapName, TiledMap.class);
    }
    public MapAsset(AssetDescriptor<TiledMap> getescriptor) {
        this.descriptor = descriptor;
    }
}
