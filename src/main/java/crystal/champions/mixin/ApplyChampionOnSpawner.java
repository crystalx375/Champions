package crystal.champions.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import crystal.champions.IChampions;
import crystal.champions.util.ChampionRank;
import net.minecraft.block.spawner.MobSpawnerLogic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static crystal.champions.util.PrepareChampions.*;

@Mixin(MobSpawnerLogic.class)
public class ApplyChampionOnSpawner {
    @WrapOperation(
            method = "serverTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;spawnNewEntityAndPassengers(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    private boolean initChampionsOrPass(ServerWorld instance, Entity entity, Operation<Boolean> original) {
        if (entity instanceof MobEntity mobEntity
                && mobEntity instanceof IChampions i
                && canBeChampion(mobEntity))
        {
            final ChampionRank rank = ChampionRank.getRandomRank(mobEntity.getRandom());
            if (rank.tier() > 0)
            {
                i.champions$setChampionTier(rank.tier());
                prepareAttributes(mobEntity, rank);
                i.champions$setAffixesString(prepareAffixes(rank));
            }
        }

        return original.call(instance, entity);
    }
}
