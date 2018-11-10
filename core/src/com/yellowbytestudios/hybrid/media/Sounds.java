package com.yellowbytestudios.hybrid.media;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Sounds {

    private static boolean soundFXEnabled = true;
    private static boolean musicEnabled = false;
    private static Music GAME_MUSIC;

    public static void play(String s) {
        if (soundFXEnabled) {
            Sound sound = Assets.manager.get(s, Sound.class);
            sound.play(0.05f);
        }
    }

    public static void stop(String s) {
        if (soundFXEnabled) {
            Assets.manager.get(s, Sound.class).stop();
        }
    }

    public static void loop(String s) {
        if (soundFXEnabled) {
            Assets.manager.get(s, Sound.class).loop();
        }
    }

    public static void switchMusic(String music) {
        if (musicEnabled) {
            if (GAME_MUSIC.isPlaying()) {
                GAME_MUSIC.stop();
            }
            setMusic(music);
        }
    }

    public static void setMusic(String music) {
        GAME_MUSIC = Assets.manager.get(music, Music.class);
        if (musicEnabled) {
            GAME_MUSIC.setLooping(true);
            GAME_MUSIC.setVolume(0.1f);
            GAME_MUSIC.play();
        }
    }

    public static boolean toggleMusic() {
        musicEnabled = !musicEnabled;
        if (musicEnabled) {
            GAME_MUSIC.setLooping(true);
            GAME_MUSIC.play();
        } else {
            if (GAME_MUSIC.isPlaying()) {
                GAME_MUSIC.stop();
            }
        }
        return musicEnabled;
    }

    public static boolean toggleSound() {
        soundFXEnabled = !soundFXEnabled;
        return soundFXEnabled;
    }

    public static boolean isSoundFXEnabled() {
        return soundFXEnabled;
    }

    public static boolean isMusicEnabled() {
        return musicEnabled;
    }
}