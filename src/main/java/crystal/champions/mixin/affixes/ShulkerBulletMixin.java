package crystal.champions.mixin.affixes;

import crystal.champions.Interface.IBullet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBulletEntity.class)
public abstract class ShulkerBulletMixin implements IBullet {
    /**
     * Инжектим в буллет шалкера и добавляем свои проверки в хит
     *         if (this.isArctic) {
     *             target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 5));
     *             ci.cancel();
     *         }
     *         if (this.isMolten) {
     *             target.setOnFireFor(40);
     *             ci.cancel();
     *         }
     */
    @Unique private boolean isArctic = false;
    @Unique private boolean isMolten = false;

    @Override
    public void champions$setArctic(boolean arctic) {
        this.isArctic = arctic;
    }

    @Override
    public void champions$setMolten(boolean molten) {
        this.isMolten = molten;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeChampionData(NbtCompound nbt, CallbackInfo ci) {
        if (this.isArctic) nbt.putBoolean("arctic", true);
        if (this.isMolten) nbt.putBoolean("molten", true);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readChampionData(NbtCompound nbt, CallbackInfo ci) {
        this.isArctic = nbt.getBoolean("arctic");
        this.isMolten = nbt.getBoolean("molten");
    }

    @Inject(method = "onEntityHit", at = @At("HEAD"), cancellable = true)
    private void championEffect(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity targetEntity = entityHitResult.getEntity();
        if (!(targetEntity instanceof LivingEntity target)) return;

        if (this.isArctic) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 5));
            ci.cancel();
        }
        if (this.isMolten) {
            target.setOnFireFor(40);
            ci.cancel();
        }
    }
}