package crystal.champions.mixin;

import crystal.champions.IChampions;
import crystal.champions.affix.AffixRegistry;
import crystal.champions.util.ChampionRank;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonMixin extends LivingEntity implements IChampions {

    protected EnderDragonMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void applyChampionsLogic(EntityType entityType, World world, CallbackInfo ci) {

        MobEntity mob = (MobEntity) (Object) this;
        ChampionRank rank = ChampionRank.getBossRank(mob.getRandom());
        if (rank.tier() > 0) {
            champions$setChampionTier(rank.tier());
            prepareAttributes(mob, rank);
            champions$setAffixesString(prepareAffixes(rank));
        }
    }

    @Unique
    private void prepareAttributes(MobEntity mob, ChampionRank rank) {
        float h = rank.growth_h();
        float s = rank.growth_s();
        modifyAttribute(mob, EntityAttributes.GENERIC_MAX_HEALTH, h);
        mob.setHealth(mob.getMaxHealth());

        if (mob.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE) != null) {
            modifyAttribute(mob, EntityAttributes.GENERIC_ATTACK_DAMAGE, s);
        }
    }

    @Unique
    private String prepareAffixes(ChampionRank rank) {
        List<String> pool = new ArrayList<>(AffixRegistry.ALL_AFFIXES.keySet());
        Collections.shuffle(pool);

        int count = Math.min(rank.affixes(), pool.size());

        List<String> selected = pool.subList(0, count);
        return String.join(",", selected);
    }

    @Unique
    private void modifyAttribute(MobEntity entity, RegistryEntry<EntityAttribute> attribute, float m) {
        var instance = entity.getAttributeInstance(attribute);
        if (instance != null) {
            instance.setBaseValue(instance.getBaseValue() * m);
        }
    }
}