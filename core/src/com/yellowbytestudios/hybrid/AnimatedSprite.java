package com.yellowbytestudios.hybrid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.hybrid.media.Assets;

public class AnimatedSprite extends Sprite {

    private static float FRAME_DURATION = .05f;
    private Animation animation;
    private float elapsed_time = 0f;

    public AnimatedSprite(String atlas, float x, float y) {
        super(new Sprite());
        setPosition(x, y);
        Array<TextureAtlas.AtlasRegion> runningFrames = Assets.getAtlas(atlas).findRegions("p");
        animation = new Animation(FRAME_DURATION, runningFrames, Animation.PlayMode.NORMAL);
    }

    public void setPlayMode(Animation.PlayMode mode) {
        animation.setPlayMode(mode);
    }

    public void render(SpriteBatch sb) {
        elapsed_time += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = (TextureRegion) animation.getKeyFrame(elapsed_time);
        sb.draw(currentFrame, getX(), getY());
    }
}
