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

    private float FRAME_DURATION = .07f;
    private Animation animation;
    private TextureRegion currentFrame;
    private float elapsed_time = 0f;
    private boolean left = false;

    public AnimatedSprite(String atlas, float x, float y, String name) {
        super(new Sprite());
        setPosition(x, y);
        Array<TextureAtlas.AtlasRegion> runningFrames = Assets.getAtlas(atlas).findRegions(name);
        animation = new Animation(FRAME_DURATION, runningFrames, Animation.PlayMode.LOOP);
    }

    public void setPlayMode(Animation.PlayMode mode) {
        animation.setPlayMode(mode);
    }

    public void render(SpriteBatch sb) {

        elapsed_time += Gdx.graphics.getDeltaTime();
        currentFrame = (TextureRegion) animation.getKeyFrame(elapsed_time);
        if (isLeft() && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

        if(!isLeft() && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

        sb.draw(currentFrame, getX(), getY());
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }
}
