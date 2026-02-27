package crystal.champions.affix;

import crystal.champions.effects.CustomStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.Random;

import static crystal.champions.config.ChampionsConfigAffixes.*;

public class ParalyzingAffix extends Affix{

    public ParalyzingAffix() {
        super("paralyzing");
    }

    @Override
    public void onHurt(LivingEntity champion, LivingEntity target) {
        Random rnd = new Random();
        if (rnd.nextFloat() < paralyzeChance) {
            target.addStatusEffect(new StatusEffectInstance(CustomStatusEffects.STUN, paralyzeDuration, 0));
        }
    }
}
