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
    private CameraManager cameraManager;
    private PhysicsContactListener contactListener;
    private ArrayList<PhysicsPlayer> physicsPlayers;
    private ArrayList<PhysicsObject> worldObjects;
    private ArrayList<Integer> objectsToRemove;

    //Debugging
    private static final boolean drawDebug = true;
    private boolean gameOver = false;


    public PhysicsManager(OrthographicCamera camera) {
        world = new World(new Vector2(0, GRAVITY), true);
        contactListener = new PhysicsContactListener();
        world.setContactListener(contactListener);
        cameraManager = new CameraManager(camera, new OrthographicCamera());

        worldObjects = new ArrayList<PhysicsObject>();
        objectsToRemove = new ArrayList<Integer>();

        physicsPlayers = new ArrayList<PhysicsPlayer>();

        if (ControllerManager.hasController()) {
            createPlayer(new XboxController());
        } else {
            createPlayer(new KeyboardController());
        }


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
        cameraManager.update(delta, getPlayer(0).getSprite());
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void render(SpriteBatch sb) {
        if (drawDebug) {
            cameraManager.renderDebug(sb, world);
        }

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
