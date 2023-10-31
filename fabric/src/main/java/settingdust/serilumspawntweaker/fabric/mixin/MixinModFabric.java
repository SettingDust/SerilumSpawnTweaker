package settingdust.serilumspawntweaker.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.natamus.villagespawnpoint.ModFabric;
import com.natamus.villagespawnpoint_common_fabric.data.Constants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ModFabric.class, remap = false)
public class MixinModFabric {
	@ModifyExpressionValue(method = "setGlobalConstants", at = @At(value = "INVOKE", target = "Lnet/fabricmc/loader/api/FabricLoader;isModLoaded(Ljava/lang/String;)Z"))
	private static boolean serilumspawntweaker$setConstantsCorrectly(boolean original) {
		Constants.biomeSpawnPointLoaded = original;
		return original;
	}
}
