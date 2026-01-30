package crystal.champions.mixin;

import crystal.champions.Interface.IChampions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(LivingEntity.class)
public class ReflectionMixin {
    /**
     * Наносим урон при уроне моба (шипы)
     *                 attacker.damage(source, 2.0f);
     *                 attacker.playSound(SoundEvents.ENCHANT_THORNS_HIT, 2, 1);
     */
    @Inject(method = "damage", at = @At("TAIL"))
    private void applyReflection(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if ((Object)this instanceof IChampions champion && champion.champions$getAffixesString().contains("reflection")) {
            if (source.getAttacker() instanceof LivingEntity attacker) {
                if (source.getAttacker() == null) return;
                attacker.damage(attacker.getWorld().getDamageSources().magic(), 2.0f);
                Random rnd = new Random();
                double random = rnd.nextDouble(-0.15, 0.15);
                final double x = attacker.getVelocity().x + random;
                final double z = attacker.getVelocity().z + random;
                attacker.takeKnockback(0.4, x, z);
                attacker.getWorld().playSound(
                        null,
                        attacker.getBlockPos(),
                        SoundEvents.ENCHANT_THORNS_HIT,
                        SoundCategory.PLAYERS,
                        1.0f,
                        1.0f
                );
            }
        }
    }
}
