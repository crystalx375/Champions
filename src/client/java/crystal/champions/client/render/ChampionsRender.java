package crystal.champions.client.render;

import crystal.champions.client.ChampionHudRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import static crystal.champions.client.render.ChampionsColor.applyColor;
import static crystal.champions.client.render.ChampionsColor.resetColor;

public class ChampionsRender {
    private static final Identifier CHAMPION_STARS = Identifier.of("champions", "textures/gui/staricon.png");
    private static final Identifier CUSTOM_BARS = Identifier.of("champions", "textures/gui/custom_bars.png");

    /**
     * Отрисовка и рендер
     * Все по порядку от верха к низу
     */
    public static void renderChampion(DrawContext context, int centerX, int y, ChampionHudRender.ChampionData data, MinecraftClient client) {
        int color = data.color();

        renderStars(context, centerX, y - 5, data.tier(), color);

        MutableText title = Text.literal("Skilled ").append(data.name().copy());
        context.drawCenteredTextWithShadow(client.textRenderer, title, centerX, y + 7, color);

        renderProgressBar(context, centerX, y + 19, data.percent(), color);

        if (data.affixes() != null && !data.affixes().isEmpty()) {
            renderAffixes(context, client, centerX, y + 29, data.affixes());
        }
    }

    private static void renderStars(DrawContext context, int centerX, int y, int count, int color) {
        if (count <= 0) return;
        int sSize = 10;
        int totalW = (count * 11);
        int startX = centerX - (totalW / 2);

        applyColor(context, color);
        for (int i = 0; i < count; i++) {
            context.drawTexture(CHAMPION_STARS, startX + (i * 11), y, 0, 0, sSize, sSize, sSize, sSize);
        }
        resetColor(context);
    }

    private static void renderProgressBar(DrawContext context, int centerX, int y, float percent, int color) {
        int barW = 182;
        int barH = 5;
        int x = centerX - barW / 2;

        applyColor(context, color);
        context.drawTexture(CUSTOM_BARS, x, y, 0, 0, barW, barH, 182, 10);

        int fillW = (int) (percent * barW);
        if (fillW > 0) {
            context.drawTexture(CUSTOM_BARS, x, y, 0, 5, fillW, barH, 182, 10);
            resetColor(context);
        } else {
            resetColor(context);
        }
    }

    private static void renderAffixes(DrawContext context, MinecraftClient client, int centerX, int y, String raw) {
        String[] split = raw.split("•|·|\\s+|,");
        StringBuilder processed = new StringBuilder();
        for (String s : split) {
            String t = s.trim();
            if (t.isEmpty()) continue;
            MutableText translate = Text.translatable(t.startsWith("affix.") ? t : "affix." + t);
            if (!processed.isEmpty()) processed.append(" • ");
            processed.append(translate);
        }
        context.drawCenteredTextWithShadow(client.textRenderer, Text.literal(processed.toString()).formatted(Formatting.GRAY, Formatting.ITALIC), centerX, y, 0xFFFFFF);
    }
}
