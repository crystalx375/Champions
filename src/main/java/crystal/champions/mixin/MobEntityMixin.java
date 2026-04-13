package crystal.champions.mixin;

import crystal.champions.IChampions;
import crystal.champions.util.ChampionRank;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static crystal.champions.util.PrepareChampions.prepareAffixes;
import static crystal.champions.util.PrepareChampions.prepareAttributes;


@Mixin(MobEntity.class)
public abstract class MobEntityMixin implements IChampions {

    @Inject(method = "initialize", at = @At("TAIL"))
    private void applyChampionStats(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
        MobEntity mob = (MobEntity) (Object) this;

        final boolean isAggressive = mob instanceof HostileEntity || mob instanceof Angerable
                || mob instanceof CaveSpiderEntity || mob instanceof GhastEntity
                || mob instanceof PhantomEntity || mob instanceof ShulkerEntity
                || mob instanceof SilverfishEntity || mob instanceof SlimeEntity
                || mob instanceof EnderDragonEntity || mob instanceof WitherEntity;

        final boolean notAggressive = mob instanceof IronGolemEntity
                || mob instanceof PolarBearEntity || mob instanceof WolfEntity
                || mob instanceof EnderDragonEntity || mob instanceof WitherEntity;

        if (!isAggressive || notAggressive) return;

        ChampionRank rank = ChampionRank.getRandomRank(mob.getRandom());
        if (rank.tier() > 0) {
            champions$setChampionTier(rank.tier());
            prepareAttributes(mob, rank);
            champions$setAffixesString(prepareAffixes(rank));
        }
    }
}