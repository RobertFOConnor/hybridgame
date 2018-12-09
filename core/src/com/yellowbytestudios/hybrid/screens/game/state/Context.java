package com.yellowbytestudios.hybrid.screens.game.state;

public class Context {

    private static GameState currentState;

    public static void setState(GameState state) {
        currentState = state;
    }
}
