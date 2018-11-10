package com.yellowbytestudios.hybrid.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.hybrid.AnimatedSprite;
import com.yellowbytestudios.hybrid.controller.types.BasicController;
import com.yellowbytestudios.hybrid.media.Assets;
import com.yellowbytestudios.hybrid.physics.atoms.PhysicsCharacter;

import static com.yellowbytestudios.hybrid.physics.consts.ObjectNames.PLAYER;
import static com.yellowbytestudios.hybrid.physics.consts.PhysicsValues.PPM;

public class PhysicsPlayer extends PhysicsCharacter {

    private AnimatedSprite runningSprite;
    private BasicController controller;
    private float SPEED = 8f;
    private float RUN_SPEED = 16f;
    private float DASH_SPEED = 24f;
    private float JUMP = 30f;
    private float WALL_JUMP = 10f;

    private int groundContacts = 0;
    private int leftContacts = 0;
    private int rightContacts = 0;
    private boolean wallJumping = false;
    private boolean facingLeft = false;
    private boolean dashing = false;
    private int dashCounter = 0;

    public PhysicsPlayer(Sprite sprite, BasicController controller) {
        super(PLAYER, sprite);
        this.controller = controller;

        //sprites
        runningSprite = new AnimatedSprite(Assets.ATLAS, 0, 0);
    }

    // Links player object with physics body (needed for contacts).
    public void setupUserData() {
        body.setUserData(this);
        body.setFixedRotation(true);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        runningSprite.setPosition(sprite.getX(), sprite.getY());

        updateMoving();
    }

    @Override
    public void render(SpriteBatch sb) {
        runningSprite.render(sb);
    }

    public void updateMoving() {
        Body body = getBody();
        Vector2 velocity = body.getLinearVelocity();

        if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            dashing = true;
        }

        if (controller != null) {
            updateWalking();
            updateJumping();
        }

        if (velocity.x > 0) {
            facingLeft = false;
        } else if (velocity.x < 0) {
            facingLeft = true;
        }

        updateSprite();

        System.out.println(getXCoord() + ", " + getYCoord());
    }

    private void updateJumping() {
        Vector2 velocity = body.getLinearVelocity();
        if (!dashing) {
            if (controller.jumpJustPressed()) {
                if (isGrounded()) {
                    startJump();
                } else if (isOnLeftWall()) {
                    beginWallJump(true);
                } else if (isOnRightWall()) {
                    beginWallJump(false);
                }
            } else {
                if (!controller.jumpPressed() && body.getLinearVelocity().y > 0) {
                    body.setLinearVelocity(velocity.x, 0);
                }
            }
        }
    }

    public void startJump() {
        if (isGrounded()) {
            Vector2 velocity = body.getLinearVelocity();
            body.setLinearVelocity(velocity.x, JUMP);
        }
    }

    private void beginWallJump(boolean left) {
        body.setLinearVelocity(left ? WALL_JUMP : -WALL_JUMP, JUMP);
        setWallJumping(true);
    }

    private void updateWalking() {
        // Move player
        if (dashing) {
            dashCounter++;
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            getBody().setGravityScale(0f);
            if (facingLeft) {
                applyXSpeed(-DASH_SPEED);
            } else {
                applyXSpeed(DASH_SPEED);
            }

        } else if (!isWallJumping()) {
            float speed = SPEED;
            if (controller.runPressed()) {
                speed = RUN_SPEED;
            }

            if (controller.leftPressed()) {
                applyXSpeed(-speed);
            } else if (controller.rightPressed()) {
                applyXSpeed(speed);
            } else {
                applyXSpeed(0);
            }
        }

        if (dashCounter > 10) {
            dashing = false;
            dashCounter = 0;
            getBody().setGravityScale(1f);
        }
    }

    private void applyXSpeed(float speed) {
        body.setLinearVelocity(speed, body.getLinearVelocity().y);
    }

    private void updateSprite() {
        getSprite().setFlip(facingLeft, false);
    }

    public float getXCoord() {
        return sprite.getX() / 80;
    }

    public float getYCoord() {
        return sprite.getY() / 80;
    }

    public int getTileX() {
        if (facingLeft) {
            return ((int) getXCoord()) - 1;
        }
        return ((int) getXCoord()) + 2;
    }

    public int getTileY() {
        return ((int) getYCoord()) + 1;
    }

    public int getGroundContacts() {
        return groundContacts;
    }

    public void setGroundContacts(int groundContacts) {
        this.groundContacts = groundContacts;
        setWallJumping(false);
    }

    public void setPos(float x, float y) {
        getBody().setTransform(x, y, 0);
    }

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }

    public int getLeftContacts() {
        return leftContacts;
    }

    public void setLeftContacts(int leftContacts) {
        this.leftContacts = leftContacts;
    }

    public boolean isOnLeftWall() {
        return this.leftContacts > 0;
    }

    public int getRightContacts() {
        return rightContacts;
    }

    public void setRightContacts(int rightContacts) {
        this.rightContacts = rightContacts;
    }

    public boolean isOnRightWall() {
        return this.rightContacts > 0;
    }

    public boolean isGrounded() {
        return this.groundContacts > 0;
    }

    public boolean isWallJumping() {
        return wallJumping;
    }

    public void setWallJumping(boolean wallJumping) {
        this.wallJumping = wallJumping;
    }

    public float getCenterX() {
        return getBody().getPosition().x - sprite.getWidth() / 2 / PPM;
    }

    public float getCenterY() {
        return getBody().getPosition().y - sprite.getHeight() / 2 / PPM;
    }


}
