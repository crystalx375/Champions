package crystal.champions.mixin;

import crystal.champions.Champions;
import crystal.champions.Interface.IChampions;
import crystal.champions.affix.AffixRegistry;
import crystal.champions.rank.ChampionRank;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
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
        MobEntity entity = (MobEntity) (Object) this;
        boolean isAggressive = entity instanceof HostileEntity || entity instanceof Angerable;

        if (!isAggressive) return;
        ChampionRank rank = ChampionRank.getRandomRank(entity.getRandom());
        if (rank.tier() > 0) {
            if (entity instanceof SilverfishEntity) return;
            champions$setChampionTier(rank.tier());
            List<String> pool = new ArrayList<>(AffixRegistry.ALL_AFFIXES.keySet());
            Collections.shuffle(pool);
            int count = Math.min(rank.affixes(), pool.size());
            List<String> selected = pool.subList(0, count);

            String result = String.join(",", selected);
            champions$setAffixesString(result);
            float multiplier = rank.growth();
            modifyAttribute(entity, EntityAttributes.GENERIC_MAX_HEALTH, multiplier);
            entity.setHealth(entity.getMaxHealth());
            if (entity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE) != null) {
                modifyAttribute(entity, EntityAttributes.GENERIC_ATTACK_DAMAGE, multiplier);
            }

            Champions.LOGGER.info("Spawned champion tier {} with affixes: {}", rank.tier(), result);
        }
    }

    @Unique
    private void modifyAttribute(MobEntity entity, EntityAttribute attribute, float multiplier) {
        EntityAttributeInstance instance = entity.getAttributeInstance(attribute);
        if (instance != null) {
            instance.addPersistentModifier(new EntityAttributeModifier(
                    "champion_modifier",
                    multiplier - 1.0,
                    EntityAttributeModifier.Operation.MULTIPLY_BASE
            ));
        }
    }
}