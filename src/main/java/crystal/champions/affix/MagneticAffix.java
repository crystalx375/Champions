package crystal.champions.affix;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Vec3d;

import static crystal.champions.config.ChampionsConfigAffixes.strength;

/**
 * MagneticAffix
 * Из интересно здесь именно через set надо target.setVelocity(pX, pY, pZ);
 * Без него не будет как в оригинальном моде
 */
public class MagneticAffix extends Affix {
    public MagneticAffix() {
        super("magnetic");
    }

    @Override
    public void onAttack(LivingEntity entity, MobEntity mob) {
        if (entity.age % 400 <= 200) return;
        LivingEntity target = mob.getTarget();
        if (target != null) {
            Vec3d pullDir = entity.getPos().subtract(target.getPos()).normalize();
            double i = 0.01 * strength;
            float pX = (float) (pullDir.x * i + target.getVelocity().x * 0.5);
            float pY = (float) (pullDir.y * i + target.getVelocity().y * 0.6);
            float pZ = (float) (pullDir.z * i + target.getVelocity().z * 0.5);
            target.setVelocity(pX, pY, pZ);

            target.velocityModified = true;
        }
    }
}