package crystal.champions.config;

import crystal.champions.util.SimpleConfig;

public class ChampionsConfig {
    private static ChampionsConfig INSTANCE;

    // Arctic
    public static int cooldownBeforeBulletArtic;
    public static float dampeningAmount;
    // Molten
    public static int cooldownBeforeBulletMolten;
    // Hasty
    public static byte hastyAmplifier;
    // Plagued
    public static int poisonDuration;
    public static byte poisonAmplifier;
    // Desecrating
    public static int timeBeforeDesecrating;
    public static int cloudDuration;
    // Knocking
    public static double knockback;
    // Infected
    public static int timeBeforeInfected;
    public static int maxSilverFishCount;
    public static short infectedSilverfish;
    public static float infectedFactorHealth;
    // Magnetic
    public static double strength;
    // Lively
    public static int entityHeal;
    public static int entityHealTime;
    // Reflective
    public static int reflectionDamage;
    // Shield
    public static int shieldAllTime;
    public static int shieldWork;

    public static int maxBossTier;

    // Использую SimpleConfig
    // https://github.com/magistermaks/fabric-simplelibs/blob/master/simple-config/SimpleConfig.java
    private ChampionsConfig() {
        SimpleConfig CONFIG = SimpleConfig.of("champions_common")
                .provider(this::defaultConfig)
                .version(1)
                .request();

        cooldownBeforeBulletArtic = CONFIG.getOrDefault("cooldown_arctic", 160);

        dampeningAmount = (float) CONFIG.getOrDefault("dampening_amount", 0.5);

        timeBeforeDesecrating = CONFIG.getOrDefault("time_before_desecrating", 1000);
        cloudDuration = CONFIG.getOrDefault("cloud_desecrating_duration", 200);

        hastyAmplifier = (byte) CONFIG.getOrDefault("hasty_amplifier", 4);

        timeBeforeInfected = CONFIG.getOrDefault("time_before_infecting", 300);
        maxSilverFishCount = CONFIG.getOrDefault("max_silverfish_count", 10);
        infectedSilverfish = (short) CONFIG.getOrDefault("infected_silverfish", 3);
        infectedFactorHealth = (float) CONFIG.getOrDefault("infected_factor_health", 0.1);

        knockback = (float) CONFIG.getOrDefault("knocking_strength", 1.5);

        entityHeal = CONFIG.getOrDefault("entity_heal", 2);
        entityHealTime = CONFIG.getOrDefault("when_entity_heal", 20);

        strength = CONFIG.getOrDefault("magnetic_strength", 1);

        cooldownBeforeBulletMolten = CONFIG.getOrDefault("cooldown_molten", 90);

        poisonDuration = CONFIG.getOrDefault("plagued_poison_duration", 200);
        poisonAmplifier = (byte) CONFIG.getOrDefault("plagued_poison_amplifier", 0);

        reflectionDamage = CONFIG.getOrDefault("reflection_damage", 2);

        shieldAllTime = CONFIG.getOrDefault("shield_all_time", 200);
        shieldWork = CONFIG.getOrDefault("shield_working_time", 100);

        maxBossTier = CONFIG.getOrDefault("max_boss_tier", 1);
    }

    private String defaultConfig(String filename) {
        return """
                # Champions Mod Configuration
                
                # Arctic
                # Cooldown between arctic bullets (ticks)
                cooldown_arctic = 160
                # Damage reduction multiplier (0.5 = 50% less damage taken)
                dampening_amount = 0.5
                
                # Molten
                # Cooldown between molten bullets (ticks)
                cooldown_molten = 90
                
                # Desecrating
                # Ticks between desecrating clouds
                time_before_desecrating = 1000
                # Duration of the cloud (ticks)
                cloud_desecrating_duration = 200
                
                # Hasty
                # Speed effect amplifier
                hasty_amplifier = 4
                
                # Infected
                # Ticks between spawning silverfish
                time_before_infecting = 300
                # Max silverfish that can exist around champion
                max_silverfish_count = 10
                # Base number of silverfish to spawn
                infected_silverfish = 3
                # Additional silverfish (health * factor)
                infected_factor_health = 0.1
                
                # Knocking
                # Strength of the knockback
                knocking_strength = 1.5
                
                # Healing
                # Amount of heal
                entity_heal = 2
                # Ticks between healing
                when_entity_heal = 20
                
                # Magnetic
                # Strength of the pull effect towards the champion
                magnetic_strength = 1.0
                
                # Plagued
                # Duration of poison effect (ticks)
                plagued_poison_duration = 200
                # Poison amplifier (0 = Level I)
                plagued_poison_amplifier = 0
                
                # Reflective
                # Damage reflected back to the attacker
                reflection_damage = 2
                
                # Shield
                # Cycle time of the shield (ticks)
                shield_all_time = 200
                # How long the shield stays active (ticks)
                shield_working_time = 100
                
                max_boss_tier = 1
                """;
    }

    public static ChampionsConfig get() {
        if (INSTANCE == null) {
            INSTANCE = new ChampionsConfig();
        }
        return INSTANCE;
    }
}
