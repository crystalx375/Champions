package crystal.champions.affix;

import crystal.champions.Champions;
import crystal.champions.config.ChampionsConfigAffixes;

import java.util.HashMap;
import java.util.Map;

/**
 * Регистрация всех аффиксов, чтобы не по отдельности их делать
 */
public class AffixRegistry {
    private AffixRegistry() {
        /* This utility class should not be instantiated */
    }
    public static final Map<String, Affix> ALL_AFFIXES = new HashMap<>();
    public static void affixesRegister() {
        ChampionsConfigAffixes config = ChampionsConfigAffixes.get();

        if (config.r1) register(new HastyAffix());
        if (config.r2) register(new ArcticAffix());
        if (config.r3) register(new MoltenAffix());
        if (config.r4) register(new DesecratingAffix());
        if (config.r5) register(new PlaguedAffix());
        if (config.r6) register(new InfectedAffix());
        if (config.r7) register(new AdaptiveAffix());
        if (config.r8) register(new KnockingAffix());
        if (config.r9) register(new ShieldingAffix());
        if (config.r10) register(new ReflectionAffix());
        if (config.r11) register(new MagneticAffix());
        if (config.r12) register(new DampingAffix());
        if (config.r13) register(new LivelyAffix());
        if (config.r14) register(new BlindedAffix());
        if (config.r15) register(new ParalyzingAffix());

        Champions.LOGGER.info("Registered {} champion affixes", ALL_AFFIXES.size());
    }

    private static void register(Affix affix) {
        ALL_AFFIXES.put(affix.getName(), affix);
    }
}