package crystal.champions.mixin;

import crystal.champions.Interface.IChampions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
@Mixin(MobEntity.class)
public abstract class ChampionBossBar extends LivingEntity implements IChampions {

    protected ChampionBossBar(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique private static final Map<UUID, ServerBossBar> playerNamesBars = new HashMap<>();
    @Unique private static final Map<UUID, ServerBossBar> playerAffixesBars = new HashMap<>();
    @Unique private static final Map<UUID, UUID> playerCurrentTarget = new HashMap<>();


    @Inject(method = "tick", at = @At("TAIL"))
    private void updateChampionBossBar(CallbackInfo ci) {

        if (this.getWorld().isClient) return;

        Box box = this.getBoundingBox().expand(25.0);
        List<ServerPlayerEntity> nearbyPlayers = this.getWorld().getEntitiesByClass(ServerPlayerEntity.class, box, player -> true);

        for (ServerPlayerEntity player : nearbyPlayers) updateBestBossForPlayer(player);
    }

    @Unique
    private void updateBestBossForPlayer(ServerPlayerEntity player) {
        List<MobEntity> nearbyChampions = player.getWorld().getEntitiesByClass(MobEntity.class, player.getBoundingBox().expand(25.0),
                entity -> entity instanceof IChampions champion && champion.champions$getChampionTier() > 0 && entity.isAlive()
        );
        MobEntity target = nearbyChampions.stream().max(Comparator.comparingInt((MobEntity e) -> ((IChampions) e).champions$getChampionTier())
                .thenComparingDouble(e -> -e.squaredDistanceTo(player))).orElse(null);

        if (nearbyChampions.isEmpty()) {
            removePlayerBars(player);
            return;
        }
        if (target == null || target.getHealth() <= 0) {
            removePlayerBars(player);
            return;
        }

        displayBossToPlayer(player, target);
    }

    @Unique
    private void displayBossToPlayer(ServerPlayerEntity player, MobEntity target) {
        IChampions champion = (IChampions) target;
        int tier = champion.champions$getChampionTier();


        ServerBossBar nameBar = playerNamesBars.computeIfAbsent(player.getUuid(), id -> {
            ServerBossBar bar = new ServerBossBar(Text.literal(""), getBarColorByTier(tier), BossBar.Style.PROGRESS);
            bar.addPlayer(player);
            return bar;
        });

        ServerBossBar affixesBar = playerAffixesBars.computeIfAbsent(player.getUuid(), id -> {
            ServerBossBar bar = new ServerBossBar(Text.literal(""), BossBar.Color.WHITE, BossBar.Style.NOTCHED_20);
            bar.addPlayer(player);
            return bar;
        });

        nameBar.setPercent(target.getHealth() / target.getMaxHealth());
        nameBar.setColor(getBarColorByTier(tier));
        playerCurrentTarget.put(player.getUuid(),target.getUuid());

        MutableText nameText = Text.literal("§e" + "★".repeat(tier) + " ");
        nameText.append(target.getDisplayName().copy().formatted(getBarColorByTier(tier).getTextFormat()));
        nameBar.setName(nameText);
        affixesBar.setName(formatAffixes(champion.champions$getAffixesString()));
        affixesBar.setPercent(0.0f);
        affixesBar.setColor(BossBar.Color.WHITE);
    }

    @Unique
    private void removePlayerBars(ServerPlayerEntity player) {
        ServerBossBar b1 = playerNamesBars.remove(player.getUuid());
        ServerBossBar b2 = playerAffixesBars.remove(player.getUuid());
        if (b1 != null) b1.clearPlayers();
        if (b2 != null) b2.clearPlayers();
    }

    @Unique
    private Text formatAffixes(String rawAffixes) {
        if (rawAffixes == null || rawAffixes.isEmpty()) return Text.empty();

        MutableText formatted = Text.empty();
        String[] affixes = rawAffixes.split(",");

        for (int i = 0; i < affixes.length; i++) {
            String affixName = affixes[i].trim();
            formatted.append(Text.translatable("affix.champions." + affixName.toLowerCase()));

            if (i < affixes.length - 1) {
                formatted.append(Text.literal(" §8• "));
            }
        }
        return formatted;
    }

    @Unique
    private BossBar.Color getBarColorByTier(int tier) {
        return switch (tier) {
            case 1 -> BossBar.Color.YELLOW;
            case 2 -> BossBar.Color.RED;
            case 3 -> BossBar.Color.BLUE;
            case 4 -> BossBar.Color.GREEN;
            default -> BossBar.Color.PINK;
        };
    }
}
