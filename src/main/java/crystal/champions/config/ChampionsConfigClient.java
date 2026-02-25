package crystal.champions.config;

import crystal.champions.Champions;
import crystal.champions.util.SimpleConfig;

public class ChampionsConfigClient {
    private static final int version = 1;
    private static ChampionsConfigClient INSTANCE;
    static boolean first_tick = true;

    public static int yOffsetStars;
    public static int yOffsetText;
    public static int yOffsetBar;
    public static int yOffsetAffixes;

    public static int xOffsetStars;
    public static int xOffsetText;
    public static int xOffsetBar;
    public static int xOffsetAffixes;

    public static short cache_client;
    public static short cache_server;
    public static boolean only_for_view;

    public String hex_tier_1;
    public String hex_tier_2;
    public String hex_tier_3;
    public String hex_tier_4;
    public String hex_tier_5;


    private ChampionsConfigClient() {
        SimpleConfig CONFIG = SimpleConfig.of("Champions","champions_client")
                .provider(this::defaultConfig)
                .version(version)
                .request();

        yOffsetStars = CONFIG.getOrDefault("y_offset_stars", -5);
        yOffsetText = CONFIG.getOrDefault("y_offset_text", 7);
        yOffsetBar = CONFIG.getOrDefault("y_offset_bar", 19);
        yOffsetAffixes = CONFIG.getOrDefault("y_offset_affixes", 29);

        xOffsetStars = CONFIG.getOrDefault("x_offset_stars", 0);
        xOffsetText = CONFIG.getOrDefault("x_offset_text", 0);
        xOffsetBar = CONFIG.getOrDefault("x_offset_bar", 0);
        xOffsetAffixes = CONFIG.getOrDefault("x_offset_affixes", 0);

        this.hex_tier_1 = CONFIG.getOrDefault("hex_tier_1", "#FFFF55");
        this.hex_tier_2 = CONFIG.getOrDefault("hex_tier_2", "#F57C2C");
        this.hex_tier_3 = CONFIG.getOrDefault("hex_tier_3", "#46DFFA");
        this.hex_tier_4 = CONFIG.getOrDefault("hex_tier_4", "#8823DB");
        this.hex_tier_5 = CONFIG.getOrDefault("hex_tier_5", "#F98AFF");


        cache_client = (short) CONFIG.getOrDefault("cache_client", 1000);
        cache_server = (short) CONFIG.getOrDefault("cache_server", 5000);
        only_for_view = CONFIG.getOrDefault("only_for_view", false);
    }

    private String defaultConfig(String filename) {
        return """
                # Champions Client
                
                # Champions color (Hex)
                hex_tier_1 = #FFFF55
                hex_tier_2 = #F57C2C
                hex_tier_3 = #46DFFA
                hex_tier_4 = #8823DB
                hex_tier_5 = #F98AFF
                
                # Champions HUD position
                y_offset_stars = -5
                y_offset_text = 7
                y_offset_bar = 19
                y_offset_affixes = 29
                
                x_offset_stars = 0
                x_offset_text = 0
                x_offset_bar = 0
                x_offset_affixes = 0
                
                # Time cache for champions (millis) (max = 32767)
                cache_client = 1000
                cache_server = 5000
                # If true, it will be render only when looking at the champion
                only_for_view = false
                """;
    }

    public static ChampionsConfigClient get() {
        if (first_tick) {
            Champions.LOGGER.info("Registering champions_client");
            first_tick = false;
        }
        if (INSTANCE == null) {
            INSTANCE = new ChampionsConfigClient();
        }
        return INSTANCE;
    }
}
