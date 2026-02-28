package crystal.champions.config;

import crystal.champions.Champions;
import crystal.champions.util.SimpleConfig;

public class ChampionsConfigServer {
    private static final int version = 1;
    private static ChampionsConfigServer INSTANCE;
    static boolean first_tick = true;

    public static int a1, a2, a3, a4, a5;
    public static int w0, w1, w2, w3, w4, w5;
    public static float gh1, gh2, gh3, gh4, gh5;
    public static float gs1, gs2, gs3, gs4, gs5;

    public static int maxBossTier;

    private ChampionsConfigServer() {
        SimpleConfig CONFIG = SimpleConfig.of("Champions", "champions_common")
                .provider(this::defaultConfig)
                .version(version)
                .request();

        a1 = CONFIG.getOrDefault("tier1_affixes_count", 1);
        a2 = CONFIG.getOrDefault("tier2_affixes_count", 2);
        a3 = CONFIG.getOrDefault("tier3_affixes_count", 3);
        a4 = CONFIG.getOrDefault("tier4_affixes_count", 4);
        a5 = CONFIG.getOrDefault("tier5_affixes_count", 8);

        w0 = CONFIG.getOrDefault("tier0_weight", 9320);
        w1 = CONFIG.getOrDefault("tier1_weight", 500);
        w2 = CONFIG.getOrDefault("tier2_weight", 150);
        w3 = CONFIG.getOrDefault("tier3_weight", 26);
        w4 = CONFIG.getOrDefault("tier4_weight", 3);
        w5 = CONFIG.getOrDefault("tier5_weight", 1);

        gh1 = (float) CONFIG.getOrDefault("tier1_growth_health", 1.5);
        gh2 = (float) CONFIG.getOrDefault("tier2_growth_health", 3.0);
        gh3 = (float) CONFIG.getOrDefault("tier3_growth_health", 5.0);
        gh4 = (float) CONFIG.getOrDefault("tier4_growth_health", 12.0);
        gh5 = (float) CONFIG.getOrDefault("tier5_growth_health", 30.0);

        gs1 = (float) CONFIG.getOrDefault("tier1_growth_strength", 1.5);
        gs2 = (float) CONFIG.getOrDefault("tier2_growth_strength", 2.0);
        gs3 = (float) CONFIG.getOrDefault("tier3_growth_strength", 4.0);
        gs4 = (float) CONFIG.getOrDefault("tier4_growth_strength", 7.0);
        gs5 = (float) CONFIG.getOrDefault("tier5_growth_strength", 10.0);

        maxBossTier = CONFIG.getOrDefault("max_boss_tier", 0);
    }

    private String defaultConfig(String filename) {
        return """
                # Champions Server
                
                # Tier 0 (not champion)
                tier0_weight = 9320
                
                # Tier 1
                tier1_affixes_count = 1
                tier1_weight = 500
                tier1_growth_health = 1.5
                tier1_growth_strength = 1.5

                # Tier 2
                tier2_affixes_count = 2
                tier2_weight = 150
                tier2_growth_health = 3.0
                tier2_growth_strength = 2.0

                # Tier 3
                tier3_affixes_count = 3
                tier3_weight = 30
                tier3_growth_health = 5.0
                tier3_growth_strength = 4.0

                # Tier 4
                tier4_affixes_count = 4
                tier4_weight = 3
                tier4_growth_health = 12.0
                tier4_growth_strength = 7.0

                # Tier 5
                tier5_affixes_count = 8
                tier5_weight = 1
                tier5_growth_health = 30.0
                tier5_growth_strength = 10.0
                
                # Maximum tier for bosses (Wither, Ender Dragon)
                max_boss_tier = 0
                """;
    }

    public static ChampionsConfigServer get() {
        if (first_tick) {
            Champions.LOGGER.info("Registering champions_common");
            first_tick = false;
        }
        if (INSTANCE == null) {
            INSTANCE = new ChampionsConfigServer();
        }
        return INSTANCE;
    }
}
