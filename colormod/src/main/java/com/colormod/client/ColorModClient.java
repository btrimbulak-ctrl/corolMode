package com.colormod.client;

import com.colormod.client.gui.ColorPickerScreen;
import com.colormod.client.gui.HudButtonRenderer;
import com.colormod.config.ColorConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColorModClient implements ClientModInitializer {

    public static final String MOD_ID = "colormod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static boolean wasLeftDown = false;

    @Override
    public void onInitializeClient() {
        LOGGER.info("[ColorMod] Initialising...");
        ColorConfig.load();
        HudButtonRenderer.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.currentScreen != null) { wasLeftDown = false; return; }

            long window = client.getWindow().getHandle();
            boolean leftDown = GLFW.glfwGetMouseButton(window,
                    GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;

            if (leftDown && !wasLeftDown) {
                int sw = client.getWindow().getScaledWidth();
                int sh = client.getWindow().getScaledHeight();
                double mx = client.mouse.getX() * sw / client.getWindow().getWidth();
                double my = client.mouse.getY() * sh / client.getWindow().getHeight();
                if (HudButtonRenderer.isHovered(mx, my, sh)) {
                    client.setScreen(new ColorPickerScreen(null));
                }
            }
            wasLeftDown = leftDown;
        });

        LOGGER.info("[ColorMod] Ready.");
    }
}
