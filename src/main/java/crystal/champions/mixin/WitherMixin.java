package crystal.champions.mixin;

import crystal.champions.IChampions;
import crystal.champions.affix.AffixRegistry;
import crystal.champions.util.ChampionRank;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(WitherEntity.class)
public abstract class WitherMixin extends LivingEntity implements IChampions {

    protected WitherMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void applyChampionsLogic(EntityType entityType, World world, CallbackInfo ci) {

        MobEntity mob = (MobEntity) (Object) this;
        ChampionRank rank = ChampionRank.getBossRank(mob.getRandom());
        if (rank.tier() > 0) {
            champions$setChampionTier(rank.tier());
            prepareAttributes(mob, rank.growth_h(), rank.growth_s());
            prepareAffixes(rank.affixes());
        }
    }



    @Unique
    private void prepareAttributes(MobEntity mob, float h, float s) {
        modifyAttribute(mob, EntityAttributes.GENERIC_MAX_HEALTH, h);

        if (mob.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE) != null) {
            modifyAttribute(mob, EntityAttributes.GENERIC_ATTACK_DAMAGE, s);
        }
    }

    @Unique
    private void prepareAffixes(int count) {
        List<String> pool = new ArrayList<>(AffixRegistry.ALL_AFFIXES.keySet());
        Collections.shuffle(pool);

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
