package com.eve.eng1.tiled;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.eve.eng1.asset.AssetService;
import com.eve.eng1.asset.MapAsset;

import java.util.function.Consumer;

public class TiledService {
    private final AssetService assetService;

    private TiledMap currentMap;

    private Consumer<TiledMap> mapChangeConsumer;
    private Consumer<TiledMapTileMapObject> loadObjectConsumer;

    public TiledService(AssetService assetService) {
        this.assetService = assetService;
        this.mapChangeConsumer = null;
        this.loadObjectConsumer = null;
        this.currentMap = null;
    }

    public TiledMap loadMap(MapAsset mapAsset) {
        TiledMap tiledMap = this.assetService.load(mapAsset);
        tiledMap.getProperties().put("mapAsset", mapAsset);

        return tiledMap;
    }

    public void setMap(TiledMap map) {
        // If the current map is already loaded, unload it, then reload it with the specified map
        if(this.currentMap != null) {
            this.assetService.unload(this.currentMap.getProperties().get("mapAsset", MapAsset.class));
        }

        this.currentMap = map;
        loadMapObjects(map);
        if (this.mapChangeConsumer != null) {
            this.mapChangeConsumer.accept(map);
        }
    }

    private void loadMapObjects(TiledMap tiledMap) {
        //
        for (MapLayer layer : tiledMap.getLayers()) {
            if ("objects".equals(layer.getName())) {
                loadObjectLayer(layer);
            }
        }
    }

    private void loadObjectLayer(MapLayer objectLayer) {
        if (loadObjectConsumer == null) return;

        for (MapObject mapObject : objectLayer.getObjects()) {
            if (mapObject instanceof  TiledMapTileMapObject tileMapObject){
                loadObjectConsumer.accept(tileMapObject);
            } else {
                throw new GdxRuntimeException("Unsupported object: " + mapObject.getClass().getSimpleName());
            }
        }
    }

    public void setMapChangeConsumer(Consumer<TiledMap> mapChangeConsumer) {
        this.mapChangeConsumer = mapChangeConsumer;
    }

    public void setLoadObjectConsumer(Consumer<TiledMapTileMapObject> loadObjectConsumer) {
        this.loadObjectConsumer = loadObjectConsumer;
    }
}
