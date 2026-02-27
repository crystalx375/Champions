package crystal.champions.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class STUN extends StatusEffect {
    private static final Map<UUID, Float> frozenYaw = new HashMap<>();
    private static final Map<UUID, Float> frozenPitch = new HashMap<>();

    public STUN() {
        super(StatusEffectCategory.HARMFUL, 0x999999);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.setVelocity(0, 0, 0);

        if (entity instanceof PlayerEntity player) {
            player.setSprinting(false);
            player.setJumping(false);

            UUID id = player.getUuid();
            if (!frozenYaw.containsKey(id)) {
                frozenYaw.put(id, player.getYaw());
                frozenPitch.put(id, player.getPitch());
            }
            player.setYaw(frozenYaw.get(id));
            player.setPitch(frozenPitch.get(id));

            player.setYaw(player.getYaw() + (float)((Math.random() - 0.5) * 0.3));
            player.setPitch(player.getPitch() + (float)((Math.random() - 0.5) * 0.3));

            player.setVelocity(0, 0, 0);
        }
        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onRemoved(LivingEntity entity, net.minecraft.entity.attribute.AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        if (entity instanceof PlayerEntity player) {
            UUID id = player.getUuid();
            frozenYaw.remove(id);
            frozenPitch.remove(id);
        }
    }
}
