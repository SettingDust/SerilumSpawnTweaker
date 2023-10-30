package settingdust.serilumspawntweaker.forge.mixin;

import com.natamus.collective_common_forge.functions.BlockPosFunctions;
import com.natamus.villagespawnpoint_common_forge.events.VillageSpawnEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import settingdust.serilumspawntweaker.SerilumSpawnTweaker;

@Mixin(VillageSpawnEvent.class)
public class MixinVillageSpawnEvent {
    @Redirect(
            method = "onWorldLoad*",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lcom/natamus/collective_common_forge/functions/BlockPosFunctions;getCenterNearbyVillage(Lnet/minecraft/server/level/ServerLevel;)Lnet/minecraft/core/BlockPos;"),
            remap = false)
    private static BlockPos spawntweaker$filterBlacklist(ServerLevel serverLevel) {
        return BlockPosFunctions.getCenterNearbyStructure(
                serverLevel, HolderSet.direct(SerilumSpawnTweaker.spawnVillages(serverLevel)));
    }
}
