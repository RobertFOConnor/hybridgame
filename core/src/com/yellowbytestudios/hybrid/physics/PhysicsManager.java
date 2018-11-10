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

import java.util.ArrayList;

import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.GRAVITY;
import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.PPM;

public class PhysicsManager {

    private World world;
    private OrthographicCamera camera;
    private PhysicsContactListener contactListener;
    private ArrayList<PhysicsPlayer> physicsPlayers;
    private ArrayList<PhysicsObject> worldObjects;

    //Debugging
    private boolean drawDebug = true;
    private Box2DDebugRenderer box2DDebugRenderer;
    private OrthographicCamera debugCamera;


    private boolean trackPlayer = true;

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

        if (ControllerManager.hasController()) {
            createPlayer(new XboxController());
        } else {
            createPlayer(new KeyboardController());
        }


        worldObjects = new ArrayList<PhysicsObject>();
        worldObjects.addAll(physicsPlayers);

        Sprite npcSprite = new Sprite(new Texture("textures/player/p_1.png"));
        npcSprite.setPosition(400, 460);
        worldObjects.add(new PhysicsObject("", npcSprite));

        for (PhysicsObject object : worldObjects) {
            object.build(world);
        }

        for (PhysicsPlayer physicsPlayer : physicsPlayers) {
            physicsPlayer.setupUserData();
        }
    }

    public void createPlayer(BasicController controller) {
        Sprite playerSprite = new Sprite(new Texture("textures/player/p_1.png"));
        playerSprite.setPosition(0, 0);
        physicsPlayers.add(new PhysicsPlayer(playerSprite, controller));
    }

    public void update(float delta) {
        world.step(delta, 6, 2);
        debugCamera.position.set(camera.position);
        debugCamera.update();

        for (PhysicsObject object : worldObjects) {
            object.update(delta);
        }

        trackPlayer();
    }

    public void trackPlayer() {
        if (trackPlayer) {
            PhysicsPlayer player = physicsPlayers.get(0);
            Sprite playerSprite = player.getSprite();
            setCamPos(playerSprite.getX(), playerSprite.getY() + 200);
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
    }
}
