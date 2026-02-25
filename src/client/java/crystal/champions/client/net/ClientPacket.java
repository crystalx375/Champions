package crystal.champions.client.net;

import crystal.champions.Champions;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ClientPacket {
    public static final Identifier CHAMPION_UPDATE_PACKET = Identifier.of("champions", "update_hud");
    public static final Identifier CHAMPION_REMOVE_PACKET = Identifier.of("champions", "remove_hud");
    public static final Identifier CHAMPION_UPDATE_CLIENT_PACKET = Identifier.of("champions", "update_client_hud");

    public static final Map<UUID, ChampionDisplayInfo> activeChampions = new ConcurrentHashMap<>();
    public static final Map<UUID, ChampionDisplayInfo> activeChampionsCl = new ConcurrentHashMap<>();
    /**
     * Сетевые пакеты
     * Регистрация
     */
    public static void registerPackets() {

        assert CHAMPION_REMOVE_PACKET != null;
        ClientPlayNetworking.registerGlobalReceiver(CHAMPION_REMOVE_PACKET, (client, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            client.execute(() -> activeChampions.remove(uuid));
        });

        assert CHAMPION_UPDATE_PACKET != null;
        ClientPlayNetworking.registerGlobalReceiver(CHAMPION_UPDATE_PACKET, (client, handler, buf, responseSender) -> {
            ServerBuf(buf, client);
        });

        assert CHAMPION_UPDATE_CLIENT_PACKET != null;
        ClientPlayNetworking.registerGlobalReceiver(CHAMPION_UPDATE_CLIENT_PACKET, (client, handler, buf, responseSender) -> {
            ClientBuf(buf, client);
        });
        Champions.LOGGER.info("Registering network packets");
    }

    /**
     * Воспринимайте это как разделение от логики:
     * Прогрузки бокса (server)
     * Прогрузки по наведению на энтити (client)
     */
    private static void ServerBuf(net.minecraft.network.PacketByteBuf buf, net.minecraft.client.MinecraftClient client) {
        UUID uuid = buf.readUuid();
        ChampionDisplayInfo info = new ChampionDisplayInfo(
                buf.readText(),
                buf.readInt(),
                buf.readString(),
                buf.readFloat(),
                buf.readFloat(),
                System.currentTimeMillis(),
                System.currentTimeMillis()
        );
        client.execute(() -> activeChampions.put(uuid, info));
    }
    private static void ClientBuf(net.minecraft.network.PacketByteBuf buf, net.minecraft.client.MinecraftClient client) {
        UUID uuid = buf.readUuid();
        ChampionDisplayInfo info = new ChampionDisplayInfo(
                buf.readText(),
                buf.readInt(),
                buf.readString(),
                buf.readFloat(),
                buf.readFloat(),
                System.currentTimeMillis(),
                System.currentTimeMillis()
        );
        client.execute(() -> activeChampionsCl.put(uuid, info));
    }
}