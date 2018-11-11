package com.yellowbytestudios.hybrid.physics.atoms;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.BIT_PLAYER;
import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.BIT_WALL;
import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.PPM;

public class PhysicsObject {

    private String name;
    protected Body body;
    protected Sprite sprite;
    private boolean dynamic = true;
    private boolean playerTouched = false;

    public PhysicsObject(String name, Sprite sprite) {
        this.name = name;
        this.sprite = sprite;
        this.sprite.setPosition(sprite.getX() / PPM, sprite.getY() / PPM);
    }

    public void build(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = isDynamic() ? BodyDef.BodyType.DynamicBody : BodyDef.BodyType.StaticBody;
        bodyDef.position.set(sprite.getX(), sprite.getY());

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2 / PPM, sprite.getHeight() / 2 / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.01f;
        fixtureDef.filter.categoryBits = BIT_WALL;
        fixtureDef.filter.maskBits = BIT_PLAYER;
        body.createFixture(fixtureDef).setUserData("wall");

        shape.dispose();

        this.body = body;
        setupUserData();
    }

    public boolean isPlayerTouched() {
        return playerTouched;
    }

    public void setPlayerTouched(boolean playerTouched) {
        this.playerTouched = playerTouched;
    }

    public void setupUserData() {
        body.setUserData(this);
        body.setFixedRotation(true);
    }

    public void update(float delta) {
        sprite.setPosition(body.getPosition().x * PPM - sprite.getWidth() / 2, body.getPosition().y * PPM - sprite.getHeight() / 2);
    }

    public void render(SpriteBatch sb) {
        sb.draw(sprite, sprite.getX(), sprite.getY());
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public Body getBody() {
        return body;
    }

    public String getName() {
        return name;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void dispose() {
    }
}
