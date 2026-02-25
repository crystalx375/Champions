package crystal.champions.mixin;

import crystal.champions.Interface.IChampions;
import crystal.champions.affix.AffixRegistry;
import crystal.champions.rank.ChampionRank;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Mixin(MobEntity.class)
public abstract class MobEntityMixin implements IChampions {

    @Inject(method = "initialize", at = @At("TAIL"))
    private void applyChampionStats(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                                    EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        MobEntity mob = (MobEntity) (Object) this;

        boolean isAggressive = mob instanceof HostileEntity || mob instanceof Angerable
                || mob instanceof CaveSpiderEntity || mob instanceof GhastEntity
                || mob instanceof PhantomEntity || mob instanceof ShulkerEntity
                || mob instanceof SilverfishEntity || mob instanceof SlimeEntity
                || mob instanceof EnderDragonEntity || mob instanceof WitherEntity;

        boolean notAggressive = mob instanceof IronGolemEntity
                || mob instanceof PolarBearEntity || mob instanceof WolfEntity
                || mob instanceof EnderDragonEntity || mob instanceof WitherEntity;

        if (!isAggressive || notAggressive) return;

        ChampionRank rank = ChampionRank.getRandomRank(mob.getRandom());
        if (rank.tier() > 0) {
            champions$setChampionTier(rank.tier());
            prepareAttributes(mob, rank);
            prepareAffixes(rank);
        }
    }


    @Unique
    private void prepareAttributes(MobEntity mob, ChampionRank rank) {
        float mult = rank.growth();
        modifyAttribute(mob, EntityAttributes.GENERIC_MAX_HEALTH, mult);
        mob.setHealth(mob.getMaxHealth());

        if (mob.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE) != null) {
            modifyAttribute(mob, EntityAttributes.GENERIC_ATTACK_DAMAGE, mult);
        }
    }

    @Unique
    private void prepareAffixes(ChampionRank rank) {
        List<String> pool = new ArrayList<>(AffixRegistry.ALL_AFFIXES.keySet());
        Collections.shuffle(pool);

        int count = Math.min(rank.affixes(), pool.size());

        List<String> selected = pool.subList(0, count);
        String result = String.join(",", selected);

        champions$setAffixesString(result);
    }

    @Unique
    private void modifyAttribute(MobEntity entity, EntityAttribute attribute, float m) {
        EntityAttributeInstance instance = entity.getAttributeInstance(attribute);
        if (instance != null) {
            instance.addPersistentModifier(new EntityAttributeModifier(
                    "champion_modifier",
                    m - 1.0,
                    EntityAttributeModifier.Operation.MULTIPLY_BASE
            ));
        }
    }
}