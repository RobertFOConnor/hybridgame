package com.yellowbytestudios.hybrid.effects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Floater {

    private Sprite particle;
    private float translateX, translateY;
    private float alpha = 1;

    public Floater(float x, float y, float translateX, float translateY, float lifeSpan) {
        particle = new Sprite(new Texture("effects/particle.png"));
        particle.setPosition(x, y);
        this.translateX = translateX;
        this.translateY = translateY;
        particle.setScale(0.1f);
    }

    public void update() {
        particle.translate(translateX, translateY);
        alpha -= 0.01f;
        //particle.setAlpha(alpha);
    }

    public void render(SpriteBatch sb) {
        particle.draw(sb);
    }
}
