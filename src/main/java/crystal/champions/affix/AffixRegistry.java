package crystal.champions.affix;

import crystal.champions.Champions;
import crystal.champions.config.ChampionsConfigAffixes;

import java.util.HashMap;
import java.util.Map;

/**
 * Регистрация всех аффиксов, чтобы не по отдельности их делать
 */
public class AffixRegistry {

    public static final Map<String, Affix> ALL_AFFIXES = new HashMap<>();

    public static void init() {
        if (ChampionsConfigAffixes.r1) register(new HastyAffix());
        if (ChampionsConfigAffixes.r2) register(new ArcticAffix());
        if (ChampionsConfigAffixes.r3) register(new MoltenAffix());
        if (ChampionsConfigAffixes.r4) register(new DesecratingAffix());
        if (ChampionsConfigAffixes.r5) register(new PlaguedAffix());
        if (ChampionsConfigAffixes.r6) register(new InfectedAffix());
        if (ChampionsConfigAffixes.r7) register(new AdaptiveAffix());
        if (ChampionsConfigAffixes.r8) register(new KnockingAffix());
        if (ChampionsConfigAffixes.r9) register(new ShieldingAffix());
        if (ChampionsConfigAffixes.r10) register(new ReflectionAffix());
        if (ChampionsConfigAffixes.r11) register(new MagneticAffix());
        if (ChampionsConfigAffixes.r12) register(new DampingAffix());
        if (ChampionsConfigAffixes.r13) register(new LivelyAffix());

        Champions.LOGGER.info("Registered {} champion affixes", ALL_AFFIXES.size());
    }

    private static void register(Affix affix) {
        ALL_AFFIXES.put(affix.getName(), affix);
    }
}