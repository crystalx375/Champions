package crystal.champions.mixin;

import crystal.champions.IChampions;
import crystal.champions.affix.AffixRegistry;
import crystal.champions.util.ChampionRank;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(SlimeEntity.class)
public abstract class SlimeEntityMixin extends Entity {

    protected SlimeEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * Инжектимся в метод remove, где создаются слаймы.
     */
    @Inject(
            method = "remove",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void applyChampions(RemovalReason reason, CallbackInfo ci, int i, Text text, boolean bl, float f, float g, int j, int k, int l, float h, float m, SlimeEntity slimeEntity) {
        IChampions child = (IChampions) slimeEntity;
        ChampionRank rank = ChampionRank.getRandomRank(slimeEntity.getRandom());
        if (rank.tier() > 0) {
            child.champions$setChampionTier(rank.tier());
            child.champions$setAffixesString(prepareAffixes(rank));
            prepareAttributes(slimeEntity, rank);
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