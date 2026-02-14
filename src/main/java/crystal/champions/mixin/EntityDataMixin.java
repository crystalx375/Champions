package crystal.champions.mixin;

import crystal.champions.Interface.IChampions;
import crystal.champions.affix.Affix;
import crystal.champions.affix.AffixRegistry;
import crystal.champions.net.ChampionsNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class EntityDataMixin extends Entity implements IChampions {
    @Shadow
    public Hand preferredHand;

    @Shadow
    @Final
    public static float BABY_SCALE_FACTOR;

    /**
     * Записываем сюда все данные для их использования в чемпионах
     * По названию и так понятно я думаю
     */
    protected EntityDataMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique private static final TrackedData<Integer> CHAMPION_TIER = DataTracker.registerData(EntityDataMixin.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique private static final TrackedData<String> AFFIXES = DataTracker.registerData(EntityDataMixin.class, TrackedDataHandlerRegistry.STRING);
    @Unique private static final TrackedData<String> ADAPT_TYPE = DataTracker.registerData(EntityDataMixin.class, TrackedDataHandlerRegistry.STRING);
    @Unique private static final TrackedData<Integer> ADAPT_COUNT = DataTracker.registerData(EntityDataMixin.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique private static final TrackedData<Boolean> IS_SHIELDING = DataTracker.registerData(EntityDataMixin.class, TrackedDataHandlerRegistry.BOOLEAN);

    /**
     * Устанавливаем сеттеры и геттеры
     */
    @Override
    public int champions$getChampionTier() {
        return this.dataTracker.get(CHAMPION_TIER);
    }
    @Override
    public void champions$setChampionTier(int tier) {
        this.dataTracker.set(CHAMPION_TIER, tier);
    }
    // affixes
    @Override
    public String champions$getAffixesString() {
        return this.dataTracker.get(AFFIXES);
    }
    @Override
    public void champions$setAffixesString(String affixes) {
        this.dataTracker.set(AFFIXES, affixes);
    }
    // adaptive
    @Override
    public String champions$getAdaptationType() {
        return this.dataTracker.get(ADAPT_TYPE);
    }
    @Override
    public void champions$setAdaptationType(String type) {
        this.dataTracker.set(ADAPT_TYPE, type);
    }
    @Override
    public int champions$getAdaptation() {
        return this.dataTracker.get(ADAPT_COUNT);
    }
    @Override
    public void champions$setAdaptation(int count) {
        this.dataTracker.set(ADAPT_COUNT, count);
    }
    // shield
    @Override
    public boolean champions$isShielding() { return this.dataTracker.get(IS_SHIELDING); }

    @Override
    public void champions$setShielding(boolean value) { this.dataTracker.set(IS_SHIELDING, value); }

    /**
     * Ну и логика тут дальше
     */
    @Inject(method = "initDataTracker", at = @At("TAIL"))
    protected void initChampionTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(AFFIXES, "");
        this.dataTracker.startTracking(CHAMPION_TIER, 0);

        this.dataTracker.startTracking(ADAPT_TYPE, "");
        this.dataTracker.startTracking(ADAPT_COUNT, 0);

        this.dataTracker.startTracking(IS_SHIELDING, false);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onChampionTick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!entity.getWorld().isClient) {
            this.champions$getActiveAffixes().forEach(affix -> {
                affix.onTick(entity);
                if (entity instanceof MobEntity mob) affix.onAttack(entity, mob);
                if (entity.age % 40 == 0) {
                    entity.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.SPEED, 40, champions$getChampionTier(), false, false, false));
                    entity.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.STRENGTH, 40, champions$getChampionTier() / 3, false, false, false));
                }
            });
        } else {
            int tier = this.champions$getChampionTier();
            if (tier > 0) {
                if (entity.getRandom().nextInt(50) == 0) {
                    entity.getWorld().addParticle(
                            ParticleTypes.CLOUD,
                            entity.getParticleX(0.5),
                            entity.getRandomBodyY(),
                            entity.getParticleZ(0.5),
                            0, 0.2, 0
                    );
                }
            }
        }
    }
    
    // Array
    @Override
    public List<Affix> champions$getActiveAffixes() {
        String raw = this.dataTracker.get(AFFIXES);
        if (raw.isEmpty()) return Collections.emptyList();
        return Arrays.stream(raw.split(","))
                .map(AffixRegistry.ALL_AFFIXES::get).filter(Objects::nonNull).toList();
    }


    // NBT
    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeChampionData(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("tier", this.champions$getChampionTier());
        nbt.putString("affixes", this.champions$getAffixesString());

        nbt.putString("adaptationType", this.champions$getAdaptationType());
        nbt.putInt("adaptationCount", this.champions$getAdaptation());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readChampionData(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("tier")) {
            this.champions$setChampionTier(nbt.getInt("tier"));
        }
        if (nbt.contains("affixes")) {
            this.champions$setAffixesString(nbt.getString("affixes"));
        }
        if (nbt.contains("adaptationType")) {
            this.champions$setAdaptationType(nbt.getString("adaptationType"));
        }
        if (nbt.contains("adaptationCount")) {
            this.champions$setAdaptation(nbt.getInt("adaptationCount"));
        }
    }
}