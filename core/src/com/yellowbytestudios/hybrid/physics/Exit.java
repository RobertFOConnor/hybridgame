package com.yellowbytestudios.hybrid.physics;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.yellowbytestudios.hybrid.physics.atoms.PhysicsObject;
import com.yellowbytestudios.hybrid.physics.consts.ObjectNames;

import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.BIT_PLAYER;
import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.BIT_WALL;
import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.PPM;

public class Exit extends PhysicsObject {

    private String linkedMap;
    private float linkX;
    private float linkY;

    public Exit(Sprite sprite, String linkedMap, float linkX, float linkY) {
        super(ObjectNames.EXIT, sprite);
        this.linkedMap = linkedMap;
        this.linkX = linkX;
        this.linkY = linkY;
    }

    @Override
    public void build(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(sprite.getX(), sprite.getY());
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2 / PPM, sprite.getHeight() / 2 / PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = BIT_WALL;
        fixtureDef.filter.maskBits = BIT_PLAYER;
        body.createFixture(fixtureDef).setUserData(ObjectNames.EXIT);
        shape.dispose();
        this.body = body;
        this.body.setUserData(this);
    }

    @Override
    public void render(SpriteBatch sb) {
        //do nothing
    }

    public String getLinkedMap() {
        return linkedMap;
    }

    public float getLinkX() {
        return linkX;
    }

    public float getLinkY() {
        return linkY;
    }
}
