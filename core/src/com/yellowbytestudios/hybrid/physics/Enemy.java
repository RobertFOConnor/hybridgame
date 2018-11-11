package com.yellowbytestudios.hybrid.physics;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.yellowbytestudios.hybrid.AnimatedSprite;
import com.yellowbytestudios.hybrid.media.Assets;
import com.yellowbytestudios.hybrid.physics.atoms.PhysicsCharacter;

import static com.yellowbytestudios.hybrid.physics.consts.ObjectNames.ENEMY;
import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.BIT_ENEMY;
import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.BIT_PLAYER;
import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.BIT_WALL;
import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.PPM;

public class Enemy extends PhysicsCharacter {

    private AnimatedSprite runningSprite;

    public Enemy(Sprite sprite) {
        super("Enemy", sprite);
        runningSprite = new AnimatedSprite(Assets.WALK_ATLAS, 0, 0, "p_walk");
    }

    @Override
    public void build(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(sprite.getX(), sprite.getY());

        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2 / PPM, sprite.getHeight() / 2 / PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0f;
        fixtureDef.isSensor = false;
        fixtureDef.filter.categoryBits = BIT_ENEMY;
        fixtureDef.filter.maskBits = BIT_WALL | BIT_PLAYER;
        body.createFixture(fixtureDef).setUserData(ENEMY);
        shape.dispose();

        this.body = body;
        this.body.setUserData(this);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        runningSprite.setPosition(sprite.getX(), sprite.getY());
        Vector2 velocity = body.getLinearVelocity();
        if (velocity.x > 0) {
            setFacingLeft(false);
            runningSprite.setLeft(false);
        } else if (velocity.x < 0) {
            setFacingLeft(true);
            runningSprite.setLeft(true);
        }

        if (isFacingLeft()) {
            applyXSpeed(-100f * delta);
        } else {
            applyXSpeed(100f * delta);
        }

        if (velocity.x == 0) {
            setFacingLeft(!isFacingLeft());
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        runningSprite.render(sb);
    }
}
