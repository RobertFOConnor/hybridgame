package com.yellowbytestudios.hybrid.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yellowbytestudios.hybrid.media.Fonts;

import static com.yellowbytestudios.hybrid.MainGame.WIDTH;

public class DeveloperTools {

    private static final String versionNumber = "1.1";
    private BitmapFont devFont;
    private FPSLogger fpsLogger;

    public DeveloperTools() {
        fpsLogger = new FPSLogger();
        devFont = Fonts.getFont(Fonts.FUTURA);
    }

    public void renderOverlay(SpriteBatch sb) {
        sb.begin();
        devFont.draw(sb, "FPS:" + Gdx.graphics.getFramesPerSecond(), 10, 70);
        devFont.draw(sb, "Version:" + versionNumber, WIDTH - 320, 70);
        sb.end();
    }

    public void logFPS() {
        fpsLogger.log();
    }
}
