package crystal.champions;

import crystal.champions.client.mixin.BossBarsAccessor;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChampionHudRender implements HudRenderCallback {
    private static final Identifier CHAMPION_STARS = Identifier.of("champions", "textures/gui/staricon.png");
    private static final Identifier CUSTOM_BARS = Identifier.of("champions", "textures/gui/custom_bars.png");

    private static final Map<UUID, String> NAME_CACHE = new HashMap<>();

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden) return;

        BossBarsAccessor accessor = (BossBarsAccessor) client.inGameHud.getBossBarHud();
        Map<UUID, ClientBossBar> bossBars = accessor.getBossBars();

        if (bossBars == null || bossBars.isEmpty()) {
            NAME_CACHE.clear();
            return;
        }

        int width = client.getWindow().getScaledWidth();
        int centerX = width / 2;
        int currentY = 10;

        for (Map.Entry<UUID, ClientBossBar> entry : bossBars.entrySet()) {
            ClientBossBar bar = entry.getValue();
            UUID uuid = entry.getKey();
            String rawName = bar.getName().getString();

            // Используем "метку": если это наш кастомный боссбар чемпионов
            if (isChampionBar(bar)) {
                NAME_CACHE.put(uuid, rawName);
                renderChampionInterface(context, centerX, currentY, bar, client, rawName);

                // Если это основной бар (с ХП), увеличиваем Y. Если это бар аффиксов, не увеличиваем,
                // так как мы рисуем всё в одном блоке.
                if (bar.getStyle() == BossBar.Style.PROGRESS) {
                    currentY += 45;
                }
            }
        }
        NAME_CACHE.keySet().removeIf(id -> !bossBars.containsKey(id));
    }

    /**
     * Метка: проверяем, является ли боссбар частью системы чемпионов.
     * На сервере в MobEntityMixin мы задаем PROGRESS для имен и NOTCHED_20 для аффиксов.
     */
    private boolean isChampionBar(BossBar bar) {
        String name = bar.getName().getString();
        // Проверка по стилю (NOTCHED_20 используется нами для аффиксов)
        // или по спец-символам в имени.
        return bar.getStyle() == BossBar.Style.NOTCHED_20 ||
                name.contains("★") ||
                name.contains("Skilled") ||
                name.contains("affix.");
    }

    private void renderChampionInterface(DrawContext context, int centerX, int y, BossBar bar, MinecraftClient client, String originalName) {
        // Мы рендерим интерфейс только для основного бара (PROGRESS),
        // а данные берем из обоих, если нужно. Но так как у нас два бара на одного моба,
        // логика отрисовки должна срабатывать один раз.
        if (bar.getStyle() == BossBar.Style.NOTCHED_20) return;

        int starCount = 0;
        for (char c : originalName.toCharArray()) {
            if (c == '★' || c == '*') starCount++;
        }

        String cleanName = originalName
                .replaceAll("[★*]", "")
                .replace("Skilled", "")
                .replaceAll("affix\\.[a-z.]+", "")
                .replace("•", "")
                .trim();

        if (cleanName.isEmpty()) cleanName = "Champion";

        renderStars(context, centerX, y, starCount, bar.getColor());

        Formatting tierColor = getTierColor(bar.getColor());
        MutableText title = Text.literal("Skilled ").formatted(tierColor)
                .append(Text.literal(cleanName).formatted(Formatting.WHITE));

        context.drawCenteredTextWithShadow(client.textRenderer, title, centerX, y + 12, 0xFFFFFF);

        renderCustomProgressBar(context, centerX, y + 24, bar);

        // Ищем бар аффиксов для этого же моба (обычно он идет следующим или имеет тот же контент)
        renderAffixes(context, client, centerX, y + 34, originalName);
    }

    private void renderStars(DrawContext context, int centerX, int y, int count, BossBar.Color color) {
        if (count <= 0) return;
        int sSize = 10;
        int totalW = (count * sSize) + ((count - 1) * 2);
        int startX = centerX - (totalW / 2);

        applyHexColor(context, getHexColor(color));
        for (int i = 0; i < count; i++) {
            context.drawTexture(CHAMPION_STARS, startX + (i * 12), y, 0, 0, sSize, sSize, 10, 10);
        }
        context.setShaderColor(1f, 1f, 1f, 1f);
    }

    private void renderCustomProgressBar(DrawContext context, int centerX, int y, BossBar bar) {
        int barW = 182;
        int barH = 5;
        int x = centerX - barW / 2;

        context.setShaderColor(0.1f, 0.1f, 0.1f, 0.7f);
        context.drawTexture(CUSTOM_BARS, x, y, 0, 0, barW, barH, 256, 256);

        int fillW = (int) (bar.getPercent() * barW);
        if (fillW > 0) {
            applyHexColor(context, getHexColor(bar.getColor()));
            context.drawTexture(CUSTOM_BARS, x, y, 0, 0, fillW, barH, 256, 256);
        }
        context.setShaderColor(1f, 1f, 1f, 1f);
    }

    private void renderAffixes(DrawContext context, MinecraftClient client, int centerX, int y, String raw) {
        StringBuilder sb = new StringBuilder();
        String[] parts = raw.split("[\\s•]+");
        for (String p : parts) {
            if (p.startsWith("affix.")) {
                String name = p.substring(p.lastIndexOf('.') + 1);
                if (name.equalsIgnoreCase("skilled")) continue;

                name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                if (sb.length() > 0) sb.append(" • ");
                sb.append(name);
            }
        }

        if (sb.length() > 0) {
            context.drawCenteredTextWithShadow(client.textRenderer,
                    Text.literal(sb.toString()).formatted(Formatting.AQUA, Formatting.ITALIC),
                    centerX, y, 0xFFFFFF);
        }
    }

    private void applyHexColor(DrawContext context, int hex) {
        float r = (float)(hex >> 16 & 255) / 255f;
        float g = (float)(hex >> 8 & 255) / 255f;
        float b = (float)(hex & 255) / 255f;
        context.setShaderColor(r, g, b, 1f);
    }

    private int getHexColor(BossBar.Color color) {
        return switch (color) {
            case RED -> 0xFF4444;
            case YELLOW -> 0xFFDD00;
            case PURPLE -> 0xCC44FF;
            case GREEN -> 0x44FF44;
            case BLUE -> 0x44AAFF;
            default -> 0xFFFFFF;
        };
    }

    private Formatting getTierColor(BossBar.Color color) {
        return switch (color) {
            case RED -> Formatting.RED;
            case YELLOW -> Formatting.GOLD;
            case PURPLE -> Formatting.DARK_PURPLE;
            case GREEN -> Formatting.GREEN;
            case BLUE -> Formatting.BLUE;
            default -> Formatting.WHITE;
        };
    }
}