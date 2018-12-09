package com.yellowbytestudios.hybrid.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class FloaterManager {

    private ArrayList<Floater> floaters;

    public FloaterManager() {
        floaters = new ArrayList<Floater>();
        for (int i = 0; i < 100; i++) {
            floaters.add(new Floater((int) (Math.random() * 1000), (int) (Math.random() * 1000), (int) (Math.random() * 200) / 100f, (int) (Math.random() * 200) / 100f, 1));
        }
    }

    public void updateAndRender(SpriteBatch sb) {
        sb.begin();
        for (int i = 0; i < floaters.size(); i++) {
            floaters.get(i).update();
            floaters.get(i).render(sb);
        }
        sb.end();
    }
}
