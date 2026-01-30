package crystal.champions.mixin;

import crystal.champions.Interface.IChampions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBulletEntity.class)
public class ShulkerBulletMixin {
    /**
     * Инжектим в буллет шалкера и добавляем свои проверки
     *                 if (affixes.contains("arctic")) {
     *                     target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 5));
     *                     ci.cancel();
     *                 }
     *                 if (affixes.contains("molten")) {
     *                     target.setOnFireFor(100);
     *                     ci.cancel();
     *                 }
     */
    @Inject(method = "onEntityHit", at = @At("HEAD"), cancellable = true)
    private void championEffect(EntityHitResult entityHitResult, CallbackInfo ci) {
        ShulkerBulletEntity bullet = (ShulkerBulletEntity) (Object) this;
        if (bullet.getOwner() instanceof IChampions champion && champion.champions$isChampion()) {
            Entity entity = entityHitResult.getEntity();

            if (entity instanceof LivingEntity target) {
                String affixes = champion.champions$getAffixesString();
                
                if (affixes == null || champion == null) return;

                if (affixes.contains("arctic")) {
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 5));
                    ci.cancel();
                }
                if (affixes.contains("molten")) {
                    target.setOnFireFor(100);
                    ci.cancel();
                }
            }
        }
    }
}