package crystal.champions.mixin;

import crystal.champions.IChampions;
import crystal.champions.util.ChampionRank;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static crystal.champions.util.PrepareChampions.prepareAffixes;
import static crystal.champions.util.PrepareChampions.prepareAttributes;

@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonMixin extends LivingEntity implements IChampions {

    protected EnderDragonMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void applyChampionsLogic(EntityType entityType, World world, CallbackInfo ci) {

        MobEntity mob = (MobEntity) (Object) this;
        ChampionRank rank = ChampionRank.getRandomRank(mob.getRandom());
        if (rank.tier() > 0) {
            champions$setChampionTier(rank.tier());
            prepareAttributes(mob, rank);
            champions$setAffixesString(prepareAffixes(rank));
        }
    }
}