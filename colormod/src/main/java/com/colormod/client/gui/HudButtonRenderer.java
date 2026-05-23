package com.colormod.client.gui;

import com.colormod.config.ColorConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class HudButtonRenderer {

    public static final int BTN_X = 6;
    public static final int BTN_Y_FROM_BOTTOM = 26;
    public static final int BTN_W = 54;
    public static final int BTN_H = 16;

    public static int getBtnY(int screenHeight) {
        return screenHeight - BTN_Y_FROM_BOTTOM;
    }

    public static boolean isHovered(double mx, double my, int screenHeight) {
        int by = getBtnY(screenHeight);
        return mx >= BTN_X && mx <= BTN_X + BTN_W
                && my >= by && my <= by + BTN_H;
    }

    private static final int COL_BTN_NORMAL = 0xFF555555;
    private static final int COL_BTN_HOVER  = 0xFF777777;
    private static final int COL_BTN_BORDER = 0xFF999999;
    private static final int COL_BTN_SHADOW = 0xFF333333;
    private static final int COL_TEXT       = 0xFFFFFFFF;
    private static final int COL_DOT_BORDER = 0xFF222222;

    public static void register() {
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.currentScreen != null) return;

            int screenW = client.getWindow().getScaledWidth();
            int screenH = client.getWindow().getScaledHeight();

            double mouseX = client.mouse.getX() * screenW / client.getWindow().getWidth();
            double mouseY = client.mouse.getY() * screenH / client.getWindow().getHeight();
            boolean hovered = isHovered(mouseX, mouseY, screenH);

            renderButton(drawContext, screenH, hovered);

            ColorConfig cfg = ColorConfig.get();
            if (cfg.opacity > 0.005f) {
                drawContext.fill(0, 0, screenW, screenH, cfg.toARGB());
            }
        });
    }

    public static void renderButton(DrawContext ctx, int screenH, boolean hovered) {
        MinecraftClient client = MinecraftClient.getInstance();
        int by = getBtnY(screenH);

        ctx.fill(BTN_X + 2, by + 2, BTN_X + BTN_W + 2, by + BTN_H + 2, 0x55000000);

        int fill = hovered ? COL_BTN_HOVER : COL_BTN_NORMAL;
        ctx.fill(BTN_X, by, BTN_X + BTN_W, by + BTN_H, fill);
        ctx.fill(BTN_X, by, BTN_X + BTN_W, by + 1, hovered ? 0xFFAAAAAA : 0xFF888888);
        drawBorder(ctx, BTN_X, by, BTN_W, BTN_H, COL_BTN_BORDER);
        ctx.fill(BTN_X + 1, by + BTN_H - 2, BTN_X + BTN_W - 1, by + BTN_H - 1, COL_BTN_SHADOW);

        ColorConfig cfg = ColorConfig.get();
        int dotColor = 0xFF000000
                | ((int)(cfg.red   * 255) << 16)
                | ((int)(cfg.green * 255) << 8)
                |  (int)(cfg.blue  * 255);
        int dotX = BTN_X + 4;
        int dotY = by + (BTN_H - 8) / 2;
        ctx.fill(dotX, dotY, dotX + 8, dotY + 8, COL_DOT_BORDER);
        ctx.fill(dotX + 1, dotY + 1, dotX + 7, dotY + 7, dotColor);

        Text label = Text.translatable("colormod.button.open");
        ctx.drawText(client.textRenderer, label, BTN_X + 14, by + (BTN_H - 8) / 2, COL_TEXT, true);
    }

    public static void drawBorder(DrawContext ctx, int x, int y, int w, int h, int color) {
        ctx.fill(x,         y,         x + w,     y + 1,     color);
        ctx.fill(x,         y + h - 1, x + w,     y + h,     color);
        ctx.fill(x,         y,         x + 1,     y + h,     color);
        ctx.fill(x + w - 1, y,         x + w,     y + h,     color);
    }
}
