package crystal.champions.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import static crystal.champions.client.net.ClientPacket.registerPackets;


public class ChampionsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new ChampionHudRender());
        registerPackets();
    }
}