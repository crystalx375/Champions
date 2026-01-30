package crystal.champions.affix;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.world.World;

import static crystal.champions.config.ChampionsConfig.*;

public class PlaguedAffix extends Affix {
    /**
     * Здесь мы на моба ставим сплэш зелье на отравление и все
     */
    public PlaguedAffix() {
        super("plagued");
    }

    @Override
    public void onTick(LivingEntity entity) {
        if (entity.age % 10 != 0) return;
        World world = entity.getWorld();
        AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(world, entity.getX(), entity.getY(), entity.getZ());
        cloud.setRadius(3.0f);
        cloud.setWaitTime(0);
        cloud.setDuration(10);
        StatusEffectInstance plagued = new StatusEffectInstance(StatusEffects.POISON, poisonDuration, plaguedAmplifier);
        cloud.addEffect(plagued);
        world.spawnEntity(cloud);
        entity.removeStatusEffect(StatusEffects.POISON);
    }
}
