package com.eve.eng1.asset;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;

public enum MusicAsset implements Asset<Music> {

    MUSIC_RELAX("music_relax.ogg");

    private final AssetDescriptor<Music> descriptor;

    MusicAsset(String musicFile) {
        this.descriptor = new AssetDescriptor<>("audio/" + musicFile, Music.class);
    }

    @Override
    public AssetDescriptor<Music> getDescriptor() {
        return descriptor;
    }
}
