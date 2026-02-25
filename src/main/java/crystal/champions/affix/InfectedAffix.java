package crystal.champions.affix;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.List;

import static crystal.champions.config.ChampionsConfigAffixes.*;

/**
 * InfectedAffix
 * Создаем чешуйниц от хп моба
 * При большом кол-ве отменяем спавн
 */
public class InfectedAffix extends Affix {

    public InfectedAffix() {
        super("infected");
    }

    @Override
    public void onTick(LivingEntity entity) {
        if (entity.age % timeBeforeInfected != 0) return;
        ServerWorld world = (ServerWorld) entity.getWorld();
        List<SilverfishEntity> nearby = world.getEntitiesByClass(SilverfishEntity.class, entity.getBoundingBox().expand(40.0), e -> true);
        if (nearby.size() > maxSilverFishCount) return;

        BlockPos pos = entity.getBlockPos();
        int count = (int) (entity.getHealth() * infectedFactorHealth + infectedSilverfish);
        count = Math.min(count, maxSilverFishCount);
        for (int i = 0; i < count; i++) {
            SilverfishEntity silverfish = EntityType.SILVERFISH.create(world);
            if (silverfish != null) {
                silverfish.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.getRandom().nextFloat() * 360.0F, 0.0F);
                silverfish.initialize(world, world.getLocalDifficulty(pos), SpawnReason.EVENT, null, null);

                world.spawnEntity(silverfish);
            }
        }
    }
}
