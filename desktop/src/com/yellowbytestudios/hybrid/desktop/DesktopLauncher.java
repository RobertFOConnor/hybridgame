package com.yellowbytestudios.hybrid.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.yellowbytestudios.hybrid.MainGame;
import com.yellowbytestudios.hybrid.utils.DeviceTypes;

public class DesktopLauncher {

    private static final String title = "Night Of The Living Twerkers";

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = getProductionConfig();
        new LwjglApplication(new MainGame(DeviceTypes.DESKTOP), config);
    }

    public static LwjglApplicationConfiguration getDiscreteConfig() {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = title;
        config.width = 1280 / 4;
        config.height = 720 / 4;
        config.x = 0;
        config.y = 0;
        config.resizable = true;
        config.vSyncEnabled = true;
        return config;
    }

    public static LwjglApplicationConfiguration getProductionConfig() {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = title;
        config.width = 1280;
        config.height = 720;
        config.vSyncEnabled = true;
        config.fullscreen = true;
        return config;
    }
}
