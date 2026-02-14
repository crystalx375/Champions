package crystal.champions.mixin;

import crystal.champions.Interface.IChampions;
import crystal.champions.net.ChampionsNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(MobEntity.class)
public abstract class ChampionBossBar extends LivingEntity implements IChampions {
    @Unique private final Map<UUID, ServerBossBar> healthBars = new HashMap<>();
    @Unique private final Map<UUID, ServerBossBar> affixBars = new HashMap<>();
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

        Set<UUID> currentIds = new HashSet<>();
        for (ServerPlayerEntity player : nearby) {
            assert ChampionsNetworking.CHAMPION_UPDATE_PACKET != null;
            UUID pUuid = player.getUuid();
            currentIds.add(pUuid);
            if (!isBestChampionForPlayer(player)) {
                removeAllBars(player);
                continue;
            }

            if (ServerPlayNetworking.canSend(player, ChampionsNetworking.CHAMPION_UPDATE_PACKET)) {
                ChampionsNetworking.sendUpdate(player, (MobEntity)(Object)this, champions$getChampionTier(), champions$getAffixesString());
                removeAllBars(player);
                trackedPlayerIds.add(pUuid);
            } else {
                updateHealth(player);
                updateAffixes(player);
                trackedPlayerIds.add(pUuid);
            }
        }
        // remove
        Iterator<UUID> it = trackedPlayerIds.iterator();
        while (it.hasNext()) {
            UUID id = it.next();
            if (!currentIds.contains(id)) {
                ServerPlayerEntity player = (ServerPlayerEntity) this.getWorld().getPlayerByUuid(id);
                if (player != null) {
                    ChampionsNetworking.sendRemove(player, (MobEntity)(Object)this);
                    removeAllBars(player);
                }
                it.remove();
            }
        }
    }

    @Unique
    private boolean isBestChampionForPlayer(ServerPlayerEntity player) {
        List<MobEntity> champions = player.getWorld().getEntitiesByClass(
                MobEntity.class, player.getBoundingBox().expand(25.0),
                e -> e instanceof IChampions && ((IChampions)e).champions$getChampionTier() > 0
        );

        Optional<MobEntity> best = champions.stream()
                .max(Comparator.comparingInt(e -> ((IChampions)e).champions$getChampionTier()));

        return best.map(mobEntity -> mobEntity == (Object) this ||
                ((IChampions) mobEntity).champions$getChampionTier() <= this.champions$getChampionTier()).orElse(true);
    }

    @Unique
    private void updateHealth(ServerPlayerEntity player) {
        int tier = this.champions$getChampionTier();
        BossBar.Color color = getColor(tier);

        ServerBossBar bar = healthBars.computeIfAbsent(player.getUuid(), id -> {
            ServerBossBar b = new ServerBossBar(this.getDisplayName(), color, BossBar.Style.PROGRESS);
            b.addPlayer(player);
            return b;
        });

        MutableText stars = Text.literal("★".repeat(tier)).formatted(Formatting.YELLOW);
        MutableText skilledPrefix = Text.literal(" Skilled ").formatted(color.getTextFormat());
        MutableText entityName = this.getDisplayName().copy().formatted(color.getTextFormat());

        bar.setPercent(this.getHealth() / this.getMaxHealth());
        bar.setColor(color);
        bar.setName(stars.append(skilledPrefix).append(entityName));
    }

    @Unique
    private void updateAffixes(ServerPlayerEntity player) {
        ServerBossBar bar = affixBars.computeIfAbsent(player.getUuid(), id -> {
            ServerBossBar b = new ServerBossBar(Text.empty(), BossBar.Color.WHITE, BossBar.Style.NOTCHED_20);
            b.addPlayer(player);
            return b;
        });

        String affixesStr = champions$getAffixesString();
        bar.setName(Text.literal(affixesStr != null ? affixesStr : "").formatted(Formatting.GRAY, Formatting.ITALIC));
        bar.setColor(BossBar.Color.WHITE);
    }
    @Unique
    private void removeIt() {
        healthBars.values().forEach(ServerBossBar::clearPlayers);
        affixBars.values().forEach(ServerBossBar::clearPlayers);
        healthBars.clear();
        affixBars.clear();

        if (this.getWorld().getServer() == null) return;

        // Итерируемся по копии trackedPlayerIds
        Set<UUID> idsCopy = new HashSet<>(trackedPlayerIds);
        for (UUID id : idsCopy) {
            ServerPlayerEntity player = this.getWorld().getServer().getPlayerManager().getPlayer(id);
            if (player != null) {
                ChampionsNetworking.sendRemove(player, (MobEntity)(Object)this);
            }
        }
        trackedPlayerIds.clear();
    }

    @Unique
    private void removeAllBars(ServerPlayerEntity player) {
        ServerBossBar hBar = healthBars.remove(player.getUuid());
        if (hBar != null) hBar.clearPlayers();

        ServerBossBar aBar = affixBars.remove(player.getUuid());
        if (aBar != null) aBar.clearPlayers();
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        removeIt();
    }


    @Unique
    private BossBar.Color getColor(int tier) {
        try {
            return switch (tier) {
                case 1 -> BossBar.Color.YELLOW;
                case 2 -> BossBar.Color.RED;
                case 3 -> BossBar.Color.BLUE;
                case 4 -> BossBar.Color.PURPLE;
                default -> BossBar.Color.PINK;
            };
        } catch (Exception e) {
            return BossBar.Color.YELLOW;
        }
    }
}