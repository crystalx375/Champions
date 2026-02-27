package crystal.champions;

import crystal.champions.config.ChampionsConfigAffixes;
import crystal.champions.config.ChampionsConfigServer;
import crystal.champions.effects.CustomStatusEffects;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static crystal.champions.affix.AffixRegistry.init;

public class Champions implements ModInitializer {
	public static final String MOD_ID = "champions";
	public static final Logger LOGGER = LoggerFactory.getLogger("Champions");
    public static final DefaultParticleType CHAMPIONS_SPELL = FabricParticleTypes.simple();

	@Override
	public void onInitialize() {
		LOGGER.info("Loading champions...");
        ChampionsConfigServer.get();
        ChampionsConfigAffixes.get();
        init();
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "champions_spell"), CHAMPIONS_SPELL);
        CustomStatusEffects.register();
    }
}