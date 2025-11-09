package com.eve.eng1.audio;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.eve.eng1.asset.AssetService;
import com.eve.eng1.asset.MusicAsset;
import com.eve.eng1.asset.SoundAsset;

public class AudioService {
    private final AssetService assetService;
    private Music currentMusic;
    private MusicAsset currentMusicAsset;
    private float musicVolume;
    private float soundVolume;

    public AudioService(AssetService assetService) {
        this.assetService = assetService;
        this.currentMusic = null;
        this.currentMusicAsset = null;
        this.musicVolume = 0.5f;
        this.soundVolume = 0.33f;
    }

    public void setMusicVolume(float musicVolume){
        this.musicVolume = MathUtils.clamp(musicVolume, 0f, 1f);
        if (this.currentMusic != null) {
            this.currentMusic.setVolume(musicVolume);
        }
    }

    public float getMusicVolume(){
        return musicVolume;
    }

    public void setSoundVolume(float soundVolume){
        this.soundVolume = MathUtils.clamp(soundVolume, 0f, 1f);
    }

    public float getSoundVolume(){return soundVolume;}

    public void playMusic(MusicAsset musicAsset){
        if (this.currentMusicAsset == musicAsset) return;
        if (this.currentMusic != null){
            this.assetService.unload(this.currentMusicAsset);
        }

        this.currentMusic = this.assetService.load(musicAsset);
        this.currentMusic.setLooping(true);
        this.currentMusic.setVolume(musicVolume);
        this.currentMusic.play();
        this.currentMusicAsset = musicAsset;
    }

    public void playSound(SoundAsset soundAsset){
        this.assetService.get(soundAsset).play(soundVolume);
    }

    public void setMap(TiledMap tiledMap){
        String musicAssetStr = tiledMap.getProperties().get("music", "", String.class);
        if (musicAssetStr.isBlank()) return;

        MusicAsset musicAsset = MusicAsset.valueOf(musicAssetStr);
        playMusic(musicAsset);
    }
}


