package com.yellowbytestudios.hybrid.media;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Fonts {

    private static BitmapFont futuraMedium; //size: 56
    public static final String FUTURA = "fonts/futura";

    public static void load() {
        futuraMedium = new BitmapFont(Gdx.files.internal(FUTURA.concat(".fnt")), Gdx.files.internal(FUTURA.concat(".png")), false);
        futuraMedium.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public static BitmapFont getFont(String fontName) {
        if(fontName.equals(FUTURA)) {
            return futuraMedium;
        }
        return null;
    }
}
