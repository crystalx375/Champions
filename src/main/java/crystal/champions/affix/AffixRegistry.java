package crystal.champions.affix;

import java.util.HashMap;
import java.util.Map;

/**
 * Регистрация всех аффиксов, чтобы не по отдельности их делать
 */
public class AffixRegistry {

    public static final Map<String, Affix> ALL_AFFIXES = new HashMap<>();

    private static final Affix HASTY = register(new HastyAffix());
    private static final Affix ARCTIC = register(new ArcticAffix());
    private static final Affix MOLTEN = register(new MoltenAffix());
    private static final Affix DESECRATING = register(new DesecratingAffix());
    private static final Affix PLAGUED = register(new PlaguedAffix());
    private static final Affix INFECTED = register(new InfectedAffix());
    private static final Affix ADAPTIVE = register(new AdaptiveAffix());
    private static final Affix KNOCKING = register(new KnockingAffix());
    private static final Affix SHIELDING = register(new ShieldingAffix());
    private static final Affix REFLECTION = register(new ReflectionAffix());
    private static final Affix MAGNETIC = register(new MagneticAffix());
    private static final Affix DAMPENING = register(new DampingAffix());
    private static final Affix LIVELY = register(new LivelyAffix());

    private static Affix register(Affix affix) {
        ALL_AFFIXES.put(affix.getName(), affix);
        return affix;
    }
}