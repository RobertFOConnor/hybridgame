package com.yellowbytestudios.hybrid.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.yellowbytestudios.hybrid.MainGame;
import com.yellowbytestudios.hybrid.utils.DeviceTypes;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Night Of The Living Twerkers";
        config.width = 1280;
        config.height = 720;
        config.resizable = true;
        config.vSyncEnabled = true;
        config.fullscreen = true;
        new LwjglApplication(new MainGame(DeviceTypes.DESKTOP), config);
    }
}
