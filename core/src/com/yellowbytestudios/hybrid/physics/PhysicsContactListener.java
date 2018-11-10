package com.yellowbytestudios.hybrid.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import static com.yellowbytestudios.hybrid.physics.consts.ObjectNames.PLAYER_FOOT;
import static com.yellowbytestudios.hybrid.physics.consts.ObjectNames.PLAYER_LEFT_SIDE;
import static com.yellowbytestudios.hybrid.physics.consts.ObjectNames.PLAYER_RIGHT_SIDE;

public class PhysicsContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA == null || fixtureB == null) return;

        checkPlayerContacts(fixtureA, true);
        checkPlayerContacts(fixtureB, true);
    }

    private void checkPlayerContacts(Fixture fixture, boolean isStartContact) {
        checkPlayerGrounded(fixture, isStartContact);
        checkPlayerLeftContact(fixture, isStartContact);
        checkPlayerRightContact(fixture, isStartContact);
    }

    public void checkPlayerGrounded(Fixture fixture, boolean starting) {
        Object data = fixture.getUserData();
        if (data != null && data.equals(PLAYER_FOOT)) {
            PhysicsPlayer physicsPlayer = (PhysicsPlayer) fixture.getBody().getUserData();
            physicsPlayer.setGroundContacts(physicsPlayer.getGroundContacts() + (starting ? 1 : -1));
        }
    }

    public void checkPlayerLeftContact(Fixture fixture, boolean starting) {
        Object data = fixture.getUserData();
        if (data != null && data.equals(PLAYER_LEFT_SIDE)) {
            PhysicsPlayer physicsPlayer = (PhysicsPlayer) fixture.getBody().getUserData();
            physicsPlayer.setLeftContacts(physicsPlayer.getLeftContacts() + (starting ? 1 : -1));
        }
    }

    public void checkPlayerRightContact(Fixture fixture, boolean starting) {
        Object data = fixture.getUserData();
        if (data != null && data.equals(PLAYER_RIGHT_SIDE)) {
            PhysicsPlayer physicsPlayer = (PhysicsPlayer) fixture.getBody().getUserData();
            physicsPlayer.setRightContacts(physicsPlayer.getRightContacts() + (starting ? 1 : -1));
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA == null || fixtureB == null) return;

        checkPlayerContacts(fixtureA, false);
        checkPlayerContacts(fixtureB, false);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
