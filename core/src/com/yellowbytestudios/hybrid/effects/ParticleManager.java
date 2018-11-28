package com.yellowbytestudios.hybrid.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class ParticleManager {

    //Particle Objects
    private ParticleEffectPool particleEffectPool;
    private Array<PooledEffect> effects = new Array<PooledEffect>();
    private ArrayList<Integer> effectsToRemove;


    public ParticleManager() {
        ParticleEffect particleEffect = new ParticleEffect();
        effectsToRemove = new ArrayList<Integer>();
        particleEffect.load(Gdx.files.internal("effects/blood.p"), Gdx.files.internal("effects"));
        particleEffectPool = new ParticleEffectPool(particleEffect, 1, 10);
    }

    public void addEffect(float x, float y) {
        PooledEffect effect = particleEffectPool.obtain();
        effect.setPosition(x, y);
        effect.start();
        effects.add(effect);
    }

    public void update(float delta) {
        for (PooledEffect effect : effects) {
            effect.update(delta);
        }
    }

    public void render(SpriteBatch sb) {
        for (int i = 0; i < effects.size; i++) {
            PooledEffect effect = effects.get(i);
            if (effect.isComplete()) {
                effectsToRemove.add(i);
            } else {
                effect.draw(sb);
            }
        }

        for (int index : effectsToRemove) {
            PooledEffect effect = effects.get(index);
            effect.free();
            effects.removeValue(effect, false);
            effect.dispose();
        }
        effectsToRemove.clear();
    }
}