package crystal.champions.util;

import crystal.champions.affix.AffixRegistry;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrepareChampions {
    private PrepareChampions() {
        /* This utility class should not be instantiated */
    }

    public static void prepareAttributes(MobEntity mob, ChampionRank rank) {
        final float h = rank.growth_h();
        final float s = rank.growth_s();
        modifyAttribute(mob, EntityAttributes.GENERIC_MAX_HEALTH, h);
        mob.setHealth(mob.getMaxHealth());

        if (mob.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE) != null) {
            modifyAttribute(mob, EntityAttributes.GENERIC_ATTACK_DAMAGE, s);
        }
    }

    public static String prepareAffixes(ChampionRank rank) {
        List<String> pool = new ArrayList<>(AffixRegistry.ALL_AFFIXES.keySet());
        Collections.shuffle(pool);

        int count = Math.min(rank.affixes(), pool.size());
        List<String> selected = pool.subList(0, count);

        return String.join(",", selected);
    }

    private static void modifyAttribute(MobEntity entity, RegistryEntry<EntityAttribute> attribute, float m) {
        var instance = entity.getAttributeInstance(attribute);
        if (instance != null) {
            instance.setBaseValue(instance.getBaseValue() * m);
        }
    }

    @Unique
    public static boolean canBeChampion(MobEntity mob) {
        final boolean isAgg = mob instanceof HostileEntity || mob instanceof Angerable
                || mob instanceof CaveSpiderEntity || mob instanceof GhastEntity
                || mob instanceof PhantomEntity || mob instanceof ShulkerEntity
                || mob instanceof SilverfishEntity || mob instanceof SlimeEntity;

        final boolean isException = mob instanceof EnderDragonEntity || mob instanceof WitherEntity
                || mob instanceof IronGolemEntity || mob instanceof PolarBearEntity
                || mob instanceof WolfEntity;

        return isAgg && !isException;
    }
}
