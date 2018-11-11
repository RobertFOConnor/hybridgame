package com.yellowbytestudios.hybrid.physics.atoms;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.yellowbytestudios.hybrid.physics.consts.ObjectNames.PLAYER_FOOT;
import static com.yellowbytestudios.hybrid.physics.consts.ObjectNames.PLAYER_LEFT_SIDE;
import static com.yellowbytestudios.hybrid.physics.consts.ObjectNames.PLAYER_RIGHT_SIDE;
import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.BIT_ENEMY;
import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.BIT_PLAYER;
import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.BIT_WALL;
import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.PPM;

public class PhysicsCharacter extends PhysicsObject {

    private boolean facingLeft = false;

    public PhysicsCharacter(String name, Sprite sprite) {
        super(name, sprite);
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
        fixtureDef.density = 0.1f;
        fixtureDef.friction = 0f;
        fixtureDef.filter.categoryBits = BIT_PLAYER;
        fixtureDef.filter.maskBits = BIT_WALL | BIT_ENEMY;
        body.createFixture(fixtureDef);
        shape.dispose();

        this.body = body;

        float sideWidth = 5 / PPM;
        float sideHeight = sprite.getHeight() / 4 / PPM;
        float sideX = sprite.getWidth() / 2 / PPM;

        buildSensorFixture(sprite.getWidth() / 2 / PPM - 0.02f, 20 / 4 / PPM, new Vector2(0, -sprite.getHeight() / 2 / PPM), PLAYER_FOOT);
        buildSensorFixture(sideWidth, sideHeight, new Vector2(-sideX, 0), PLAYER_LEFT_SIDE);
        buildSensorFixture(sideWidth, sideHeight, new Vector2(sideX, 0), PLAYER_RIGHT_SIDE);
    }

    private void buildSensorFixture(float w, float h, Vector2 pos, String userData) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(w, h, pos, 0);
        FixtureDef rdef = new FixtureDef();
        rdef.shape = shape;
        rdef.isSensor = true;
        rdef.filter.categoryBits = BIT_PLAYER;
        rdef.filter.maskBits = BIT_WALL | BIT_ENEMY;
        this.body.createFixture(rdef).setUserData(userData);
        shape.dispose();
    }

    public void applyXSpeed(float speed) {
        body.setLinearVelocity(speed, body.getLinearVelocity().y);
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }
}
