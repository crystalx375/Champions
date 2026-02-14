package crystal.champions.affix;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;

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
            double strength = 0.01;
            float pX = (float) (pullDir.x * strength + target.getVelocity().x * 0.6);
            float pY = (float) (pullDir.y * strength + target.getVelocity().y * 0.4);
            float pZ = (float) (pullDir.z * strength + target.getVelocity().z * 0.6);
            target.setVelocity(pX, pY, pZ);

            target.velocityModified = true;
        }
    }
}