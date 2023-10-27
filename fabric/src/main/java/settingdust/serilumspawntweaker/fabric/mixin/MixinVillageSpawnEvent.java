package settingdust.serilumspawntweaker.fabric.mixin;

import com.natamus.collective_common_fabric.functions.BlockPosFunctions;
import com.natamus.villagespawnpoint_common_fabric.events.VillageSpawnEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VillageSpawnEvent.class)
public class MixinVillageSpawnEvent {
	@Redirect(method = "onWorldLoad*", at = @At(value = "INVOKE", target = "Lcom/natamus/collective_common_fabric/functions/BlockPosFunctions;getCenterNearbyVillage(Lnet/minecraft/server/level/ServerLevel;)Lnet/minecraft/core/BlockPos;"))
	private static BlockPos spawntweaker$filterBlacklist(ServerLevel serverLevel) {
		return BlockPosFunctions.getCenterNearbyVillage(serverLevel);
	}
}
