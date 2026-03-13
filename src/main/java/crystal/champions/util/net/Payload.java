package crystal.champions.util.net;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;

import java.util.UUID;

public abstract class Payload implements CustomPayload {
    public record ChampionUpdate(UUID uuid, Text name, int tier, String affixes, float health, float maxHealth) implements CustomPayload {
        public static final Id<ChampionUpdate> ID = new Id<>(Identifier.of("champions", "update_hud"));
        public static final PacketCodec<RegistryByteBuf, ChampionUpdate> CODEC = PacketCodec.tuple(
                Uuids.PACKET_CODEC, ChampionUpdate::uuid,
                TextCodecs.REGISTRY_PACKET_CODEC, ChampionUpdate::name,
                PacketCodecs.VAR_INT, ChampionUpdate::tier,
                PacketCodecs.STRING, ChampionUpdate::affixes,
                PacketCodecs.FLOAT, ChampionUpdate::health,
                PacketCodecs.FLOAT, ChampionUpdate::maxHealth,
                ChampionUpdate::new
        );
        @Override public Id<? extends CustomPayload> getId() { return ID; }
    }
    public record ChampionUpdateCl(UUID uuid, Text name, int tier, String affixes, float health, float maxHealth) implements CustomPayload {
        public static final Id<ChampionUpdateCl> ID = new Id<>(Identifier.of("champions", "update_client_hud"));
        public static final PacketCodec<RegistryByteBuf, ChampionUpdateCl> CODEC = PacketCodec.tuple(
                Uuids.PACKET_CODEC, ChampionUpdateCl::uuid,
                TextCodecs.REGISTRY_PACKET_CODEC, ChampionUpdateCl::name,
                PacketCodecs.VAR_INT, ChampionUpdateCl::tier,
                PacketCodecs.STRING, ChampionUpdateCl::affixes,
                PacketCodecs.FLOAT, ChampionUpdateCl::health,
                PacketCodecs.FLOAT, ChampionUpdateCl::maxHealth,
                ChampionUpdateCl::new
        );
        @Override public Id<? extends CustomPayload> getId() { return ID; }
    }
    public record ChampionRemove(UUID uuid) implements CustomPayload {
        public static final Id<ChampionRemove> ID = new Id<>(Identifier.of("champions", "remove_hud"));
        public static final PacketCodec<RegistryByteBuf, ChampionRemove> CODEC = PacketCodec.tuple(
                Uuids.PACKET_CODEC, ChampionRemove::uuid,
                ChampionRemove::new
        );
        @Override public Id<? extends CustomPayload> getId() { return ID; }
    }
}
