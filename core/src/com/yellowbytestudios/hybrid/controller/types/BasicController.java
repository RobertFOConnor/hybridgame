package com.yellowbytestudios.hybrid.controller.types;

public interface BasicController {

    boolean leftPressed();

    boolean rightPressed();

    boolean upPressed();

    boolean downPressed();

    boolean shootPressed();

    boolean switchGunPressed();

    boolean pausePressed();

    boolean jumpPressed();

    boolean jumpJustPressed();

    boolean runPressed();

    boolean menuUp();

    boolean menuDown();

    boolean menuSelect();

    boolean menuBack();
}