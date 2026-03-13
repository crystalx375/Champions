package crystal.champions.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StunStatusEffect extends StatusEffect {
    private static final Map<UUID, Float> frozenYaw = new HashMap<>();
    private static final Map<UUID, Float> frozenPitch = new HashMap<>();

    public StunStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0x999999);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.setVelocity(0, 0, 0);

        if (entity instanceof PlayerEntity player) {
            player.setSprinting(false);
            player.setJumping(false);
            UUID id = player.getUuid();
            if (!frozenYaw.containsKey(id)) {
                frozenYaw.put(id, player.getYaw());
                frozenPitch.put(id, player.getPitch());
            }
            player.setYaw(frozenYaw.get(id) + (float)((Math.random() - 0.5) * 0.1));
            player.setPitch(frozenPitch.get(id) + (float)((Math.random() - 0.5) * 0.1));
            player.velocityDirty = true;
        }

        return true;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onRemoved(AttributeContainer attributeContainer) {
        super.onRemoved(attributeContainer);
    }
}
