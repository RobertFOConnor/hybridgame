package com.yellowbytestudios.hybrid.physics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.yellowbytestudios.hybrid.MainGame;
import com.yellowbytestudios.hybrid.tile.TileManager;

import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.PPM;

public class CameraManager {

    private OrthographicCamera camera;
    private OrthographicCamera debugCamera;
    private Box2DDebugRenderer box2DDebugRenderer;

    public CameraManager(OrthographicCamera camera, OrthographicCamera debugCamera) {
        this.camera = camera;
        this.debugCamera = debugCamera;
        this.debugCamera.setToOrtho(false, MainGame.WIDTH / PPM, MainGame.HEIGHT / PPM);
        this.debugCamera.update();
        box2DDebugRenderer = new Box2DDebugRenderer();
    }

    public void update(float delta, Sprite playerSprite) {

        float targetX = playerSprite.getX() + MainGame.WIDTH / 4;
        float targetY = playerSprite.getY() + playerSprite.getHeight() / 2;

        float camX = camera.position.x;
        float camY = camera.position.y;

        float SPEEDX = delta * (Math.abs(targetX - camX) * 3f);
        float SPEEDY = delta * (Math.abs(targetY - camY) * 5f);

        if (camX < targetX - 5) {
            setCamPos(camX + SPEEDX, camY);
        } else if (camX > targetX + 5) {
            setCamPos(camX - SPEEDX, camY);
        }

        camX = camera.position.x;
        camY = camera.position.y;

        if (camY < targetY - 5) {
            setCamPos(camX, camY + SPEEDY);
        } else if (camY > targetY + 5) {
            setCamPos(camX, camY - SPEEDY);
        }

        float leftLimit = MainGame.WIDTH / 2;
        float rightLimit = TileManager.mapWidth * 80 - MainGame.WIDTH / 2;

        float bottomLimit = MainGame.HEIGHT / 2;
        float topLimit = TileManager.mapHeight * 80 - MainGame.HEIGHT / 2;

        camX = camera.position.x;
        camY = camera.position.y;

        if (camX < leftLimit) {
            setCamPos(leftLimit, camY);
        } else if (camX > rightLimit) {
            setCamPos(rightLimit, camY);
        }

        camX = camera.position.x;
        camY = camera.position.y;

        if (camY < bottomLimit) {
            setCamPos(camX, bottomLimit);
        } else if (camY > topLimit) {
            setCamPos(camX, topLimit);
        }

        debugCamera.position.set(camera.position);
        debugCamera.update();
    }

    public void renderDebug(World world) {
        box2DDebugRenderer.render(world, debugCamera.combined);
    }

    public void setCamPos(float x, float y) {
        camera.position.set(x, y, 0);
        camera.update();
        debugCamera.position.set(x, y, 0);
        debugCamera.update();
    }
}
