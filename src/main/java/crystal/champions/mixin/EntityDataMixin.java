package crystal.champions.mixin;

import crystal.champions.Champions;
import crystal.champions.IChampions;
import crystal.champions.affix.Affix;
import crystal.champions.affix.AffixRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static crystal.champions.ChampionsColorServer.getColor;

@Mixin(LivingEntity.class)
public abstract class EntityDataMixin extends Entity implements IChampions {
    /**
     * Записываем сюда все данные для их использования в чемпионах
     * По названию и так понятно я думаю
     */
    protected EntityDataMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique private int CHAMPION_TIER = 0;
    @Unique private String AFFIXES = "";
    @Unique private String ADAPT_TYPE = "";
    @Unique private int ADAPT_COUNT = 0;
    @Unique private boolean IS_SHIELDING = false;

    /**
     * Устанавливаем сеттеры и геттеры
     */
    @Override
    public int champions$getChampionTier() {
        return CHAMPION_TIER;
    }
    @Override
    public void champions$setChampionTier(int tier) {
        CHAMPION_TIER = tier;
    }
    // affixes
    @Override
    public String champions$getAffixesString() {
        return AFFIXES;
    }
    @Override
    public void champions$setAffixesString(String affixes) {
        AFFIXES = affixes;
    }
    // adaptive
    @Override
    public String champions$getAdaptationType() {
        return ADAPT_TYPE;
    }
    @Override
    public void champions$setAdaptationType(String type) {
        ADAPT_TYPE = type;
    }
    @Override
    public int champions$getAdaptation() {
        return ADAPT_COUNT;
    }
    @Override
    public void champions$setAdaptation(int count) {
        ADAPT_COUNT = count;
    }
    // shield
    @Override
    public boolean champions$isShielding() {
        return IS_SHIELDING;
    }

    @Override
    public void champions$setShielding(boolean value) {
        IS_SHIELDING = value;
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
                            StatusEffects.SPEED, 40, CHAMPION_TIER / 3, false, false, false));
                }
            });
        } else {
            if (champions$getChampionTier() > 0) {
                spawnChampionParticles(entity);
            }
        }
    }

    @Unique
    private void spawnChampionParticles(LivingEntity entity) {
        if (entity.age % 4 == 0) {
            int color = getColor(champions$getChampionTier());
            entity.getWorld().addParticle(
                    Champions.CHAMPIONS_SPELL,
                    entity.getParticleX(0.5), entity.getRandomBodyY(), entity.getParticleZ(0.5),
                    color, 0, 0
            );
        }
    }

    // Array
    @Override
    public List<Affix> champions$getActiveAffixes() {
        String raw = AFFIXES;
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