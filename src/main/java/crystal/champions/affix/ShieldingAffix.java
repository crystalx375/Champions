package crystal.champions.affix;

import crystal.champions.IChampions;
import crystal.champions.config.ChampionsConfigAffixes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;

/**
 * ShieldingAffix
 * В миксине отменяем урон раз в некоторое кол-во времени
 */
public class ShieldingAffix extends Affix {

    public ShieldingAffix() {
        super("shielding");
    }

    ChampionsConfigAffixes config = ChampionsConfigAffixes.get();

    @Override
    public void onTick(LivingEntity entity) {
        if (entity.getWorld().isClient) return;

        IChampions champion = (IChampions) entity;
        long time = entity.getWorld().getTime();

        boolean shieldWork = (time % config.shieldAllTime) < config.shieldWork;

        if (champion.champions$isShielding() != shieldWork) {
            champion.champions$setShielding(shieldWork);
        }
        if (shieldWork) {
            ((ServerWorld) entity.getWorld()).spawnParticles(
                    ParticleTypes.EFFECT,
                    entity.getX(), entity.getRandomBodyY(), entity.getZ(),
                    0, 1.0, 1.0, 1.0, 0.8
            );
        }
    }
}
