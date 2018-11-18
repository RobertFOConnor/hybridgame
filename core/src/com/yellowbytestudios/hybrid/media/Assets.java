package com.yellowbytestudios.hybrid.media;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Assets {

    public static AssetManager manager = new AssetManager();
    private static HashMap<String, Class> assetMap = new HashMap<String, Class>();

    public static final String ATLAS = "pack.atlas";
    public static final String WALK_ATLAS = "textures/player/walk.atlas";
    public static final String IDLE_ATLAS = "textures/player/idle.atlas";
    public static final String DASH_ATLAS = "textures/player/dash.atlas";


    public static final String PLAYER = "player";

    // Loads Assets
    public static void load() {

        assetMap.put(ATLAS, TextureAtlas.class);
        assetMap.put(WALK_ATLAS, TextureAtlas.class);
        assetMap.put(IDLE_ATLAS, TextureAtlas.class);
        assetMap.put(DASH_ATLAS, TextureAtlas.class);

        Iterator it = assetMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            manager.load((String) pair.getKey(), (Class) pair.getValue());
            it.remove();
        }
    }

    public static TextureAtlas getAtlas(String name) {
        return manager.get(name, TextureAtlas.class);
    }

    public static Texture getTexture(String name) {
        return manager.get(name, Texture.class);
    }

    public static Sprite getSprite(String name) {
        return manager.get(ATLAS, TextureAtlas.class).createSprite(name);
    }

    public static void dispose() {
        manager.dispose();
    }

    public static boolean update() {
        return manager.update();
    }
}
