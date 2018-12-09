package com.yellowbytestudios.hybrid.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.yellowbytestudios.hybrid.controller.ControllerManager;
import com.yellowbytestudios.hybrid.controller.types.BasicController;
import com.yellowbytestudios.hybrid.controller.types.KeyboardController;
import com.yellowbytestudios.hybrid.controller.types.XboxController;
import com.yellowbytestudios.hybrid.effects.LightManager;
import com.yellowbytestudios.hybrid.effects.ParticleManager;
import com.yellowbytestudios.hybrid.physics.atoms.PhysicsObject;
import com.yellowbytestudios.hybrid.screens.game.GameScreen;
import com.yellowbytestudios.hybrid.screens.ScreenManager;

import java.util.ArrayList;

import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.GRAVITY;

public class PhysicsManager {

    private World world;
    private LightManager lightManager;
    private CameraManager cameraManager;
    private ParticleManager particleManager;
    private PhysicsContactListener contactListener;
    private ArrayList<PhysicsPlayer> physicsPlayers;
    private ArrayList<PhysicsObject> worldObjects;
    private ArrayList<Integer> objectsToRemove;

    //Debugging
    private static final boolean drawDebug = true;
    private boolean gameOver = false;


    public PhysicsManager(OrthographicCamera camera, float startX, float startY) {
        world = new World(new Vector2(0, GRAVITY), true);
        contactListener = new PhysicsContactListener();
        world.setContactListener(contactListener);
        cameraManager = new CameraManager(camera, new OrthographicCamera());
        lightManager = new LightManager(world, camera);
        particleManager = new ParticleManager();

        worldObjects = new ArrayList<PhysicsObject>();
        objectsToRemove = new ArrayList<Integer>();

        physicsPlayers = new ArrayList<PhysicsPlayer>();

        if (ControllerManager.hasController()) {
            createPlayer(new XboxController(), startX, startY);
        } else {
            createPlayer(new KeyboardController(), startX, startY);
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

    public void createPlayer(BasicController controller, float startX, float startY) {
        Sprite playerSprite = new Sprite(new Texture("textures/player/p_idle.png"));
        playerSprite.setPosition(startX + playerSprite.getWidth() / 2, startY);
        physicsPlayers.add(new PhysicsPlayer(playerSprite, controller));
    }

    public void update(float delta) {
        world.step(delta, 6, 2);

        for (int i = 0; i < worldObjects.size(); i++) {
            PhysicsObject object = worldObjects.get(i);
            object.update(delta);

            if (object.isPlayerTouched()) {
                if (object instanceof Exit) {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                        Exit exit = (Exit) object;
                        ScreenManager.setScreen(new GameScreen(exit.getLinkedMap(), exit.getLinkX(), exit.getLinkY()));
                        break;
                    }
                } else if (object instanceof Enemy) {
                    if (getPlayer(0).isDashing()) {
                        objectsToRemove.add(i);
                        Sprite enemy = object.getSprite();
                        particleManager.addEffect(enemy.getX() + enemy.getWidth() / 2, enemy.getY() + enemy.getHeight() / 2);
                    } else {
                        gameOver = true;
                        // damage player
                    }
                } else if (object instanceof Coin) {
                    objectsToRemove.add(i);
                }
            }
        }

        int objectsRemoved = 0;
        for (Integer index : objectsToRemove) {
            PhysicsObject obj = worldObjects.get(index - objectsRemoved);
            worldObjects.remove(obj);
            world.destroyBody(obj.getBody());
            obj.dispose();
            objectsRemoved++;
        }
        objectsToRemove.clear();
        cameraManager.update(delta, getPlayer(0).getSprite());
        particleManager.update(delta);

        if (getPlayer(0).getBody().getPosition().y < -50) {
            ScreenManager.setScreen(new GameScreen());
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void render(SpriteBatch sb) {

        sb.begin();
        for (PhysicsObject object : worldObjects) {
            object.render(sb);
        }
        particleManager.render(sb);
        sb.end();
        if (drawDebug) {
            cameraManager.renderDebug(world);
        }
        lightManager.render();
    }

    public PhysicsPlayer getPlayer(int index) {
        return physicsPlayers.get(index);
    }

    public World getWorld() {
        return world;
    }

    public void dispose() {
        world.dispose();
        lightManager.dispose();
        contactListener = null;
    }
}
