package com.yellowbytestudios.hybrid.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yellowbytestudios.hybrid.AnimatedSprite;
import com.yellowbytestudios.hybrid.controller.types.BasicController;
import com.yellowbytestudios.hybrid.media.Assets;
import com.yellowbytestudios.hybrid.physics.atoms.PhysicsCharacter;

import static com.yellowbytestudios.hybrid.physics.consts.ObjectNames.PLAYER;

public class PhysicsPlayer extends PhysicsCharacter {

    private AnimatedSprite currSprite;
    private AnimatedSprite idleSprite;
    private AnimatedSprite runningSprite;
    private BasicController controller;
    private static final float SPEED = 400f;
    private static final float RUN_SPEED = 600f;
    private static final float DASH_SPEED = 1200f;
    private static final float JUMP = 30f;
    private static final float WALL_JUMP = 10f;

    private int groundContacts = 0;
    private int leftContacts = 0;
    private int rightContacts = 0;
    private boolean wallJumping = false;
    private boolean dashing = false;
    private int dashCounter = 0;

    public PhysicsPlayer(Sprite sprite, BasicController controller) {
        super(PLAYER, sprite);
        this.controller = controller;

        //sprites
        idleSprite = new AnimatedSprite(Assets.IDLE_ATLAS, 0, 0, "p_idle");
        runningSprite = new AnimatedSprite(Assets.WALK_ATLAS, 0, 0, "p_walk");
        currSprite = idleSprite;
    }

    // Links player object with physics body (needed for contacts).
    public void setupUserData() {
        body.setUserData(this);
        body.setFixedRotation(true);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        currSprite.setPosition(sprite.getX(), sprite.getY());

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            dashing = true;
        }

        if (controller != null) {
            updateWalking(delta);
            updateJumping();
        }
        updateSprite();
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
                if (!controller.jumpPressed() && velocity.y > 0) {
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

    private void updateWalking(float delta) {
        // Move player

        if (dashing) {
            dashCounter++;
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            getBody().setGravityScale(0f);
            if (isFacingLeft()) {
                applyXSpeed(-DASH_SPEED * delta);
            } else {
                applyXSpeed(DASH_SPEED * delta);
            }

        } else if (!isWallJumping()) {
            float speed = SPEED * delta;
            if (controller.runPressed()) {
                speed = RUN_SPEED * delta;
            }

            if (controller.leftPressed()) {
                setFacingLeft(true);
                currSprite.setLeft(true);
                updateSprite(runningSprite);
                applyXSpeed(-speed);
            } else if (controller.rightPressed()) {
                setFacingLeft(false);
                currSprite.setLeft(false);
                updateSprite(runningSprite);
                applyXSpeed(speed);
            } else {
                updateSprite(idleSprite);
                applyXSpeed(0);
            }
        }

        if (dashCounter > 10 || body.getLinearVelocity().x == 0) {
            dashing = false;
            dashCounter = 0;
            getBody().setGravityScale(1f);
        }
    }

    public void updateSprite(AnimatedSprite sprite) {
        currSprite = sprite;
        currSprite.setPosition(this.sprite.getX(), this.sprite.getY());
    }

    @Override
    public void render(SpriteBatch sb) {
        currSprite.render(sb);
    }

    public boolean isDashing() {
        return dashing;
    }

    private void updateSprite() {
        getSprite().setFlip(isFacingLeft(), false);
    }

    public int getGroundContacts() {
        return groundContacts;
    }

    public void setGroundContacts(int groundContacts) {
        this.groundContacts = groundContacts;
        setWallJumping(false);
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
}
