package settingdust.serilumspawntweaker.fabric;

import settingdust.serilumspawntweaker.SerilumSpawnTweaker;
import net.fabricmc.api.ModInitializer;

public class SerilumSpawnTweakerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SerilumSpawnTweaker.init();
    }
}