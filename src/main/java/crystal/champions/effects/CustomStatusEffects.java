package crystal.champions.effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CustomStatusEffects {
    public static final StatusEffect STUN = Registry.register(
            Registries.STATUS_EFFECT,
            new Identifier("champions", "stun"),
            new STUN()
    );

    public static void register() {
    }
}
