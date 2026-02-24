package crystal.champions.client;

import crystal.champions.client.net.ChampionDisplayInfo;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Map;
import java.util.UUID;

import static crystal.champions.client.net.ClientPacket.activeChampions;
import static crystal.champions.client.net.ClientPacket.activeChampionsCl;
import static crystal.champions.client.render.ChampionsColor.getColor;
import static crystal.champions.client.render.ChampionsRender.renderChampion;

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

    private ChampionData findBestChampionCl(MinecraftClient client, float delta) {
        long now = System.currentTimeMillis();
        ClientLook at = performFarRaycast(client, delta, 40.0);

        if (at != null && at.check() && activeChampionsCl.containsKey(at.uuid())) {
            targetUuid = at.uuid();
            lastUpdateAt = now;
        }

        if (targetUuid != null) {
            ChampionDisplayInfo info = activeChampionsCl.get(targetUuid);
            if (info != null && (now - lastUpdateAt < 1000) && info.health() > 0) {
                return dataWrite(info);
            } else {
                activeChampionsCl.remove(targetUuid);
                targetUuid = null;
            }
        }
        return null;
    }

    private ChampionData findBestChampion() {
        ChampionData best = null;
        long now = System.currentTimeMillis();

        for (Map.Entry<UUID, ChampionDisplayInfo> entry : activeChampions.entrySet()) {
            ChampionDisplayInfo info = entry.getValue();

            if (now - info.lastUpdate() > 5000 || (now - info.lastUpdate() > 100 && info.health() <= 0)) {
                activeChampions.remove(entry.getKey());
                continue;
            }

            if (best == null || info.tier() > best.tier()) {
                best = dataWrite(info);
            }
        }
        return best;
    }

    private ClientLook performFarRaycast(MinecraftClient client, float delta, double distance) {
        Entity camera = client.getCameraEntity();
        if (camera == null) return null;

        Vec3d pos = camera.getCameraPosVec(delta);
        Vec3d rotation = camera.getRotationVec(delta);
        Vec3d endPos = pos.add(rotation.x * distance, rotation.y * distance, rotation.z * distance);
        Box box = camera.getBoundingBox().stretch(rotation.multiply(distance)).expand(3.0, 3.0, 3.0);

        EntityHitResult entityHitResult = ProjectileUtil.raycast(
                camera,
                pos,
                endPos,
                box,
                entity -> !entity.isSpectator() && entity.canHit(),
                distance * distance
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
}