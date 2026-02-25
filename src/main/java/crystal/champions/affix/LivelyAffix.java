package crystal.champions.affix;

import net.minecraft.entity.LivingEntity;

import static crystal.champions.config.ChampionsConfigAffixes.entityHeal;
import static crystal.champions.config.ChampionsConfigAffixes.entityHealTime;

/**
 * LivelyAffix
 * Просто реген моба
 */
public class LivelyAffix extends Affix {

    public LivelyAffix() {
        super("lively");
    }

    @Override
    public void onTick(LivingEntity entity) {
        if (entity.age % entityHealTime != 0) return;
        entity.heal(entityHeal);
    }
}
