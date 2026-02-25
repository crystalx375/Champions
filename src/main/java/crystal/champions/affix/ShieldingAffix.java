package crystal.champions.affix;

import crystal.champions.Interface.IChampions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;

import static crystal.champions.config.ChampionsConfigAffixes.shieldAllTime;
import static crystal.champions.config.ChampionsConfigAffixes.shieldWork;

/**
 * ShieldingAffix
 * В миксине отменяем урон раз в некоторое кол-во времени
 */
public class ShieldingAffix extends Affix {

    public ShieldingAffix() {
        super("shielding");
    }

    @Override
    public void onTick(LivingEntity entity) {
        if (entity.getWorld().isClient) return;

        IChampions champion = (IChampions) entity;
        long time = entity.getWorld().getTime();

        boolean Shield = (time % shieldAllTime) < shieldWork;

        if (champion.champions$isShielding() != Shield) {
            champion.champions$setShielding(Shield);
        }
        if (Shield) {
            ((ServerWorld) entity.getWorld()).spawnParticles(
                    ParticleTypes.ENTITY_EFFECT,
                    entity.getX(), entity.getY() + 1, entity.getZ(),
                    0, 1.0, 1.0, 1.0, 0.8
            );
        }
    }
}
