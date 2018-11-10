package com.yellowbytestudios.hybrid.physics;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

public class NPC extends PhysicsPlayer {

    private float SPEED = 2f;
    private boolean left = false;
    private Timer timer;
    private Timer.Task task;

    public NPC(Sprite sprite) {
        super(sprite, null);

        task = new Timer.Task() {
            @Override
            public void run() {
                left = (int) (Math.random() * 2) == 0;
                
            }
        };

        timer = new Timer();
        timer.scheduleTask(task, 0.5f, 0.5f);
        timer.start();
    }

    public void update(float delta) {
        super.update(delta);
        walk(left);
    }

    private void walk(boolean left) {
        Vector2 velocity = body.getLinearVelocity();
        if (left) {
            body.setLinearVelocity(-SPEED, velocity.y);
        } else {
            body.setLinearVelocity(SPEED, velocity.y);
        }
    }

    public void dispose() {
        Timer.instance().stop();
        Timer.instance().clear();
        task.cancel();

    }
}
