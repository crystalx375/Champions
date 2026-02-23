package crystal.champions.net;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

/**
 * Даем Identifier пакетам и отправляем их
 * Разделено на разные секции
 * sendUpdateS (server)
 * sendUpdateC (client)
 */
public class ChampionsNetworking {
    public static final Identifier CHAMPION_UPDATE_PACKET = Identifier.of("champions", "update_hud");
    public static final Identifier CHAMPION_REMOVE_PACKET = Identifier.of("champions", "remove_hud");
    public static final Identifier CHAMPION_UPDATE_CLIENT_PACKET = Identifier.of("champions", "update_client_hud");

    public static void sendUpdateS(ServerPlayerEntity player, MobEntity entity, int tier, String affixes) {

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(entity.getUuid());
        buf.writeText(entity.getDisplayName());
        buf.writeInt(tier);
        buf.writeString(affixes != null ? affixes : "");
        buf.writeFloat(entity.getHealth());
        buf.writeFloat(entity.getMaxHealth());

        if (CHAMPION_UPDATE_PACKET != null) ServerPlayNetworking.send(player, CHAMPION_UPDATE_PACKET, buf);
    }

    public static void sendUpdateC(ServerPlayerEntity player, MobEntity entity, int tier, String affixes) {
        PacketByteBuf bufcl = PacketByteBufs.create();
        bufcl.writeUuid(entity.getUuid());
        bufcl.writeText(entity.getDisplayName());
        bufcl.writeInt(tier);
        bufcl.writeString(affixes != null ? affixes : "");
        bufcl.writeFloat(entity.getHealth());
        bufcl.writeFloat(entity.getMaxHealth());

        if (CHAMPION_UPDATE_CLIENT_PACKET != null) ServerPlayNetworking.send(player, CHAMPION_UPDATE_CLIENT_PACKET, bufcl);
    }

    public static void sendRemove(ServerPlayerEntity player, MobEntity entity) {

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(entity.getUuid());

        if (CHAMPION_REMOVE_PACKET != null) ServerPlayNetworking.send(player, CHAMPION_REMOVE_PACKET, buf);
    }
}
