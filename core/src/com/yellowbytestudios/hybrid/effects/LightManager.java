package com.yellowbytestudios.hybrid.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

public class LightManager {

    private OrthographicCamera camera;
    private RayHandler handler;
    PointLight light;
    ArrayList<ConeLight> worldLights;

    private float brightness = 0f;

    public LightManager(World world, OrthographicCamera camera) {
        this.camera = camera;
        handler = new RayHandler(world);
        handler.setCombinedMatrix(camera);
        handler.setAmbientLight(brightness);
        handler.setBlur(true);
        handler.setCulling(true);
        float h = 30 * 80;

        worldLights = new ArrayList<ConeLight>();
        for (int i = 0; i < 10000; i++) {
            worldLights.add(createConeLight(i * 2000, h, i % 2 == 0 ? Color.BLUE : Color.FOREST));
        }

        //createLight(5 * 100, 2 * 100);
    }

    public ConeLight createConeLight(float x, float y, Color c) {
        ConeLight light = new ConeLight(handler, 10, c, 80 * 45, x, y, -90, 90);
        light.setSoft(true);
        light.setXray(false);
        return light;
    }

    public PointLight createLight(float x, float y) {
        light = new PointLight(handler, 5, Color.YELLOW, 50, x, y);
        light.setSoft(true);
        light.setXray(false);
        light.setSoftnessLength(10);
        return light;
    }

    public void removeLight(PointLight l) {
        l.setActive(false);
    }

    public void render() {
        for (ConeLight l : worldLights) {
            // l.setPosition(l.getX() + 1, l.getY());
        }

        //light.setDistance(400 + (int) (Math.random() * 50));
        //handler.setCombinedMatrix(camera);
        //handler.updateAndRender();
    }

    public void dispose() {
        handler.dispose();
    }
}
