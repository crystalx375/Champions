package crystal.champions.client;

import crystal.champions.client.net.ClientPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.S2CPlayChannelEvents;

import static crystal.champions.client.net.ClientPacket.registerPackets;


public class ChampionsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new ChampionHudRender());
        registerPackets();
    }
}