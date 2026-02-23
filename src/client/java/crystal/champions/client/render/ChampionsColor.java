package crystal.champions.client.render;

import net.minecraft.client.gui.DrawContext;

public class ChampionsColor {
    /**
     * Это использую для того, чтобы потом через кфг с hex цвета менять в числовое
     * Чуть вайб кодинга было тут
     */
    public static void applyColor(DrawContext context, int hex) {
        float r = (float) (hex >> 16 & 255) / 255.0F;
        float g = (float) (hex >> 8 & 255) / 255.0F;
        float b = (float) (hex & 255) / 255.0F;
        context.setShaderColor(r, g, b, 1.0F);
    }

    public static void resetColor(DrawContext context) {
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static int getColor(int tier) {
        try {
            return switch (tier) {
                case 1 -> 0xffff55;
                case 2 -> 0xf57c2c;
                case 3 -> 0x46dffa;
                case 4 -> 0x8823db;
                default -> 0xf98aff;
            };
        } catch (Exception e) {
            return 0xffffff;
        }
    }

}
