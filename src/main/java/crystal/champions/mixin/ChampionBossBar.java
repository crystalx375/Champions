package crystal.champions.mixin;

import crystal.champions.Interface.IChampions;
import crystal.champions.net.ChampionsNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.*;

@Mixin(MobEntity.class)
public abstract class ChampionBossBar extends LivingEntity implements IChampions {
    @Unique private final Map<UUID, ServerBossBar> vanillaBars = new HashMap<>();
    @Unique private final Set<UUID> trackedPlayerIds = new HashSet<>();

    protected ChampionBossBar(net.minecraft.entity.EntityType<? extends LivingEntity> type, net.minecraft.world.World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void manageChampionHud(CallbackInfo ci) {
        if (this.getWorld().isClient || this.champions$getChampionTier() <= 0) return;

        List<ServerPlayerEntity> nearby = this.getWorld().getEntitiesByClass(
                ServerPlayerEntity.class, this.getBoundingBox().expand(25.0), p -> true
        );

        Set<UUID> currentNearbyIds = new HashSet<>();
        for (ServerPlayerEntity player : nearby) {
            assert ChampionsNetworking.CHAMPION_UPDATE_PACKET != null;
            UUID pUuid = player.getUuid();
            currentNearbyIds.add(pUuid);

            if (ServerPlayNetworking.canSend(player, ChampionsNetworking.CHAMPION_UPDATE_PACKET)) {
                ChampionsNetworking.sendUpdate(player, (MobEntity)(Object)this, champions$getChampionTier(), champions$getAffixesString());
                removeVanillaBar(player);
                trackedPlayerIds.add(pUuid);
            } else {
                updateVanillaBar(player);
                trackedPlayerIds.add(pUuid);
            }
        }

        // Удаление для тех, кто ушел
        Iterator<UUID> it = trackedPlayerIds.iterator();
        while (it.hasNext()) {
            UUID id = it.next();
            if (!currentNearbyIds.contains(id)) {
                ServerPlayerEntity player = (ServerPlayerEntity) this.getWorld().getPlayerByUuid(id);
                if (player != null) {
                    ChampionsNetworking.sendRemove(player, (MobEntity)(Object)this);
                    removeVanillaBar(player);
                }
                it.remove();
            }
        }
    }

    @Unique
    private void updateVanillaBar(ServerPlayerEntity player) {
        ServerBossBar bar = vanillaBars.computeIfAbsent(player.getUuid(), id -> {
            ServerBossBar b = new ServerBossBar(this.getDisplayName(), BossBar.Color.RED, BossBar.Style.PROGRESS);
            b.addPlayer(player);
            return b;
        });
        bar.setPercent(this.getHealth() / this.getMaxHealth());
        bar.setName(Text.literal("★ ".repeat(this.champions$getChampionTier())).append(this.getDisplayName()));
    }

    @Unique
    private void removeVanillaBar(ServerPlayerEntity player) {
        ServerBossBar bar = vanillaBars.remove(player.getUuid());
        if (bar != null) bar.clearPlayers();
    }
}