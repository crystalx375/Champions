package crystal.champions.affix;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import static crystal.champions.config.ChampionsConfigAffixes.knockback;

/**
 * KnockingAffix
 * Откидываем игрока
 */
public class KnockingAffix extends Affix {

    public KnockingAffix() {
        super("knocking");
    }

    @Override
    public void onHurt(LivingEntity champion, LivingEntity target) {
        float yaw = champion.getYaw();
        double x = -Math.sin(yaw * 0.017453292F);
        double z = Math.cos(yaw * 0.017453292F);
        
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 1));
        target.teleport(target.getX(), target.getY() + 0.1, + target.getZ());
        target.addVelocity(knockback * x, knockback * 0.3, knockback * z);
        target.velocityModified = true;
    }
}
