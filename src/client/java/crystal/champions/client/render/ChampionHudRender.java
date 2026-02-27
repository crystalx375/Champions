package crystal.champions.client.render;

import crystal.champions.client.net.ChampionDisplayInfo;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.Map;
import java.util.UUID;

import static crystal.champions.client.net.ClientPacket.activeChampions;
import static crystal.champions.client.net.ClientPacket.activeChampionsCl;
import static crystal.champions.client.render.ChampionsColor.getColor;
import static crystal.champions.client.render.ChampionsRender.renderChampion;
import static crystal.champions.config.ChampionsConfigClient.*;

public class ChampionHudRender implements HudRenderCallback {

    private UUID targetUuid = null;
    private long lastUpdateAt = 0;

    /**
     * Отрисовка на клиент сайде
     *
     * @param context Здесь мы рисуем все что до этого сделали
     * @param delta Используем в будущем для камеры и просчета
     */
    @Override
    public void onHudRender(DrawContext context, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || client.options.hudHidden) return;
        ChampionData bestChampion = findBestChampion();
        ChampionData bestChampionCl = findBestChampionCl(client, delta);

        int cX = client.getWindow().getScaledWidth() / 2;
        int y = 12;

        if (bestChampionCl != null) { renderChampion(context, cX, y, bestChampionCl, client); }
        else if (bestChampion != null) { renderChampion(context, cX, y, bestChampion, client); }
    }

    /**
     * When looking render
     */
    private ChampionData findBestChampionCl(MinecraftClient client, float delta) {
        long now = System.currentTimeMillis();
        ClientLook at = performRaycast(client, delta, 80.0);

        if (at != null && at.check() && activeChampionsCl.containsKey(at.uuid())) {
            targetUuid = at.uuid();
            lastUpdateAt = now;
        }

        if (targetUuid != null) {
            ChampionDisplayInfo info = activeChampionsCl.get(targetUuid);
            if (info != null && (now - lastUpdateAt < cache_client) && info.health() > 0) {
                return dataWrite(info);
            } else {
                activeChampionsCl.remove(targetUuid);
                targetUuid = null;
            }
        }
        return null;
    }

    /**
     * Box render
     */
    private ChampionData findBestChampion() {
        if (only_for_view) return null;
        ChampionData best = null;
        long now = System.currentTimeMillis();

        for (Map.Entry<UUID, ChampionDisplayInfo> entry : activeChampions.entrySet()) {
            ChampionDisplayInfo info = entry.getValue();

            if ((now - info.lastUpdate() > cache_server) || (now - info.lastUpdate() > 100 && info.health() <= 0)) {
                activeChampions.remove(entry.getKey());
                continue;
            }

            if (info.tier() > 10) {
                best = dataWriteBoss(info);
            } else if (best == null || info.tier() > best.tier()) {
                best = dataWrite(info);
            }
        }
        return best;
    }

    /**
     * Use when looking
     */
    private ClientLook performRaycast(MinecraftClient client, float delta, double distance) {
        Entity camera = client.getCameraEntity();
        if (camera == null || client.world == null) return null;

        Vec3d pos = camera.getCameraPosVec(delta);
        Vec3d rotation = camera.getRotationVec(delta);
        Vec3d endPos = pos.add(rotation.x * distance, rotation.y * distance, rotation.z * distance);

        BlockHitResult blockHit = client.world.raycast(new RaycastContext(
                pos,
                endPos,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                camera
        ));

        double sq = blockHit.getType() != HitResult.Type.MISS
                ? blockHit.getPos().squaredDistanceTo(pos)
                : distance * distance;

        Box box = camera.getBoundingBox().stretch(rotation.multiply(distance)).expand(1.0, 1.0, 1.0);
        EntityHitResult entityHitResult = ProjectileUtil.raycast(
                camera,
                pos,
                endPos,
                box,
                entity -> !entity.isSpectator() && entity.canHit(),
                sq
        );

        if (entityHitResult != null && entityHitResult.getEntity() != null) {
            return new ClientLook(true, System.currentTimeMillis(), entityHitResult.getEntity().getUuid());
        }

        return new ClientLook(false, System.currentTimeMillis(), null);
    }

    // Records
    public record ChampionData(
            Text name,
            int tier,
            String affixes,
            float percent,
            int color
    ) {}

    private record ClientLook(
            boolean check,
            long lastUpdate,
            UUID uuid
    ) {}


    private ChampionData dataWrite(ChampionDisplayInfo info) {
        return new ChampionData(
                info.name(),
                info.tier(),
                info.affixes(),
                info.health() / info.maxHealth(),
                getColor(info.tier())
        );
    }

    private ChampionData dataWriteBoss(ChampionDisplayInfo info) {
        return new ChampionData(
                info.name(),
                info.tier() - 10,
                info.affixes(),
                info.health() / info.maxHealth(),
                getColor(info.tier() - 10)
        );
    }
}