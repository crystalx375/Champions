package crystal.champions.mixin;

import crystal.champions.IChampions;
import crystal.champions.util.ChampionRank;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static crystal.champions.util.PrepareChampions.*;

@Mixin(MobEntity.class)
public class ApplyChampionOnSpawn {
    @Inject(method = "initialize", at = @At("TAIL"))
    private void initChampionsOrPass(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
        final MobEntity mobEntity = (MobEntity) (Object) this;
        if (spawnReason != SpawnReason.TRIAL_SPAWNER
                && spawnReason != SpawnReason.SPAWNER
                && canBeChampion(mobEntity)
                && mobEntity instanceof IChampions i)
        {
            final ChampionRank rank = ChampionRank.getRandomRank(mobEntity.getRandom());
            if (rank.tier() > 0)
            {
                i.champions$setChampionTier(rank.tier());
                prepareAttributes(mobEntity, rank);
                i.champions$setAffixesString(prepareAffixes(rank));
            }
        }
    }
}