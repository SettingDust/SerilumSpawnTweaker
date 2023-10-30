package settingdust.serilumspawntweaker.fabric;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraftforge.fml.config.ModConfig;
import settingdust.serilumspawntweaker.SerilumSpawnTweaker;

public class SerilumSpawnTweakerFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		SerilumSpawnTweaker.init();
		ForgeConfigRegistry.INSTANCE.register(SerilumSpawnTweaker.MOD_ID, ModConfig.Type.SERVER, SerilumSpawnTweaker.serverConfig());
	}
}
