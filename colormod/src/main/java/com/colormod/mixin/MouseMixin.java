package com.colormod.mixin;

import com.colormod.client.gui.ColorPickerScreen;
import com.colormod.client.gui.HudButtonRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "method_1598", at = @At("HEAD"), cancellable = true)
    private void colormod$onMouseButton(long window, int button, int action,
                                        int mods, CallbackInfo ci) {
        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) return;
        if (action != GLFW.GLFW_PRESS)             return;
        if (client.currentScreen != null)           return;

        int screenW = client.getWindow().getScaledWidth();
        int screenH = client.getWindow().getScaledHeight();

        double mouseX = client.mouse.getX()
                * screenW / client.getWindow().getWidth();
        double mouseY = client.mouse.getY()
                * screenH / client.getWindow().getHeight();

        if (HudButtonRenderer.isHovered(mouseX, mouseY, screenH)) {
            client.setScreen(new ColorPickerScreen(null));
            ci.cancel();
        }
    }
}
