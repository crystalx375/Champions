package crystal.champions.color;

import crystal.champions.config.ChampionsConfigClient;

public class ChampionsColorServer {
    static ChampionsConfigClient cfg = ChampionsConfigClient.get();
    private static int parseHex(String hex) {
        try {
            return Integer.decode(hex);
        } catch (NumberFormatException e) {
            return 0xFFFFFF;
        }
    }

    public static int getColor(int tier) {
        try {
            return switch (tier) {
                case 1 -> parseHex(cfg.hex_tier_1);
                case 2 -> parseHex(cfg.hex_tier_2);
                case 3 -> parseHex(cfg.hex_tier_3);
                case 4 -> parseHex(cfg.hex_tier_4);
                default -> parseHex(cfg.hex_tier_5);
            };
        } catch (Exception e) {
            return 0xffffff;
        }
    }
}
