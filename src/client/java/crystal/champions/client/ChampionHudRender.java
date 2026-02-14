package crystal.champions.client;

import crystal.champions.client.net.ChampionDisplayInfo;
import crystal.champions.client.net.ClientPacket;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.UUID;

public class ChampionHudRender implements HudRenderCallback {
    private static final Identifier CHAMPION_STARS = Identifier.of("champions", "textures/gui/staricon.png");
    private static final Identifier CUSTOM_BARS = Identifier.of("champions", "textures/gui/custom_bars.png");

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden) return;

        int centerX = client.getWindow().getScaledWidth() / 2;
        int currentY = 12;

        // Ищем самого сильного чемпиона среди всех доступных источников
        ChampionData bestChampion = findBestChampion();

        if (bestChampion != null) {
            renderChampion(context, centerX, currentY, bestChampion, client);
        }
    }

    private ChampionData findBestChampion() {
        ChampionData best = null;
        long now = System.currentTimeMillis();

        for (Map.Entry<UUID, ChampionDisplayInfo> entry : ClientPacket.activeChampions.entrySet()) {
            ChampionDisplayInfo info = entry.getValue();
            if (now - info.lastUpdate() > 100) {
                ClientPacket.activeChampions.remove(entry.getKey());
                continue;
            }
            if (best == null || info.tier() > best.tier) {
                int color = getColor(info.tier());
                best = new ChampionData(info.name(), info.tier(), info.affixes(), info.health() / info.maxHealth(), color);
            }
        }
        return best;
    }

    private void renderChampion(DrawContext context, int centerX, int y, ChampionData data, MinecraftClient client) {
        int color = data.color;

        renderStars(context, centerX, y - 5, data.tier, color);

        MutableText title = Text.literal("Skilled ").append(data.name.copy());
        context.drawCenteredTextWithShadow(client.textRenderer, title, centerX, y + 7, color);

        renderProgressBar(context, centerX, y + 19, data.percent, color);

        if (data.affixes != null && !data.affixes.isEmpty()) {
            renderAffixes(context, client, centerX, y + 29, data.affixes);
        }
    }

    private void renderStars(DrawContext context, int centerX, int y, int count, int color) {
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

    private void renderProgressBar(DrawContext context, int centerX, int y, float percent, int color) {
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

    private void applyColor(DrawContext context, int hex) {
        float r = (float) (hex >> 16 & 255) / 255.0F;
        float g = (float) (hex >> 8 & 255) / 255.0F;
        float b = (float) (hex & 255) / 255.0F;
        context.setShaderColor(r, g, b, 1.0F);
    }

    private void resetColor(DrawContext context) {
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderAffixes(DrawContext context, MinecraftClient client, int centerX, int y, String raw) {
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
    private int getColor(int tier) {
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
    private record ChampionData(
            Text name,
            int tier,
            String affixes,
            float percent,
            int color
    ) {}
}