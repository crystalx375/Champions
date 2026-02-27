package crystal.champions.affix;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;

import static crystal.champions.config.ChampionsConfigAffixes.*;

/**
 * LivelyAffix
 * Просто реген моба
 */
public class LivelyAffix extends Affix {

    public LivelyAffix() {
        super("lively");
    }

    @Override
    public void onAttack(LivingEntity entity, MobEntity mob) {
        if (entity.age % entityHealTime != 0) return;
        LivingEntity target = mob.getTarget();
        if (target == null) {
            mob.heal(entityHealNoTarget);
        } else {
            mob.heal(entityHeal);
        }
    }
}
