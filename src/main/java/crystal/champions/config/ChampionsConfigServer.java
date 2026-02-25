package crystal.champions.config;

import crystal.champions.Champions;
import crystal.champions.util.SimpleConfig;

public class ChampionsConfigServer {
    private static final int version = 1;
    private static ChampionsConfigServer INSTANCE;
    static boolean first_tick = true;

    public static int a1, a2, a3, a4, a5;
    public static int w0, w1, w2, w3, w4, w5;
    public static float g1, g2, g3, g4, g5;


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

        w0 = CONFIG.getOrDefault("tier0_weight", 7500);
        w1 = CONFIG.getOrDefault("tier1_weight", 1250);
        w2 = CONFIG.getOrDefault("tier2_weight", 700);
        w3 = CONFIG.getOrDefault("tier3_weight", 450);
        w4 = CONFIG.getOrDefault("tier4_weight", 99);
        w5 = CONFIG.getOrDefault("tier5_weight", 1);

        g1 = (float) CONFIG.getOrDefault("tier1_growth", 1.5);
        g2 = (float) CONFIG.getOrDefault("tier2_growth", 3.0);
        g3 = (float) CONFIG.getOrDefault("tier3_growth", 6.0);
        g4 = (float) CONFIG.getOrDefault("tier4_growth", 12.0);
        g5 = (float) CONFIG.getOrDefault("tier5_growth", 30.0);

        maxBossTier = CONFIG.getOrDefault("max_boss_tier", 0);
    }

    private String defaultConfig(String filename) {
        return """
                # Champions Server
                
                # Tier 0 (not champion)
                tier0_weight = 7500
                
                # Tier 1
                tier1_affixes_count = 1
                tier1_weight = 1250
                tier1_growth = 1.5

                # Tier 2
                tier2_affixes_count = 2
                tier2_weight = 700
                tier2_growth = 3.0

                # Tier 3
                tier3_affixes_count = 3
                tier3_weight = 450
                tier3_growth = 6.0

                # Tier 4
                tier4_affixes_count = 4
                tier4_weight = 99
                tier4_growth = 12.0

                # Tier 5
                tier5_affixes_count = 8
                tier5_weight = 1
                tier5_growth = 30.0
                
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
