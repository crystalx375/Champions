package crystal.champions.mixin;

import crystal.champions.IChampions;
import crystal.champions.util.net.ChampionsNetworking;
import crystal.champions.util.net.Payload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

/**
 * Боссбар на сервере / клиенте
 * Если сервер то рендерим тут
 * Если клиент отправляем пакеты с данными
 */
@Mixin(MobEntity.class)
public abstract class ServerUpdatePackets extends LivingEntity implements IChampions {
    @Shadow
    protected abstract void removeFromDimension();

    @Unique private final Set<UUID> trackedPlayerIds = new HashSet<>();

    protected ServerUpdatePackets(net.minecraft.entity.EntityType<? extends LivingEntity> type, net.minecraft.world.World world) {
        super(type, world);
    }

    /**
     * Сделал так, чтобы отправлял разные пакеты, и с ними уже можно делать хоть что-то
     * При наведении чтобы можно было видеть дальше
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void manageChampionHud(CallbackInfo ci) {
        if (this.getWorld().isClient || this.champions$getChampionTier() <= 0) return;

        Set<UUID> currentIds = new HashSet<>();
        MobEntity mob = (MobEntity) (Object) this;
        boolean Bosses = mob instanceof EnderDragonEntity || mob instanceof WitherEntity;

        List<ServerPlayerEntity> nearby = this.getWorld().getEntitiesByClass(
                ServerPlayerEntity.class, this.getBoundingBox().expand(80.0), p -> true
        );

        for (ServerPlayerEntity player : nearby) {
            UUID uuid = player.getUuid();
            double distance = player.squaredDistanceTo(this);

            if (ServerPlayNetworking.canSend(player, Payload.ChampionUpdate.ID)) {
                if (!Bosses) {
                    if (distance <= 1600) {
                        if (distance <= 225.0) {
                            ChampionsNetworking.sendUpdateS(player, mob, champions$getChampionTier(), champions$getAffixesString());
                            trackedPlayerIds.add(uuid);
                            currentIds.add(uuid);
                        }
                        ChampionsNetworking.sendUpdateC(player, mob, champions$getChampionTier(), champions$getAffixesString());
                    }
                } else {
                    ChampionsNetworking.sendUpdateS(player, mob, champions$getChampionTier(), champions$getAffixesString());
                    ChampionsNetworking.sendUpdateC(player, mob, champions$getChampionTier(), champions$getAffixesString());
                    trackedPlayerIds.add(uuid);
                    currentIds.add(uuid);
                }
            }
        }

        // remover
        Iterator<UUID> it = trackedPlayerIds.iterator();
        while (it.hasNext()) {
            UUID id = it.next();
            if (!currentIds.contains(id)) {
                try {
                    ServerPlayerEntity player = Objects.requireNonNull(this.getWorld().getServer()).getPlayerManager().getPlayer(id);
                    if (player != null) {
                        ChampionsNetworking.sendRemove(player, mob);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                it.remove();
            }
        }
    }
    /**
     * Removers
     * Подчищаем и удаляем все тут с баров
     * Ну и сами бары
     */
    @Unique
    private void removeIt() {
        if (this.getWorld().getServer() == null) return;

        Set<UUID> ids= new HashSet<>(trackedPlayerIds);
        for (UUID id : ids) {
            ServerPlayerEntity player = this.getWorld().getServer().getPlayerManager().getPlayer(id);
            MobEntity mob = (MobEntity) (Object) this;
            if (player != null && mob.isDead()) {
                ChampionsNetworking.sendRemove(player, mob);
            }
        }
        trackedPlayerIds.clear();
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        removeIt();
    }
}