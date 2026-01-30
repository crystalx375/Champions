package crystal.champions.affix;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import static crystal.champions.config.ChampionsConfig.hastySpeedAmplifier;

public class HastyAffix extends Affix {

    public HastyAffix() {
        super("hasty");
    }

    @Override
    public void onTick(LivingEntity entity) {
        if (entity.age % 20 == 0) {
            entity.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SPEED, 20, hastySpeedAmplifier, true, false, false
            ));
        }
    }
}
