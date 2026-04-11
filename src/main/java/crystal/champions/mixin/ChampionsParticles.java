package crystal.champions.mixin;

import crystal.champions.Champions;
import crystal.champions.IChampions;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static crystal.champions.ChampionsColorServer.getColor;

@Mixin(LivingEntity.class)
public class ChampionsParticles {

    @Unique int tier = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onServerTick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!entity.getWorld().isClient && entity instanceof IChampions c) {
            tier = c.champions$getChampionTier();
        }
    }

    @Unique private void spawnChampionParticles(LivingEntity entity, int tier) {
        if (entity.age % 4 == 0) {
            int color = getColor(tier);
            entity.getWorld().addParticle(
                    Champions.CHAMPIONS_SPELL,
                    entity.getParticleX(0.5),
                    entity.getRandomBodyY(),
                    entity.getParticleZ(0.5),
                    color,
                    0.05,
                    0
            );
        }
    }
}
