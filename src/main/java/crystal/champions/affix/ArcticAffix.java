package crystal.champions.affix;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.util.math.Direction;

import static crystal.champions.config.ChampionsConfig.cooldownBeforeBulletArtic;

public class ArcticAffix extends Affix {

    public ArcticAffix() {
        super("arctic");
    }

    @Override
    public void onAttack(LivingEntity entity, MobEntity mob) {
        if (entity.age % cooldownBeforeBulletArtic != 0) return;
        LivingEntity target = mob.getTarget();
        if (target != null) {
            ShulkerBulletEntity bullet = new ShulkerBulletEntity(entity.getWorld(), entity, target, Direction.Axis.Y);
            bullet.setPosition(entity.getX(), entity.getEyeY(), entity.getZ());
            entity.getWorld().spawnEntity(bullet);
        }
    }
}
