package settingdust.serilumspawntweaker.forge;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import settingdust.serilumspawntweaker.SerilumSpawnTweaker;

@Mod(SerilumSpawnTweaker.MOD_ID)
public class SerilumSpawnTweakerForge {
	public SerilumSpawnTweakerForge() {
		SerilumSpawnTweaker.init();
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SerilumSpawnTweaker.serverConfig());
	}
}
