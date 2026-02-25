package crystal.champions.config;

import crystal.champions.Champions;
import crystal.champions.util.SimpleConfig;

public class ChampionsConfigAffixes {
    private static final int version = 1;
    private static ChampionsConfigAffixes INSTANCE;
    static boolean first_tick = true;

    public static int cooldownBeforeBulletArtic;
    public static float dampeningAmount;
    public static int cooldownBeforeBulletMolten;
    public static byte hastyAmplifier;
    public static int poisonDuration;
    public static byte poisonAmplifier;
    public static int timeBeforeDesecrating;
    public static int cloudDuration;
    public static double knockback;
    public static int timeBeforeInfected;
    public static int maxSilverFishCount;
    public static short infectedSilverfish;
    public static float infectedFactorHealth;
    public static double strength;
    public static int entityHeal;
    public static int entityHealTime;
    public static int reflectionDamage;
    public static int shieldAllTime;
    public static int shieldWork;

    public static boolean r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13;

    // Использую SimpleConfig
    // https://github.com/magistermaks/fabric-simplelibs/blob/master/simple-config/SimpleConfig.java
    private ChampionsConfigAffixes() {
        SimpleConfig CONFIG = SimpleConfig.of("Champions", "champions_affixes")
                .provider(this::defaultConfig)
                .version(version)
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

        r1 = CONFIG.getOrDefault("hasty_affix", true);
        r2 = CONFIG.getOrDefault("arctic_affix", true);
        r3 = CONFIG.getOrDefault("molten_affix", true);
        r4 = CONFIG.getOrDefault("desecrating_affix", true);
        r5 = CONFIG.getOrDefault("plagued_affix", true);
        r6 = CONFIG.getOrDefault("infected_affix", true);
        r7 = CONFIG.getOrDefault("adaptive_affix", true);
        r8 = CONFIG.getOrDefault("knocking_affix", true);
        r9 = CONFIG.getOrDefault("shielding_affix", true);
        r10 = CONFIG.getOrDefault("reflective_affix", true);
        r11 = CONFIG.getOrDefault("magnetic_affix", true);
        r12 = CONFIG.getOrDefault("dampening_affix", true);
        r13 = CONFIG.getOrDefault("lively_affix", true);
    }

    private String defaultConfig(String filename) {
        return """
                # Champions Affixes
                
                # Registry
                # If true, the affix will be in the pool
                hasty_affix = true
                arctic_affix = true
                molten_affix = true
                desecrating_affix = true
                plagued_affix = true
                infected_affix = true
                adaptive_affix = true
                knocking_affix = true
                shielding_affix = true
                reflective_affix = true
                magnetic_affix = true
                dampening_affix = true
                lively_affix = true
                
                # Arctic
                # Cooldown between arctic bullets (ticks)
                cooldown_arctic = 160
                # Molten
                # Cooldown between molten bullets (ticks)
                cooldown_molten = 90
                
                # Desecrating
                # Ticks between desecrating clouds
                time_before_desecrating = 1000
                # Duration of the cloud (ticks)
                cloud_desecrating_duration = 200
                
                # Plagued
                # Duration of poison effect (ticks)
                plagued_poison_duration = 200
                # Poison amplifier
                plagued_poison_amplifier = 0
                
                # Infected
                # Ticks between spawning silverfish
                time_before_infecting = 300
                # Max silverfish that can exist around champion
                max_silverfish_count = 10
                # Base number of silverfish to spawn
                infected_silverfish = 3
                # Additional silverfish (health * factor)
                infected_factor_health = 0.1
                
                # Reflective
                # Damage reflected back to the attacker
                reflection_damage = 2
                
                # Healing
                # Amount of heal
                entity_heal = 2
                # Ticks between healing
                when_entity_heal = 20
                
                # Knocking
                knocking_strength = 1.5
                
                # Hasty
                # Speed effect amplifier
                hasty_amplifier = 4
                
                # Damage reduction multiplier (0.5 = 50% less damage taken)
                dampening_amount = 0.5
                
                # Shield
                # Cycle time of the shield (ticks)
                shield_all_time = 200
                # How long the shield stays active (ticks)
                shield_working_time = 100
                
                """;
    }

    public static ChampionsConfigAffixes get() {
        if (first_tick) {
            Champions.LOGGER.info("Registering champions_affixes");
            first_tick = false;
        }
        if (INSTANCE == null) {
            INSTANCE = new ChampionsConfigAffixes();
        }
        return INSTANCE;
    }
}
