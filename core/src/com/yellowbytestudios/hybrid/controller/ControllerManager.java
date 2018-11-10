package com.yellowbytestudios.hybrid.controller;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

public class ControllerManager {

    public static boolean hasController() {
        int unusableControllers = 0;
        if (Controllers.getControllers().size > 0) {
            for (Controller c : Controllers.getControllers()) {
                if (c.getName().equals("Monect Hid Device")) { // Shows as controller but isn't a controller.
                    unusableControllers++;
                }
            }
        }
        return (Controllers.getControllers().size > unusableControllers);
    }
}
