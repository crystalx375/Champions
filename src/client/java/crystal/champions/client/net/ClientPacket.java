package crystal.champions.client.net;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.util.Identifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ClientPacket {
    public static final Identifier CHAMPION_UPDATE_PACKET = Identifier.of("champions", "update_hud");
    public static final Identifier CHAMPION_REMOVE_PACKET = Identifier.of("champions", "remove_hud");

    // Используем ConcurrentHashMap для предотвращения крашей при доступе из разных потоков
    public static final Map<UUID, ChampionDisplayInfo> activeChampions = new ConcurrentHashMap<>();

    public static void registerPackets() {
        assert CHAMPION_UPDATE_PACKET != null;
        assert CHAMPION_REMOVE_PACKET != null;

        ClientPlayNetworking.registerGlobalReceiver(CHAMPION_UPDATE_PACKET, (client, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            ChampionDisplayInfo info = new ChampionDisplayInfo(
                    buf.readText(),
                    buf.readInt(),
                    buf.readString(),
                    buf.readFloat(),
                    buf.readFloat(),
                    System.currentTimeMillis()
            );

            client.execute(() -> activeChampions.put(uuid, info));
        });
        ClientPlayNetworking.registerGlobalReceiver(CHAMPION_REMOVE_PACKET, (client, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            client.execute(() -> activeChampions.remove(uuid));
        });
    }
}