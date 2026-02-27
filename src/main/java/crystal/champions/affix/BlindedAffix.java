package crystal.champions.affix;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import java.util.Random;

import static crystal.champions.config.ChampionsConfigAffixes.blindChance;
import static crystal.champions.config.ChampionsConfigAffixes.blindDuration;

public class BlindedAffix extends Affix {

    public BlindedAffix() {
        super("blinded");
    }

    @Override
    public void onHurt(LivingEntity champion, LivingEntity target) {
        Random rnd = new Random();
        if (rnd.nextFloat() < blindChance) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, blindDuration, 0, false, true, true));
        }
    }
}
