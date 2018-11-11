package com.yellowbytestudios.hybrid.physics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.yellowbytestudios.hybrid.MainGame;
import com.yellowbytestudios.hybrid.controller.ControllerManager;
import com.yellowbytestudios.hybrid.controller.types.BasicController;
import com.yellowbytestudios.hybrid.controller.types.KeyboardController;
import com.yellowbytestudios.hybrid.controller.types.XboxController;
import com.yellowbytestudios.hybrid.physics.atoms.PhysicsObject;
import com.yellowbytestudios.hybrid.tile.TileManager;

import java.util.ArrayList;

import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.GRAVITY;
import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.PPM;

public class PhysicsManager {

    private World world;
    private OrthographicCamera camera;
    private PhysicsContactListener contactListener;
    private ArrayList<PhysicsPlayer> physicsPlayers;
    private ArrayList<PhysicsObject> worldObjects;
    ArrayList<Integer> objectsToRemove;

    //Debugging
    private boolean drawDebug = true;
    private Box2DDebugRenderer box2DDebugRenderer;
    private OrthographicCamera debugCamera;
    private boolean gameOver = false;


    public PhysicsManager(OrthographicCamera camera) {
        this.camera = camera;
        world = new World(new Vector2(0, GRAVITY), true);
        contactListener = new PhysicsContactListener();
        world.setContactListener(contactListener);
        box2DDebugRenderer = new Box2DDebugRenderer();
        debugCamera = new OrthographicCamera();
        debugCamera.setToOrtho(false, MainGame.WIDTH / PPM, MainGame.HEIGHT / PPM);
        debugCamera.update();

        physicsPlayers = new ArrayList<PhysicsPlayer>();
        objectsToRemove = new ArrayList<Integer>();

        if (ControllerManager.hasController()) {
            createPlayer(new XboxController());
        } else {
            createPlayer(new KeyboardController());
        }


        worldObjects = new ArrayList<PhysicsObject>();
        worldObjects.addAll(physicsPlayers);

        for (PhysicsObject object : worldObjects) {
            object.build(world);
        }

        for (PhysicsPlayer physicsPlayer : physicsPlayers) {
            physicsPlayer.setupUserData();
        }
    }

    public void addObject(PhysicsObject object) {
        object.build(world);
        worldObjects.add(object);
    }

    public void createPlayer(BasicController controller) {
        Sprite playerSprite = new Sprite(new Texture("textures/player/p_idle.png"));
        playerSprite.setPosition(80 * 3, 80 * 4);
        physicsPlayers.add(new PhysicsPlayer(playerSprite, controller));
    }

    public void update(float delta) {
        world.step(delta, 6, 2);
        debugCamera.position.set(camera.position);
        debugCamera.update();

        for (PhysicsObject object : worldObjects) {
            object.update(delta);

            if (object.isPlayerTouched()) {
                if (object instanceof Exit) {
                    gameOver = true;
                } else if (object instanceof Enemy) {
                    if (getPlayer(0).isDashing()) {
                        objectsToRemove.add(worldObjects.indexOf(object));
                    } else {
                        gameOver = true;
                        // damage player
                    }
                }
            }
        }

        for (Integer index : objectsToRemove) {
            PhysicsObject obj = worldObjects.get(index);
            worldObjects.remove(obj);
            world.destroyBody(obj.getBody());
            obj.dispose();
        }
        objectsToRemove.clear();
        cameraTrackPlayer();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void cameraTrackPlayer() {
        PhysicsPlayer player = physicsPlayers.get(0);
        Sprite playerSprite = player.getSprite();
        setCamPos(playerSprite.getX() + MainGame.WIDTH / 3, playerSprite.getY());
        float camX = camera.position.x;
        float camY = camera.position.y;

        float leftLimit = MainGame.WIDTH / 2;
        float rightLimit = TileManager.mapWidth * 80 - MainGame.WIDTH / 2;

        float bottomLimit = MainGame.HEIGHT / 2;
        float topLimit = TileManager.mapHeight * 80 - MainGame.HEIGHT / 2;

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
    }

    public void setCamPos(float x, float y) {
        camera.position.set(x, y, 0);
        camera.update();
        debugCamera.position.set(x, y, 0);
        debugCamera.update();
    }

    public void render(SpriteBatch sb) {

        sb.end();
        if (drawDebug) {
            box2DDebugRenderer.render(world, debugCamera.combined);
        }
        sb.begin();

        for (PhysicsObject object : worldObjects) {
            object.render(sb);
        }
    }

    public PhysicsPlayer getPlayer(int index) {
        return physicsPlayers.get(index);
    }

    public World getWorld() {
        return world;
    }

    public void dispose() {
        world.dispose();
        contactListener = null;
    }
}
